package com.an0m3l1.guns.client.handler;

import com.an0m3l1.guns.init.ModSyncedDataKeys;
import com.an0m3l1.guns.init.ModTags;
import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.lwjgl.glfw.GLFW;

/**
 * Controls Player behavior (sprinting, crouching, using items, etc.)
 * <p>
 * Author: An0m3L1
 */
public class PlayerHandler
{
	private static PlayerHandler instance;
	private boolean wasSprintDown = false;
	private boolean wasCrouchDown = false;
	private boolean wasUseDown = false;
	
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
		boolean keySprintDown = isKeyDown(mc.options.keySprint.getKey());
		boolean restrictSprint = heldItem.is(ModTags.Items.HEAVY) || player.isVisuallyCrawling() || player.isCrouching() || player.isBlocking() || ModSyncedDataKeys.RELOADING.getValue(player);
		wasSprintDown = handleRestriction(restrictSprint, wasSprintDown, keySprintDown, mc.options.keySprint, () -> player.setSprinting(false));
		
		// Crouching restrictions
		boolean keyCrouchDown = isKeyDown(mc.options.keyShift.getKey());
		boolean restrictCrouch = player.isVisuallyCrawling() || (player.isInWater() && player.getPose() == Pose.SWIMMING && !player.isSwimming());
		wasCrouchDown = handleRestriction(restrictCrouch, wasCrouchDown, keyCrouchDown, mc.options.keyShift, () -> player.setShiftKeyDown(false));
		
		// Use restrictions
		boolean keyUseDown = isKeyDown(mc.options.keyUse.getKey());
		boolean restrictUse = ModSyncedDataKeys.RELOADING.getValue(player) && player.isBlocking();
		wasUseDown = handleRestriction(restrictUse, wasUseDown, keyUseDown, mc.options.keyUse, player::stopUsingItem);
	}
	
	private boolean handleRestriction(boolean shouldRestrict, boolean wasKeyDown, boolean keyDown, KeyMapping key, Runnable stopAction)
	{
		if(shouldRestrict)
		{
			if(keyDown)
			{
				wasKeyDown = true;
				key.setDown(false);
			}
			stopAction.run();
		}
		else if(wasKeyDown)
		{
			if(keyDown)
			{
				key.setDown(true);
			}
			wasKeyDown = false;
		}
		return wasKeyDown;
	}
	
	public static boolean isKeyDown(InputConstants.Key key)
	{
		if(key.getType() == InputConstants.Type.KEYSYM)
		{
			long window = Minecraft.getInstance().getWindow().getWindow();
			return GLFW.glfwGetKey(window, key.getValue()) == GLFW.GLFW_PRESS;
		}
		return false;
	}
}