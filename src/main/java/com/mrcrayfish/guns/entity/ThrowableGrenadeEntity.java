package com.mrcrayfish.guns.entity;

import com.mrcrayfish.guns.Config;
import com.mrcrayfish.guns.init.ModEntities;
import com.mrcrayfish.guns.init.ModItems;
import com.mrcrayfish.guns.init.ModSounds;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

/**
 * Author: MrCrayfish
 */
public class ThrowableGrenadeEntity extends ThrowableItemEntity
{
    public float rotation;
    public float prevRotation;

    public ThrowableGrenadeEntity(EntityType<? extends ThrowableItemEntity> entityType, Level worldIn)
    {
        super(entityType, worldIn);
        bounceSound = ModSounds.ENTITY_GRENADE_BOUNCE.get();
        useCustomBounceSound = true;
    }

    public ThrowableGrenadeEntity(EntityType<? extends ThrowableItemEntity> entityType, Level world, LivingEntity entity)
    {
        super(entityType, world, entity);
        this.setShouldBounce(true);
        this.setGravityVelocity(0.05F);
        this.setItem(new ItemStack(ModItems.GRENADE.get()));
        this.setMaxLife(20 * 3);
        bounceSound = ModSounds.ENTITY_GRENADE_BOUNCE.get();
        useCustomBounceSound = true;
    }

    public ThrowableGrenadeEntity(Level world, LivingEntity entity, int timeLeft)
    {
        super(ModEntities.THROWABLE_GRENADE.get(), world, entity);
        this.setShouldBounce(true);
        this.setGravityVelocity(0.05F);
        this.setItem(new ItemStack(ModItems.GRENADE.get()));
        this.setMaxLife(timeLeft);
        bounceSound = ModSounds.ENTITY_GRENADE_BOUNCE.get();
        useCustomBounceSound = true;
    }

    @Override
    protected void defineSynchedData()
    {
    }

    @Override
    public void tick()
    {
        super.tick();
        this.prevRotation = this.rotation;
        double speed = this.getDeltaMovement().length();
        if (speed > 0.1)
        {
            this.rotation += speed * 50;
        }
        if (this.level.isClientSide)
        {
            this.level.addParticle(ParticleTypes.SMOKE, true, this.getX(), this.getY() + 0.25, this.getZ(), 0, 0, 0);
        }
    }

    @Override
    public void onDeath()
    {
    	double explosionDamage = (Config.COMMON.grenades.grenadeDamage.get());
    	GrenadeEntity.createExplosion(this, Config.COMMON.grenades.explosionRadius.get().floatValue(), (float) (explosionDamage), false);
    }
}
