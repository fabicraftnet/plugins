package net.fabicraft.paper.survival.listener;

import net.fabicraft.common.locale.Components;
import net.fabicraft.common.locale.MessageType;
import net.fabicraft.paper.survival.FabiCraftPaperSurvival;
import net.fabicraft.paper.survival.gathering.Gathering;
import net.fabicraft.paper.survival.gathering.GatheringInventoryHolder;
import net.fabicraft.paper.survival.gathering.GatheringManager;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Container;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.*;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public final class GatheringListener implements Listener {
	private final GatheringManager gatheringManager;

	public GatheringListener(FabiCraftPaperSurvival plugin) {
		this.gatheringManager = plugin.gatheringManager();
	}

	@EventHandler
	public void onContainerOpen(InventoryOpenEvent event) {
		if (!(event.getInventory().getHolder() instanceof Container container)) {
			return;
		}

		Gathering gathering = this.gatheringManager.gathering(container.getLocation());
		if (gathering == null) {
			return;
		}

		event.setCancelled(true);

		if (gathering.isCompleted()) {
			event.getPlayer().sendMessage(Components.translatable(
					"fabicraft.paper.survival.gathering.completed",
					MessageType.ERROR,
					gathering.displayName()
			));
			return;
		}

		event.getPlayer().openInventory(gathering.inventoryHolder().getInventory());
	}

	@EventHandler
	public void onClose(InventoryCloseEvent event) {
		HumanEntity player = event.getPlayer();
		Inventory gatheringInventory = event.getView().getTopInventory();
		if (!(gatheringInventory.getHolder() instanceof GatheringInventoryHolder(Gathering gathering))) {
			return;
		}
		int added = 0;
		for (ItemStack item : gatheringInventory) {
			if (item == null || Material.AIR.equals(item.getType())) {
				continue;
			}

			if (!gathering.material().equals(item.getType())) {
				player.getInventory().addItem(item);
				continue;
			}

			added += item.getAmount();
		}

		gathering.add(added);
		this.gatheringManager.save(gathering);

		if (added > 0) {
			player.sendMessage(Components.translatable(
					"fabicraft.paper.survival.gathering.add",
					MessageType.INFO,
					added,
					gathering.displayName()
			));
		}

		if (gathering.isCompleted()) {
			Bukkit.broadcast(Components.translatable(
					"fabicraft.paper.survival.gathering.completed.broadcast",
					MessageType.SUCCESS,
					Components.player(player),
					gathering.displayName()
			));
		}
	}

	@EventHandler
	public void onContainerAdd(InventoryClickEvent event) {
		Inventory topInventory = event.getView().getTopInventory();
		if (!(topInventory.getHolder() instanceof GatheringInventoryHolder(Gathering gathering))) {
			return;
		}

		ItemStack cursor = event.getCursor();
		ItemStack current = event.getCurrentItem();

		//Player clicked the top inventory
		if (topInventory.equals(event.getClickedInventory())) {
			if (event.getAction().equals(InventoryAction.HOTBAR_SWAP) || current == null && !gathering.material().equals(cursor.getType())) {
				event.setCancelled(true);
			}
			return;
		}

		if (event.getAction() == InventoryAction.MOVE_TO_OTHER_INVENTORY) {
			if (current != null && !gathering.material().equals(current.getType())) {
				event.setCancelled(true);
			}
		}
	}

	@EventHandler
	public void onDrag(InventoryDragEvent event) {
		Inventory topInventory = event.getView().getTopInventory();
		if (topInventory.getHolder() instanceof GatheringInventoryHolder) {
			event.setCancelled(true);
		}
	}
}
