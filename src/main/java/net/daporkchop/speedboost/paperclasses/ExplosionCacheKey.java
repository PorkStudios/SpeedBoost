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

package net.daporkchop.speedboost.paperclasses;

import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;

public class ExplosionCacheKey {
    private final World world;
    private final double posX;
    private final double posY;
    private final double posZ;
    private final double minX;
    private final double minY;
    private final double minZ;
    private final double maxX;
    private final double maxY;
    private final double maxZ;

    public ExplosionCacheKey(Explosion explosion, AxisAlignedBB aabb) {
        this.world = explosion.world;
        this.posX = explosion.x;
        this.posY = explosion.y;
        this.posZ = explosion.z;
        this.minX = aabb.minX;
        this.minY = aabb.minY;
        this.minZ = aabb.minZ;
        this.maxX = aabb.maxX;
        this.maxY = aabb.maxY;
        this.maxZ = aabb.maxZ;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || this.getClass() != o.getClass()) return false;

        ExplosionCacheKey cacheKey = (ExplosionCacheKey) o;

        if (Double.compare(cacheKey.posX, this.posX) != 0) return false;
        if (Double.compare(cacheKey.posY, this.posY) != 0) return false;
        if (Double.compare(cacheKey.posZ, this.posZ) != 0) return false;
        if (Double.compare(cacheKey.minX, this.minX) != 0) return false;
        if (Double.compare(cacheKey.minY, this.minY) != 0) return false;
        if (Double.compare(cacheKey.minZ, this.minZ) != 0) return false;
        if (Double.compare(cacheKey.maxX, this.maxX) != 0) return false;
        if (Double.compare(cacheKey.maxY, this.maxY) != 0) return false;
        if (Double.compare(cacheKey.maxZ, this.maxZ) != 0) return false;
        return this.world.equals(cacheKey.world);
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = this.world.hashCode();
        temp = Double.doubleToLongBits(this.posX);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(this.posY);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(this.posZ);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(this.minX);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(this.minY);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(this.minZ);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(this.maxX);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(this.maxY);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(this.maxZ);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        return result;
    }

}
