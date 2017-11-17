package net.daporkchop.speedboost.mixin.antinulltiles;

import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import javax.annotation.Nullable;
import java.util.Iterator;
import java.util.List;

@Mixin(World.class)
public abstract class MixinWorld {
    @Shadow @Final public List<TileEntity> tickableTileEntities;

    @Shadow @Nullable public abstract MinecraftServer getMinecraftServer();

    @Inject(method = "Lnet/minecraft/world/World;updateEntities()V",
        at = @At(value = "INVOKE_ASSIGN", target = "Ljava/util/Iterator;next()Ljava/lang/Object;"))
    public void preventNullTileEntities(CallbackInfo callbackInfo)  {
        Iterator secondIterator = tickableTileEntities.iterator();

        while (secondIterator.hasNext())    {
            if (secondIterator.next() == null)  {
                secondIterator.remove();
                getMinecraftServer().logSevere("Spigot (SpeedBoost) has detected a null entity and has removed it, preventing a crash");
            }
        }
    }
}
