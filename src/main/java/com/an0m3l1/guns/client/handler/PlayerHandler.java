package com.an0m3l1.guns.client.handler;

import com.an0m3l1.guns.init.ModSyncedDataKeys;
import com.an0m3l1.guns.init.ModTags;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

/**
 * Author: An0m3L1
 */
public class PlayerHandler
{
	private static PlayerHandler instance;
	
	public static PlayerHandler get()
	{
		if(instance == null)
		{
			instance = new PlayerHandler();
		}
		return instance;
	}
	
	private PlayerHandler()
	{
	}
	
	@SubscribeEvent
	public void onClientTick(TickEvent.ClientTickEvent event)
	{
		if(event.phase != TickEvent.Phase.START)
		{
			return;
		}
		
		Minecraft mc = Minecraft.getInstance();
		Player player = mc.player;
		
		if(player == null)
		{
			return;
		}
		
		ItemStack heldItem = player.getMainHandItem();
		
		// Sprinting restrictions
		if(heldItem.is(ModTags.Items.HEAVY) || player.isVisuallyCrawling() || player.isCrouching() || player.isBlocking() || ModSyncedDataKeys.RELOADING.getValue(player))
		{
			mc.options.keySprint.setDown(false);
			player.setSprinting(false);
		}
		
		// Crouching restrictions
		if(player.isVisuallyCrawling() || (player.isInWater() && player.getPose() == Pose.SWIMMING && !player.isSwimming()))
		{
			mc.options.keyShift.setDown(false);
			player.setShiftKeyDown(false);
		}
		
		// Using items restrictions
		if(ModSyncedDataKeys.RELOADING.getValue(player) && player.isBlocking())
		{
			mc.options.keyUse.setDown(false);
			player.stopUsingItem();
		}
	}
}
