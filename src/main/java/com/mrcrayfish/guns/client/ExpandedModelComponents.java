package com.mrcrayfish.guns.client;

import com.mrcrayfish.guns.Reference;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.ModelEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

/**
 * Author: MrCrayfish
 */
@Mod.EventBusSubscriber(modid = Reference.MOD_ID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public enum ExpandedModelComponents
{
    PISTOL_BASE("gun/pistol/pistol_base"),
    PISTOL_SLIDE("gun/pistol/pistol_slide"),
    PISTOL_SIGHTMOUNT("gun/pistol/pistol_sightmount"),
    PISTOL_MAGAZINE("gun/pistol/pistol_magazine"),
    PISTOL_EXTENDED_MAG("gun/pistol/pistol_extended_mag"),
    
    SHOTGUN_BASE("gun/shotgun/shotgun_base"),
    SHOTGUN_BASE_1("gun/shotgun/shotgun_base_1"),
    SHOTGUN_HEAT_SHIELD("gun/shotgun/shotgun_heat_shield"),
    SHOTGUN_HEAT_SHIELD_1("gun/shotgun/shotgun_heat_shield_1"),
    SHOTGUN_BOLT("gun/shotgun/shotgun_bolt"),
    SHOTGUN_SHELL("gun/shotgun/shotgun_shell"),
    
    ASSAULT_RIFLE_BASE("gun/assault_rifle/assault_rifle_base"),
    ASSAULT_RIFLE_SIGHTS("gun/assault_rifle/assault_rifle_sights"),
    ASSAULT_RIFLE_BOLT_HANDLE("gun/assault_rifle/assault_rifle_bolt_handle"),
    ASSAULT_RIFLE_MAGAZINE("gun/assault_rifle/assault_rifle_magazine"),
    ASSAULT_RIFLE_LIGHT_MAG("gun/assault_rifle/assault_rifle_light_mag"),
    ASSAULT_RIFLE_EXTENDED_MAG("gun/assault_rifle/assault_rifle_extended_mag"),
    
    RIFLE_BASE("gun/rifle/rifle_base"),
    RIFLE_BASE_1("gun/rifle/rifle_base_1"),
    RIFLE_SIGHTS("gun/rifle/rifle_rear_sight"),
    RIFLE_BOLT("gun/rifle/rifle_bolt"),
    RIFLE_MAGAZINE("gun/rifle/rifle_magazine"),
    RIFLE_LIGHT_MAG("gun/rifle/rifle_light_mag"),
    RIFLE_EXTENDED_MAG("gun/rifle/rifle_extended_mag"),
    
    MACHINE_PISTOL_BASE("gun/machine_pistol/machine_pistol_base"),
    MACHINE_PISTOL_SIGHTS("gun/machine_pistol/machine_pistol_rear_sight"),
    MACHINE_PISTOL_BOLT("gun/machine_pistol/machine_pistol_bolt_handle"),
    MACHINE_PISTOL_MAGAZINE("gun/machine_pistol/machine_pistol_magazine"),
    MACHINE_PISTOL_LIGHT_MAG("gun/machine_pistol/machine_pistol_light_mag"),
    MACHINE_PISTOL_EXTENDED_MAG("gun/machine_pistol/machine_pistol_extended_mag");

    /**
     * The location of an item model in the [MOD_ID]/models/special/[NAME] folder
     */
    private final ResourceLocation modelLocation;

    /**
     * Cached model
     */
    private BakedModel cachedModel;

    /**
     * Sets the model's location
     *
     * @param modelName name of the model file
     */
    ExpandedModelComponents(String modelName)
    {
        this.modelLocation = new ResourceLocation(Reference.MOD_ID, "special/" + modelName);
    }

    /**
     * Gets the model
     *
     * @return isolated model
     */
    public BakedModel getModel()
    {
        if(this.cachedModel == null)
        {
            this.cachedModel = Minecraft.getInstance().getModelManager().getModel(this.modelLocation);
        }
        return this.cachedModel;
    }

    /**
     * Registers the special models into the Forge Model Bakery. This is only called once on the
     * load of the game.
     */
    @SubscribeEvent
    public static void registerAdditional(ModelEvent.RegisterAdditional event)
    {
        for(ExpandedModelComponents model : values())
        {
            event.register(model.modelLocation);
        }
    }

    /**
     * Clears the cached BakedModel since it's been rebuilt. This is needed since the models may
     * have changed when a resource pack was applied, or if resources are reloaded.
     */
    @SubscribeEvent
    public static void onBake(ModelEvent.BakingCompleted event)
    {
        for(ExpandedModelComponents model : values())
        {
            model.cachedModel = null;
        }
    }
}
