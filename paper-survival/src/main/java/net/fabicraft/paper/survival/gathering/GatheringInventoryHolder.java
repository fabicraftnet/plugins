package net.fabicraft.paper.survival.gathering;

import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.jetbrains.annotations.NotNull;

public record GatheringInventoryHolder(Gathering gathering) implements InventoryHolder {
	@Override
	public @NotNull Inventory getInventory() {
		return Bukkit.createInventory(this, 27, this.gathering.displayName());
	}
}
