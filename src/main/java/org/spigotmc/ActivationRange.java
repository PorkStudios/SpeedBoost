package org.spigotmc;

import net.daporkchop.speedboost.add.entityactivation.IActivationEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IProjectile;
import net.minecraft.entity.boss.EntityDragon;
import net.minecraft.entity.boss.EntityWither;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.entity.item.EntityFireworkRocket;
import net.minecraft.entity.item.EntityTNTPrimed;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.passive.EntityAmbientCreature;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.entity.projectile.EntityFireball;
import net.minecraft.util.ClassInheritanceMultiMap;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;

import static net.daporkchop.speedboost.PorkInject.server;
import static net.daporkchop.speedboost.config.impl.EntityActivationTranslator.INSTANCE;

public class ActivationRange {

    static AxisAlignedBB maxBB = new AxisAlignedBB(0, 0, 0, 0, 0, 0);
    static AxisAlignedBB miscBB = new AxisAlignedBB(0, 0, 0, 0, 0, 0);
    static AxisAlignedBB animalBB = new AxisAlignedBB(0, 0, 0, 0, 0, 0);
    static AxisAlignedBB monsterBB = new AxisAlignedBB(0, 0, 0, 0, 0, 0);

    /**
     * Initializes an entities type on construction to specify what group this
     * entity is in for activation ranges.
     *
     * @param entity
     * @return group id
     */
    public static byte initializeEntityActivationType(Entity entity) {
        if (entity instanceof IMob) {
            return 1; // Monster
        } else if (entity instanceof EntityCreature || entity instanceof EntityAmbientCreature) {
            return 2; // Animal
        } else {
            return 3; // Misc
        }
    }

    /**
     * These entities are excluded from Activation range checks.
     *
     * @param entity
     * @return boolean If it should always tick.
     */
    public static boolean initializeEntityActivationState(Entity entity) {
        byte activationType = ((IActivationEntity) entity).getActivationType();

        return (activationType == 3 && INSTANCE.miscActivationRange == 0)
                || (activationType == 2 && INSTANCE.animalActivationRange == 0)
                || (activationType == 1 && INSTANCE.monsterActivationRange == 0)
                || entity instanceof EntityPlayer
                || entity instanceof IProjectile
                || entity instanceof EntityDragon
                || entity instanceof EntityEnderCrystal
                || entity instanceof EntityWither
                || entity instanceof EntityFireball
                || entity instanceof EntityLightningBolt
                || entity instanceof EntityTNTPrimed
                || entity instanceof EntityEnderCrystal
                || entity instanceof EntityFireworkRocket;

    }

    /**
     * Find what entities are in range of the players in the world and set
     * active if in range.
     *
     * @param world
     */
    public static void activateEntities(World world) {
        //SpigotTimings.entityActivationCheckTimer.startTiming();
        final int miscActivationRange = INSTANCE.miscActivationRange;
        final int animalActivationRange = INSTANCE.animalActivationRange;
        final int monsterActivationRange = INSTANCE.monsterActivationRange;

        int maxRange = Math.max(monsterActivationRange, animalActivationRange);
        maxRange = Math.max(maxRange, miscActivationRange);
        maxRange = Math.min((server.getPlayerList().getViewDistance() << 4) - 8, maxRange);

        for (EntityPlayer player : world.playerEntities) {

            ((IActivationEntity) player).setActivatedTick(server.tickCounter);
            maxBB = player.getEntityBoundingBox().grow(maxRange, 256, maxRange);
            miscBB = player.getEntityBoundingBox().grow(miscActivationRange, 256, miscActivationRange);
            animalBB = player.getEntityBoundingBox().grow(animalActivationRange, 256, animalActivationRange);
            monsterBB = player.getEntityBoundingBox().grow(monsterActivationRange, 256, monsterActivationRange);

            int minX = MathHelper.floor(maxBB.minX / 16.0D);
            int maxX = MathHelper.floor(maxBB.maxX / 16.0D);
            int minY = MathHelper.floor(maxBB.minY / 16.0D);
            int maxY = MathHelper.floor(maxBB.maxY / 16.0D);
            int minZ = MathHelper.floor(maxBB.minZ / 16.0D);
            int maxZ = MathHelper.floor(maxBB.maxZ / 16.0D);

            for (int x = minX; x <= maxX; ++x) {
                for (int y = minY; y <= maxY; ++y) {
                    for (int z = minZ; z <= maxZ; ++z) {
                        if (world.isBlockLoaded(new BlockPos(x * 16, y * 16, z * 16))) {
                            activateChunkEntities(world.getChunkFromChunkCoords(x, z));
                        }
                    }
                }
            }
        }
        //SpigotTimings.entityActivationCheckTimer.stopTiming();
    }

    /**
     * Checks for the activation state of all entities in this chunk.
     *
     * @param chunk
     */
    private static void activateChunkEntities(Chunk chunk) {
        for (ClassInheritanceMultiMap<Entity> slice : chunk.getEntityLists()) {
            for (Entity entity : slice) {
                IActivationEntity activationEntity = (IActivationEntity) entity;
                if (server.tickCounter > activationEntity.getActivatedTick()) {
                    if (activationEntity.defaultActivationState()) {
                        activationEntity.setActivatedTick(server.tickCounter);
                        continue;
                    }
                    switch (activationEntity.getActivationType()) {
                        case 1:
                            if (monsterBB.intersects(entity.getEntityBoundingBox())) {
                                activationEntity.setActivatedTick(server.tickCounter);
                            }
                            break;
                        case 2:
                            if (animalBB.intersects(entity.getEntityBoundingBox())) {
                                activationEntity.setActivatedTick(server.tickCounter);
                            }
                            break;
                        case 3:
                        default:
                            if (miscBB.intersects(entity.getEntityBoundingBox())) {
                                activationEntity.setActivatedTick(server.tickCounter);
                            }
                    }
                }
            }
        }
    }

    /**
     * If an entity is not in range, do some more checks to see if we should
     * give it a shot.
     *
     * @param entity
     * @return
     */
    public static boolean checkEntityImmunities(Entity entity) {
        // quick checks.
        if (entity.isInWater() || entity.fire > 0) {
            return true;
        }
        if (!(entity instanceof EntityArrow)) {
            if (!entity.onGround || !entity.getPassengers().isEmpty() || entity.isRiding()) {
                return true;
            }
        } else if (!((EntityArrow) entity).inGround) {
            return true;
        }
        // special cases.
        if (entity instanceof EntityLivingBase) {
            EntityLivingBase living = (EntityLivingBase) entity;
            if ( /*TODO: Missed mapping? living.attackTicks > 0 || */ living.hurtTime > 0 || living.getActivePotionEffects().size() > 0) {
                return true;
            }
            if (entity instanceof EntityCreature && ((EntityCreature) entity).getAttackTarget() != null) {
                return true;
            }
            if (entity instanceof EntityVillager && ((EntityVillager) entity).isMating()) {
                return true;
            }
            if (entity instanceof EntityAnimal) {
                EntityAnimal animal = (EntityAnimal) entity;
                if (animal.isChild() || animal.isInLove()) {
                    return true;
                }
                if (entity instanceof EntitySheep && ((EntitySheep) entity).getSheared()) {
                    return true;
                }
            }
            if (entity instanceof EntityCreeper && ((EntityCreeper) entity).hasIgnited()) { // isExplosive
                return true;
            }
        }
        return false;
    }

    /**
     * Checks if the entity is active for this tick.
     *
     * @param entity
     * @return
     */
    public static boolean checkIfActive(Entity entity) {
        //SpigotTimings.checkIfActiveTimer.startTiming();
        // Never safe to skip fireworks or entities not yet added to chunk
        // PAIL: inChunk - boolean under datawatchers
        if (!entity.addedToChunk || entity instanceof EntityFireworkRocket) {
            //SpigotTimings.checkIfActiveTimer.stopTiming();
            return true;
        }

        IActivationEntity activationEntity = (IActivationEntity) entity;
        boolean isActive = activationEntity.getActivatedTick() >= server.tickCounter || activationEntity.defaultActivationState();

        // Should this entity tick?
        if (!isActive) {
            if ((server.tickCounter - activationEntity.getActivatedTick() - 1) % 20 == 0) {
                // Check immunities every 20 ticks.
                if (checkEntityImmunities(entity)) {
                    // Triggered some sort of immunity, give 20 full ticks before we check again.
                    activationEntity.setActivatedTick(server.tickCounter + 20);
                }
                isActive = true;
            }
            // Add a little performance juice to active entities. Skip 1/4 if not immune.
        } else if (!activationEntity.defaultActivationState() && entity.ticksExisted % 4 == 0 && !checkEntityImmunities(entity)) {
            isActive = false;
        }
        int x = MathHelper.floor(entity.posX);
        int y = MathHelper.floor(entity.posY);
        int z = MathHelper.floor(entity.posZ);
        // Make sure not on edge of unloaded chunk
        Chunk chunk = entity.world.getChunkProvider().getLoadedChunk(x >> 4, z >> 4);
        if (isActive && !(chunk != null && entity.world.isAreaLoaded(new BlockPos(((x >> 4) - 1) * 16, ((y >> 4) - 1) * 16, ((z >> 4) - 1) * 16),
                new BlockPos(((x >> 4) + 1) * 16, ((y >> 4) + 1) * 16, ((z >> 4) + 1) * 16)))) {
            isActive = false;
        }
        //SpigotTimings.checkIfActiveTimer.stopTiming();
        return isActive;
    }
}