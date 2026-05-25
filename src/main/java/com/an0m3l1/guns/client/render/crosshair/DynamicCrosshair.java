package com.an0m3l1.guns.client.render.crosshair;

import com.an0m3l1.guns.GunConfig;
import com.an0m3l1.guns.GunMod;
import com.an0m3l1.guns.client.DotRenderMode;
import com.an0m3l1.guns.client.handler.AimingHandler;
import com.an0m3l1.guns.common.Gun;
import com.an0m3l1.guns.common.tracker.SpreadTracker;
import com.an0m3l1.guns.item.GunItem;
import com.an0m3l1.guns.util.GunCompositeStatHelper;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import com.mojang.math.Matrix4f;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;

public class DynamicCrosshair extends Crosshair
{
	private static final ResourceLocation DYNAMIC_H = new ResourceLocation(GunMod.MOD_ID, "textures/crosshair/dynamic_h.png");
	private static final ResourceLocation DYNAMIC_V = new ResourceLocation(GunMod.MOD_ID, "textures/crosshair/dynamic_v.png");
	private static final ResourceLocation SHOTGUN_H = new ResourceLocation(GunMod.MOD_ID, "textures/crosshair/shotgun_h.png");
	private static final ResourceLocation SHOTGUN_V = new ResourceLocation(GunMod.MOD_ID, "textures/crosshair/shotgun_v.png");
	private static final ResourceLocation DOT = new ResourceLocation(GunMod.MOD_ID, "textures/crosshair/dot.png");
	
	private float scale;
	private float prevScale;
	private float fireBloom;
	private float prevFireBloom;
	
	private float smoothPenaltyDisplay = 0.0F;
	private float prevSmoothPenaltyDisplay = 0.0F;
	private boolean lastPenaltyState = false;
	
	public static float currentCrosshairSpread = 0.0F;
	public static float currentFinalSpreadTranslate = 0.0F;
	public static float currentScaleSize = 0.0F;
	
	public DynamicCrosshair()
	{
		super(new ResourceLocation(GunMod.MOD_ID, "dynamic"));
	}
	
	@Override
	public void tick()
	{
		this.prevScale = this.scale;
		this.scale *= 0.5F;
		this.prevFireBloom = this.fireBloom;
		if(this.fireBloom > 0.0F)
		{
			float i = (float) GunConfig.COMMON.spreadThreshold.get() / 50.0F;
			this.fireBloom -= Math.min(3.0F / (Math.max(i, 1.0F)), this.fireBloom);
		}
		this.prevSmoothPenaltyDisplay = this.smoothPenaltyDisplay;
		
		Minecraft mc = Minecraft.getInstance();
		if(mc.player != null)
		{
			// Check for sprint/airborne
			boolean currentPenaltyState = GunConfig.COMMON.doSpreadPenalties.get() && (mc.player.isSprinting() || !mc.player.isOnGround());
			// Change between penalty and no penalty states
			if(currentPenaltyState != lastPenaltyState)
			{
				lastPenaltyState = currentPenaltyState;
			}
			float targetPenalty = currentPenaltyState ? 1.0F : 0.0F;
			float change = 1.0F / 5.0F; // 5 ticks for interpolation to correlate to visual change in GunRenderingHandler
			if(this.smoothPenaltyDisplay < targetPenalty)
			{
				this.smoothPenaltyDisplay = Math.min(this.smoothPenaltyDisplay + change, targetPenalty);
			}
			else if(this.smoothPenaltyDisplay > targetPenalty)
			{
				this.smoothPenaltyDisplay = Math.max(this.smoothPenaltyDisplay - change, targetPenalty);
			}
		}
	}
	
	@Override
	public void onGunFired()
	{
		this.prevScale = 0.0F;
		this.scale = 0.5F;
		this.fireBloom = 3.0F;
	}
	
	private float calculateSpread(SpreadTracker spreadTracker, ItemStack heldItem, GunItem gunItem, Gun modifiedGun, float aiming, float currentSpread, float partialTicks)
	{
		float rawProgress = Math.min(currentSpread + (GunConfig.COMMON.doSpreadPenalties.get() ? 1.0F + aiming : 1.0F) / (float) GunConfig.COMMON.maxCount.get(), 1.0F);
		float spreadModifier = rawProgress * Math.min(Mth.lerp(partialTicks, this.prevFireBloom, this.fireBloom), 1.0F);
		
		float baseSpread = GunCompositeStatHelper.getCompositeSpread(heldItem, modifiedGun);
		float minSpread = GunCompositeStatHelper.getCompositeMinSpread(heldItem, modifiedGun);
		boolean isAlwaysSpread = modifiedGun.getGeneral().getAlwaysSpread();
		
		if(modifiedGun.getGeneral().getRestingSpread() == 0.0F)
		{
			if(isAlwaysSpread)
			{
				minSpread = baseSpread;
			}
			else
			{
				minSpread = 0.0F;
			}
		}
		
		float smoothPenaltyDisplay = Mth.lerp(partialTicks, this.prevSmoothPenaltyDisplay, this.smoothPenaltyDisplay);
		float visualMinSpread;
		
		if(isAlwaysSpread)
		{
			visualMinSpread = minSpread;
		}
		else
		{
			float penaltyMinSpread = (baseSpread - minSpread) * 0.5F;
			float currentPenaltyMinSpread = penaltyMinSpread * smoothPenaltyDisplay;
			visualMinSpread = Math.min(minSpread + currentPenaltyMinSpread, baseSpread);
		}
		
		float aimingSpreadMultiplier = Mth.lerp(aiming, 1.0F, 1.0F - modifiedGun.getGeneral().getSpreadAdsReduction());
		return Math.max(Mth.lerp(spreadModifier, visualMinSpread, baseSpread) * aimingSpreadMultiplier, 0.0F);
	}
	
	@Override
	public void render(Minecraft mc, PoseStack stack, int windowWidth, int windowHeight, float partialTicks)
	{
		float alpha = 1.0F;
		float size1 = 7.0F;
		float size2 = 1.0F;
		float spread = 0.0F;
		boolean renderDot = false;
		boolean multishot = false;
		SpreadTracker spreadTracker = mc.player != null ? SpreadTracker.get(mc.player) : null;
		Gun modifiedGun;
		
		if(mc.player != null && spreadTracker != null)
		{
			ItemStack heldItem = mc.player.getMainHandItem();
			if(heldItem.getItem() instanceof GunItem gunItem)
			{
				modifiedGun = gunItem.getModifiedGun(heldItem);
				multishot = modifiedGun.getGeneral().getProjectileAmount() >= 2 && GunConfig.CLIENT.specialCrosshairForShotguns.get();
				float aiming = (float) AimingHandler.get().getNormalisedAdsProgress();
				float currentSpread = spreadTracker.getSpread(mc.player, gunItem);
				
				spread = calculateSpread(spreadTracker, heldItem, gunItem, modifiedGun, aiming, currentSpread, partialTicks);
				
				DotRenderMode dotRenderMode = GunConfig.CLIENT.dynamicCrosshairDotMode.get();
				boolean penaltyActive = GunConfig.COMMON.doSpreadPenalties.get() && (mc.player.isSprinting() || !mc.player.isOnGround());
				boolean alwaysSpread = modifiedGun.getGeneral().getAlwaysSpread();
				boolean isAtMinSpread;
				
				if(penaltyActive && !alwaysSpread)
				{
					// Dot disappears immediately to better convey that the spread change is instant
					isAtMinSpread = false;
				}
				else
				{
					isAtMinSpread = currentSpread == 0.0F;
				}
				
				renderDot = (dotRenderMode == DotRenderMode.ALWAYS) || (dotRenderMode == DotRenderMode.AT_MIN_SPREAD && isAtMinSpread) && (!GunConfig.CLIENT.onlyRenderDotWhileAiming.get() || aiming > 0.9F);
			}
		}
		
		double windowCenteredX = Math.round((windowWidth) / 2.0F) - 0.5;
		double windowCenteredY = Math.round((windowHeight) / 2.0F) - 0.5;
		
		RenderSystem.enableBlend();
		if(GunConfig.CLIENT.blendCrosshair.get())
		{
			RenderSystem.blendFuncSeparate(GlStateManager.SourceFactor.ONE_MINUS_DST_COLOR, GlStateManager.DestFactor.ONE_MINUS_SRC_COLOR, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
		}
		
		// Common spread calculation
		float baseScale = 1.0F + (Mth.lerp(partialTicks, this.prevScale, this.scale) * 2.0F);
		float scale = baseScale + (spread * 2.0F);
		float scaleSize = (scale / 6.0F) + 1.15F;
		float crosshairBaseTightness = 0.3F;
		float finalSpreadTranslate = (float) ((Mth.lerp(0.95, scaleSize - 1.0F, Math.log(scaleSize))) * (2.8F));
		
		// Values used for debugging
		currentCrosshairSpread = spread;
		currentFinalSpreadTranslate = finalSpreadTranslate;
		currentScaleSize = scaleSize;
		
		// Offsets for shotgun crosshair
		float rawOffset = (size1 / 2.0F + finalSpreadTranslate - crosshairBaseTightness) * 0.5F;
		float offset = rawOffset * scaleSize;
		
		// Shotgun crosshair
		if(multishot)
		{
			// Top
			drawCrosshairPart(stack, windowCenteredX - offset, windowCenteredY - offset, 1.0F, 1.0F, 0.0F, 0.0F, 2.0F * offset, size2, SHOTGUN_H, 0.0F, 0.0F, 1.0F, 1.0F / 9.0F, alpha);
			// Bottom
			drawCrosshairPart(stack, windowCenteredX - offset, windowCenteredY + offset - size2, 1.0F, 1.0F, 0.0F, 0.0F, 2.0F * offset, size2, SHOTGUN_H, 0.0F, 8.0F / 9.0F, 1.0F, 1.0F, alpha);
			// Left
			drawCrosshairPart(stack, windowCenteredX - offset, windowCenteredY - offset, 1.0F, 1.0F, 0.0F, 0.0F, size2, 2.0F * offset, SHOTGUN_V, 0.0F, 0.0F, 1.0F / 9.0F, 1.0F, alpha);
			// Right
			drawCrosshairPart(stack, windowCenteredX + offset - size2, windowCenteredY - offset, 1.0F, 1.0F, 0.0F, 0.0F, size2, 2.0F * offset, SHOTGUN_V, 8.0F / 9.0F, 0.0F, 1.0F, 1.0F, alpha);
		}
		// Normal crosshair
		else
		{
			// Left
			drawCrosshairPart(stack, windowCenteredX, windowCenteredY, scaleSize, 1.0F, (-size1 / 2.0F) - finalSpreadTranslate + crosshairBaseTightness, -size2 / 2.0F, size1, size2, DYNAMIC_H, 0.0F, 0.0F, 1.0F, 1.0F / 9.0F, alpha);
			// Right
			drawCrosshairPart(stack, windowCenteredX, windowCenteredY, scaleSize, 1.0F, (-size1 / 2.0F) + finalSpreadTranslate - crosshairBaseTightness, -size2 / 2.0F, size1, size2, DYNAMIC_H, 0.0F, 8.0F / 9.0F, 1.0F, 1.0F, alpha);
			// Top
			drawCrosshairPart(stack, windowCenteredX, windowCenteredY, 1.0F, scaleSize, -size2 / 2.0F, (-size1 / 2.0F) - finalSpreadTranslate + crosshairBaseTightness, size2, size1, DYNAMIC_V, 0.0F, 0.0F, 1.0F / 9.0F, 1.0F, alpha);
			// Bottom
			drawCrosshairPart(stack, windowCenteredX, windowCenteredY, 1.0F, scaleSize, -size2 / 2.0F, (-size1 / 2.0F) + finalSpreadTranslate - crosshairBaseTightness, size2, size1, DYNAMIC_V, 8.0F / 9.0F, 0.0F, 1.0F, 1.0F, alpha);
		}
		
		// Center dot
		if(renderDot && !multishot)
		{
			stack.pushPose();
			{
				int dotSize = 9;
				RenderSystem.setShader(GameRenderer::getPositionTexColorShader);
				RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
				RenderSystem.setShaderTexture(0, DOT);
				Matrix4f matrix = stack.last().pose();
				stack.translate(windowCenteredX, windowCenteredY, 0.0);
				stack.translate(-dotSize / 2.0F, -dotSize / 2.0F, 0.0);
				BufferBuilder buffer = Tesselator.getInstance().getBuilder();
				buffer.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX_COLOR);
				buffer.vertex(matrix, 0.0F, dotSize, 0.0F).uv(0.0F, 1.0F).color(1.0F, 1.0F, 1.0F, alpha).endVertex();
				buffer.vertex(matrix, dotSize, dotSize, 0.0F).uv(1.0F, 1.0F).color(1.0F, 1.0F, 1.0F, alpha).endVertex();
				buffer.vertex(matrix, dotSize, 0.0F, 0.0F).uv(1.0F, 0.0F).color(1.0F, 1.0F, 1.0F, alpha).endVertex();
				buffer.vertex(matrix, 0.0F, 0.0F, 0.0F).uv(0.0F, 0.0F).color(1.0F, 1.0F, 1.0F, alpha).endVertex();
				BufferUploader.drawWithShader(buffer.end());
			}
			stack.popPose();
		}
	}
	
	private void drawCrosshairPart(PoseStack stack, double centerX, double centerY, float scaleX, float scaleY, float offsetX, float offsetY, float width, float height, ResourceLocation texture, float u0, float v0, float u1, float v1, float alpha)
	{
		stack.pushPose();
		{
			Matrix4f matrix = stack.last().pose();
			RenderSystem.setShader(GameRenderer::getPositionTexColorShader);
			RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
			RenderSystem.setShaderTexture(0, texture);
			
			stack.translate(centerX, centerY, 0.0);
			stack.scale(scaleX, scaleY, 1.0F);
			stack.translate(offsetX, offsetY, 0.0);
			
			BufferBuilder buffer = Tesselator.getInstance().getBuilder();
			buffer.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX_COLOR);
			buffer.vertex(matrix, 0.0F, height, 0.0F).uv(u0, v1).color(1.0F, 1.0F, 1.0F, alpha).endVertex();
			buffer.vertex(matrix, width, height, 0.0F).uv(u1, v1).color(1.0F, 1.0F, 1.0F, alpha).endVertex();
			buffer.vertex(matrix, width, 0.0F, 0.0F).uv(u1, v0).color(1.0F, 1.0F, 1.0F, alpha).endVertex();
			buffer.vertex(matrix, 0.0F, 0.0F, 0.0F).uv(u0, v0).color(1.0F, 1.0F, 1.0F, alpha).endVertex();
			BufferUploader.drawWithShader(buffer.end());
		}
		stack.popPose();
	}
}