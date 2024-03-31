package net.hezaerd.terraccessories.items;

import com.ibm.icu.impl.number.AffixPatternProvider;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.client.util.math.Vector2f;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.packet.s2c.play.PositionFlag;
import net.minecraft.registry.RegistryKey;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;
import java.util.Vector;

public class DemonConch extends Item {
    public static  final String MOD_ID = "terraccessories";

    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
    public DemonConch(Settings settings) {
        super(settings);
    }

    public static BlockPos findSuitable(World world, BlockPos playerPos) {
        //search in chunk suitable pos
        Chunk chunk = world.getChunk(playerPos);

        for (int x =  chunk.getPos().getStartX() ; x <= chunk.getPos().getEndX(); x++)
        {
            for (int z = chunk.getPos().getStartZ() ; z <= chunk.getPos().getEndZ() ; z++)
            {
                for (int y = world.getBottomY(); y < 128 ; y++)
                {
                    BlockPos blockToTest = new BlockPos(x,y,z);
                    BlockState blockState = chunk.getBlockState(blockToTest);
                    if (world.isAir(blockToTest) && world.isAir(blockToTest.up()) && !world.isAir(blockToTest.down()))
                    {
                        return blockToTest;
                    }
                }
            }
        }

        return null;
    }
    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity player, Hand hand)
    {
        if (!world.isClient())
        {
            ServerWorld currentWorld = (ServerWorld) world;
            ServerWorld netherWorld = currentWorld.getServer().getWorld(World.NETHER);
            if (netherWorld != null)
            {
                LOGGER.info("User " + player.getName().getString() + " right-clicked with Demon Cock");
                BlockPos netherSpawnPos = new BlockPos(player.getBlockPos().getX() / 8, player.getBlockPos().getY(), player.getBlockPos().getZ() / 8);
                BlockPos suitablePos = findSuitable(netherWorld, netherSpawnPos);
                if (suitablePos != null)
                {
                    player.teleport(netherWorld, suitablePos.getX(),suitablePos.getY(), suitablePos.getZ(),null, player.getYaw(), player.getPitch() );
                }
            }
        }
        return  TypedActionResult.success(player.getStackInHand(hand));
    }

}
