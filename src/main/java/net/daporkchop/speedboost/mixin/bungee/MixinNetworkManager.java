package net.daporkchop.speedboost.mixin.bungee;

import com.mojang.authlib.properties.Property;
import io.netty.channel.Channel;
import net.daporkchop.speedboost.add.bungee.IBungeeNetworkManager;
import net.minecraft.network.NetworkManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.net.SocketAddress;
import java.util.UUID;

@Mixin(NetworkManager.class)
public abstract class MixinNetworkManager implements IBungeeNetworkManager {
    @Shadow private SocketAddress socketAddress;
    @Shadow public Channel channel;
    public UUID spoofedUUID;
    public Property[] spoofedProfile;

    @Override
    public UUID spoofedUUID()  {
        return spoofedUUID;
    }

    @Override
    public Property[] spoofedProfile()  {
        return spoofedProfile;
    }

    @Override
    public SocketAddress getRawAddress()    {
        return channel.remoteAddress();
    }

    @Override
    public void spoofedUUID(UUID uuid) {
        this.spoofedUUID = uuid;
    }

    @Override
    public void spoofedProfile(Property[] properties) {
        this.spoofedProfile = properties;
    }

    @Override
    public void setRemoteAddress(SocketAddress address) {
        this.socketAddress = address;
    }
}
