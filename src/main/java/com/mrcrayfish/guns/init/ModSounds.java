package com.mrcrayfish.guns.init;

import com.mrcrayfish.guns.Reference;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModSounds 
{
	public static final DeferredRegister<SoundEvent> REGISTER = DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, Reference.MOD_ID);

	// Default sounds from CGM:
	public static final RegistryObject<SoundEvent> ITEM_PISTOL_FIRE = register("item.pistol.fire");
	public static final RegistryObject<SoundEvent> ITEM_PISTOL_SILENCED_FIRE = register("item.pistol.silenced_fire");
	public static final RegistryObject<SoundEvent> ITEM_PISTOL_ENCHANTED_FIRE = register("item.pistol.enchanted_fire");
	public static final RegistryObject<SoundEvent> ITEM_PISTOL_RELOAD = register("item.pistol.reload");
	public static final RegistryObject<SoundEvent> ITEM_PISTOL_COCK = register("item.pistol.cock");
	public static final RegistryObject<SoundEvent> ITEM_SHOTGUN_FIRE = register("item.shotgun.fire");
	public static final RegistryObject<SoundEvent> ITEM_SHOTGUN_SILENCED_FIRE = register("item.shotgun.silenced_fire");
	public static final RegistryObject<SoundEvent> ITEM_SHOTGUN_ENCHANTED_FIRE = register("item.shotgun.enchanted_fire");
	public static final RegistryObject<SoundEvent> ITEM_SHOTGUN_COCK = register("item.shotgun.cock");
	public static final RegistryObject<SoundEvent> ITEM_RIFLE_FIRE = register("item.rifle.fire");
	public static final RegistryObject<SoundEvent> ITEM_RIFLE_SILENCED_FIRE = register("item.rifle.silenced_fire");
	public static final RegistryObject<SoundEvent> ITEM_RIFLE_ENCHANTED_FIRE = register("item.rifle.enchanted_fire");
	public static final RegistryObject<SoundEvent> ITEM_RIFLE_COCK = register("item.rifle.cock");
	public static final RegistryObject<SoundEvent> ITEM_ASSAULT_RIFLE_FIRE = register("item.assault_rifle.fire");
	public static final RegistryObject<SoundEvent> ITEM_ASSAULT_RIFLE_SILENCED_FIRE = register("item.assault_rifle.silenced_fire");
	public static final RegistryObject<SoundEvent> ITEM_ASSAULT_RIFLE_ENCHANTED_FIRE = register("item.assault_rifle.enchanted_fire");
	public static final RegistryObject<SoundEvent> ITEM_ASSAULT_RIFLE_COCK = register("item.assault_rifle.cock");
	public static final RegistryObject<SoundEvent> ITEM_GRENADE_LAUNCHER_FIRE = register("item.grenade_launcher.fire");
	public static final RegistryObject<SoundEvent> ITEM_BAZOOKA_FIRE = register("item.bazooka.fire");
	public static final RegistryObject<SoundEvent> ITEM_MINI_GUN_FIRE = register("item.mini_gun.fire");
	public static final RegistryObject<SoundEvent> ITEM_MINI_GUN_ENCHANTED_FIRE = register("item.mini_gun.enchanted_fire");
	public static final RegistryObject<SoundEvent> ITEM_MACHINE_PISTOL_FIRE = register("item.machine_pistol.fire");
	public static final RegistryObject<SoundEvent> ITEM_MACHINE_PISTOL_SILENCED_FIRE = register("item.machine_pistol.silenced_fire");
	public static final RegistryObject<SoundEvent> ITEM_MACHINE_PISTOL_ENCHANTED_FIRE = register("item.machine_pistol.enchanted_fire");
	public static final RegistryObject<SoundEvent> ITEM_HEAVY_RIFLE_FIRE = register("item.heavy_rifle.fire");
	public static final RegistryObject<SoundEvent> ITEM_HEAVY_RIFLE_SILENCED_FIRE = register("item.heavy_rifle.silenced_fire");
	public static final RegistryObject<SoundEvent> ITEM_HEAVY_RIFLE_ENCHANTED_FIRE = register("item.heavy_rifle.enchanted_fire");
	public static final RegistryObject<SoundEvent> ITEM_HEAVY_RIFLE_COCK = register("item.heavy_rifle.cock");
	public static final RegistryObject<SoundEvent> ITEM_GRENADE_PIN = register("item.grenade.pin");
	public static final RegistryObject<SoundEvent> ENTITY_STUN_GRENADE_EXPLOSION = register("entity.stun_grenade.explosion");
	public static final RegistryObject<SoundEvent> ENTITY_STUN_GRENADE_RING = register("entity.stun_grenade.ring");
	public static final RegistryObject<SoundEvent> UI_WEAPON_ATTACH = register("ui.weapon.attach");
	
	
	// The following sounds were added in CGM Expanded:
	public static final RegistryObject<SoundEvent> ITEM_EMPTY_CLICK = register("item.empty_click");
	public static final RegistryObject<SoundEvent> ITEM_SELECTOR_SWITCH = register("item.selector_switch");
	
	public static final RegistryObject<SoundEvent> ITEM_MACHINE_PISTOL_COCK = register("item.machine_pistol.cock");
	
	public static final RegistryObject<SoundEvent> ITEM_SHOTGUN_LOADSHELL = register("item.shotgun.load_shell");
	
	public static final RegistryObject<SoundEvent> ITEM_GRENADE_THROW = register("item.grenade.throw");
	
	public static final RegistryObject<SoundEvent> FOLEY_SMALL_RATTLE = register("item.foley.small_rattle");
	
	public static final RegistryObject<SoundEvent> FOLEY_MEDIUM_RATTLE = register("item.foley.medium_rattle");
	public static final RegistryObject<SoundEvent> FOLEY_MEDIUM_SHOULDER = register("item.foley.medium_shoulder");
	
	public static final RegistryObject<SoundEvent> FOLEY_LARGE_RATTLE = register("item.foley.large_rattle");
	
	public static final RegistryObject<SoundEvent> FOLEY_CARBINE_SHOULDER = register("item.foley.carbine_shoulder");
	
	public static final RegistryObject<SoundEvent> ENTITY_GRENADE_BOUNCE = register("entity.grenade.bounce");

	// Legacy foley sounds, which will be phased out:
	public static final RegistryObject<SoundEvent> ITEM_PISTOL1_DRAW = register("item.foley.pistol1_draw");
	public static final RegistryObject<SoundEvent> ITEM_SMG1_DRAW = register("item.foley.smg1_draw");
	public static final RegistryObject<SoundEvent> ITEM_SMG2_DRAW = register("item.foley.smg2_draw");
	public static final RegistryObject<SoundEvent> ITEM_AR1_DRAW = register("item.foley.ar1_draw");
	public static final RegistryObject<SoundEvent> ITEM_AR2_DRAW = register("item.foley.ar2_draw");
	public static final RegistryObject<SoundEvent> ITEM_AR3_DRAW_START = register("item.foley.ar3_draw_start");
	public static final RegistryObject<SoundEvent> ITEM_AR3_DRAW_END = register("item.foley.ar3_draw_end");
	public static final RegistryObject<SoundEvent> ITEM_AR4_DRAW = register("item.foley.ar4_draw");
	public static final RegistryObject<SoundEvent> ITEM_RIFLE1_DRAW = register("item.foley.rifle1_draw");
	public static final RegistryObject<SoundEvent> ITEM_RIFLE2_DRAW = register("item.foley.rifle2_draw");
	public static final RegistryObject<SoundEvent> ITEM_RIFLE3_DRAW = register("item.foley.rifle3_draw");
	public static final RegistryObject<SoundEvent> ITEM_RIFLE4_DRAW = register("item.foley.rifle4_draw");
	public static final RegistryObject<SoundEvent> ITEM_SNIPER1_DRAW = register("item.foley.sniper1_draw");
	public static final RegistryObject<SoundEvent> ITEM_SNIPER2_DRAW = register("item.foley.sniper2_draw");
	public static final RegistryObject<SoundEvent> ITEM_SNIPER3_DRAW = register("item.foley.sniper3_draw");
	public static final RegistryObject<SoundEvent> ITEM_SNIPER4_DRAW = register("item.foley.sniper4_draw");
	

	private static RegistryObject<SoundEvent> register(String key)
	{
		return REGISTER.register(key, () -> new SoundEvent(new ResourceLocation(Reference.MOD_ID, key)));
	}
}
