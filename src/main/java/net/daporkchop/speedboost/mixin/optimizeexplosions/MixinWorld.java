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

import it.unimi.dsi.fastutil.objects.Object2FloatAVLTreeMap;
import it.unimi.dsi.fastutil.objects.Object2FloatMap;
import it.unimi.dsi.fastutil.objects.Object2FloatOpenHashMap;
import it.unimi.dsi.fastutil.objects.Object2FloatRBTreeMap;
import lombok.Getter;
import lombok.experimental.Accessors;
import net.daporkchop.speedboost.add.optimizeexplosions.IExplosionsWorld;
import net.daporkchop.speedboost.paperclasses.ExplosionCacheKey;
import net.minecraft.profiler.Profiler;
import net.minecraft.world.World;
import net.minecraft.world.WorldProvider;
import net.minecraft.world.storage.ISaveHandler;
import net.minecraft.world.storage.WorldInfo;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.HashMap;
import java.util.Map;

@Mixin(World.class)
@Accessors(fluent = true)
public abstract class MixinWorld implements IExplosionsWorld {
    @Getter
    public final Object2FloatMap<ExplosionCacheKey> explosionDensityCache = new Object2FloatOpenHashMap<>();

    @Inject(
            method = "Lnet/minecraft/world/World;<init>(Lnet/minecraft/world/storage/ISaveHandler;Lnet/minecraft/world/storage/WorldInfo;Lnet/minecraft/world/WorldProvider;Lnet/minecraft/profiler/Profiler;Z)V",
            at = @At("RETURN"))
    private void setCacheDefaultReturnValue(ISaveHandler saveHandlerIn, WorldInfo info, WorldProvider providerIn, Profiler profilerIn, boolean client, CallbackInfo ci)    {
        this.explosionDensityCache.defaultReturnValue(Float.NaN);
    }
}
