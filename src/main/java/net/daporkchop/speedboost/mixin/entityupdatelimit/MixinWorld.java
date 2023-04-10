/*
 * Adapted from the Wizardry License
 *
 * Copyright (c) 2018-2023 DaPorkchop_ and contributors
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

package net.daporkchop.speedboost.mixin.entityupdatelimit;

import net.daporkchop.speedboost.config.impl.EntityUpdateLimitTranslator;
import net.minecraft.entity.Entity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.Slice;

import java.util.List;

/**
 * @author DaPorkchop_
 */
@Mixin(World.class)
public abstract class MixinWorld {
    @Unique
    protected long updateEndTime;

    @ModifyConstant(method = "Lnet/minecraft/world/World;updateEntities()V",
            constant = @Constant(intValue = 0,
                    ordinal = 0),
            slice = @Slice(from = @At(value = "CONSTANT",
                    args = "stringValue=regular")),
            require = 1, allow = 1)
    private int adjustEntityUpdateIterationIndexStart(int value) {
        this.updateEndTime = System.nanoTime() + EntityUpdateLimitTranslator.INSTANCE.maxEntityUpdateNanos;
        return value;
    }

    @Redirect(method = "Lnet/minecraft/world/World;updateEntities()V",
            at = @At(value = "INVOKE",
                    target = "Ljava/util/List;size()I",
                    ordinal = 0),
            slice = @Slice(from = @At(value = "CONSTANT",
                    args = "stringValue=regular")),
            require = 1, allow = 1)
    private int adjustEntityListSize(List<Entity> loadedEntityList) {
        return System.nanoTime() < this.updateEndTime ? loadedEntityList.size() : -1;
    }
}
