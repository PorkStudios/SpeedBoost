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

package net.daporkchop.speedboost.mixin.fixentitytrackerconcurrency;

import net.minecraft.entity.EntityTracker;
import net.minecraft.entity.EntityTrackerEntry;
import net.minecraft.world.WorldServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Collections;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author DaPorkchop_
 */
@Mixin(EntityTracker.class)
abstract class MixinEntityTracker {
    @Shadow
    private Set<EntityTrackerEntry> entries;

    /*@Shadow
    @Final
    public WorldServer world;*/

    @Inject(
            method = "Lnet/minecraft/entity/EntityTracker;<init>(Lnet/minecraft/world/WorldServer;)V",
            at = @At("RETURN"))
    private void makeEntriesMapConcurrent(WorldServer server, CallbackInfo ci) {
        this.entries = Collections.newSetFromMap(new ConcurrentHashMap<>());
    }

    /*@Inject(
            method = "Lnet/minecraft/entity/EntityTracker;track(Lnet/minecraft/entity/Entity;)V",
            at = @At("HEAD"),
            cancellable = true
    )
    private void ensure_track_isCalledFromMainThread(Entity entity, CallbackInfo ci) {
        if (!this.world.getMinecraftServer().isCallingFromMinecraftThread()) {
            this.world.getMinecraftServer().addScheduledTask(() -> this.track(entity));
            ci.cancel();
        }
    }

    @Inject(
            method = "Lnet/minecraft/entity/EntityTracker;track(Lnet/minecraft/entity/Entity;IIZ)V",
            at = @At("HEAD"),
            cancellable = true
    )
    private void ensure_track_isCalledFromMainThread(Entity entity, int trackingRange, final int updateFrequency, boolean sendVelocityUpdates, CallbackInfo ci) {
        if (!this.world.getMinecraftServer().isCallingFromMinecraftThread()) {
            this.world.getMinecraftServer().addScheduledTask(() -> this.track(entity, trackingRange, updateFrequency, sendVelocityUpdates));
            ci.cancel();
        }
    }

    @Inject(
            method = "Lnet/minecraft/entity/EntityTracker;untrack(Lnet/minecraft/entity/Entity;)V",
            at = @At("HEAD"),
            cancellable = true
    )
    private void ensure_untrack_isCalledFromMainThread(Entity entity, CallbackInfo ci) {
        if (!this.world.getMinecraftServer().isCallingFromMinecraftThread()) {
            this.world.getMinecraftServer().addScheduledTask(() -> this.untrack(entity));
            ci.cancel();
        }
    }

    @Inject(
            method = "Lnet/minecraft/entity/EntityTracker;tick()V",
            at = @At("HEAD"),
            cancellable = true
    )
    private void ensure_tick_isCalledFromMainThread(CallbackInfo ci) {
        if (!this.world.getMinecraftServer().isCallingFromMinecraftThread()) {
            this.world.getMinecraftServer().addScheduledTask(this::tick);
            ci.cancel();
        }
    }

    @Inject(
            method = "Lnet/minecraft/entity/EntityTracker;sendToTracking(Lnet/minecraft/entity/Entity;Lnet/minecraft/network/Packet;)V",
            at = @At("HEAD"),
            cancellable = true
    )
    private void ensure_sendToTracking_isCalledFromMainThread(Entity entity, Packet<?> packet, CallbackInfo ci) {
        if (!this.world.getMinecraftServer().isCallingFromMinecraftThread()) {
            this.world.getMinecraftServer().addScheduledTask(() -> this.sendToTracking(entity, packet));
            ci.cancel();
        }
    }

    @Inject(
            method = "Lnet/minecraft/entity/EntityTracker;removePlayerFromTrackers(Lnet/minecraft/entity/player/EntityPlayerMP;)V",
            at = @At("HEAD"),
            cancellable = true
    )
    private void ensure_removePlayerFromTrackers_isCalledFromMainThread(EntityPlayerMP player, CallbackInfo ci) {
        if (!this.world.getMinecraftServer().isCallingFromMinecraftThread()) {
            this.world.getMinecraftServer().addScheduledTask(() -> this.removePlayerFromTrackers(player));
            ci.cancel();
        }
    }

    @Inject(
            method = "Lnet/minecraft/entity/EntityTracker;sendLeashedEntitiesInChunk(Lnet/minecraft/entity/player/EntityPlayerMP;Lnet/minecraft/world/chunk/Chunk;)V",
            at = @At("HEAD"),
            cancellable = true
    )
    private void ensure_sendLeashedEntitiesInChunk_isCalledFromMainThread(EntityPlayerMP player, Chunk chunkIn, CallbackInfo ci) {
        if (!this.world.getMinecraftServer().isCallingFromMinecraftThread()) {
            this.world.getMinecraftServer().addScheduledTask(() -> this.sendLeashedEntitiesInChunk(player, chunkIn));
            ci.cancel();
        }
    }

    @Inject(
            method = "Lnet/minecraft/entity/EntityTracker;setViewDistance(I)V",
            at = @At("HEAD"),
            cancellable = true
    )
    private void ensure_setViewDistance_isCalledFromMainThread(int distance, CallbackInfo ci) {
        if (!this.world.getMinecraftServer().isCallingFromMinecraftThread()) {
            this.world.getMinecraftServer().addScheduledTask(() -> this.setViewDistance(distance));
            ci.cancel();
        }
    }

    @Shadow
    public abstract void track(Entity entityIn);

    @Shadow
    public abstract void track(Entity entityIn, int trackingRange, int updateFrequency, boolean sendVelocityUpdates);

    @Shadow
    public abstract void untrack(Entity entityIn);

    @Shadow
    public abstract void tick();

    @Shadow
    public abstract void sendToTracking(Entity entityIn, Packet<?> packetIn);

    @Shadow
    public abstract void removePlayerFromTrackers(EntityPlayerMP player);

    @Shadow
    public abstract void sendLeashedEntitiesInChunk(EntityPlayerMP player, Chunk chunkIn);

    @Shadow
    public abstract void setViewDistance(int distance);*/
}
