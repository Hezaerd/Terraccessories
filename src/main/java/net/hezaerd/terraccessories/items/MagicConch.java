package net.hezaerd.terraccessories.items;

import io.wispforest.owo.itemgroup.OwoItemSettings;
import net.hezaerd.terraccessories.Terraccessories;
import net.hezaerd.terraccessories.utils.BiomesUtils;
import net.hezaerd.terraccessories.utils.Teleport;
import net.hezaerd.terraccessories.workers.BiomeSearchWorker;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeKeys;

import java.util.List;
import java.util.Optional;

public class MagicConch extends Item {

    private static final int cooldown = 200;
    public MagicConch() {super(new OwoItemSettings().group(Terraccessories.TERRACCESSORIES_GROUP).maxCount(1));}
    public BiomeSearchWorker worker;

    @Override
    public void appendTooltip(ItemStack stack, World world, List<Text> tooltip, TooltipContext context) {
        tooltip.add(Text.translatable("item.terraccessories.magic_conch.tooltip").formatted(Formatting.DARK_AQUA));
    }

    /* Usage animation */
    @Override
    public UseAction getUseAction(ItemStack stack) { return UseAction.BOW; }

    /* Maximum usage time */
    @Override
    public int getMaxUseTime(ItemStack stack) { return 16; }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        user.setCurrentHand(hand);

        return TypedActionResult.success(user.getStackInHand(hand));
    }

    @Override
    public ItemStack finishUsing(ItemStack stack, World world, LivingEntity user)
    {
        if (world.isClient()) { return super.finishUsing(stack, world, user); }
        ServerPlayerEntity player = (ServerPlayerEntity)user;
        if(!player.isCreative())
            player.getItemCooldownManager().set(this, cooldown);
        if (worker != null)
        {
            worker.stop();
            worker = null;
        }
        searchForBiome((ServerWorld) world, (PlayerEntity) user, BiomeKeys.BEACH.getValue(),user.getBlockPos(), stack);
        player.sendMessage(Text.translatable("item.terraccessories.magic_conch.tooltip").formatted(Formatting.DARK_AQUA), true);


        return super.finishUsing(stack,world,user);

    }



    public void searchForBiome(ServerWorld world, PlayerEntity player, Identifier biomeID, BlockPos pos, ItemStack stack)
    {
        Optional<Biome> optionalBiome = BiomesUtils.getBiomeForIdentifier(world, biomeID);
        if (optionalBiome.isPresent()) {
            if (worker != null) {
                worker.stop();
            }
            worker = new BiomeSearchWorker(world, player, stack, optionalBiome.get(), pos);
            worker.start();
        }
    }

    public void succeed(World world, ItemStack stack, PlayerEntity player, int x, int z, int samples ) {
        BlockPos newPos = new BlockPos(x, 65, z);
        world.playSound(null, player.getBlockPos(), SoundEvents.ENTITY_DOLPHIN_AMBIENT_WATER, SoundCategory.PLAYERS, 1.0F, 1.0F);
        Teleport.teleportToPos(player, newPos);
        world.playSound(null, player.getBlockPos(),SoundEvents.ENTITY_DOLPHIN_JUMP, SoundCategory.PLAYERS, 1.0F, 1.0F);
        player.sendMessage(Text.translatable("item.terraccessories.magic_conch.success").formatted(Formatting.DARK_AQUA), true);
        worker = null;
    }

    public void fail() {
        worker = null;
    }





}
