package net.daporkchop.speedboost.mixin.general;

import net.daporkchop.speedboost.PorkInject;
import net.minecraft.server.MinecraftServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MinecraftServer.class)
public abstract class MixinMinecraftServer {
    @Inject(method = "Lnet/minecraft/server/MinecraftServer;<init>(Ljava/io/File;Ljava/net/Proxy;Lnet/minecraft/util/datafix/DataFixer;Lcom/mojang/authlib/yggdrasil/YggdrasilAuthenticationService;Lcom/mojang/authlib/minecraft/MinecraftSessionService;Lcom/mojang/authlib/GameProfileRepository;Lnet/minecraft/server/management/PlayerProfileCache;)V",
            at = @At("RETURN"))
    public void constructorEnd(CallbackInfo callbackInfo) {
        PorkInject.server = MinecraftServer.class.cast(this);
    }
}
