package net.hezaerd.terraccessories.item;

import io.wispforest.owo.itemgroup.OwoItemSettings;
import net.hezaerd.terraccessories.Terraccessories;
import net.hezaerd.terraccessories.utils.Log;
import net.hezaerd.terraccessories.utils.Raycast;
import net.hezaerd.terraccessories.utils.Suitable;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.UseAction;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

import java.util.List;

public class RodOfDiscord extends Item {
    private static final int rayRange = 8;
    private int range = Terraccessories.CONFIG.rod_of_discord.minRange();

    public RodOfDiscord() {
        super(new OwoItemSettings().group(Terraccessories.TERRACCESSORIES_GROUP).maxCount(1));
    }

    /* Tooltip */
    @Override
    public void appendTooltip(ItemStack stack, World world, List<Text> tooltip, TooltipContext context) {
        tooltip.add(Text.translatable("item.terraccessories.rod_of_discord.tooltip").formatted(Formatting.LIGHT_PURPLE));
        tooltip.add(Text.translatable("item.terraccessories.rod_of_discord.tooltip2").formatted(Formatting.LIGHT_PURPLE, Formatting.BOLD));
    }

    /* Usage animation */
    @Override
    public UseAction getUseAction(ItemStack stack) {
        return UseAction.BOW;
    }

    /* Maximum usage time */
    @Override
    public int getMaxUseTime(ItemStack stack) { return Terraccessories.CONFIG.rod_of_discord.useTime(); }

    /* Usage */
    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        user.setCurrentHand(hand);
        return TypedActionResult.consume(user.getStackInHand(hand));
    }

    /* Usage tick*/
    public void usageTick(World world, LivingEntity user, ItemStack stack, int remainingUseTicks) {

        int temp = Terraccessories.CONFIG.rod_of_discord.useTime() - remainingUseTicks;

        range = MathHelper.lerp((float) temp / Terraccessories.CONFIG.rod_of_discord.useTime(),
                Terraccessories.CONFIG.rod_of_discord.minRange(),
                Terraccessories.CONFIG.rod_of_discord.maxRange());

        Log.i("Lerp %: " + (float) temp / Terraccessories.CONFIG.rod_of_discord.useTime());
        Log.i("Range: " + range);

//        world.playSound(null, user.getX(), user.getY(), user.getZ(), SoundEvents.ENTITY_EXPERIENCE_ORB_PICKUP, SoundCategory.PLAYERS, 1.0F, 1.0F);
    }

    @Override
    public void onStoppedUsing(ItemStack stack, World world, LivingEntity user, int remainingUseTicks) {
        MinecraftClient client = MinecraftClient.getInstance();
        HitResult hitResult = client.cameraEntity.raycast(rayRange, 1.0f, false);

        Log.i("range: " + range);
        PlayerEntity player = (PlayerEntity) user;
        player.sendMessage(Text.of("Range: " + range), true);

        switch (hitResult.getType()) {
            case ENTITY, BLOCK, MISS:
                HitResult hit = Raycast.forwardFromPlayer(range);
                double distance = client.cameraEntity.getEyePos().distanceTo(hit.getPos());
                BlockPos pos = Suitable.findOpenSpotBackwards(hit, distance);

                if (pos != null) {
                    BlockPos playerPos = BlockPos.ofFloored(client.player.getPos());

                    if(!pos.equals(playerPos)) {
                        user.teleport(pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5);
                    }
                }
                break;

            default:
                Log.e("Unknown hit result type");
        }
    }

    /* Usage end */
    @Override
    public ItemStack finishUsing(ItemStack stack, World world, LivingEntity user) {
        MinecraftClient client = MinecraftClient.getInstance();
        HitResult hitResult = client.cameraEntity.raycast(rayRange, 1.0f, false);

        Log.i("range: " + range);
        PlayerEntity player = (PlayerEntity) user;
        player.sendMessage(Text.of("Range: " + Terraccessories.CONFIG.rod_of_discord.maxRange()), true);

        switch (hitResult.getType()) {
            case ENTITY, BLOCK, MISS:
                HitResult hit = Raycast.forwardFromPlayer(range);
                double distance = client.cameraEntity.getEyePos().distanceTo(hit.getPos());
                BlockPos pos = Suitable.findOpenSpotBackwards(hit, distance);

                if (pos != null) {
                    BlockPos playerPos = BlockPos.ofFloored(client.player.getPos());

                    if(!pos.equals(playerPos)) {
                        user.teleport(pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5);
                        return stack;
                    }
                }
                break;

            default:
                Log.e("Unknown hit result type");
                return stack;
        }
        return stack;
    }
}

