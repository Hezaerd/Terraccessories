package net.hezaerd.terraccessories.items;

import net.hezaerd.terraccessories.common.Teleport;
import net.minecraft.entity.LivingEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.UseAction;
import net.minecraft.util.math.BlockPos;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class MagicMirror extends Item {

    public static final String MOD_ID = "terraccessories";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    /* Properties */
    private static final int cooldown = 200;
    private static final boolean interdimensional = true;

    public MagicMirror(Settings settings) {
        super(settings.maxCount(1));
    }

    /* Tooltip */
    @Override
    public void appendTooltip(ItemStack stack, World world, java.util.List<net.minecraft.text.Text> tooltip, net.minecraft.client.item.TooltipContext context) {
        tooltip.add(Text.translatable("item.terraccessories.magic_mirror.tooltip").formatted(Formatting.DARK_PURPLE));
    }

    /* Usage animation */
    @Override
    public UseAction getUseAction(ItemStack stack) {
        return UseAction.BOW;
    }

    /* Maximum usage time */
    @Override
    public int getMaxUseTime(ItemStack stack) {
        return 64;
    }

    /* Enchantment glint */
    @Override
    public boolean hasGlint(ItemStack stack) {
        return true;
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        user.setCurrentHand(hand);
        return TypedActionResult.success(user.getStackInHand(hand));
    }

    @Override
    public ItemStack finishUsing(ItemStack stack, World world, LivingEntity user) {
        if(world.isClient()) { return super.finishUsing(stack, world, user); }

        ServerPlayerEntity player = (ServerPlayerEntity)user;
        BlockPos spawnPos = player.getSpawnPointPosition();

        player.getItemCooldownManager().set(this, cooldown);

        if (spawnPos != null) {
            switch (Teleport.teleportToSpawn(player, world, interdimensional)) {
                case 0, 1, 2 -> {
                    player.sendMessage(Text.translatable("item.terraccessories.magic_mirror.success").formatted(Formatting.AQUA), true);
                    world.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.BLOCK_PORTAL_TRAVEL, SoundCategory.PLAYERS, 1.0F, 1.0F);
                }
                case 3 -> {
                    player.sendMessage(Text.translatable("item.terraccessories.magic_mirror.fail").formatted(Formatting.GREEN), true);
                    world.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.ENTITY_SHULKER_BULLET_HURT, SoundCategory.PLAYERS, 1.0F, 1.0F);
                }
            }
        }
        else {
            player.sendMessage(Text.translatable("item.terraccessories.magic_mirror.fail").formatted(Formatting.DARK_RED), true);
            world.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.ENTITY_SHULKER_BULLET_HURT, SoundCategory.PLAYERS, 1.0F, 1.0F);
        }

        return super.finishUsing(stack, world, user);
    }
}
