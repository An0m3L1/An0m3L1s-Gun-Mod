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
		addHeadshotBox(EntityType.ALLAY, HeadshotBox.builder()
				.general(5.0, 5.0, 0.0, 4.5, 0.0, false, false)
				.build());
		addHeadshotBox(EntityType.AXOLOTL, HeadshotBox.builder()
				.general(7.0, 5.0, 0.0, 2.0, 6.5, true, true)
				.child(0.5, 0.5, 1.0, 0.5, 0.5)
				.build());
		addHeadshotBox(EntityType.BLAZE, HeadshotBox.builder()
				.general(8.0, 8.0, 0.0, 20.0, 0.0, false, false)
				.build());
		addHeadshotBox(EntityType.CAT, HeadshotBox.builder()
				.general(4.0, 3.25, 0.0, 5.5, 7.625, false, true)
				.child(0.75, 0.75, 1.0, 0.55, 0.4375)
				.build());
		addHeadshotBox(EntityType.CAVE_SPIDER, HeadshotBox.builder()
				.general(5.5, 5.5, 0.0, 3.6125, 4.875, false, true)
				.build());
		addHeadshotBox(EntityType.CHICKEN, HeadshotBox.builder()
				.general(4.0, 6.0, 0.0, 9.0, 5.0, false, true)
				.child(1.0, 1.0, 1.0, 0.45, 0.6)
				.build());
		addHeadshotBox(EntityType.COW, HeadshotBox.builder()
				.general(8.0, 8.0, 0.0, 16.0, 11.0, false, true)
				.child(1.0, 1.0, 1.0, 0.375, 0.625)
				.build());
		addHeadshotBox(EntityType.CREEPER, HeadshotBox.builder()
				.general(8.0, 8.0, 0.0, 18.0, 0.0, false, false)
				.build());
		addHeadshotBox(EntityType.DOLPHIN, HeadshotBox.builder()
				.general(8.0, 7.0, 0.0, 2.0, 9.0, true, true)
				.build());
		addHeadshotBox(EntityType.DONKEY, HeadshotBox.builder()
				.general(7.5, 8.0, 0.0, 20.0, 13.0, false, true)
				.child(0.5, 0.5, 1.0, 0.65, 0.5)
				.build());
		addHeadshotBox(EntityType.DROWNED, HeadshotBox.builder()
				.general(8.5, 8.5, 0.0, 23.75, 0.0, false, false)
				.child(0.75, 0.75, 1.0, 0.5, 1.0)
				.build());
		addHeadshotBox(EntityType.EVOKER, HeadshotBox.builder()
				.general(7.5, 9.4, 0.0, 22.5, 0.0, false, false)
				.build());
		addHeadshotBox(EntityType.FOX, HeadshotBox.builder()
				.general(8.0, 6.0, 0.0, 3.5, 6.0, false, true)
				.child(0.75, 0.75, 1.0, 0.75, 0.325)
				.build());
		addHeadshotBox(EntityType.FROG, HeadshotBox.builder()
				.general(6.0, 4.0, 0.0, 4.0, 3.0, false, true)
				.build());
		addHeadshotBox(EntityType.GIANT, HeadshotBox.builder()
				.general(48.0, 48.0, 0.0, 144.0, 0.0, false, false)
				.child(0.75, 0.75, 1.0, 0.5, 1.0)
				.build());
		addHeadshotBox(EntityType.GOAT, HeadshotBox.builder()
				.general(6.0, 10.0, 0.5, 11.0, 11.0, false, true)
				.child(0.7, 0.5, 0.5, 0.5, 0.55)
				.build());
		addHeadshotBox(EntityType.HOGLIN, HeadshotBox.builder()
				.general(14.0, 16.0, 0.0, 7.0, 19.0, false, true)
				.child(0.75, 0.85, 1.0, 0.5, 0.575)
				.build());
		addHeadshotBox(EntityType.HORSE, HeadshotBox.builder()
				.general(10.0, 8.0, 0.0, 26.0, 16.0, false, true)
				.child(0.5, 0.5, 1.0, 0.65, 0.5)
				.build());
		addHeadshotBox(EntityType.HUSK, HeadshotBox.builder()
				.general(8.5, 8.5, 0.0, 25.5, 0.0, false, false)
				.child(0.75, 0.75, 1.0, 0.5, 1.0)
				.build());
		addHeadshotBox(EntityType.ILLUSIONER, HeadshotBox.builder()
				.general(7.5, 9.4, 0.0, 22.5, 0.0, false, false)
				.build());
		addHeadshotBox(EntityType.IRON_GOLEM, HeadshotBox.builder()
				.general(8.0, 10.0, 0.0, 33.0, 3.5, false, true)
				.build());
		addHeadshotBox(EntityType.LLAMA, HeadshotBox.builder()
				.general(8.0, 8.0, 0.0, 26.0, 10.0, false, true)
				.child(0.75, 0.75, 1.0, 0.45, 0.5)
				.build());
		addHeadshotBox(EntityType.MOOSHROOM, HeadshotBox.builder()
				.general(8.0, 8.0, 0.0, 16.0, 11.0, false, true)
				.child(1.0, 1.0, 1.0, 0.375, 0.625)
				.build());
		addHeadshotBox(EntityType.MULE, HeadshotBox.builder()
				.general(7.5, 8.0, 0.0, 21.0, 14.0, false, true)
				.child(0.5, 0.5, 1.0, 0.65, 0.55)
				.build());
		addHeadshotBox(EntityType.OCELOT, HeadshotBox.builder()
				.general(5.0, 4.0, 0.0, 7.0, 9.5, false, true)
				.child(0.75, 0.75, 1.0, 0.5375, 0.4375)
				.build());
		addHeadshotBox(EntityType.PANDA, HeadshotBox.builder()
				.general(13.0, 10.0, 0.0, 7.5, 16.5, false, true)
				.child(0.55, 0.55, 1.0, 0.275, 0.4)
				.build());
		addHeadshotBox(EntityType.PARROT, HeadshotBox.builder()
				.general(3.0, 3.5, 0.0, 7.0, 4.0, false, true)
				.build());
		addHeadshotBox(EntityType.PHANTOM, HeadshotBox.builder()
				.general(7.0, 3.0, -0.5, 1.5, 6.5, true, true)
				.build());
		addHeadshotBox(EntityType.PIG, HeadshotBox.builder()
				.general(8.0, 8.0, 0.0, 8.0, 10.0, false, true)
				.child(1.0, 1.0, 1.0, 0.5, 0.6)
				.build());
		addHeadshotBox(EntityType.PIGLIN, HeadshotBox.builder()
				.general(8.0, 8.0, 0.0, 24.0, 0.0, false, false)
				.child(0.75, 0.75, 1.0, 0.5, 1.0)
				.build());
		addHeadshotBox(EntityType.PIGLIN_BRUTE, HeadshotBox.builder()
				.general(8.0, 8.0, 0.0, 24.0, 0.0, false, false)
				.build());
		addHeadshotBox(EntityType.PILLAGER, HeadshotBox.builder()
				.general(7.5, 9.4, 0.0, 22.5, 0.0, false, false)
				.build());
		addHeadshotBox(EntityType.POLAR_BEAR, HeadshotBox.builder()
				.general(8.5, 8.25, 0.0, 12.0, 20.0, false, true)
				.child(0.75, 0.75, 1.0, 0.4, 0.5)
				.build());
		addHeadshotBox(EntityType.RABBIT, HeadshotBox.builder()
				.general(3.0, 2.5, 0.0, 4.75, 2.125, false, true)
				.child(0.925, 0.925, 1.0, 0.525, 0.425)
				.build());
		addHeadshotBox(EntityType.RAVAGER, HeadshotBox.builder()
				.general(18.0, 21.0, 0.0, 14.0, 18.0, false, true)
				.build());
		addHeadshotBox(EntityType.SHEEP, HeadshotBox.builder()
				.general(7.25, 7.25, 0.0, 15.375, 9.0, false, true)
				.child(1.0, 1.0, 1.0, 0.475, 0.55)
				.build());
		addHeadshotBox(EntityType.SKELETON, HeadshotBox.builder()
				.general(8.0, 8.0, 0.0, 24.0, 0.0, false, false)
				.build());
		addHeadshotBox(EntityType.SKELETON_HORSE, HeadshotBox.builder()
				.general(10.0, 8.0, 0.0, 23.0, 16.0, false, true)
				.child(0.5, 0.5, 1.0, 0.65, 0.5)
				.build());
		addHeadshotBox(EntityType.SNOW_GOLEM, HeadshotBox.builder()
				.general(10.0, 10.0, 0.0, 20.5, 0.0, false, false)
				.build());
		addHeadshotBox(EntityType.SPIDER, HeadshotBox.builder()
				.general(8.0, 8.0, 0.0, 7.0, 7.0, false, true)
				.build());
		addHeadshotBox(EntityType.STRAY, HeadshotBox.builder()
				.general(8.0, 8.0, 0.0, 24.0, 0.0, false, false)
				.build());
		addHeadshotBox(EntityType.STRIDER, HeadshotBox.builder()
				.general(16.0, 14.0, 0.0, 14.0, 0.0, false, false)
				.child(0.5, 0.5, 1.0, 0.5, 1.0)
				.build());
		addHeadshotBox(EntityType.TRADER_LLAMA, HeadshotBox.builder()
				.general(8.0, 8.0, 0.0, 26.0, 10.0, false, true)
				.child(0.75, 0.75, 1.0, 0.45, 0.5)
				.build());
		addHeadshotBox(EntityType.TURTLE, HeadshotBox.builder()
				.general(6.0, 5.0, 0.0, 1.0, 10.0, false, true)
				.child(0.175, 0.175, 0.175, 0.175, 0.175)
				.build());
		addHeadshotBox(EntityType.VEX, HeadshotBox.builder()
				.general(4.0, 4.0, 0.0, 7.5, 0.0, false, false)
				.build());
		addHeadshotBox(EntityType.VILLAGER, HeadshotBox.builder()
				.general(7.5, 9.4, 0.0, 22.5, 0.0, false, false)
				.child(0.5, 0.5, 1.0, 0.5, 1.0)
				.build());
		addHeadshotBox(EntityType.VINDICATOR, HeadshotBox.builder()
				.general(7.5, 9.4, 0.0, 22.5, 0.0, false, false)
				.build());
		addHeadshotBox(EntityType.WANDERING_TRADER, HeadshotBox.builder()
				.general(7.5, 9.4, 0.0, 22.5, 0.0, false, false)
				.build());
		addHeadshotBox(EntityType.WARDEN, HeadshotBox.builder()
				.general(8.0, 8.0, 0.0, 20.0, 6.0, false, true)
				.build());
		addHeadshotBox(EntityType.WITCH, HeadshotBox.builder()
				.general(7.5, 9.4, 0.0, 22.5, 0.0, false, false)
				.build());
		addHeadshotBox(EntityType.WITHER, HeadshotBox.builder()
				.general(16.0, 16.0, 0.0, 40.0, 0.0, false, false)
				.build());
		addHeadshotBox(EntityType.WITHER_SKELETON, HeadshotBox.builder()
				.general(9.5, 9.5, 0.0, 28.75, 0.0, false, false)
				.build());
		addHeadshotBox(EntityType.WOLF, HeadshotBox.builder()
				.general(6.0, 6.0, 0.0, 7.5, 8.0, false, true)
				.child(1.0, 1.0, 1.0, 0.3375, 0.75)
				.build());
		addHeadshotBox(EntityType.ZOGLIN, HeadshotBox.builder()
				.general(14.0, 16.0, 0.0, 7.0, 19.0, false, true)
				.build());
		addHeadshotBox(EntityType.ZOMBIE, HeadshotBox.builder()
				.general(8.5, 8.5, 0.0, 23.75, 0.0, false, false)
				.child(0.75, 0.75, 1.0, 0.5, 1.0)
				.build());
		addHeadshotBox(EntityType.ZOMBIE_HORSE, HeadshotBox.builder()
				.general(10.0, 8.0, 0.0, 23.0, 16.0, false, true)
				.build());
		addHeadshotBox(EntityType.ZOMBIE_VILLAGER, HeadshotBox.builder()
				.general(8.0, 10.0, 0.0, 24.0, 0.0, false, false)
				.build());
		addHeadshotBox(EntityType.ZOMBIFIED_PIGLIN, HeadshotBox.builder()
				.general(8.0, 8.0, 0.0, 24.0, 0.0, false, false)
				.child(0.75, 0.75, 1.0, 0.5, 1.0)
				.build());
	}
}