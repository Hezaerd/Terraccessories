package net.hezaerd.terraccessories.block.entity;

import io.wispforest.owo.util.ImplementedInventory;
import net.hezaerd.terraccessories.Terraccessories;
import net.hezaerd.terraccessories.recipe.TinkererWorkshopRecipe;
import net.hezaerd.terraccessories.registry.BlockInit;
import net.hezaerd.terraccessories.registry.ItemsInit;
import net.hezaerd.terraccessories.screen.TinkererScreenHandler;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventories;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.text.Text;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class TinkererWorkshopEntity extends BlockEntity implements NamedScreenHandlerFactory, ImplementedInventory {
    private final DefaultedList<ItemStack> inventory = DefaultedList.ofSize(3, ItemStack.EMPTY);

    private static final int INPUT_SLOT = 0;
    private static final int OUTPUT_SLOT = 1;

    public TinkererWorkshopEntity(BlockPos pos, BlockState state) {
        super(BlockInit.Entities.TINKERER_WORKSHOP, pos, state);
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);

        this.inventory.clear();
        Inventories.readNbt(nbt, this.inventory);
    }

    @Override
    protected void writeNbt(NbtCompound nbt) {
        super.writeNbt(nbt);
        Inventories.writeNbt(nbt, this.inventory);
    }

    @Override
    public Text getDisplayName() {
        return this.getCachedState().getBlock().getName();
    }

    public DefaultedList<ItemStack> inventory() {
        return this.inventory;
    }

    @Nullable
    @Override
    public ScreenHandler createMenu(int syncId, PlayerInventory inv, PlayerEntity player) {
        return new TinkererScreenHandler(
                syncId,
                player.getInventory(),
                ScreenHandlerContext.create(this.world, this.pos),
                (ImplementedInventory) () -> TinkererWorkshopEntity.this.inventory
        );
    }

    public void tick(World world) {
        if(world.isClient()) {
            return;
        }

        if(isOutputSlotEmptyOrReceivable()) {
            if (this.hasRecipe()) {
                this.craftItem();
            }
        }
    }

    private Optional<TinkererWorkshopRecipe> getCurrentRecipe() {
        SimpleInventory inv = new SimpleInventory(this.size());
        for(int i = 0; i < this.size(); i++) {
            inv.setStack(i, this.getStack(i));
        }

        return getWorld().getRecipeManager().getFirstMatch(TinkererWorkshopRecipe.Type.INSTANCE, inv, getWorld());
    }

    private void craftItem() {
        Optional<TinkererWorkshopRecipe> recipe = getCurrentRecipe();

        this.removeStack(INPUT_SLOT, 1);

        this.setStack(OUTPUT_SLOT, new ItemStack(ItemsInit.MAGIC_MIRROR, 1));
    }

    private boolean canInsertItemIntoOutputSlot(Item item) {
        return this.getStack(OUTPUT_SLOT).getItem() == item || this.getStack(OUTPUT_SLOT).isEmpty();
    }

    private boolean canInsertAmountIntoOutputSlot(ItemStack result) {
        return this.getStack(OUTPUT_SLOT).getCount() + result.getCount() <= getStack(OUTPUT_SLOT).getMaxCount();
    }

    private boolean isOutputSlotEmptyOrReceivable() {
        return this.getStack(OUTPUT_SLOT).isEmpty() || this.getStack(OUTPUT_SLOT).getCount() < this.getStack(OUTPUT_SLOT).getMaxCount();
    }

    private boolean hasRecipe() {
        ItemStack result = new ItemStack(ItemsInit.DEMON_CONCH);
        boolean hasInput = getStack(INPUT_SLOT).getItem() == Items.BLAZE_ROD;

        return hasInput && canInsertAmountIntoOutputSlot(result) && canInsertItemIntoOutputSlot(result.getItem());
    }

    @Override
    public DefaultedList<ItemStack> getItems() {
        return inventory;
    }
}
