package net.daporkchop.speedboost.mixin.entityactivationrange.heirachy;

import net.minecraft.entity.EntityCreature;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(EntityCreature.class)
public abstract class MixinEntityCreature extends MixinEntityLiving {
    @Override
    public void inactiveTick() {
        super.inactiveTick();
    }
}
