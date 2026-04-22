package com.an0m3l1.guns.datagen;

import com.an0m3l1.guns.common.headshot.HeadshotBox;
import net.minecraft.data.DataGenerator;
import net.minecraft.world.entity.EntityType;

public class HeadshotBoxGen extends HeadshotBoxProvider
{
	
	public HeadshotBoxGen(DataGenerator generator)
	{
		super(generator);
	}
	
	@Override
	protected void registerHeadshotBoxes()
	{
		// Zombie-like mobs
		addHeadshotBox(EntityType.ZOMBIE, HeadshotBox.builder().width(8.0).offsetY(24.0).child(0.75, 0.75, 1.0, 0.5, 1.0).build());
		addHeadshotBox(EntityType.DROWNED, HeadshotBox.builder().width(8.0).offsetY(24.0).child(0.75, 0.75, 1.0, 0.5, 1.0).build());
		addHeadshotBox(EntityType.ZOMBIFIED_PIGLIN, HeadshotBox.builder().width(8.0).offsetY(24.0).child(0.75, 0.75, 1.0, 0.5, 1.0).build());
		addHeadshotBox(EntityType.HUSK, HeadshotBox.builder().width(8.0).offsetY(24.0).child(0.75, 0.75, 1.0, 0.5, 1.0).build());
		addHeadshotBox(EntityType.PIGLIN, HeadshotBox.builder().width(8.0).offsetY(24.0).child(0.75, 0.75, 1.0, 0.5, 1.0).build());
		
		// Skeletons
		addHeadshotBox(EntityType.SKELETON, HeadshotBox.builder().width(8.0).offsetY(24.0).build());
		addHeadshotBox(EntityType.STRAY, HeadshotBox.builder().width(8.0).offsetY(24.0).build());
		
		// Creeper
		addHeadshotBox(EntityType.CREEPER, HeadshotBox.builder().width(8.0).offsetY(18.0).build());
		
		// Spider
		addHeadshotBox(EntityType.SPIDER, HeadshotBox.builder().width(8.0).height(5.0).offsetY(7.0).offsetZ(7.0).rotateYaw(true).build());
		
		// Villagers
		addHeadshotBox(EntityType.VILLAGER, HeadshotBox.builder().width(8.0).height(9.0).offsetY(23.0).build());
		addHeadshotBox(EntityType.ZOMBIE_VILLAGER, HeadshotBox.builder().width(8.0).height(9.0).offsetY(23.0).build());
		addHeadshotBox(EntityType.VINDICATOR, HeadshotBox.builder().width(8.0).height(9.0).offsetY(23.0).build());
		addHeadshotBox(EntityType.EVOKER, HeadshotBox.builder().width(8.0).height(9.0).offsetY(23.0).build());
		addHeadshotBox(EntityType.PILLAGER, HeadshotBox.builder().width(8.0).height(9.0).offsetY(23.0).build());
		addHeadshotBox(EntityType.ILLUSIONER, HeadshotBox.builder().width(8.0).height(9.0).offsetY(23.0).build());
		addHeadshotBox(EntityType.WANDERING_TRADER, HeadshotBox.builder().width(8.0).height(9.0).offsetY(23.0).build());
		addHeadshotBox(EntityType.WITCH, HeadshotBox.builder().width(8.0).height(9.0).offsetY(23.0).build());
		
		// Animals
		addHeadshotBox(EntityType.SHEEP, HeadshotBox.builder().width(7.5).height(8.0).offsetY(15.0).offsetZ(9.5).rotateYaw(true).build());
		addHeadshotBox(EntityType.CHICKEN, HeadshotBox.builder().width(4.0).height(6.0).offsetY(9.0).offsetZ(5.0).rotateYaw(true).build());
		addHeadshotBox(EntityType.COW, HeadshotBox.builder().width(7.5).height(8.0).offsetY(16.0).offsetZ(10.5).rotateYaw(true).build());
		addHeadshotBox(EntityType.MOOSHROOM, HeadshotBox.builder().width(7.5).height(8.0).offsetY(16.0).offsetZ(10.5).rotateYaw(true).build());
		addHeadshotBox(EntityType.PIG, HeadshotBox.builder().width(8.0).height(8.0).offsetY(10.0).offsetZ(10.0).rotateYaw(true).build());
		
		// Horses
		addHeadshotBox(EntityType.HORSE, HeadshotBox.builder().width(10.0).offsetY(26.0).offsetZ(16.0).rotateYaw(true).build());
		addHeadshotBox(EntityType.SKELETON_HORSE, HeadshotBox.builder().width(10.0).offsetY(26.0).offsetZ(16.0).rotateYaw(true).build());
		addHeadshotBox(EntityType.DONKEY, HeadshotBox.builder().width(7.5).height(8.0).offsetY(20.0).offsetZ(13.0).rotateYaw(true).build());
		addHeadshotBox(EntityType.MULE, HeadshotBox.builder().width(7.5).height(8.0).offsetY(21.0).offsetZ(14.0).rotateYaw(true).build());
		
		// Llamas
		addHeadshotBox(EntityType.LLAMA, HeadshotBox.builder().width(8.0).offsetY(26.0).offsetZ(10.0).rotateYaw(true).build());
		addHeadshotBox(EntityType.TRADER_LLAMA, HeadshotBox.builder().width(8.0).offsetY(26.0).offsetZ(10.0).rotateYaw(true).build());
		
		// Polar Bear
		addHeadshotBox(EntityType.POLAR_BEAR, HeadshotBox.builder().width(9.0).height(12.0).offsetY(20.0).rotateYaw(true).build());
		
		// Snow Golem
		addHeadshotBox(EntityType.SNOW_GOLEM, HeadshotBox.builder().width(10.0).offsetY(20.5).build());
		
		// Turtle
		addHeadshotBox(EntityType.TURTLE, HeadshotBox.builder().width(6.0).height(5.0).offsetY(1.0).offsetZ(10.0).rotateYaw(true).build());
		
		// Iron Golem
		addHeadshotBox(EntityType.IRON_GOLEM, HeadshotBox.builder().width(8.0).height(10.0).offsetY(33.0).offsetZ(3.5).rotateYaw(true).build());
		
		// Phantom
		addHeadshotBox(EntityType.PHANTOM, HeadshotBox.builder().width(6.0).height(3.0).offsetY(1.5).offsetZ(6.5).rotatePitch(true).rotateYaw(true).build());
		
		// Hoglins
		addHeadshotBox(EntityType.HOGLIN, HeadshotBox.builder().width(14.0).height(16.0).offsetY(7.0).offsetZ(19.0).rotateYaw(true).build());
		addHeadshotBox(EntityType.ZOGLIN, HeadshotBox.builder().width(14.0).height(16.0).offsetY(7.0).offsetZ(19.0).rotateYaw(true).build());
		
		// Player is handled separately in BoundingBoxManager code, no JSON needed.
	}
}