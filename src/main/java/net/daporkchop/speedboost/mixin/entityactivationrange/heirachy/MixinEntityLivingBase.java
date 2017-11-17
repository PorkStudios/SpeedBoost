package net.daporkchop.speedboost.mixin.entityactivationrange.heirachy;

import net.minecraft.entity.EntityLivingBase;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(EntityLivingBase.class)
public abstract class MixinEntityLivingBase extends MixinEntity {
    @Shadow
    protected int idleTime;

    @Override
    public void inactiveTick() {
        super.inactiveTick();

        this.idleTime++;
    }
}
