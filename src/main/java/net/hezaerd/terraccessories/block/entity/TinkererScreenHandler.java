package net.hezaerd.terraccessories.block.entity;

import io.wispforest.owo.client.screens.ScreenUtils;
import io.wispforest.owo.client.screens.SlotGenerator;
import io.wispforest.owo.client.screens.ValidatingSlot;
import net.hezaerd.terraccessories.registry.BlockInit;
import net.hezaerd.terraccessories.registry.ItemsInit;
import net.hezaerd.terraccessories.registry.ScreenHandlerInit;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerContext;

public class TinkererScreenHandler extends ScreenHandler {
    private final ScreenHandlerContext context;

    public TinkererScreenHandler(int index, PlayerInventory playerInventory) {
        this(index, playerInventory, ScreenHandlerContext.EMPTY, new SimpleInventory(3));
    }

    public TinkererScreenHandler(int syncId, PlayerInventory playerInventory, ScreenHandlerContext context, Inventory entityInventory) {
        super(ScreenHandlerInit.TINKERER_WORKSHOP_SCREEN_HANDLER_TYPE, syncId);
        this.context = context;

        this.addSlot(new ValidatingSlot(entityInventory, 0, 62, 26, stack -> stack.isOf(ItemsInit.AGLET)));
        this.addSlot(new ValidatingSlot(entityInventory, 1, 80, 26, stack -> stack.isOf(ItemsInit.AGLET)));
        this.addSlot(new ValidatingSlot(entityInventory, 2, 98, 26, stack -> stack.isOf(ItemsInit.AGLET)));

        SlotGenerator.begin(this::addSlot, 8, 63).playerInventory(playerInventory);
    }

    @Override
    public ItemStack quickMove(PlayerEntity player, int index) {
        return ScreenUtils.handleSlotTransfer(this, index, 3);
    }

    @Override
    public boolean canUse(PlayerEntity player) {
        return canUse(context, player, BlockInit.TINKERER_WORKSHOP);
    }
}