package net.daporkchop.speedboost.mixin.entityactivationrange.heirachy;

import net.daporkchop.speedboost.add.entityactivation.IActivationEntity;
import net.minecraft.entity.Entity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Entity.class)
public abstract class MixinEntity implements IActivationEntity {
    public final byte activationType = org.spigotmc.ActivationRange.initializeEntityActivationType(Entity.class.cast(this));
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
            defaultActivationState = false;
        } else {
            defaultActivationState = org.spigotmc.ActivationRange.initializeEntityActivationState(Entity.class.cast(this));
        }
    }

    @Override
    public boolean defaultActivationState() {
        return defaultActivationState;
    }

    @Override
    public long getActivatedTick() {
        return activatedTick;
    }

    @Override
    public void setActivatedTick(long tick) {
        activatedTick = tick;
    }

    @Override
    public byte getActivationType() {
        return activationType;
    }
}
