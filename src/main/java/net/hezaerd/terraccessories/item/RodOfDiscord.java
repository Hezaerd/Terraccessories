package net.hezaerd.terraccessories.item;

import io.wispforest.owo.itemgroup.OwoItemSettings;
import net.hezaerd.terraccessories.Terraccessories;
import net.hezaerd.terraccessories.utils.Log;
import net.hezaerd.terraccessories.utils.Raycast;
import net.hezaerd.terraccessories.utils.Suitable;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.List;

public class RodOfDiscord extends Item {

    private static final int reach = 8; // reach in blocks

    public RodOfDiscord() {
        super(new OwoItemSettings().group(Terraccessories.TERRACCESSORIES_GROUP).maxCount(1));
    }

    /* Tooltip */
    @Override
    public void appendTooltip(ItemStack stack, World world, List<Text> tooltip, TooltipContext context) {
        tooltip.add(Text.translatable("item.terraccessories.rod_of_discord.tooltip").formatted(Formatting.LIGHT_PURPLE));
        tooltip.add(Text.translatable("item.terraccessories.rod_of_discord.tooltip2").formatted(Formatting.LIGHT_PURPLE, Formatting.BOLD));
    }

    /* Usage */
    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        MinecraftClient client = MinecraftClient.getInstance();
        HitResult hitResult = client.cameraEntity.raycast(reach, 1.0f, false);

        switch (hitResult.getType()) {
            case ENTITY, BLOCK, MISS:
                HitResult hit = Raycast.forwardFromPlayer(Terraccessories.CONFIG.rod_of_discord.range());
                double distance = client.cameraEntity.getEyePos().distanceTo(hit.getPos());
                BlockPos pos = Suitable.findOpenSpotBackwards(hit, distance);

                if (pos != null) {
                    BlockPos playerPos = BlockPos.ofFloored(client.player.getPos());

                    if(!pos.equals(playerPos)) {
                        user.teleport(pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5);
                        return TypedActionResult.success(user.getStackInHand(hand));
                    }
                }
                break;

            default:
                Log.e("Unknown hit result type");
                return TypedActionResult.fail(user.getStackInHand(hand));
        }

        return TypedActionResult.success(user.getStackInHand(hand));
    }

}
