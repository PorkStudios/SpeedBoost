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

package net.daporkchop.speedboost.mixin.general;

import net.minecraft.inventory.IInventory;
import net.minecraft.tileentity.TileEntityHopper;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import static net.minecraft.util.math.MathHelper.floor;

@Mixin(TileEntityHopper.class)
public abstract class MixinTileEntityHopper {
    @Inject(method = "Lnet/minecraft/tileentity/TileEntityHopper;getInventoryAtPosition(Lnet/minecraft/world/World;DDD)Lnet/minecraft/inventory/IInventory;",
            at = @At("HEAD"),
            cancellable = true,
            locals = LocalCapture.CAPTURE_FAILHARD)
    private static void preventChunkLoading(World world, double x, double y, double z, CallbackInfoReturnable<IInventory> callbackInfo) {
        if (!world.isBlockLoaded(new BlockPos(floor(x), floor(y), floor(z)))) {
            callbackInfo.setReturnValue(null);
        }
    }
}
