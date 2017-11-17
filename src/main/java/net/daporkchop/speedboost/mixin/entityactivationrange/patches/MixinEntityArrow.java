package net.daporkchop.speedboost.mixin.entityactivationrange.patches;

import net.daporkchop.speedboost.mixin.entityactivationrange.heirachy.MixinEntity;
import net.minecraft.entity.projectile.EntityArrow;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(EntityArrow.class)
public abstract class MixinEntityArrow extends MixinEntity {
    @Shadow
    protected boolean inGround;

    @Shadow
    private int ticksInGround;

    @Override
    public void inactiveTick() {
        if (this.inGround) {
            this.ticksInGround++;
        }

        super.inactiveTick();
    }
}
