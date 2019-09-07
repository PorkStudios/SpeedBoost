/*
 * Adapted from the Wizardry License
 *
 * Copyright (c) 2018-2019 DaPorkchop_ and contributors
 *
 * Permission is hereby granted to any persons and/or organizations using this software to copy, modify, merge, publish, and distribute it.
 * Said persons and/or organizations are not allowed to use the software or any derivatives of the work for commercial use or any other means to generate income, nor are they allowed to claim this software as their own.
 *
 * The persons and/or organizations are also disallowed from sub-licensing and/or trademarking this software without explicit permission from DaPorkchop_.
 *
 * Any persons and/or organizations using this software must disclose their source code and have it publicly available, include this license, provide sufficient credit to the original authors of the project (IE: DaPorkchop_), as well as provide a link to the original project.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NON INFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 *
 */

package net.daporkchop.speedboost.mixin.optimizeexplosions;

import it.unimi.dsi.fastutil.objects.Object2FloatMap;
import net.daporkchop.speedboost.add.optimizeexplosions.IExplosionsWorld;
import net.daporkchop.speedboost.paperclasses.ExplosionCacheKey;
import net.minecraft.entity.Entity;
import net.minecraft.util.EntitySelectors;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.List;
import java.util.Map;

@Mixin(Explosion.class)
public abstract class MixinExplosion {
    @Shadow
    @Final
    private World world;

    @Redirect(
            method = "Lnet/minecraft/world/Explosion;doExplosionA()V",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/World;getBlockDensity(Lnet/minecraft/util/math/Vec3d;Lnet/minecraft/util/math/AxisAlignedBB;)F"
            ))
    public float optimizeExplosions(World world, Vec3d vec3d, AxisAlignedBB aabb) {
        ExplosionCacheKey key = new ExplosionCacheKey((Explosion) (Object) this, aabb);
        Object2FloatMap<ExplosionCacheKey> densityCache = ((IExplosionsWorld) this.world).explosionDensityCache();

        float blockDensity = densityCache.getFloat(key);
        if (Float.isNaN(blockDensity))  {
            densityCache.put(key, blockDensity = this.world.getBlockDensity(vec3d, aabb));
        }
        return blockDensity;
    }

    @Redirect(
            method = "Lnet/minecraft/world/Explosion;doExplosionA()V",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/World;getEntitiesWithinAABBExcludingEntity(Lnet/minecraft/entity/Entity;Lnet/minecraft/util/math/AxisAlignedBB;)Ljava/util/List;"
            ))
    public List preventAddingDeadEntities(World world, Entity entity, AxisAlignedBB bb) {
        return world.getEntitiesInAABBexcluding(entity, bb, e -> EntitySelectors.NOT_SPECTATING.apply(e) && !e.isDead);
    }
}
