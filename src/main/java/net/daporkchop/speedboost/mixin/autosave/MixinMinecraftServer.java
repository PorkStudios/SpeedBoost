/*
 * Adapted from the Wizardry License
 *
 * Copyright (c) 2018-2020 DaPorkchop_ and contributors
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

package net.daporkchop.speedboost.mixin.autosave;

import net.daporkchop.speedboost.config.impl.AutoSaveTranslator;
import net.minecraft.server.MinecraftServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

/**
 * @author DaPorkchop_
 */
@Mixin(MinecraftServer.class)
public abstract class MixinMinecraftServer {
    @ModifyConstant(
            method = "Lnet/minecraft/server/MinecraftServer;tick()V",
            constant = @Constant(intValue = 900))
    private int modifyInterval(int val) {
        return AutoSaveTranslator.INSTANCE.interval <= 0 ? val : AutoSaveTranslator.INSTANCE.interval;
    }

    @ModifyConstant(
            method = "Lnet/minecraft/server/MinecraftServer;tick()V",
            constant = @Constant(intValue = 0, ordinal = 1))
    private int modifyTrigger(int val) {
        return AutoSaveTranslator.INSTANCE.interval <= 0 ? -1 : val;
    }
}
