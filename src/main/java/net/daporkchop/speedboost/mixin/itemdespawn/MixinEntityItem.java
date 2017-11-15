package net.daporkchop.speedboost.mixin.itemdespawn;

import net.daporkchop.speedboost.config.impl.ItemDespawn;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EntityItem.class)
public abstract class MixinEntityItem {
    @Shadow public int lifespan;

    @Inject(method = "Lnet/minecraft/entity/item/EntityItem;<init>(Lnet/minecraft/world/World;DDD)V", at = @At("RETURN"))
    public void changeConstructor1(World world, double x, double y, double z, CallbackInfo callbackInfo)   {
        lifespan = ItemDespawn.INSTANCE.lifespan;
    }

    @Inject(method = "Lnet/minecraft/entity/item/EntityItem;<init>(Lnet/minecraft/world/World;DDDLnet/minecraft/item/ItemStack;)V", at = @At("RETURN"))
    public void changeConstructor2(World world, double x, double y, double z, ItemStack stack, CallbackInfo callbackInfo)   {
        lifespan = ItemDespawn.INSTANCE.lifespan;
    }
}
