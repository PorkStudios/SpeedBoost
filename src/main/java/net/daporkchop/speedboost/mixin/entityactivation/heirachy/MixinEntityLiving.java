package net.daporkchop.speedboost.mixin.entityactivation.heirachy;

import net.minecraft.entity.EntityLiving;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(EntityLiving.class)
public abstract class MixinEntityLiving extends MixinEntityLivingBase {
    @Override
    public void inactiveTick() {
        super.inactiveTick();
    }
}
