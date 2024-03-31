package net.hezaerd.terraccessories.items;

import net.hezaerd.terraccessories.Terraccessories;
import net.hezaerd.terraccessories.common.Teleport;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.UseAction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class MagicMirror extends Item {

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
        return 48;
    }

    /* Enchantment glint */
    @Override
    public boolean hasGlint(ItemStack stack) {
        return true;
    }

    private boolean canUse(PlayerEntity player) {
        return player.experienceLevel >= Terraccessories.CONFIG.mirror.mirror_cost() || player.isCreative();
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        user.setCurrentHand(hand);
        return TypedActionResult.success(user.getStackInHand(hand));
    }

    @Override
    public void usageTick(World world, LivingEntity user, ItemStack stack, int remainingUseTicks) {


        if (remainingUseTicks % 16 == 0) {
            world.playSound(null, user.getX(), user.getY(), user.getZ(), SoundEvents.ENTITY_EXPERIENCE_ORB_PICKUP, SoundCategory.PLAYERS, 1.0F, 1.0F);
        }
    }

    @Override
    public ItemStack finishUsing(ItemStack stack, World world, LivingEntity user) {
        if(world.isClient()) { return super.finishUsing(stack, world, user); }

        ServerPlayerEntity player = (ServerPlayerEntity)user;
        BlockPos spawnPos = player.getSpawnPointPosition();

        if (!player.isCreative())
            player.getItemCooldownManager().set(this, Terraccessories.CONFIG.mirror.mirror_cooldown() * 10);

        if (!canUse(player)) {
            player.sendMessage(Text.translatable("item.terraccessories.magic_mirror.missing_xp").formatted(Formatting.DARK_RED), true);
            return super.finishUsing(stack, world, user);
        }

        if (spawnPos != null) {
            switch (Teleport.teleportToSpawn(player, world, Terraccessories.CONFIG.mirror.mirror_interdimensional())) {
                case 0, 1, 2 -> {
                    if(!player.isCreative()) player.setExperienceLevel(player.experienceLevel - Terraccessories.CONFIG.mirror.mirror_cost());
                    if (Terraccessories.CONFIG.mirror.mirror_debuff()) applyDebuff(player);
                    player.sendMessage(Text.translatable("item.terraccessories.magic_mirror.success").formatted(Formatting.GREEN), true);
                    world.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.BLOCK_PORTAL_TRAVEL, SoundCategory.PLAYERS, 1.0F, 1.0F);
                }
                case 3 -> {
                    player.sendMessage(Text.translatable("item.terraccessories.magic_mirror.missing_spawn").formatted(Formatting.DARK_RED), true);
                    world.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.BLOCK_GLASS_BREAK, SoundCategory.PLAYERS, 1.0F, 1.0F);
                }
            }
        }
        else {
            player.sendMessage(Text.translatable("item.terraccessories.magic_mirror.missing_spawn").formatted(Formatting.DARK_RED), true);
            world.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.BLOCK_GLASS_BREAK, SoundCategory.PLAYERS, 1.0F, 1.0F);
        }

        return super.finishUsing(stack, world, user);
    }

    private void applyDebuff(PlayerEntity player) {
        player.addStatusEffect(new StatusEffectInstance(StatusEffects.BLINDNESS, 2 * 10, 5));
        player.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS, 3 * 10, 10));
    }
}
