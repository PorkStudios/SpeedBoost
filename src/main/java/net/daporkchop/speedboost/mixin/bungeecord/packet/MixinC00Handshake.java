/*
 * Adapted from the Wizardry License
 *
 * Copyright (c) 2018-2019 DaPorkchop_ and contributors
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

package net.daporkchop.speedboost.mixin.bungeecord.packet;

import net.minecraft.network.EnumConnectionState;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.handshake.client.C00Handshake;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.io.IOException;

@Mixin(C00Handshake.class)
public abstract class MixinC00Handshake {
    @Shadow private int protocolVersion;
    @Shadow public String ip;
    @Shadow public int port;
    @Shadow private EnumConnectionState requestedState;
    @Shadow(remap = false) private boolean hasFMLMarker;

    @Inject(method = "readPacketData(Lnet/minecraft/network/PacketBuffer;)V", at = @At("HEAD"), cancellable = true, locals = LocalCapture.CAPTURE_FAILSOFT)
    public void readPacketData(PacketBuffer buf, CallbackInfo callbackInfo) throws IOException {
        // Sponge start
        this.protocolVersion = buf.readVarInt();

        /*if (!SpongeImpl.getGlobalConfig().getConfig().getModules().usePluginBungeeCord()
                || !SpongeImpl.getGlobalConfig().getConfig().getBungeeCord().getIpForwarding()) {
            this.ip = buf.readString(255);
        } else {*/
        this.ip = buf.readString(Short.MAX_VALUE);
        String split[] = this.ip.split("\0\\|", 2);
        this.ip = split[0];
        // If we have extra data, check to see if it is telling us we have a
        // FML marker
        if (split.length == 2) {
            this.hasFMLMarker = split[1].contains("\0FML\0");
        }
        //}

        // Check for FML marker and strip if found, but only if it wasn't
        // already in the extra data.
        if (!this.hasFMLMarker) {
            this.hasFMLMarker = this.ip.contains("\0FML\0");
            if (this.hasFMLMarker) {
                this.ip = this.ip.split("\0")[0];
            }
        }

        this.port = buf.readUnsignedShort();
        this.requestedState = EnumConnectionState.getById(buf.readVarInt());
        // Sponge end

        callbackInfo.cancel();
    }
}
