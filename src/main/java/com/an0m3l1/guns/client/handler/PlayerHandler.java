package com.an0m3l1.guns.client.handler;

import com.an0m3l1.guns.init.ModSyncedDataKeys;
import com.an0m3l1.guns.init.ModTags;
import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.lwjgl.glfw.GLFW;

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
	
	private boolean wasSprintDown = false;
	private boolean wasCrouchDown = false;
	private boolean wasUseDown = false;
	
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
		
		boolean restrictSprint = heldItem.is(ModTags.Items.HEAVY) || player.isVisuallyCrawling() || player.isCrouching() || player.isBlocking() || ModSyncedDataKeys.RELOADING.getValue(player);
		boolean restrictCrouch = player.isVisuallyCrawling() || (player.isInWater() && player.getPose() == Pose.SWIMMING && !player.isSwimming());
		boolean restrictUse = ModSyncedDataKeys.RELOADING.getValue(player) && player.isBlocking();
		
		// Sprinting restrictions
		{
			boolean keySprintDown = isKeyDown(mc.options.keySprint.getKey());
			if(restrictSprint)
			{
				if(keySprintDown)
				{
					wasSprintDown = true;
					mc.options.keySprint.setDown(false);
				}
				if(player.isSprinting())
				{
					player.setSprinting(false);
				}
			}
			else if(wasSprintDown)
			{
				if(keySprintDown)
				{
					mc.options.keySprint.setDown(true);
				}
				wasSprintDown = false;
			}
		}
		
		// Crouching restrictions
		{
			boolean keyCrouchDown = isKeyDown(mc.options.keyShift.getKey());
			
			if(restrictCrouch)
			{
				if(keyCrouchDown)
				{
					wasCrouchDown = true;
					mc.options.keyShift.setDown(false);
				}
				if(player.isShiftKeyDown())
				{
					player.setShiftKeyDown(false);
				}
			}
			else if(wasCrouchDown)
			{
				if(keyCrouchDown)
				{
					mc.options.keyShift.setDown(true);
				}
				wasCrouchDown = false;
			}
		}
		
		// Use restrictions
		{
			boolean keyUseDown = isKeyDown(mc.options.keyUse.getKey());
			
			if(restrictUse)
			{
				if(keyUseDown)
				{
					wasUseDown = true;
					mc.options.keyUse.setDown(false);
				}
				if(player.isUsingItem())
				{
					player.stopUsingItem();
				}
			}
			else if(wasUseDown)
			{
				if(keyUseDown)
				{
					mc.options.keyUse.setDown(true);
				}
				wasUseDown = false;
			}
		}
	}
	
	public boolean isKeyDown(InputConstants.Key key)
	{
		if(key.getType() == InputConstants.Type.KEYSYM)
		{
			long window = Minecraft.getInstance().getWindow().getWindow();
			return GLFW.glfwGetKey(window, key.getValue()) == GLFW.GLFW_PRESS;
		}
		return false;
	}
}