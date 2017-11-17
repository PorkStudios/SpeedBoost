/*
 * Adapted from the Wizardry License
 *
 * Copyright (c) 2017 DaPorkchop_ and contributors
 *
 * Permission is hereby granted to any persons and/or organizations using this software to copy, modify, merge, publish, and distribute it.
 * Said persons and/or organizations are not allowed to use the software or any derivatives of the work for commercial use or any other means to generate income, nor are they allowed to claim this software as their own.
 *
 * The persons and/or organizations are also disallowed from sub-licensing and/or trademarking this software without explicit permission from DaPorkchop_.
 *
 * Any persons and/or organizations using this software must disclose their source code and have it publicly available, include this license, provide sufficient credit to the original authors of the project (IE: DaPorkchop_), as well as provide a link to the original project.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NON INFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 *
 */

package net.daporkchop.speedboost.mixin.bungeecord;

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
