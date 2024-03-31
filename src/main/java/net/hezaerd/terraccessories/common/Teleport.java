package net.hezaerd.terraccessories.common;

import net.fabricmc.fabric.api.dimension.v1.FabricDimensions;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.registry.RegistryKey;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ChunkTicketType;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.TeleportTarget;
import net.minecraft.world.World;

public class Teleport {

    public static void teleportToPos(PlayerEntity playerEntity, BlockPos pos) {
        ServerPlayerEntity player = (ServerPlayerEntity)playerEntity;
        ChunkPos ticketPos = new ChunkPos(new BlockPos(pos));

        if (player.getVehicle() != null) {
            player.dismountVehicle();
        }

        player.getServerWorld().getChunkManager().addTicket(ChunkTicketType.POST_TELEPORT, ticketPos, 1, player.getId());
        player.getWorld().getChunk(ticketPos.x, ticketPos.z);
        player.fallDistance = 0.0F;
        player.requestTeleport(pos.getX() + 0.5D, pos.getY() + 0.15D, pos.getZ() + 0.5D);
    }

    public static void teleportToPos(LivingEntity livingEntity, BlockPos pos) {
        ServerWorld serverWorld = (ServerWorld)livingEntity.getEntityWorld();
        ChunkPos ticketPos = new ChunkPos(new BlockPos(pos));

        serverWorld.getChunkManager().addTicket(ChunkTicketType.POST_TELEPORT, ticketPos, 1, livingEntity.getId());
        livingEntity.getWorld().getChunk(ticketPos.x, ticketPos.z);
        livingEntity.fallDistance = 0.0F;
        livingEntity.requestTeleport(pos.getX(), pos.getY(), pos.getZ());
    }

    public static boolean changeDimensionAndTeleport(PlayerEntity playerEntity, World world, BlockPos pos) {
        ServerPlayerEntity player = (ServerPlayerEntity)playerEntity;
        ServerWorld destination = (ServerWorld)world;

        Vec3d posVector = new Vec3d(pos.getX() + 0.5D, pos.getY() + 0.15D, pos.getZ() + 0.5D);

        TeleportTarget teleportTarget = new TeleportTarget(posVector, posVector, player.getYaw(), player.getPitch());

        if (player.getServerWorld().getRegistryKey() != destination.getRegistryKey()) {
            ChunkPos ticketPos = new ChunkPos(new BlockPos(pos));
            player.getServerWorld().getChunkManager().addTicket(ChunkTicketType.POST_TELEPORT, ticketPos, 1, player.getId());
            player.getWorld().getChunk(ticketPos.x, ticketPos.z);
            player.fallDistance = 0.0F;
            FabricDimensions.teleport(player, destination, teleportTarget);
            return true;
        }
        else {
            return false;
        }
    }

    public static int teleportToSpawn(PlayerEntity playerEntity, World world, boolean interdimensional) {
        ServerWorld serverWorld = (ServerWorld)world;
        ServerPlayerEntity player = (ServerPlayerEntity)playerEntity;
        BlockPos spawnPos = player.getSpawnPointPosition();
        RegistryKey<World> spawnDimension = player.getSpawnPointDimension();
        ServerWorld destination = ((ServerWorld) world).getServer().getWorld(spawnDimension);

        if (interdimensional) {
            if (changeDimensionAndTeleport(playerEntity, destination, spawnPos)) {
                return 0;
            } else {
                teleportToPos(playerEntity, spawnPos);
                return 1;
            }
        }
        else {
            if (player.getSpawnPointDimension().equals(serverWorld.getRegistryKey())) {
                teleportToPos(playerEntity, spawnPos);
                return 2;
            } else {
                return 3;
            }
        }
    }
}
