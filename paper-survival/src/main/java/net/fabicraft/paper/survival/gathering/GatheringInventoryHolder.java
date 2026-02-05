package net.fabicraft.paper.survival.gathering;

import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.jetbrains.annotations.NotNull;

public final class GatheringInventoryHolder implements InventoryHolder {
	private final Gathering gathering;

	public GatheringInventoryHolder(Gathering gathering) {
		this.gathering = gathering;
	}

	@Override
	public @NotNull Inventory getInventory() {
		return Bukkit.createInventory(this, 27, this.gathering.displayName());
	}
}
