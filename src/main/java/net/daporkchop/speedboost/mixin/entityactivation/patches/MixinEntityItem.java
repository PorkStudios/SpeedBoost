package net.daporkchop.speedboost.mixin.entityactivation.patches;

import net.daporkchop.speedboost.mixin.entityactivation.heirachy.MixinEntity;
import net.minecraft.entity.item.EntityItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(EntityItem.class)
public abstract class MixinEntityItem extends MixinEntity {
    @Shadow
    public int lifespan;
    @Shadow
    private int pickupDelay;
    @Shadow
    private int age;

    @Override
    public void inactiveTick() {
        this.pickupDelay--;
        this.age++;

        if (this.age >= this.lifespan) {
            /*if (org.bukkit.craftbukkit.event.CraftEventFactory.callItemDespawnEvent(this).isCancelled()) {
                this.age = 0;
                return;
            }*/

            this.setDead();
        }
    }
}
