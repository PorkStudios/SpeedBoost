package net.daporkchop.speedboost.mixin.bungee;

import com.google.gson.Gson;
import com.mojang.authlib.properties.Property;
import com.mojang.util.UUIDTypeAdapter;
import net.daporkchop.speedboost.add.bungee.IBungeeNetworkManager;
import net.minecraft.network.EnumConnectionState;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.handshake.client.C00Handshake;
import net.minecraft.network.login.server.SPacketDisconnect;
import net.minecraft.server.network.NetHandlerHandshakeTCP;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.net.InetSocketAddress;

@Mixin(NetHandlerHandshakeTCP.class)
public abstract class MixinHandshakeListener {

    private static final Gson gson = new Gson();

    @Shadow
    @Final
    private NetworkManager networkManager;

    @Inject(method = "processHandshake", at = @At(value = "HEAD"), cancellable = true)
    public void onProcessHandshakeStart(C00Handshake packetIn, CallbackInfo ci) {
        if (/*SpongeImpl.getGlobalConfig().getConfig().getBungeeCord().getIpForwarding() && */packetIn.getRequestedState().equals(EnumConnectionState.LOGIN)) {
            String[] split = packetIn.ip.split("\00\\|", 2)[0].split("\00"); // ignore any extra data

            if (split.length == 3 || split.length == 4) {
                packetIn.ip = split[0];
                ((IBungeeNetworkManager) this.networkManager).setRemoteAddress(new InetSocketAddress(split[1],
                        ((InetSocketAddress) this.networkManager.getRemoteAddress()).getPort()));
                ((IBungeeNetworkManager) this.networkManager).spoofedUUID(UUIDTypeAdapter.fromString(split[2]));

                if (split.length == 4) {
                    ((IBungeeNetworkManager) this.networkManager).spoofedProfile(gson.fromJson(split[3], Property[].class));
                }
            } else {
                TextComponentString chatcomponenttext =
                        new TextComponentString("If you wish to use IP forwarding, please enable it in your BungeeCord config as well!");
                this.networkManager.sendPacket(new SPacketDisconnect(chatcomponenttext));
                this.networkManager.closeChannel(chatcomponenttext);
            }
        }
    }

    @Redirect(method = "processHandshake", at = @At(value = "INVOKE", target = "Lnet/minecraftforge/fml/common/FMLCommonHandler;handleServerHandshake"
            + "(Lnet/minecraft/network/handshake/client/C00Handshake;Lnet/minecraft/network/NetworkManager;)Z", ordinal = 0, remap = false))
    public boolean redirectFmlCheck(FMLCommonHandler handler, C00Handshake packetIn, NetworkManager networkManager) {
        // Don't bother if the player is not allowed to log in.
        if (handler.shouldAllowPlayerLogins() && packetIn.getRequestedState() == EnumConnectionState.LOGIN) {
            Property[] pr = ((IBungeeNetworkManager) networkManager).spoofedProfile();
            if (pr != null) {
                for (Property p : pr) {
                    if (p.getName().equalsIgnoreCase("forgeClient") && p.getValue().equalsIgnoreCase("true")) {
                        // Manually tell the system that we're a FML client.
                        networkManager.channel().attr(NetworkRegistry.FML_MARKER).set(true);
                        return true;
                    }
                }
            }
        }

        return handler.handleServerHandshake(packetIn, networkManager);
    }
}
