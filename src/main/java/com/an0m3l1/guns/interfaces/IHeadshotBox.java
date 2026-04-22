package com.an0m3l1.guns.interfaces;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.AABB;

import javax.annotation.Nullable;

public interface IHeadshotBox<T extends Entity>
{
	/**
	 * Gets a bounding box of the given entity's head in the world. This method can either return an
	 * axis aligned box or null for no hit box.
	 *
	 * @param entity
	 * 		the entity to create a headshot box from
	 *
	 * @return an axis aligned box of the entity's head
	 */
	@Nullable
	AABB getHeadshotBox(T entity);
}
