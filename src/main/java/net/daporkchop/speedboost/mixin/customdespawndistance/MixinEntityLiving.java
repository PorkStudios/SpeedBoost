/*
 * Adapted from the Wizardry License
 *
 * Copyright (c) 2017 DaPorkchop_ and contributors
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

package net.daporkchop.speedboost.mixin.customdespawndistance;

import net.daporkchop.speedboost.config.impl.CustomDespawnDistanceTranslator;
import net.minecraft.entity.EntityLiving;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin(EntityLiving.class)
public abstract class MixinEntityLiving {
    @ModifyConstant(method = "Lnet/minecraft/entity/EntityLiving;despawnEntity()V",
            constant = @Constant(doubleValue = 1024.0d))
    public double fixSoftDespawnRadius(double in) {
        return CustomDespawnDistanceTranslator.INSTANCE.softDespawnDistance;
    }

    @ModifyConstant(method = "Lnet/minecraft/entity/EntityLiving;despawnEntity()V",
            constant = @Constant(doubleValue = 16384.0d))
    public double fixHardDespawnRadius(double in) {
        return CustomDespawnDistanceTranslator.INSTANCE.hardDespawnDistance;
    }
}
