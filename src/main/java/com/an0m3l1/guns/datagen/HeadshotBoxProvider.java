package com.an0m3l1.guns.datagen;

import com.an0m3l1.guns.GunMod;
import com.an0m3l1.guns.common.headshot.HeadshotBox;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.DataProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraftforge.registries.ForgeRegistries;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

public abstract class HeadshotBoxProvider implements DataProvider
{
	
	private static final Logger LOGGER = LogManager.getLogger();
	private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
	
	private final DataGenerator generator;
	private final Map<ResourceLocation, HeadshotBox> boxes = new HashMap<>();
	
	protected HeadshotBoxProvider(DataGenerator generator)
	{
		this.generator = generator;
	}
	
	protected abstract void registerHeadshotBoxes();
	
	protected final void addHeadshotBox(EntityType<?> entityType, HeadshotBox box)
	{
		ResourceLocation id = ForgeRegistries.ENTITY_TYPES.getKey(entityType);
		if(id == null)
		{
			throw new IllegalArgumentException("Entity type " + entityType + " is not registered");
		}
		this.boxes.put(id, box);
	}
	
	@Override
	public void run(CachedOutput cache)
	{
		this.boxes.clear();
		this.registerHeadshotBoxes();
		
		this.boxes.forEach((entityId, box) ->
		{
			Path path = this.generator.getOutputFolder().resolve("data/" + GunMod.MOD_ID + "/headshot_boxes/" + entityId.getNamespace() + "/" + entityId.getPath() + ".json");
			try
			{
				JsonObject json = box.toJsonObject();
				DataProvider.saveStable(cache, json, path);
			}
			catch(IOException e)
			{
				LOGGER.error("Couldn't save headshot box to {}", path, e);
			}
		});
	}
	
	@Override
	public String getName()
	{
		return "Headshot Boxes: " + GunMod.MOD_ID;
	}
}