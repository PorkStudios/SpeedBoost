package net.daporkchop.speedboost.mixin.bungee;

import com.google.common.base.Charsets;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import net.daporkchop.speedboost.add.bungee.IBungeeNetworkManager;
import net.minecraft.network.NetworkManager;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.NetHandlerLoginServer;
import org.spongepowered.asm.lib.Opcodes;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.UUID;

@Mixin(NetHandlerLoginServer.class)
public abstract class MixinNetHandlerLoginServer {
    @Shadow @Final private MinecraftServer server;
    @Shadow @Final
    public NetworkManager networkManager;
    @Shadow
    private GameProfile loginGameProfile;

    @Inject(method = "processLoginStart", at = @At(value = "FIELD", target = "Lnet/minecraft/server/network/NetHandlerLoginServer;"
            + "loginGameProfile:Lcom/mojang/authlib/GameProfile;",
            opcode = Opcodes.PUTFIELD, ordinal = 0, shift = At.Shift.AFTER))
    public void initUuid(CallbackInfo ci) {
        if (!this.server.isServerInOnlineMode()) {
            UUID uuid;
            if (((IBungeeNetworkManager) this.networkManager).spoofedUUID() != null) {
                uuid = ((IBungeeNetworkManager) this.networkManager).spoofedUUID();
            } else {
                uuid = UUID.nameUUIDFromBytes(("OfflinePlayer:" + this.loginGameProfile.getName()).getBytes(Charsets.UTF_8));
            }

            this.loginGameProfile = new GameProfile(uuid, this.loginGameProfile.getName());

            if (((IBungeeNetworkManager) this.networkManager).spoofedProfile() != null) {
                for (Property property : ((IBungeeNetworkManager) this.networkManager).spoofedProfile()) {
                    this.loginGameProfile.getProperties().put(property.getName(), property);
                }
            }
        }
    }
}
