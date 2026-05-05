package com.an0m3l1.guns.common.headshot;

import com.an0m3l1.guns.GunConfig;
import com.an0m3l1.guns.GunMod;
import com.an0m3l1.guns.interfaces.IHeadshotBox;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nullable;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.CompletableFuture;

/**
 * Manages headshot hitboxes for entities.
 * Hitboxes are loaded from JSON files located in data packs at:
 * {@code data/<modid>/headshot_boxes/<entity_namespace>/<entity_name>.json}
 */
@Mod.EventBusSubscriber(modid = GunMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class HeadshotBoxManager
{
	
	private static final Map<EntityType<?>, IHeadshotBox<?>> headshotBoxes = new HashMap<>();
	private static final WeakHashMap<Player, LinkedList<AABB>> playerBoxes = new WeakHashMap<>();
	private static final Gson GSON = new GsonBuilder().create();
	
	/**
	 * Registers a reload listener to load headshot box JSON files when resources are reloaded.
	 */
	@SubscribeEvent
	public static void onAddReloadListener(AddReloadListenerEvent event)
	{
		event.addListener((preparationBarrier, resourceManager, preparationsProfiler, reloadProfiler, backgroundExecutor, gameExecutor) -> CompletableFuture.runAsync(() -> loadHeadshotBoxes(resourceManager), backgroundExecutor)
				.thenCompose(preparationBarrier::wait));
	}
	
	/**
	 * Loads all headshot box JSON files from the resource manager.
	 * Files are expected at: data/<modid>/headshot_boxes/<entity_namespace>/<entity_name>.json
	 */
	private static void loadHeadshotBoxes(ResourceManager resourceManager)
	{
		headshotBoxes.clear();
		String folder = "head_boxes";
		
		for(EntityType<?> entityType : ForgeRegistries.ENTITY_TYPES)
		{
			ResourceLocation entityId = ForgeRegistries.ENTITY_TYPES.getKey(entityType);
			if(entityId == null)
			{
				continue;
			}
			
			String path = folder + "/" + entityId.getNamespace() + "/" + entityId.getPath() + ".json";
			ResourceLocation jsonLocation = new ResourceLocation(GunMod.MOD_ID, path);
			
			try
			{
				Optional<Resource> resource = resourceManager.getResource(jsonLocation);
				if(resource.isPresent())
				{
					try(Reader reader = new InputStreamReader(resource.get()
							.open(), StandardCharsets.UTF_8))
					{
						JsonObject json = GSON.fromJson(reader, JsonObject.class);
						HeadshotBox box = HeadshotBox.fromJson(json);
						headshotBoxes.put(entityType, box);
						if(GunConfig.COMMON.showDebugMessages.get())
						{
							GunMod.LOGGER.debug("Loaded head box for {} from {}", entityId, jsonLocation);
						}
					}
				}
			}
			catch(Exception e)
			{
				if(GunConfig.COMMON.showDebugMessages.get())
				{
					GunMod.LOGGER.error("Failed to load head box for {} from {}", entityId, jsonLocation, e);
				}
			}
		}
		
		// Player headshot box is handled separately due to complex logic (swimming, sneaking).
		registerPlayerHeadshotBox();
	}
	
	/**
	 * Registers the player headshot box with complex logic (swimming, crawling, sneaking).
	 * This is kept hardcoded due to its dynamic nature.
	 */
	private static void registerPlayerHeadshotBox()
	{
		IHeadshotBox<Player> playerBox = (entity) ->
		{
			final double scale = 30.0 / 32.0;
			final double headHalfSize = 4 * 0.0625;
			final double headHeight = 8 * 0.0625;
			AABB headBox = new AABB(-headHalfSize, 0, -headHalfSize, headHalfSize, headHeight, headHalfSize);
			
			boolean swimming = entity.isSwimming();
			boolean crawling = (!entity.isInWater() || (entity.isInWater() && !swimming)) && entity.isVisuallySwimming();
			
			if(swimming || crawling)
			{
				boolean isDynamicHeadBox = swimming || (entity.isInWater() && entity.isVisuallySwimming());
				float pitch = isDynamicHeadBox ? entity.getXRot() : 0.0F;
				
				double headScale = getPlayerHeadScale(isDynamicHeadBox, pitch);
				double headY = getPlayerHeadY(isDynamicHeadBox, pitch);
				
				headBox = headBox.move(0, headY * 0.0625, 0);
				Vec3 direction = Vec3.directionFromRotation(pitch, entity.yBodyRot)
						.normalize()
						.scale(headScale);
				headBox = headBox.move(direction);
			}
			else
			{
				double yOffset = entity.isShiftKeyDown() ? 18 * 0.0625 : 24 * 0.0625;
				headBox = headBox.move(0, yOffset, 0);
			}
			return new AABB(headBox.minX * scale, headBox.minY * scale, headBox.minZ * scale, headBox.maxX * scale, headBox.maxY * scale, headBox.maxZ * scale);
		};
		headshotBoxes.put(EntityType.PLAYER, playerBox);
	}
	
	private static double getPlayerHeadScale(boolean dynamicHeadBox, float pitch)
	{
		final double scaleUp = 0.1;
		final double scaleStraight = 0.65;
		final double scaleDown = 1.1;
		if(dynamicHeadBox)
		{
			float clampedPitch = Mth.clamp(pitch, -45.0F, 45.0F);
			if(clampedPitch <= 0)
			{
				float t = (clampedPitch + 45.0F) / 45.0F;
				return Mth.lerp(t, scaleUp, scaleStraight);
			}
			else
			{
				float t = clampedPitch / 45.0F;
				return Mth.lerp(t, scaleStraight, scaleDown);
			}
		}
		return scaleStraight;
	}
	
	private static double getPlayerHeadY(boolean dynamicHeadBox, float pitch)
	{
		final double yUp = 7.75;
		final double yStraight = 4.5;
		final double yDown = 6.75;
		if(dynamicHeadBox)
		{
			float clampedPitch = Mth.clamp(pitch, -45.0F, 45.0F);
			if(clampedPitch <= 0)
			{
				float t = (clampedPitch + 45.0F) / 45.0F;
				return Mth.lerp(t, yUp, yStraight);
			}
			else
			{
				float t = clampedPitch / 45.0F;
				return Mth.lerp(t, yStraight, yDown);
			}
		}
		return yStraight;
	}
	
	/**
	 * Retrieves the headshot box for a given entity type.
	 */
	@Nullable
	@SuppressWarnings("unchecked")
	public static <T extends LivingEntity> IHeadshotBox<T> getHeadshotBox(EntityType<?> type)
	{
		return (IHeadshotBox<T>) headshotBoxes.get(type);
	}
	
	@SubscribeEvent(receiveCanceled = true)
	public void onPlayerTick(TickEvent.PlayerTickEvent event)
	{
		if(event.side == LogicalSide.SERVER && event.phase == TickEvent.Phase.END)
		{
			if(event.player.isSpectator())
			{
				playerBoxes.remove(event.player);
				return;
			}
			LinkedList<AABB> boxes = playerBoxes.computeIfAbsent(event.player, player -> new LinkedList<>());
			boxes.addFirst(event.player.getBoundingBox());
			if(boxes.size() > 20)
			{
				boxes.removeLast();
			}
		}
	}
	
	@SubscribeEvent(receiveCanceled = true)
	public void onPlayerLoggedOut(PlayerEvent.PlayerLoggedOutEvent event)
	{
		playerBoxes.remove(event.getEntity());
	}
	
	public static AABB getBoundingBox(Player entity, int ping)
	{
		if(playerBoxes.containsKey(entity))
		{
			LinkedList<AABB> boxes = playerBoxes.get(entity);
			int index = Mth.clamp(ping, 0, boxes.size() - 1);
			return boxes.get(index);
		}
		return entity.getBoundingBox();
	}
}