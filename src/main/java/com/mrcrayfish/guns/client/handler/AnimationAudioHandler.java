package com.mrcrayfish.guns.client.handler;

import com.mrcrayfish.guns.Config;
import com.mrcrayfish.guns.Reference;
import com.mrcrayfish.guns.client.audio.StunRingingSound;
import com.mrcrayfish.guns.client.util.GunAnimationHelper;
import com.mrcrayfish.guns.init.ModEffects;
import com.mrcrayfish.guns.item.GunItem;

import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.client.resources.sounds.Sound;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.client.resources.sounds.TickableSoundInstance;
import net.minecraft.client.sounds.ChannelAccess;
import net.minecraft.client.sounds.SoundEngine;
import net.minecraft.client.sounds.WeighedSoundEvents;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.client.event.sound.PlaySoundEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.util.ObfuscationReflectionHelper;

import javax.annotation.Nullable;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Author: zaeonNineZero
 * 
 * Handler class for client-side animation-based sounds.
 * Right now these are limited to draw and reload animations.
 * Will be extended to inspect animations when the functionality is added.
 */
public class AnimationAudioHandler
{
    private static AnimationAudioHandler instance;
    private String lastAnimType = "none";
    ArrayList<String> playedSounds = new ArrayList<>();

    public static AnimationAudioHandler get()
    {
        if(instance == null)
        {
            instance = new AnimationAudioHandler();
        }
        return instance;
    }
    
    // Main tick handler for animation-based sounds.
    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent event)
    {
    	Minecraft mc = Minecraft.getInstance();
    	Player player = mc.player;
    	ItemStack weapon = player.getMainHandItem();
    	if (weapon == null || !(weapon.getItem() instanceof GunItem))
    		return;

    	float partialTick = mc.getPartialTick();
    	String animType = GunAnimationHelper.getSmartAnimationType(weapon, player, partialTick);
    	if (!animType.matches("draw|reloadStart|reloadEnd|reload"))
    		return;
    	animType = GunAnimationHelper.addReloadAnimSuffix(animType, weapon);
    	boolean changedAnim = !animType.equals(lastAnimType);
    	if (changedAnim)
    		playedSounds.clear();
    	lastAnimType = animType;

    	int soundCount = Math.min(12, GunAnimationHelper.getAnimationSoundEventCount(animType, weapon));
    	if (soundCount <= 0)
    		return;
    	
    	float progress = GunAnimationHelper.getSpecificAnimationProgress(animType, weapon, player, partialTick);
    	float scaledProgress = GunAnimationHelper.getScaledProgress(animType, weapon, progress);
    	
    	for(int i = 0; i <= soundCount-1; ++i)
    	{
    		String soundNumID = String.valueOf(i);
    		if (!playedSounds.contains(soundNumID))
    		{
    			String soundString = GunAnimationHelper.getAnimationSoundEventID(animType, weapon, i);
    			ResourceLocation soundEvent = soundString.isEmpty() ? null : new ResourceLocation(soundString);
    			float soundThreshold = GunAnimationHelper.getAnimationSoundThreshold(animType, weapon, i);
    			
    			if (scaledProgress>soundThreshold)
    			{
                	Minecraft.getInstance().getSoundManager().play(new SimpleSoundInstance(soundEvent, SoundSource.PLAYERS, 0.8F, 1.0F, Minecraft.getInstance().level.getRandom(), false, 0, SoundInstance.Attenuation.NONE, 0, 0, 0, true));
    				playedSounds.add(soundNumID);
    			}
    		}
    			
    	}
    	
    }
}