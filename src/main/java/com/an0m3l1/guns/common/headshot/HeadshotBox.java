package com.an0m3l1.guns.common.headshot;

import com.an0m3l1.guns.annotation.Optional;
import com.an0m3l1.guns.annotation.Validator;
import com.an0m3l1.guns.interfaces.IHeadshotBox;
import com.google.gson.JsonObject;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.util.INBTSerializable;

import javax.annotation.Nullable;
import java.io.InvalidObjectException;

/**
 * Data class representing a headshot hitbox configuration.
 * Supports fluent creation via {@link Builder}, serialization to JSON,
 * and runtime calculation of the hitbox AABB.
 */
public class HeadshotBox implements IHeadshotBox<LivingEntity>, INBTSerializable<CompoundTag>
{
	
	private final General general;
	@Optional
	private Child child;
	
	public HeadshotBox()
	{
		this.general = new General();
	}
	
	private HeadshotBox(Builder builder)
	{
		this.general = builder.general;
		this.child = builder.child;
		validate();
	}
	
	public double getheadWidth()
	{
		return general.getHeadWidth();
	}
	
	public double getheadHeight()
	{
		return general.getHeadHeight();
	}
	
	public double getx()
	{
		return general.getX();
	}
	
	public double gety()
	{
		return general.getY();
	}
	
	public double getz()
	{
		return general.getZ();
	}
	
	public boolean isRotatePitch()
	{
		return general.isRotatePitch();
	}
	
	public boolean isRotateYaw()
	{
		return general.isRotateYaw();
	}
	
	@Nullable
	@Override
	public AABB getHeadshotBox(LivingEntity entity)
	{
		boolean isBaby = entity.isBaby() || (entity instanceof AgeableMob && ((AgeableMob) entity).getAge() < 0);
		
		if(isBaby && child == null)
		{
			return null;
		}
		
		double finalHeadWidth = general.getHeadWidth();
		double finalHeadHeight = general.getHeadHeight();
		double finalX = general.getX();
		double finalY = general.getY();
		double finalZ = general.getZ();
		
		if(isBaby && child != null)
		{
			finalHeadWidth *= child.getHeadWidthScale();
			finalHeadHeight *= child.getHeadHeightScale();
			finalX *= child.getXScale();
			finalY *= child.getYScale();
			finalZ *= child.getZScale();
		}
		
		double halfHeadWidth = finalHeadWidth * 0.0625 / 2.0;
		double headHeightBlocks = finalHeadHeight * 0.0625;
		Vec3 offset = new Vec3(finalX * 0.0625, finalY * 0.0625, finalZ * 0.0625);
		
		if(general.isRotateYaw() || general.isRotatePitch())
		{
			float pitch = general.isRotatePitch() ? entity.getXRot() : 0.0F;
			float yaw = general.isRotateYaw() ? entity.yBodyRot : 0.0F;
			
			double pitchRad = Math.toRadians(pitch);
			double yawRad = Math.toRadians(yaw);
			double cosPitch = Math.cos(pitchRad);
			double sinPitch = Math.sin(pitchRad);
			double cosYaw = Math.cos(yawRad);
			double sinYaw = Math.sin(yawRad);
			
			double x1 = offset.x * cosYaw - offset.z * sinYaw;
			double y1 = offset.y;
			double z1 = offset.x * sinYaw + offset.z * cosYaw;
			
			double y2 = y1 * cosPitch - z1 * sinPitch;
			double z2 = y1 * sinPitch + z1 * cosPitch;
			
			offset = new Vec3(x1, y2, z2);
		}
		
		return new AABB(-halfHeadWidth + offset.x, offset.y, -halfHeadWidth + offset.z, halfHeadWidth + offset.x, headHeightBlocks + offset.y, halfHeadWidth + offset.z);
	}
	
	@Override
	public CompoundTag serializeNBT()
	{
		CompoundTag tag = new CompoundTag();
		tag.put("General", general.serializeNBT());
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
		json.add("general", general.toJsonObject());
		if(child != null)
		{
			JsonObject childJson = child.toJsonObject();
			if(childJson.size() > 0)
			{
				json.add("child", childJson);
			}
		}
		return json;
	}
	
	public static HeadshotBox fromJson(JsonObject json)
	{
		Builder builder = builder();
		
		if(json.has("general"))
		{
			builder.general(General.fromJson(json.getAsJsonObject("general")));
		}
		if(json.has("child"))
		{
			JsonObject childJson = json.getAsJsonObject("child");
			Child.Builder childBuilder = Child.builder();
			if(childJson.has("headWidthScale"))
			{
				childBuilder.headWidthScale(childJson.get("headWidthScale")
						.getAsDouble());
			}
			if(childJson.has("headHeightScale"))
			{
				childBuilder.headHeightScale(childJson.get("headHeightScale")
						.getAsDouble());
			}
			if(childJson.has("xScale"))
			{
				childBuilder.xScale(childJson.get("xScale")
						.getAsDouble());
			}
			if(childJson.has("yScale"))
			{
				childBuilder.yScale(childJson.get("yScale")
						.getAsDouble());
			}
			if(childJson.has("zScale"))
			{
				childBuilder.zScale(childJson.get("zScale")
						.getAsDouble());
			}
			builder.child(childBuilder.build());
		}
		
		return builder.build();
	}
	
	public static Builder builder()
	{
		return new Builder();
	}
	
	private void validate()
	{
		try
		{
			Validator.isValidObject(this);
			Validator.isValidObject(this.general);
			if(this.child != null)
			{
				Validator.isValidObject(this.child);
			}
		}
		catch(IllegalAccessException | InvalidObjectException e)
		{
			throw new RuntimeException("Invalid HeadshotBox configuration: " + e.getMessage(), e);
		}
	}
	
	/**
	 * General hitbox parameters (dimensions and rotation flags).
	 */
	public static class General implements INBTSerializable<CompoundTag>
	{
		private double headWidth;
		private double headHeight;
		@Optional
		private double x;
		@Optional
		private double y;
		@Optional
		private double z;
		@Optional
		private boolean rotatePitch = false;
		@Optional
		private boolean rotateYaw = false;
		
		public General()
		{
		}
		
		public General(double headWidth, double headHeight, double x, double y, double z, boolean rotatePitch, boolean rotateYaw)
		{
			this.headWidth = headWidth;
			this.headHeight = headHeight;
			this.x = x;
			this.y = y;
			this.z = z;
			this.rotatePitch = rotatePitch;
			this.rotateYaw = rotateYaw;
		}
		
		public double getHeadWidth()
		{
			return headWidth;
		}
		
		public double getHeadHeight()
		{
			return headHeight;
		}
		
		public double getX()
		{
			return x;
		}
		
		public double getY()
		{
			return y;
		}
		
		public double getZ()
		{
			return z;
		}
		
		public boolean isRotatePitch()
		{
			return rotatePitch;
		}
		
		public boolean isRotateYaw()
		{
			return rotateYaw;
		}
		
		@Override
		public CompoundTag serializeNBT()
		{
			CompoundTag tag = new CompoundTag();
			tag.putDouble("HeadWidth", headWidth);
			tag.putDouble("HeadHeight", headHeight);
			tag.putDouble("X", x);
			tag.putDouble("Y", y);
			tag.putDouble("Z", z);
			tag.putBoolean("RotatePitch", rotatePitch);
			tag.putBoolean("RotateYaw", rotateYaw);
			return tag;
		}
		
		@Override
		public void deserializeNBT(CompoundTag nbt)
		{
			// Not used for JSON loading
		}
		
		public JsonObject toJsonObject()
		{
			JsonObject json = new JsonObject();
			json.addProperty("headWidth", headWidth);
			json.addProperty("headHeight", headHeight);
			if(x != 0.0)
			{
				json.addProperty("x", x);
			}
			if(y != 0.0)
			{
				json.addProperty("y", y);
			}
			if(z != 0.0)
			{
				json.addProperty("z", z);
			}
			if(rotatePitch)
			{
				json.addProperty("rotatePitch", true);
			}
			if(rotateYaw)
			{
				json.addProperty("rotateYaw", true);
			}
			return json;
		}
		
		public static General fromJson(JsonObject json)
		{
			Builder builder = builder();
			if(json.has("headWidth"))
			{
				builder.headWidth(json.get("headWidth")
						.getAsDouble());
			}
			if(json.has("headHeight"))
			{
				builder.headHeight(json.get("headHeight")
						.getAsDouble());
			}
			if(json.has("x"))
			{
				builder.x(json.get("x")
						.getAsDouble());
			}
			if(json.has("y"))
			{
				builder.y(json.get("y")
						.getAsDouble());
			}
			if(json.has("z"))
			{
				builder.z(json.get("z")
						.getAsDouble());
			}
			if(json.has("rotatePitch"))
			{
				builder.rotatePitch(json.get("rotatePitch")
						.getAsBoolean());
			}
			if(json.has("rotateYaw"))
			{
				builder.rotateYaw(json.get("rotateYaw")
						.getAsBoolean());
			}
			return builder.build();
		}
		
		public static Builder builder()
		{
			return new Builder();
		}
		
		public static class Builder
		{
			private double headWidth;
			private double headHeight;
			@Optional
			private double x;
			@Optional
			private double y;
			@Optional
			private double z;
			@Optional
			private boolean rotatePitch = false;
			@Optional
			private boolean rotateYaw = false;
			
			public Builder headWidth(double headWidth)
			{
				this.headWidth = headWidth;
				return this;
			}
			
			public Builder headHeight(double headHeight)
			{
				this.headHeight = headHeight;
				return this;
			}
			
			public Builder x(double x)
			{
				this.x = x;
				return this;
			}
			
			public Builder y(double y)
			{
				this.y = y;
				return this;
			}
			
			public Builder z(double z)
			{
				this.z = z;
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
			
			public General build()
			{
				General general = new General(headWidth, headHeight, x, y, z, rotatePitch, rotateYaw);
				try
				{
					Validator.isValidObject(general);
				}
				catch(IllegalAccessException | InvalidObjectException e)
				{
					throw new RuntimeException("Invalid General configuration: " + e.getMessage(), e);
				}
				return general;
			}
		}
	}
	
	@SuppressWarnings("UnusedReturnValue")
	public static class Builder
	{
		private General general = new General();
		@Optional
		private Child child = null;
		
		public Builder general(General general)
		{
			this.general = general;
			return this;
		}
		
		public Builder general(double headWidth, double headHeight, double x, double y, double z, boolean rotatePitch, boolean rotateYaw)
		{
			this.general = General.builder()
					.headWidth(headWidth)
					.headHeight(headHeight)
					.x(x)
					.y(y)
					.z(z)
					.rotatePitch(rotatePitch)
					.rotateYaw(rotateYaw)
					.build();
			return this;
		}
		
		public Builder child(Child child)
		{
			this.child = child;
			return this;
		}
		
		public Builder child(double headWidthScale, double headHeightScale, double xScale, double yScale, double zScale)
		{
			this.child = new Child(headWidthScale, headHeightScale, xScale, yScale, zScale);
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
	public static class Child implements INBTSerializable<CompoundTag>
	{
		@Optional
		private double headWidthScale = 1.0;
		@Optional
		private double headHeightScale = 1.0;
		@Optional
		private double xScale = 1.0;
		@Optional
		private double yScale = 1.0;
		@Optional
		private double zScale = 1.0;
		
		public Child()
		{
		}
		
		public Child(double headWidthScale, double headHeightScale, double xScale, double yScale, double zScale)
		{
			this.headWidthScale = headWidthScale;
			this.headHeightScale = headHeightScale;
			this.xScale = xScale;
			this.yScale = yScale;
			this.zScale = zScale;
		}
		
		public double getHeadWidthScale()
		{
			return headWidthScale;
		}
		
		public double getHeadHeightScale()
		{
			return headHeightScale;
		}
		
		public double getXScale()
		{
			return xScale;
		}
		
		public double getYScale()
		{
			return yScale;
		}
		
		public double getZScale()
		{
			return zScale;
		}
		
		@Override
		public CompoundTag serializeNBT()
		{
			CompoundTag tag = new CompoundTag();
			tag.putDouble("headWidthScale", headWidthScale);
			tag.putDouble("headHeightScale", headHeightScale);
			tag.putDouble("xScale", xScale);
			tag.putDouble("yScale", yScale);
			tag.putDouble("zScale", zScale);
			return tag;
		}
		
		@Override
		public void deserializeNBT(CompoundTag nbt)
		{
		}
		
		public JsonObject toJsonObject()
		{
			JsonObject json = new JsonObject();
			json.addProperty("headWidthScale", headWidthScale);
			json.addProperty("headHeightScale", headHeightScale);
			json.addProperty("xScale", xScale);
			json.addProperty("yScale", yScale);
			json.addProperty("zScale", zScale);
			return json;
		}
		
		public static Builder builder()
		{
			return new Builder();
		}
		
		@SuppressWarnings("UnusedReturnValue")
		public static class Builder
		{
			@Optional
			private double headWidthScale;
			@Optional
			private double headHeightScale;
			@Optional
			private double xScale;
			@Optional
			private double yScale;
			@Optional
			private double zScale;
			
			public Builder headWidthScale(double headWidthScale)
			{
				this.headWidthScale = headWidthScale;
				return this;
			}
			
			public Builder headHeightScale(double headHeightScale)
			{
				this.headHeightScale = headHeightScale;
				return this;
			}
			
			public Builder xScale(double xScale)
			{
				this.xScale = xScale;
				return this;
			}
			
			public Builder yScale(double yScale)
			{
				this.yScale = yScale;
				return this;
			}
			
			public Builder zScale(double zScale)
			{
				this.zScale = zScale;
				return this;
			}
			
			public Child build()
			{
				Child child = new Child(headWidthScale, headHeightScale, xScale, yScale, zScale);
				try
				{
					Validator.isValidObject(child);
				}
				catch(IllegalAccessException | InvalidObjectException e)
				{
					throw new RuntimeException("Invalid Child configuration: " + e.getMessage(), e);
				}
				return child;
			}
		}
	}
}