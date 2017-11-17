package net.daporkchop.speedboost.mixin.entityactivationrange.patches;

import net.daporkchop.speedboost.mixin.entityactivationrange.heirachy.MixinEntityCreature;
import net.minecraft.entity.EntityAgeable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(EntityAgeable.class)
public abstract class MixinEntityAgeable extends MixinEntityCreature {
    @Shadow
    public abstract int getGrowingAge();

    @Shadow
    public abstract void setGrowingAge(int age);

    @Override
    public void inactiveTick() {
        super.inactiveTick();
        if (!this.world.isRemote/* || this.ageLocked*/) { // CraftBukkit
            int i = this.getGrowingAge();

            if (i < 0) {
                ++i;
                this.setGrowingAge(i);
            } else if (i > 0) {
                --i;
                this.setGrowingAge(i);
            }
        }
    }
}
