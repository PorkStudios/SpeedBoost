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

package net.daporkchop.speedboost.mixin.entityactivationrange.heirachy;

import net.daporkchop.speedboost.add.entityactivation.IActivationEntity;
import net.daporkchop.speedboost.spigotclasses.ActivationRange;
import net.minecraft.entity.Entity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Entity.class)
public abstract class MixinEntity implements IActivationEntity {
    public final byte activationType = ActivationRange.initializeEntityActivationType(Entity.class.cast(this));
    @Shadow
    public World world;
    public boolean defaultActivationState;
    public long activatedTick = Integer.MIN_VALUE;

    @Shadow
    public abstract void setDead();

    @Override
    public void inactiveTick() {
        //System.out.println("Inactive tick");
    }

    @Inject(method = "Lnet/minecraft/entity/Entity;<init>(Lnet/minecraft/world/World;)V",
            at = @At(value = "RETURN"))
    public void setDefaultActivationState(World worldIn, CallbackInfo callbackInfo) {
        if (worldIn == null) {
            this.defaultActivationState = false;
        } else {
            this.defaultActivationState = ActivationRange.initializeEntityActivationState(Entity.class.cast(this));
        }
    }

    @Override
    public boolean defaultActivationState() {
        return this.defaultActivationState;
    }

    @Override
    public long getActivatedTick() {
        return this.activatedTick;
    }

    @Override
    public void setActivatedTick(long tick) {
        this.activatedTick = tick;
    }

    @Override
    public byte getActivationType() {
        return this.activationType;
    }
}
