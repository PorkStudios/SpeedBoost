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

package net.daporkchop.speedboost.mixin.hoppercustomizations;

import net.daporkchop.speedboost.config.impl.HopperCustomizationsTranslator;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntityHopper;
import net.minecraft.tileentity.TileEntityLockableLoot;
import net.minecraft.util.EnumFacing;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import javax.annotation.Nullable;

@Mixin(TileEntityHopper.class)
public abstract class MixinTileEntityHopper extends TileEntityLockableLoot {
    @Shadow
    public static ItemStack putStackInInventoryAllSlots(IInventory source, IInventory destination, ItemStack stack, @Nullable EnumFacing direction) {
        return null;
    }

    @Shadow
    protected static boolean canExtractItemFromSlot(IInventory inventoryIn, ItemStack stack, int index, EnumFacing side) {
        return false;
    }

    /**
     * same reason as below
     *
     * @author DaPorkchop_
     */
    /*@Overwrite
    private static boolean pullItemFromSlot(IHopper hopper, IInventory inventoryIn, int index, EnumFacing direction) {
        ItemStack itemstack = inventoryIn.getStackInSlot(index);

        if (!itemstack.isEmpty() && canExtractItemFromSlot(inventoryIn, itemstack, index, direction)) {
            ItemStack itemstack1 = itemstack.copy();
            ItemStack itemstack2 = putStackInInventoryAllSlots(inventoryIn, hopper, inventoryIn.decrStackSize(index, 1), (EnumFacing) null);

            if (itemstack2.isEmpty()) {
                inventoryIn.markDirty();
                return true;
            }

            inventoryIn.setInventorySlotContents(index, itemstack1);
        }

        return false;
    }*/
    @Shadow
    protected abstract IInventory getInventoryForHopperTransfer();

    @Shadow
    protected abstract boolean isInventoryFull(IInventory inventoryIn, EnumFacing side);

    @Redirect(method = "Lnet/minecraft/tileentity/TileEntityHopper;update()V",
            at = @At(value = "INVOKE",
                    target = "Lnet/minecraft/tileentity/TileEntityHopper;updateHopper()Z"
            ))
    public boolean removeUpdateHopper(TileEntityHopper tileEntityHopper) {
        if (!tileEntityHopper.updateHopper() && HopperCustomizationsTranslator.INSTANCE.hopperCheck > 1) {
            tileEntityHopper.setTransferCooldown(HopperCustomizationsTranslator.INSTANCE.hopperCheck);
        }

        return false; //return value isn't used
    }

    @Redirect(method = "Lnet/minecraft/tileentity/TileEntityHopper;updateHopper()Z",
            at = @At(value = "INVOKE",
                    target = "Lnet/minecraft/tileentity/TileEntityHopper;setTransferCooldown(I)V"
            ))
    public void changeHopperUpdateDelay(TileEntityHopper tileEntityHopper, int delay) {
        tileEntityHopper.setTransferCooldown(HopperCustomizationsTranslator.INSTANCE.hopperTransfer);
    }

    /**
     * I'd rather use an overwrite for this than 400 mixins tbh
     * doesn't work
     * @author DaPorkchop_
     */
    /*@Overwrite
    private boolean transferItemsOut() {
        if (net.minecraftforge.items.VanillaInventoryCodeHooks.insertHook(TileEntityHopper.class.cast(this))) {
            return true;
        }
        IInventory iinventory = this.getInventoryForHopperTransfer();

        if (iinventory == null) {
            return false;
        } else {
            EnumFacing enumfacing = BlockHopper.getFacing(this.getBlockMetadata()).getOpposite();

            if (this.isInventoryFull(iinventory, enumfacing)) {
                return false;
            } else {
                for (int i = 0; i < this.getSizeInventory(); ++i) {
                    if (!this.getStackInSlot(i).isEmpty()) {
                        ItemStack itemstack = this.getStackInSlot(i).copy();
                        ItemStack itemstack1 = putStackInInventoryAllSlots(this, iinventory, this.decrStackSize(i, HopperCustomizationsTranslator.INSTANCE.hopperAmount), enumfacing);

                        int origCount = itemstack1.getCount();
                        if (itemstack1.isEmpty()) {
                            iinventory.markDirty();
                            return true;
                        }

                        itemstack.grow(origCount ); //TODO
                        this.setInventorySlotContents(i, itemstack);
                    }
                }

                return false;
            }
        }
    }*/ //TODO: i think this can be done with no more than 5 mixins, have a look
}
