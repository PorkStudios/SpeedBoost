package net.daporkchop.speedboost.mixin.entityactivationrange;

import net.daporkchop.speedboost.add.entityactivation.IActivationEntity;
import net.minecraft.entity.Entity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(World.class)
public abstract class MixinWorld {
    @Inject(method = "Lnet/minecraft/world/World;updateEntities()V",
            at = @At(value = "INVOKE",
                    target = "Lnet/minecraft/profiler/Profiler;endStartSection(Ljava/lang/String;)V",
                    shift = At.Shift.AFTER))
    public void activateEntities(CallbackInfo callbackInfo) {
        org.spigotmc.ActivationRange.activateEntities(World.class.cast(this)); // Spigot
    }

    @Inject(method = "Lnet/minecraft/world/World;updateEntityWithOptionalForce(Lnet/minecraft/entity/Entity;Z)V",
            at = @At("HEAD"),
            cancellable = true)
    public void checkActivatedOnUpdate(Entity entity, boolean force, CallbackInfo callbackInfo) {
        if (!force || !org.spigotmc.ActivationRange.checkIfActive(entity)) {
            entity.ticksExisted++;
            ((IActivationEntity) entity).inactiveTick();
            // Spigot end
            callbackInfo.cancel();
        }
    }
}
