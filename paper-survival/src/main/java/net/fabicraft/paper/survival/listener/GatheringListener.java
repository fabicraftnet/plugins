package net.fabicraft.paper.survival.listener;

import net.fabicraft.common.locale.Components;
import net.fabicraft.common.locale.MessageType;
import net.fabicraft.paper.survival.FabiCraftPaperSurvival;
import net.fabicraft.paper.survival.gathering.Gathering;
import net.fabicraft.paper.survival.gathering.GatheringInventoryHolder;
import net.kyori.adventure.text.TranslatableComponent;
import org.bukkit.Bukkit;
import org.bukkit.block.Container;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public final class GatheringListener implements Listener {
	private static final TranslatableComponent COMPONENT_GATHERING_COMPLETED = Components.translatable(
			"fabicraft.paper.survival.gathering.completed",
			MessageType.ERROR
	);
	private final FabiCraftPaperSurvival plugin;

	public GatheringListener(FabiCraftPaperSurvival plugin) {
		this.plugin = plugin;
	}

	@EventHandler
	public void onContainerOpen(InventoryOpenEvent event) {
		if (!(event.getInventory().getHolder() instanceof Container container)) {
			return;
		}

		Gathering gathering = this.plugin.gatheringManager().gathering(container.getLocation());
		if (gathering == null) {
			return;
		}

		event.setCancelled(true);

		if (gathering.isCompleted()) {
			event.getPlayer().sendMessage(COMPONENT_GATHERING_COMPLETED);
			return;
		}

		event.getPlayer().openInventory(gathering.inventoryHolder().getInventory());
	}

	@EventHandler
	public void onContainerAdd(InventoryClickEvent event) {
		Inventory mainInventory = event.getInventory();
		Inventory clickedInventory = event.getClickedInventory();

		if (clickedInventory == null || !(mainInventory.getHolder() instanceof GatheringInventoryHolder gatheringInventoryHolder)) {
			return;
		}

		if (mainInventory.equals(clickedInventory)) {
			event.setCancelled(true);
		}

		Gathering gathering = gatheringInventoryHolder.gathering();

		if (gathering.isCompleted()) {
			event.setCancelled(true);
			mainInventory.close();
		}

		ItemStack cursorItem = event.getCursor();
		if (!cursorItem.getType().equals(gathering.material())) {
			event.setCancelled(true);
		}

		int amount = cursorItem.getAmount();
		if (gathering.add(amount)) {
			TranslatableComponent component = Components.translatable(
					"fabicraft.paper.survival.gathering.completed.broadcast",
					MessageType.SUCCESS,
					Components.player(event.getWhoClicked()),
					gathering.identifier()
			);
			Bukkit.broadcast(component);
		}
		this.plugin.gatheringManager().save();
		Bukkit.getScheduler().runTaskLater(this.plugin, () -> mainInventory.remove(cursorItem), 20);
	}
}
