package com.mrcrayfish.guns.mixin.client;

import dev.xylonity.explosiveenhancement.ExplosiveEnhancement;
import dev.xylonity.explosiveenhancement.api.ExplosiveConfig;
import dev.xylonity.explosiveenhancement.config.ExplosiveValues;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.Explosion.BlockInteraction;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.mrcrayfish.guns.GunMod;

@Mixin(Explosion.class)
public abstract class ExplosionMixin {

    @Shadow @Final private Level level;
    @Shadow @Final private double x;
    @Shadow @Final private double y;
    @Shadow @Final private double z;
    @Shadow @Final private float radius;
    private boolean isUnderWater = false;
    @Shadow @Final BlockInteraction blockInteraction;

    private boolean damagesBlocks() {
       return this.blockInteraction != BlockInteraction.NONE;
    }

    @Inject(
       method = {"finalizeExplosion"},
       at = {@At("HEAD")}
    )
    private void onFinalizeExplosion(boolean pSpawnParticles, CallbackInfo ci) {
       if (ExplosiveValues.modEnabled) {
          BlockPos pos = new BlockPos(this.x, this.y, this.z);
          if (ExplosiveValues.underwaterExplosions && this.level.getFluidState(pos).is(FluidTags.WATER)) {
             this.isUnderWater = true;
             if (ExplosiveValues.debugLogs) {
                ExplosiveEnhancement.LOGGER.info("particle is underwater!");
             }
          }
       }

    }

    @Redirect(
      method = {"finalizeExplosion"},
      at = @At(
   value = "INVOKE",
   target = "Lnet/minecraft/world/level/Level;addParticle(Lnet/minecraft/core/particles/ParticleOptions;DDDDDD)V"
)
   )
   private void redirectAddParticle(Level level, ParticleOptions particleOptions, double x, double y, double z, double dx, double dy, double dz) {
   
    	if (ExplosiveValues.modEnabled) {
         if (ExplosiveValues.debugLogs) {
            ExplosiveEnhancement.LOGGER.info("finalizeExplosion redirectAddParticle has been called!");
         }

      ExplosiveConfig.spawnParticles(level, x, y, z, this.radius, this.isUnderWater, this.damagesBlocks());
      } else {
        level.addParticle(particleOptions, x, y, z, dx, dy, dz);
      }
  }
}