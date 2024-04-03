package net.hezaerd.terraccessories.screen;

import net.hezaerd.terraccessories.block.ModBlock;
import net.hezaerd.terraccessories.registry.ScreenHandlerInit;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.screen.slot.Slot;

public class TinkererScreenHandler extends ScreenHandler {
    private final ScreenHandlerContext context;
    private final Inventory inventory;

    public TinkererScreenHandler(int index, PlayerInventory playerInventory) {
        this(index, playerInventory, ScreenHandlerContext.EMPTY, new SimpleInventory(3));
    }

    public TinkererScreenHandler(int syncId, PlayerInventory playerInventory, ScreenHandlerContext context, Inventory entityInventory) {
        super(ScreenHandlerInit.TINKERER_WORKSHOP_SCREEN_HANDLER_TYPE, syncId);
        this.context = context;
        this.inventory = entityInventory;

        this.addSlot(new Slot(entityInventory, 0, 80, 11));
        this.addSlot(new Slot(entityInventory, 1, 80, 59));

        addPlayerInventory(playerInventory);
        addPlayerHotbar(playerInventory);
    }

    @Override
    public ItemStack quickMove(PlayerEntity player, int invSlot) {
        ItemStack newStack = ItemStack.EMPTY;
        Slot slot = this.slots.get(invSlot);
        if (slot != null && slot.hasStack()) {
            ItemStack originalStack = slot.getStack();
            newStack = originalStack.copy();
            if (invSlot < this.inventory.size()) {
                if (!this.insertItem(originalStack, this.inventory.size(), this.slots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.insertItem(originalStack, 0, this.inventory.size(), false)) {
                return ItemStack.EMPTY;
            }

            if (originalStack.isEmpty()) {
                slot.setStack(ItemStack.EMPTY);
            } else {
                slot.markDirty();
            }
        }

        return newStack;
    }

    private void addPlayerInventory(PlayerInventory playerInventory) {
        for (int i = 0; i < 3; ++i) {
            for (int l = 0; l < 9; ++l) {
                this.addSlot(new Slot(playerInventory, l + i * 9 + 9, 8 + l * 18, 84 + i * 18));
            }
        }
    }

    private void addPlayerHotbar(PlayerInventory playerInventory) {
        for (int i = 0; i < 9; ++i) {
            this.addSlot(new Slot(playerInventory, i, 8 + i * 18, 142));
        }
    }

    @Override
    public boolean canUse(PlayerEntity player) {
        return canUse(context, player, ModBlock.TINKERER_WORKSHOP);
    }
}