package com.an0m3l1.guns.client.handler;

import com.an0m3l1.guns.client.KeyBinds;
import com.an0m3l1.guns.common.AmmoContext;
import com.an0m3l1.guns.common.Gun;
import com.an0m3l1.guns.compat.PlayerReviveHelper;
import com.an0m3l1.guns.event.GunReloadEvent;
import com.an0m3l1.guns.init.ModSyncedDataKeys;
import com.an0m3l1.guns.item.GunItem;
import com.an0m3l1.guns.network.PacketHandler;
import com.an0m3l1.guns.network.message.C2SMessageReload;
import com.an0m3l1.guns.network.message.C2SMessageUnload;
import com.an0m3l1.guns.util.GunCompositeStatHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemCooldowns;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.lwjgl.glfw.GLFW;

public class ReloadHandler
{
	private static ReloadHandler instance;
	
	public static ReloadHandler get()
	{
		if(instance == null)
		{
			instance = new ReloadHandler();
		}
		return instance;
	}
	
	private int startReloadTick;
	private double reloadTimer;
	private double prevReloadTimer;
	private boolean doMagReload = false;
	private boolean reloadFromEmpty = false;
	private int storedReloadDelay;
	private int reloadingSlot;
	private boolean reloadStart;
	private boolean reloadFinish;
	
	private ReloadHandler()
	{
	}
	
	@SubscribeEvent
	public void onClientTick(TickEvent.ClientTickEvent event)
	{
		if(event.phase != TickEvent.Phase.END)
		{
			return;
		}
		
		this.prevReloadTimer = this.reloadTimer;
		
		Player player = Minecraft.getInstance().player;
		if(player != null)
		{
			if(PlayerReviveHelper.isBleeding(player) && ModSyncedDataKeys.RELOADING.getValue(player))
			{
				this.setReloading(false, true);
			}
			
			this.updateReloadDelay(player);
			if(ModSyncedDataKeys.RELOADING.getValue(player))
			{
				if(this.reloadingSlot != player.getInventory().selected)
				{
					this.setReloading(false, false);
				}
			}
			else
			{
				ItemStack stack = player.getMainHandItem();
				if(stack.getItem() instanceof GunItem)
				{
					CompoundTag tag = stack.getTag();
					if(tag != null && !tag.contains("IgnoreAmmo", Tag.TAG_BYTE))
					{
						if(tag.getInt("AmmoCount") > GunCompositeStatHelper.getAmmoCapacity(stack))
						{
							this.setReloading(false, true);
							PacketHandler.getPlayChannel().sendToServer(new C2SMessageUnload(true));
							GunRenderingHandler.get().stageReserveAmmoUpdate(2);
						}
					}
				}
			}
			
			this.updateReloadTimer(player);
		}
	}
	
	@SubscribeEvent
	public void onKeyPressed(InputEvent.Key event)
	{
		Player player = Minecraft.getInstance().player;
		if(player == null || Minecraft.getInstance().screen != null)
		{
			return;
		}
		
		ItemStack stack = player.getMainHandItem();
		if(stack.getItem() instanceof GunItem)
		{
			if(KeyBinds.KEY_RELOAD.consumeClick() && event.getAction() == GLFW.GLFW_PRESS && !PlayerReviveHelper.isBleeding(player))
			{
				if((reloadTimer <= 0 || reloadTimer >= 1))
				{
					CompoundTag tag = stack.getTag();
					if(tag != null && !tag.contains("IgnoreAmmo", Tag.TAG_BYTE))
					{
						Gun gun = ((GunItem) stack.getItem()).getModifiedGun(stack);
						int currentAmmo = tag.getInt("AmmoCount");
						int maxAmmo = GunCompositeStatHelper.getAmmoCapacity(stack, gun);
						if(currentAmmo < maxAmmo)
						{
							boolean isReloading = ModSyncedDataKeys.RELOADING.getValue(player);
							if(isReloading && ModSyncedDataKeys.MAGLOADED.getValue(player))
							{
								return;
							}
							this.setReloading(!isReloading, true);
						}
					}
				}
				GunRenderingHandler.get().updateReserveAmmo(player);
			}
			if(KeyBinds.KEY_UNLOAD.consumeClick() && event.getAction() == GLFW.GLFW_PRESS && reloadTimer <= 0 && ModSyncedDataKeys.SWITCHTIME.getValue(player) == 0 && !PlayerReviveHelper.isBleeding(player) && Gun.hasAmmo(stack))
			{
				this.setReloading(false, true);
				PacketHandler.getPlayChannel().sendToServer(new C2SMessageUnload(false));
				GunRenderingHandler.get().stageReserveAmmoUpdate(2);
			}
		}
	}
	
	public void setReloading(boolean reloading)
	{
		setReloading(reloading, false);
	}
	
	public void setReloading(boolean reloading, boolean fromInput)
	{
		Player player = Minecraft.getInstance().player;
		if(player != null)
		{
			if(reloading)
			{
				if(PlayerReviveHelper.isBleeding(player))
				{
					return;
				}
				ItemStack stack = player.getMainHandItem();
				if(stack.getItem() instanceof GunItem)
				{
					if(ModSyncedDataKeys.SWITCHTIME.getValue(player) > 0)
					{
						return;
					}
					
					CompoundTag tag = stack.getTag();
					if(tag != null && !tag.contains("IgnoreAmmo", Tag.TAG_BYTE))
					{
						reloadFromEmpty = !Gun.hasAmmo(stack);
						doMagReload = Gun.usesMagReloads(stack);
						
						Gun gun = ((GunItem) stack.getItem()).getModifiedGun(stack);
						if(Gun.findAmmo(player, gun.getProjectile().getItem()) == AmmoContext.NONE && !Gun.hasUnlimitedReloads(stack))
						{
							return;
						}
						
						ItemCooldowns tracker = Minecraft.getInstance().player.getCooldowns();
						float cooldown;
						cooldown = tracker.getCooldownPercent(stack.getItem(), Minecraft.getInstance().getFrameTime());
						if(cooldown > gun.getGeneral().getReloadAllowedCooldown())
						{
							return;
						}
						if(tag.getInt("AmmoCount") >= GunCompositeStatHelper.getAmmoCapacity(stack, gun))
						{
							return;
						}
						if(MinecraftForge.EVENT_BUS.post(new GunReloadEvent.Pre(player, stack)))
						{
							return;
						}
						ModSyncedDataKeys.RELOADING.setValue(player, true);
						PacketHandler.getPlayChannel().sendToServer(new C2SMessageReload(true));
						this.reloadingSlot = player.getInventory().selected;
						MinecraftForge.EVENT_BUS.post(new GunReloadEvent.Post(player, stack));
						GunRenderingHandler.get().updateReserveAmmo(player, gun);
						reloadFinish = true;
						reloadStart = true;
					}
				}
			}
			else
			{
				if(fromInput)
				{
					reloadFinish = false;
				}
				
				ModSyncedDataKeys.RELOADING.setValue(player, false);
				ModSyncedDataKeys.SWITCHTIME.setValue(player, storedReloadDelay + 1);
				if(ModSyncedDataKeys.RELOADING.getValue(player) == true)
				{
					ModSyncedDataKeys.SWITCHTIME.setValue(player, storedReloadDelay + 1);
					ModSyncedDataKeys.RELOADING.setValue(player, false);
				}
				PacketHandler.getPlayChannel().sendToServer(new C2SMessageReload(false));
				this.reloadingSlot = -1;
			}
		}
	}
	
	public boolean getReloading(Player player)
	{
		return ModSyncedDataKeys.RELOADING.getValue(player);
	}
	
	private void updateReloadDelay(Player player)
	{
		int reloadStartDelay = 5;
		int reloadInterruptDelay = 5;
		int reloadEndDelay = 5;
		ItemStack stack = player.getMainHandItem();
		if(player.getMainHandItem().getItem() instanceof GunItem gun)
		{
			reloadStartDelay = Math.max(reloadFromEmpty ? gun.getModifiedGun(stack).getGeneral().getReloadEmptyStartDelay() : gun.getModifiedGun(stack).getGeneral().getReloadStartDelay(), 1);
			reloadInterruptDelay = Math.max(reloadFromEmpty ? gun.getModifiedGun(stack).getGeneral().getReloadEmptyInterruptDelay() : gun.getModifiedGun(stack).getGeneral().getReloadInterruptDelay(), 5);
			reloadEndDelay = Math.max(reloadFromEmpty ? gun.getModifiedGun(stack).getGeneral().getReloadEmptyEndDelay() : gun.getModifiedGun(stack).getGeneral().getReloadEndDelay(), 1);
		}
		storedReloadDelay = (reloadFinish && !getReloading(player)) ? reloadEndDelay : ((reloadStart && getReloading(player)) ? reloadStartDelay : reloadInterruptDelay);
	}
	
	private void updateReloadTimer(Player player)
	{
		double reloadDelay = storedReloadDelay;
		
		if(getReloading(player))
		{
			if(this.startReloadTick == -1)
			{
				this.startReloadTick = player.tickCount;
			}
			if(this.reloadTimer < 1)
			{
				this.reloadTimer += 1 / reloadDelay;
			}
			else
			{
				if(reloadStart)
				{
					reloadStart = false;
				}
			}
		}
		else
		{
			if(this.reloadTimer > 0)
			{
				this.reloadTimer -= 1 / reloadDelay;
			}
			if(reloadTimer <= 0 && this.startReloadTick != -1)
			{
				this.startReloadTick = -1;
			}
		}
		reloadTimer = Mth.clamp(reloadTimer, 0, 1);
	}
	
	public int getStartReloadTick()
	{
		return this.startReloadTick;
	}
	
	public double getReloadTimer()
	{
		return this.reloadTimer;
	}
	
	public float getReloadProgress(float partialTicks)
	{
		return (float) Mth.lerp(partialTicks, this.prevReloadTimer, this.reloadTimer);
	}
	
	public boolean doReloadStartAnimation()
	{
		return reloadStart;
	}
	
	public boolean doReloadFinishAnimation()
	{
		return reloadFinish;
	}
	
	public boolean isDoMagReload()
	{
		return doMagReload;
	}
	
	public boolean isReloadFromEmpty()
	{
		return reloadFromEmpty;
	}
	
	// This method allows the ShootingHandler to tell the ReloadHandler when a weapon was switched.
	public void weaponSwitched()
	{
		reloadStart = false;
		reloadFinish = false;
		reloadTimer = 0;
		prevReloadTimer = 0;
	}
}