package com.mrcrayfish.guns.client.render.gun.model;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mrcrayfish.guns.client.GunModel;
import com.mrcrayfish.guns.client.SpecialModels;
import com.mrcrayfish.guns.client.handler.GunRenderingHandler;
import com.mrcrayfish.guns.client.render.gun.IOverrideModel;
import com.mrcrayfish.guns.client.util.PropertyHelper;
import com.mrcrayfish.guns.client.util.RenderUtil;
import com.mrcrayfish.guns.common.Gun;
import com.mrcrayfish.guns.init.ModSyncedDataKeys;
import com.mrcrayfish.guns.item.GunItem;
import com.mrcrayfish.guns.util.GunCompositeStatHelper;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.client.event.ClientPlayerNetworkEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import javax.annotation.Nullable;
import java.util.WeakHashMap;

/**
 * Author: MrCrayfish
 */
public class MiniGunModel implements IOverrideModel
{
    private WeakHashMap<LivingEntity, Rotations> rotationMap = new WeakHashMap<>();
    
    float rotationSpeed = 0F;

    @Override
    public void tick(Player player)
    {
        this.rotationMap.putIfAbsent(player, new Rotations());
        
        Rotations rotations = this.rotationMap.get(player);
        rotations.prevRotation = rotations.rotation;
        int lastFireTick = GunRenderingHandler.get().getLastFireTick();
        
        ItemStack heldItem = player.getMainHandItem();
        int fireRate = GunCompositeStatHelper.getCompositeRate(heldItem, player);
        double barrelCount = PropertyHelper.getMinigunBarrels(heldItem);
        float firingSpinRate = Math.min((360F/(float)barrelCount)*0.96F,(360F/(float)barrelCount)/(float)fireRate);
        
        boolean shooting = player.tickCount < lastFireTick+(fireRate+1) && lastFireTick!=-1;

        if(shooting)
        {
        	rotationSpeed=firingSpinRate;
        }
        else
        {
        	if (rotationSpeed>0F)
        	rotationSpeed=Math.max(Math.max((rotationSpeed-2)*0.9F,(rotationSpeed-2)-(5F/(float)barrelCount))+2F, 2F);
        }
        
        if (rotationSpeed>0F)
        rotations.rotation += rotationSpeed;
        
        if (rotations.rotation > 360/barrelCount)
        {
        	rotations.rotation -= 360/barrelCount;
        	rotations.prevRotation -= 360/barrelCount;
        	if (!shooting)
        	{
        		if (rotationSpeed<Math.max(20F/(float)barrelCount,2.2F))
        		{
        			rotationSpeed=0;
        			rotations.rotation = 0;
        		}
        		else
        		rotationSpeed=Math.max(Mth.lerp(0.4F,rotationSpeed*0.9F,rotationSpeed-(1.5F/(float)barrelCount)), 2F);
        	}
        }
        
        
        /*Rotations rotations = this.rotationMap.get(player);
        rotations.prevRotation = rotations.rotation;

        boolean shooting = ModSyncedDataKeys.SHOOTING.getValue(player);
        ItemStack heldItem = player.getMainHandItem();
        int fireRate = GunCompositeStatHelper.getCompositeRate(heldItem, player);
        float maxSpinRate = Mth.clamp(90F/(float) fireRate,10F,85F);
        if(!Gun.hasAmmo(heldItem) && !Gun.canShoot(heldItem) && !player.isCreative())
        {
            shooting = false;
        }

        if(shooting)
        {
        	rotationSpeed=maxSpinRate;
            //if (rotationSpeed<maxSpinRate)
        	//rotationSpeed+=Math.min(maxSpinRate-rotationSpeed,Math.max(maxSpinRate/5F,20F-rotationSpeed));
        }
        else
        {
        	rotationSpeed*=0.9F;
        }
        
        if(rotationSpeed<1F)
        rotationSpeed = 1F;
        
        if (rotationSpeed>0F)
        rotations.rotation += rotationSpeed;*/
        	
    }

    @Override
    public void render(float partialTicks, ItemTransforms.TransformType transformType, ItemStack stack, ItemStack parent, @Nullable LivingEntity entity, PoseStack poseStack, MultiBufferSource renderTypeBuffer, int light, int overlay)
    {
        Rotations rotations = entity != null ? this.rotationMap.computeIfAbsent(entity, uuid -> new Rotations()) : Rotations.ZERO;
        Minecraft.getInstance().getItemRenderer().render(stack, ItemTransforms.TransformType.NONE, false, poseStack, renderTypeBuffer, light, overlay, GunModel.wrap(SpecialModels.MINI_GUN_BASE.getModel()));
        poseStack.pushPose();
        boolean correctContext = (transformType == ItemTransforms.TransformType.FIRST_PERSON_RIGHT_HAND || transformType == ItemTransforms.TransformType.THIRD_PERSON_RIGHT_HAND || transformType == ItemTransforms.TransformType.FIRST_PERSON_LEFT_HAND || transformType == ItemTransforms.TransformType.THIRD_PERSON_LEFT_HAND);
        if (correctContext)
        	RenderUtil.rotateZ(poseStack, 0.0F, -0.375F, (float)rotations.prevRotation + (float)(rotations.rotation - rotations.prevRotation) * partialTicks);
        Minecraft.getInstance().getItemRenderer().render(stack, ItemTransforms.TransformType.NONE, false, poseStack, renderTypeBuffer, light, overlay, GunModel.wrap(SpecialModels.MINI_GUN_BARRELS.getModel()));
        poseStack.popPose();
    }

    private static class Rotations
    {
        private static final Rotations ZERO = new Rotations();

        private double rotation;
        private double prevRotation;
    }

    @SubscribeEvent
    public void onClientDisconnect(ClientPlayerNetworkEvent.LoggingOut event)
    {
        this.rotationMap.clear();
    }
}
