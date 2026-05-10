package com.STALJER.statsupgrader.menu;

import com.STALJER.statsupgrader.StatsUpgrader;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;

public class CustomInventoryMenu extends AbstractContainerMenu {

    public CustomInventoryMenu(int pContainerId, Inventory playerInventory) {
        super(StatsUpgrader.CUSTOM_INVENTORY_MENU.get(), pContainerId);
    }

    @Override
    public ItemStack quickMoveStack(Player pPlayer, int pIndex) {
        return ItemStack.EMPTY;
    }

    @Override
    public boolean stillValid(Player pPlayer) {
        return true;
    }
}