package net.daporkchop.speedboost.mixin.entityactivation.patches;

import net.daporkchop.speedboost.mixin.entityactivation.heirachy.MixinEntity;
import net.minecraft.entity.item.EntityFireworkRocket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(EntityFireworkRocket.class)
public abstract class MixinEntityFireworkRocket extends MixinEntity {
    @Shadow
    private int fireworkAge;

    @Override
    public void inactiveTick() {
        this.fireworkAge++;

        super.inactiveTick();
    }
}
