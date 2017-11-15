package net.daporkchop.speedboost.add.bungee;

import com.mojang.authlib.properties.Property;

import java.net.SocketAddress;
import java.util.UUID;

public interface IBungeeNetworkManager {
    UUID spoofedUUID();

    Property[] spoofedProfile();

    SocketAddress getRawAddress();

    void spoofedUUID(UUID uuid);

    void spoofedProfile(Property[] properties);

    void setRemoteAddress(SocketAddress address);
}
