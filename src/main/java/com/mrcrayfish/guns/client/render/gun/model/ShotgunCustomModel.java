package com.mrcrayfish.guns.client.render.gun.model;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;
import com.mrcrayfish.guns.common.Gun;
import com.mrcrayfish.guns.GunMod;
import com.mrcrayfish.guns.client.GunModel;
import com.mrcrayfish.guns.client.ExpandedModelComponents;
import com.mrcrayfish.guns.client.render.gun.IOverrideModel;
import com.mrcrayfish.guns.client.util.GunAnimationHelper;
import com.mrcrayfish.guns.client.util.RenderUtil;
import com.mrcrayfish.guns.item.GunItem;
import com.mrcrayfish.guns.item.attachment.IAttachment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemCooldowns;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;

/**
 * Author: MrCrayfish
 * Modified by zaeonNineZero for Nine Zero's Gun Expansion
 * Attachment detection logic based off of code from Mo' Guns by Bomb787 and AlanorMiga (MigaMi)
 */
public class ShotgunCustomModel implements IOverrideModel
{
	private boolean disableAnimations = false;
	
    @Override
	// This class renders a model with support for NBT and attachment based part variations
	// and custom animations from CGM Expanded.
	
	// We start by declaring our render function that will handle rendering the core baked model (which is a non-moving part).
    public void render(float partialTicks, ItemTransforms.TransformType transformType, ItemStack stack, ItemStack parent, @Nullable LivingEntity entity, PoseStack poseStack, MultiBufferSource buffer, int light, int overlay)
    {
		// Render the item's BakedModel, which will serve as the core of our custom model.
    	// We select which model variant to use by fetching the value of the CustomModelData tag.
        BakedModel bakedModel = ExpandedModelComponents.SHOTGUN_BASE.getModel();
        if (getVariant(stack) == 1 || getVariant(stack, "BaseVariant") == 1)
        bakedModel = ExpandedModelComponents.SHOTGUN_BASE_1.getModel();

        // Render the BakedModel we selected.
        Minecraft.getInstance().getItemRenderer().render(stack, ItemTransforms.TransformType.NONE, false, poseStack, buffer, light, overlay, GunModel.wrap(bakedModel));

        // Heat Shield element, a default cosmetic part that can be removed via NBT.
        // The actual model element used depends (again) on the base model variant.
        if(getVariant(stack, "RemoveHeatShield") == 0)
     	{
            BakedModel heatShieldModel = ExpandedModelComponents.SHOTGUN_HEAT_SHIELD.getModel();
            if (getVariant(stack) == 1 || getVariant(stack, "BaseVariant") == 1)
            heatShieldModel = ExpandedModelComponents.SHOTGUN_HEAT_SHIELD_1.getModel();
            RenderUtil.renderModel(heatShieldModel, transformType, null, stack, parent, poseStack, buffer, light, overlay);
     	}
        
        // Special animated segment for compat with the CGM Expanded fork.
        // First, some variables for animation building
        boolean isPlayer = entity != null && entity.equals(Minecraft.getInstance().player);
        boolean isFirstPerson = (transformType.firstPerson());
        boolean correctContext = (transformType.firstPerson() || transformType == ItemTransforms.TransformType.THIRD_PERSON_RIGHT_HAND || transformType == ItemTransforms.TransformType.THIRD_PERSON_LEFT_HAND);
        boolean useFallbackAnimation = false;
        
        Vec3 boltTranslations = Vec3.ZERO;
        
        Vec3 bulletTranslations = Vec3.ZERO;
        Vec3 bulletRotations = Vec3.ZERO;
        Vec3 bulletRotOffset = Vec3.ZERO;
        
        if(isPlayer && correctContext && !disableAnimations)
        {
        	try {
    				Player player = (Player) entity;
    				
    				boltTranslations = GunAnimationHelper.getSmartAnimationTrans(stack, player, partialTicks, "bolt");
        			
        	        bulletTranslations = GunAnimationHelper.getSmartAnimationTrans(stack, player, partialTicks, "bullet");
        	        bulletRotations = GunAnimationHelper.getSmartAnimationRot(stack, player, partialTicks, "bullet");
        	        bulletRotOffset = GunAnimationHelper.getSmartAnimationRotOffset(stack, player, partialTicks, "bullet");

        	    	if(!GunAnimationHelper.hasAnimation("fire", stack) && GunAnimationHelper.getSmartAnimationType(stack, player, partialTicks)=="fire")
        	    	useFallbackAnimation = true;
        		}
	    		catch(NoClassDefFoundError ignored) {
	            	disableAnimations = true;
	    		}
        		catch(Exception e) {
                	GunMod.LOGGER.error("NZGE encountered an error trying to apply animations.");
                	e.printStackTrace();
                	disableAnimations = true;
        		}
        }
		
        // Fire animation is done the old way, and added onto the existing animation.
        GunItem gunStack = (GunItem) stack.getItem();
        Gun gun = gunStack.getModifiedGun(stack);
        if(isPlayer && correctContext)
        {
            float cooldownDivider = 1.0F*Math.max((float) gun.getGeneral().getRate()/3F,1);
            float cooldownOffset1 = cooldownDivider - 1.0F;
            float intensity = 1.0F +1;
            
        	ItemCooldowns tracker = Minecraft.getInstance().player.getCooldowns();
            float cooldown = tracker.getCooldownPercent(stack.getItem(), Minecraft.getInstance().getFrameTime());
            cooldown *= cooldownDivider;
            float cooldown_a = cooldown-cooldownOffset1;

            float cooldown_b = Math.min(Math.max(cooldown_a*intensity,0),1);
            float cooldown_c = Math.min(Math.max((-cooldown_a*intensity)+intensity,0),1);
            float cooldown_d = Math.min(cooldown_b,cooldown_c);
            
            boltTranslations = boltTranslations.add(0, 0, cooldown_d * 2.3);
        }

		// Pump Shotgun bolt. This animated part cycles backward then forward after firing.
		// Push pose so we can make do transformations without affecting the models above.
        poseStack.pushPose();
		// Now we apply our transformations.
        if(isPlayer)
        poseStack.translate(0, 0, boltTranslations.z * 0.0625);
		// Our transformations are done - now we can render the model.
        RenderUtil.renderModel(ExpandedModelComponents.SHOTGUN_BOLT.getModel(), transformType, null, stack, parent, poseStack, buffer, light, overlay);
		// Pop pose to compile everything in the render matrix.
        poseStack.popPose();
        
        // SG Shell, which is only used during custom reload animations.
        if(isPlayer && isFirstPerson && !disableAnimations)
        {
    		// Push pose so we can make do transformations without affecting the models above.
            poseStack.pushPose();
            // Initial translation to the starting position.
            poseStack.translate(0.0, -5.15*0.0625, 2.2*0.0625);
            // Apply the transformations
            if(isPlayer && isFirstPerson)
            {
            	if(bulletTranslations!=Vec3.ZERO)
                	poseStack.translate(bulletTranslations.x*0.0625, bulletTranslations.y*0.0625, bulletTranslations.z*0.0625);
                if(bulletRotations!=Vec3.ZERO)
                    GunAnimationHelper.rotateAroundOffset(poseStack, bulletRotations, bulletRotOffset);
        	}
    		// Render the model.
            RenderUtil.renderModel(ExpandedModelComponents.SHOTGUN_SHELL.getModel(), transformType, null, stack, parent, poseStack, buffer, light, overlay);
    		// Pop pose to compile everything in the render matrix.
            poseStack.popPose();
        }
    }
    
    //NBT fetch code for skin variants - ported from the "hasAmmo" function under common/Gun.java
    public static int getVariant(ItemStack gunStack)
    {
        CompoundTag tag = gunStack.getOrCreateTag();
        return tag.getInt("CustomModelData");
    }
    
    //NBT fetch code for skin variants - ported from the "hasAmmo" function under common/Gun.java
    public static int getVariant(ItemStack gunStack, String tag_name)
    {
        CompoundTag tag = gunStack.getOrCreateTag();
        return tag.getInt(tag_name);
    }
}