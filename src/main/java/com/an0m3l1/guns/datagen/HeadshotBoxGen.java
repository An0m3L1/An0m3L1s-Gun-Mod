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
		addHeadshotBox(EntityType.ZOMBIE, HeadshotBox.builder().general(HeadshotBox.General.builder().headWidth(8.0).y(24.0).build()).child(0.75, 0.75, 1.0, 0.5, 1.0).build());
		addHeadshotBox(EntityType.DROWNED, HeadshotBox.builder().general(HeadshotBox.General.builder().headWidth(8.0).y(24.0).build()).child(0.75, 0.75, 1.0, 0.5, 1.0).build());
		addHeadshotBox(EntityType.ZOMBIFIED_PIGLIN, HeadshotBox.builder().general(HeadshotBox.General.builder().headWidth(8.0).y(24.0).build()).child(0.75, 0.75, 1.0, 0.5, 1.0).build());
		addHeadshotBox(EntityType.HUSK, HeadshotBox.builder().general(HeadshotBox.General.builder().headWidth(8.0).y(24.0).build()).child(0.75, 0.75, 1.0, 0.5, 1.0).build());
		addHeadshotBox(EntityType.PIGLIN, HeadshotBox.builder().general(HeadshotBox.General.builder().headWidth(8.0).y(24.0).build()).child(0.75, 0.75, 1.0, 0.5, 1.0).build());
		
		// Skeletons
		addHeadshotBox(EntityType.SKELETON, HeadshotBox.builder().general(HeadshotBox.General.builder().headWidth(8.0).y(24.0).build()).build());
		addHeadshotBox(EntityType.STRAY, HeadshotBox.builder().general(HeadshotBox.General.builder().headWidth(8.0).y(24.0).build()).build());
		
		// Creeper
		addHeadshotBox(EntityType.CREEPER, HeadshotBox.builder().general(HeadshotBox.General.builder().headWidth(8.0).y(18.0).build()).build());
		
		// Spider
		addHeadshotBox(EntityType.SPIDER, HeadshotBox.builder().general(HeadshotBox.General.builder().headWidth(8.0).headHeight(8.0).y(5.0).z(7.0).rotateYaw(true).build()).build());
		
		// Villagers
		HeadshotBox.General villager = HeadshotBox.General.builder().headWidth(8.0).headHeight(9.0).y(23.0).build();
		addHeadshotBox(EntityType.VILLAGER, HeadshotBox.builder().general(villager).build());
		addHeadshotBox(EntityType.ZOMBIE_VILLAGER, HeadshotBox.builder().general(villager).build());
		addHeadshotBox(EntityType.VINDICATOR, HeadshotBox.builder().general(villager).build());
		addHeadshotBox(EntityType.EVOKER, HeadshotBox.builder().general(villager).build());
		addHeadshotBox(EntityType.PILLAGER, HeadshotBox.builder().general(villager).build());
		addHeadshotBox(EntityType.ILLUSIONER, HeadshotBox.builder().general(villager).build());
		addHeadshotBox(EntityType.WANDERING_TRADER, HeadshotBox.builder().general(villager).build());
		addHeadshotBox(EntityType.WITCH, HeadshotBox.builder().general(villager).build());
		
		// Animals
		addHeadshotBox(EntityType.SHEEP, HeadshotBox.builder().general(HeadshotBox.General.builder().headWidth(7.5).headHeight(8.0).y(15.0).z(9.5).rotateYaw(true).build()).build());
		addHeadshotBox(EntityType.CHICKEN, HeadshotBox.builder().general(HeadshotBox.General.builder().headWidth(4.0).headHeight(6.0).y(9.0).z(5.0).rotateYaw(true).build()).build());
		addHeadshotBox(EntityType.COW, HeadshotBox.builder().general(HeadshotBox.General.builder().headWidth(7.5).headHeight(8.0).y(16.0).z(10.5).rotateYaw(true).build()).build());
		addHeadshotBox(EntityType.MOOSHROOM, HeadshotBox.builder().general(HeadshotBox.General.builder().headWidth(7.5).headHeight(8.0).y(16.0).z(10.5).rotateYaw(true).build()).build());
		addHeadshotBox(EntityType.PIG, HeadshotBox.builder().general(HeadshotBox.General.builder().headWidth(8.0).headHeight(8.0).y(10.0).z(10.0).rotateYaw(true).build()).build());
		
		// Horses
		addHeadshotBox(EntityType.HORSE, HeadshotBox.builder().general(HeadshotBox.General.builder().headWidth(10.0).y(26.0).z(16.0).rotateYaw(true).build()).build());
		addHeadshotBox(EntityType.SKELETON_HORSE, HeadshotBox.builder().general(HeadshotBox.General.builder().headWidth(10.0).y(26.0).z(16.0).rotateYaw(true).build()).build());
		addHeadshotBox(EntityType.DONKEY, HeadshotBox.builder().general(HeadshotBox.General.builder().headWidth(7.5).headHeight(8.0).y(20.0).z(13.0).rotateYaw(true).build()).build());
		addHeadshotBox(EntityType.MULE, HeadshotBox.builder().general(HeadshotBox.General.builder().headWidth(7.5).headHeight(8.0).y(21.0).z(14.0).rotateYaw(true).build()).build());
		
		// Llamas
		addHeadshotBox(EntityType.LLAMA, HeadshotBox.builder().general(HeadshotBox.General.builder().headWidth(8.0).y(26.0).z(10.0).rotateYaw(true).build()).build());
		addHeadshotBox(EntityType.TRADER_LLAMA, HeadshotBox.builder().general(HeadshotBox.General.builder().headWidth(8.0).y(26.0).z(10.0).rotateYaw(true).build()).build());
		
		// Polar Bear
		addHeadshotBox(EntityType.POLAR_BEAR, HeadshotBox.builder().general(HeadshotBox.General.builder().headWidth(9.0).headHeight(12.0).y(20.0).rotateYaw(true).build()).build());
		
		// Snow Golem
		addHeadshotBox(EntityType.SNOW_GOLEM, HeadshotBox.builder().general(HeadshotBox.General.builder().headWidth(10.0).y(20.5).build()).build());
		
		// Turtle
		addHeadshotBox(EntityType.TURTLE, HeadshotBox.builder().general(HeadshotBox.General.builder().headWidth(6.0).headHeight(5.0).y(1.0).z(10.0).rotateYaw(true).build()).build());
		
		// Iron Golem
		addHeadshotBox(EntityType.IRON_GOLEM, HeadshotBox.builder().general(HeadshotBox.General.builder().headWidth(8.0).headHeight(10.0).y(33.0).z(3.5).rotateYaw(true).build()).build());
		
		// Phantom
		addHeadshotBox(EntityType.PHANTOM, HeadshotBox.builder().general(HeadshotBox.General.builder().headWidth(6.0).headHeight(3.0).y(1.5).z(6.5).rotatePitch(true).rotateYaw(true).build()).build());
		
		// Hoglins
		addHeadshotBox(EntityType.HOGLIN, HeadshotBox.builder().general(HeadshotBox.General.builder().headWidth(14.0).headHeight(16.0).y(7.0).z(19.0).rotateYaw(true).build()).build());
		addHeadshotBox(EntityType.ZOGLIN, HeadshotBox.builder().general(HeadshotBox.General.builder().headWidth(14.0).headHeight(16.0).y(7.0).z(19.0).rotateYaw(true).build()).build());
		
		// Player is handled separately in HeadshotBoxManager code, no JSON needed.
	}
}