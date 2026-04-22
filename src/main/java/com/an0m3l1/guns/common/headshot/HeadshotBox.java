package com.an0m3l1.guns.common.headshot;

import com.an0m3l1.guns.interfaces.IHeadshotBox;
import com.google.gson.JsonObject;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.util.INBTSerializable;

import javax.annotation.Nullable;

/**
 * Data class representing a headshot hitbox configuration.
 * Supports fluent creation via {@link Builder}, serialization to JSON,
 * and runtime calculation of the hitbox AABB.
 */
public class HeadshotBox implements IHeadshotBox<LivingEntity>, INBTSerializable<CompoundTag>
{
	
	private double width = 8.0;
	private double height = 8.0;
	private double offsetX = 0.0;
	private double offsetY = 0.0;
	private double offsetZ = 0.0;
	private boolean rotatePitch = false;
	private boolean rotateYaw = false;
	private ChildSettings child = null;
	
	public HeadshotBox()
	{
	}
	
	private HeadshotBox(Builder builder)
	{
		this.width = builder.width;
		this.height = builder.height;
		this.offsetX = builder.offsetX;
		this.offsetY = builder.offsetY;
		this.offsetZ = builder.offsetZ;
		this.rotatePitch = builder.rotatePitch;
		this.rotateYaw = builder.rotateYaw;
		this.child = builder.child;
	}
	
	// Getters (used by JSON serialization and runtime)
	public double getWidth()
	{
		return width;
	}
	
	public double getHeight()
	{
		return height;
	}
	
	public double getOffsetX()
	{
		return offsetX;
	}
	
	public double getOffsetY()
	{
		return offsetY;
	}
	
	public double getOffsetZ()
	{
		return offsetZ;
	}
	
	public boolean isRotatePitch()
	{
		return rotatePitch;
	}
	
	public boolean isRotateYaw()
	{
		return rotateYaw;
	}
	
	public ChildSettings getChild()
	{
		return child;
	}
	
	public boolean hasChild()
	{
		return child != null;
	}
	
	@Nullable
	@Override
	public AABB getHeadshotBox(LivingEntity entity)
	{
		// If the entity is a baby and child support is disabled, no headshot box.
		boolean childEnabled = hasChild();
		if(entity.isBaby() && !childEnabled)
		{
			return null;
		}
		
		double finalWidth = width;
		double finalHeight = height;
		double finalOffsetX = offsetX;
		double finalOffsetY = offsetY;
		double finalOffsetZ = offsetZ;
		
		if(entity.isBaby() && childEnabled)
		{
			finalWidth *= child.widthScale;
			finalHeight *= child.heightScale;
			finalOffsetX *= child.offsetXScale;
			finalOffsetY *= child.offsetYScale;
			finalOffsetZ *= child.offsetZScale;
		}
		
		// Convert from pixels to blocks (1 pixel = 1/16 block = 0.0625)
		double halfWidth = finalWidth * 0.0625 / 2.0;
		double heightBlocks = finalHeight * 0.0625;
		double offsetXBlocks = finalOffsetX * 0.0625;
		double offsetYBlocks = finalOffsetY * 0.0625;
		double offsetZBlocks = finalOffsetZ * 0.0625;
		
		AABB box = new AABB(-halfWidth + offsetXBlocks, offsetYBlocks, -halfWidth + offsetZBlocks, halfWidth + offsetXBlocks, heightBlocks + offsetYBlocks, halfWidth + offsetZBlocks);
		
		if(rotatePitch || rotateYaw)
		{
			float pitch = rotatePitch ? entity.getXRot() : 0.0F;
			float yaw = rotateYaw ? entity.yBodyRot : 0.0F;
			Vec3 direction = Vec3.directionFromRotation(pitch, yaw).normalize();
			box = box.move(direction.scale(offsetZBlocks));
		}
		
		return box;
	}
	
	@Override
	public CompoundTag serializeNBT()
	{
		CompoundTag tag = new CompoundTag();
		tag.putDouble("Width", width);
		tag.putDouble("Height", height);
		tag.putDouble("OffsetX", offsetX);
		tag.putDouble("OffsetY", offsetY);
		tag.putDouble("OffsetZ", offsetZ);
		tag.putBoolean("RotatePitch", rotatePitch);
		tag.putBoolean("RotateYaw", rotateYaw);
		if(child != null)
		{
			tag.put("Child", child.serializeNBT());
		}
		return tag;
	}
	
	@Override
	public void deserializeNBT(CompoundTag nbt)
	{
		// Not needed for JSON loading; we use Gson.
	}
	
	public JsonObject toJsonObject()
	{
		JsonObject json = new JsonObject();
		json.addProperty("width", width);
		json.addProperty("height", height);
		if(offsetX != 0.0)
		{
			json.addProperty("offsetX", offsetX);
		}
		if(offsetY != 0.0)
		{
			json.addProperty("offsetY", offsetY);
		}
		if(offsetZ != 0.0)
		{
			json.addProperty("offsetZ", offsetZ);
		}
		if(rotatePitch)
		{
			json.addProperty("rotatePitch", true);
		}
		if(rotateYaw)
		{
			json.addProperty("rotateYaw", true);
		}
		if(child != null)
		{
			json.add("child", child.toJsonObject());
		}
		return json;
	}
	
	public static HeadshotBox fromJson(JsonObject json)
	{
		Builder builder = builder();
		
		if(json.has("width"))
		{
			builder.width(json.get("width").getAsDouble());
		}
		if(json.has("height"))
		{
			builder.height(json.get("height").getAsDouble());
		}
		if(json.has("offsetX"))
		{
			builder.offsetX(json.get("offsetX").getAsDouble());
		}
		if(json.has("offsetY"))
		{
			builder.offsetY(json.get("offsetY").getAsDouble());
		}
		if(json.has("offsetZ"))
		{
			builder.offsetZ(json.get("offsetZ").getAsDouble());
		}
		if(json.has("rotatePitch"))
		{
			builder.rotatePitch(json.get("rotatePitch").getAsBoolean());
		}
		if(json.has("rotateYaw"))
		{
			builder.rotateYaw(json.get("rotateYaw").getAsBoolean());
		}
		
		if(json.has("child"))
		{
			JsonObject childJson = json.getAsJsonObject("child");
			ChildSettings.Builder childBuilder = ChildSettings.builder();
			if(childJson.has("widthScale"))
			{
				childBuilder.widthScale(childJson.get("widthScale").getAsDouble());
			}
			if(childJson.has("heightScale"))
			{
				childBuilder.heightScale(childJson.get("heightScale").getAsDouble());
			}
			if(childJson.has("offsetXScale"))
			{
				childBuilder.offsetXScale(childJson.get("offsetXScale").getAsDouble());
			}
			if(childJson.has("offsetYScale"))
			{
				childBuilder.offsetYScale(childJson.get("offsetYScale").getAsDouble());
			}
			if(childJson.has("offsetZScale"))
			{
				childBuilder.offsetZScale(childJson.get("offsetZScale").getAsDouble());
			}
			builder.child(childBuilder.build());
		}
		
		return builder.build();
	}
	
	public static Builder builder()
	{
		return new Builder();
	}
	
	@SuppressWarnings("UnusedReturnValue")
	public static class Builder
	{
		private double width = 8.0;
		private double height = 8.0;
		private double offsetX = 0.0;
		private double offsetY = 0.0;
		private double offsetZ = 0.0;
		private boolean rotatePitch = false;
		private boolean rotateYaw = false;
		private ChildSettings child = null;
		
		public Builder width(double width)
		{
			this.width = width;
			return this;
		}
		
		public Builder height(double height)
		{
			this.height = height;
			return this;
		}
		
		public Builder offsetX(double offsetX)
		{
			this.offsetX = offsetX;
			return this;
		}
		
		public Builder offsetY(double offsetY)
		{
			this.offsetY = offsetY;
			return this;
		}
		
		public Builder offsetZ(double offsetZ)
		{
			this.offsetZ = offsetZ;
			return this;
		}
		
		public Builder rotatePitch(boolean rotatePitch)
		{
			this.rotatePitch = rotatePitch;
			return this;
		}
		
		public Builder rotateYaw(boolean rotateYaw)
		{
			this.rotateYaw = rotateYaw;
			return this;
		}
		
		public Builder child(ChildSettings child)
		{
			this.child = child;
			return this;
		}
		
		public Builder child()
		{
			this.child = new ChildSettings();
			return this;
		}
		
		public Builder child(double widthScale, double heightScale, double offsetXScale, double offsetYScale, double offsetZScale)
		{
			this.child = new ChildSettings(widthScale, heightScale, offsetXScale, offsetYScale, offsetZScale);
			return this;
		}
		
		public HeadshotBox build()
		{
			return new HeadshotBox(this);
		}
	}
	
	/**
	 * Settings for baby entities.
	 */
	public static class ChildSettings implements INBTSerializable<CompoundTag>
	{
		private double widthScale = 0.5;
		private double heightScale = 0.5;
		private double offsetXScale = 1.0;
		private double offsetYScale = 1.0;
		private double offsetZScale = 1.0;
		
		public ChildSettings()
		{
		}
		
		public ChildSettings(double widthScale, double heightScale, double offsetXScale, double offsetYScale, double offsetZScale)
		{
			this.widthScale = widthScale;
			this.heightScale = heightScale;
			this.offsetXScale = offsetXScale;
			this.offsetYScale = offsetYScale;
			this.offsetZScale = offsetZScale;
		}
		
		public double getWidthScale()
		{
			return widthScale;
		}
		
		public double getHeightScale()
		{
			return heightScale;
		}
		
		public double getOffsetXScale()
		{
			return offsetXScale;
		}
		
		public double getOffsetYScale()
		{
			return offsetYScale;
		}
		
		public double getOffsetZScale()
		{
			return offsetZScale;
		}
		
		@Override
		public CompoundTag serializeNBT()
		{
			CompoundTag tag = new CompoundTag();
			tag.putDouble("WidthScale", widthScale);
			tag.putDouble("HeightScale", heightScale);
			tag.putDouble("OffsetXScale", offsetXScale);
			tag.putDouble("OffsetYScale", offsetYScale);
			tag.putDouble("OffsetZScale", offsetZScale);
			return tag;
		}
		
		@Override
		public void deserializeNBT(CompoundTag nbt)
		{
		}
		
		public JsonObject toJsonObject()
		{
			JsonObject json = new JsonObject();
			if(widthScale != 0.5)
			{
				json.addProperty("widthScale", widthScale);
			}
			if(heightScale != 0.5)
			{
				json.addProperty("heightScale", heightScale);
			}
			if(offsetXScale != 1.0)
			{
				json.addProperty("offsetXScale", offsetXScale);
			}
			if(offsetYScale != 1.0)
			{
				json.addProperty("offsetYScale", offsetYScale);
			}
			if(offsetZScale != 1.0)
			{
				json.addProperty("offsetZScale", offsetZScale);
			}
			return json;
		}
		
		public static Builder builder()
		{
			return new Builder();
		}
		
		@SuppressWarnings("UnusedReturnValue")
		public static class Builder
		{
			private double widthScale = 0.5;
			private double heightScale = 0.5;
			private double offsetXScale = 1.0;
			private double offsetYScale = 1.0;
			private double offsetZScale = 1.0;
			
			public Builder widthScale(double widthScale)
			{
				this.widthScale = widthScale;
				return this;
			}
			
			public Builder heightScale(double heightScale)
			{
				this.heightScale = heightScale;
				return this;
			}
			
			public Builder offsetXScale(double offsetXScale)
			{
				this.offsetXScale = offsetXScale;
				return this;
			}
			
			public Builder offsetYScale(double offsetYScale)
			{
				this.offsetYScale = offsetYScale;
				return this;
			}
			
			public Builder offsetZScale(double offsetZScale)
			{
				this.offsetZScale = offsetZScale;
				return this;
			}
			
			public ChildSettings build()
			{
				return new ChildSettings(widthScale, heightScale, offsetXScale, offsetYScale, offsetZScale);
			}
		}
	}
}