package com.mrcrayfish.guns.client.handler;

import com.mrcrayfish.guns.GunMod;
import com.mrcrayfish.guns.client.util.GunAnimationHelper;
import com.mrcrayfish.guns.item.GunItem;

import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import java.util.ArrayList;
import java.util.Arrays;

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

    public static AnimationAudioHandler get()
    {
        if(instance == null)
        {
            instance = new AnimationAudioHandler();
        }
        return instance;
    }
    
    private boolean doValidAnimDebug = false;
    private boolean validAnimDebugState = true;
    
    private String lastAnimType = "none";
    private ItemStack lastWeapon;
    private ArrayList<String> readySounds = new ArrayList<>();
    private ArrayList<String> playedSounds = new ArrayList<>();
    
    // Main tick handler for animation-based sounds.
    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event)
    {
    	Minecraft mc = Minecraft.getInstance();
    	Player player = mc.player;
    	if (player == null)
    		return;
    	
    	ItemStack weapon = player.getMainHandItem();
    	if (weapon == null || !(weapon.getItem() instanceof GunItem))
    		return;
    	boolean changedWeapon = lastWeapon != null && Item.getId(lastWeapon.getItem()) != Item.getId(weapon.getItem());
    	lastWeapon = weapon;
    	if (changedWeapon)
    	{
    		readySounds.clear();
    		playedSounds.clear();
    		return;
    	}

    	float partialTick = mc.getPartialTick();
    	String baseAnimType = GunAnimationHelper.getSmartAnimationType(weapon, player, partialTick);
    	String animType = GunAnimationHelper.addReloadAnimSuffix(baseAnimType, weapon);
    	boolean changedAnim = !animType.equals(lastAnimType);
    	lastAnimType = animType;
    	if (changedAnim)
    	{
    		readySounds.clear();
    		playedSounds.clear();
    		return;
    	}
    	String[] validAnimTypes = {"draw", "fire", "reloadStart", "reload", "reloadEnd", "inspect"};
    	if (Arrays.stream(validAnimTypes).anyMatch(baseAnimType::equals)
    	&& ((ReloadHandler.get().getReloadProgress(mc.getPartialTick()) >= 0.9 || !baseAnimType.equals("reload"))
    	))
    	{
    		if (doValidAnimDebug && !validAnimDebugState)
    		{
        		validAnimDebugState = true;
    		}
    		
    		int soundCount = Math.min(16, GunAnimationHelper.getAnimationSoundEventCount(animType, weapon));
	    	if (soundCount > 0)
	    	{
		    	float progress = GunAnimationHelper.getSpecificAnimationProgress(baseAnimType, weapon, player, partialTick);
		    	float scaledProgress = GunAnimationHelper.getScaledProgress(animType, weapon, progress);
		    	if (changedWeapon)
		    	scaledProgress = 0;
		    	
		    	for(int i = 0; i < soundCount; ++i)
		    	{
		    		String soundNumID = String.valueOf(i);
		    		String soundString = GunAnimationHelper.getAnimationSoundEventID(animType, weapon, i);
		    		ResourceLocation soundEvent = soundString == null ? null : new ResourceLocation(soundString);
		    		float soundThreshold = GunAnimationHelper.getAnimationSoundParamFloat(animType, weapon, i, "playAt");
		    		//GunMod.LOGGER.info("Animation Audio Handler: Found sound with numerical ID of " + soundNumID + ", event ID of " + soundString + ". Sound plays at frame " + soundThreshold);
		    		if (soundThreshold>=0)
		    		{
		    			if (scaledProgress>=soundThreshold && readySounds.contains(soundNumID) && !playedSounds.contains(soundNumID))
					    {
			    			float pitch = GunAnimationHelper.getAnimationSoundParamFloat(animType, weapon, i, "pitch", 1.0F);
			    			float volume = GunAnimationHelper.getAnimationSoundParamFloat(animType, weapon, i, "volume", 1.0F);
				    		Minecraft.getInstance().getSoundManager().play(new SimpleSoundInstance(soundEvent, SoundSource.PLAYERS, volume, pitch, mc.level.getRandom(), false, 0, SoundInstance.Attenuation.NONE, 0, 0, 0, true));
				    		readySounds.remove(soundNumID);
				    		playedSounds.add(soundNumID);
				    	}
		    			
		    			if (!playedSounds.contains(soundNumID) && !readySounds.contains(soundNumID)
						&& scaledProgress<soundThreshold+(soundThreshold==0 ? 3 : 2))
						readySounds.add(soundNumID);
						else
						if (playedSounds.contains(soundNumID)&& scaledProgress<soundThreshold)
						playedSounds.remove(soundNumID);
		    		}
		    	}
	    	}
    	}
    	else
    	{
    		if (doValidAnimDebug && validAnimDebugState)
    		{
    			if (!baseAnimType.equals("none") && !(Arrays.stream(validAnimTypes).anyMatch(baseAnimType::equals)))
	    		{
	    			
	    			GunMod.LOGGER.info("Animation Audio Handler: Animation " + animType + " (" + baseAnimType + ") " + "does not support sound playback, skipping. (Last animation was " + lastAnimType + ")");
	        		validAnimDebugState = false;
	    		}
    		}
    	}
    }
}