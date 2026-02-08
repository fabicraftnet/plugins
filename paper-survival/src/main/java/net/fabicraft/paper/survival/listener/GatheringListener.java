package net.fabicraft.paper.survival.listener;

import net.fabicraft.common.locale.Components;
import net.fabicraft.common.locale.MessageType;
import net.fabicraft.paper.survival.FabiCraftPaperSurvival;
import net.fabicraft.paper.survival.gathering.Gathering;
import net.fabicraft.paper.survival.gathering.GatheringInventoryHolder;
import net.kyori.adventure.text.TranslatableComponent;
import org.bukkit.Bukkit;
import org.bukkit.block.Container;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryAction;
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
		Inventory topInventory = event.getView().getTopInventory();
		if (!(topInventory.getHolder() instanceof GatheringInventoryHolder gatheringInventoryHolder)) {
			return;
		}

		Gathering gathering = gatheringInventoryHolder.gathering();
		ItemStack cursorItem = event.getCursor();
		ItemStack clickedItem = event.getCurrentItem();

		//Player clicked the top inventory
		if (topInventory.equals(event.getClickedInventory())) {
			if (clickedItem == null && cursorItem.getType().equals(gathering.material())) {
				addToGathering(gathering, event.getWhoClicked(), cursorItem);
				return;
			}
			event.setCancelled(true);
			return;
		}

		//Player clicked the bottom inventory
		if (event.getAction().equals(InventoryAction.COLLECT_TO_CURSOR)) {
			event.setCancelled(true);
			return;
		}

		if (clickedItem == null) {
			return;
		}

		if (!clickedItem.getType().equals(gathering.material())) {
			event.setCancelled(true);
			return;
		}

		if (!event.getAction().equals(InventoryAction.MOVE_TO_OTHER_INVENTORY)) {
			return;
		}

		addToGathering(gathering, event.getWhoClicked(), clickedItem);
	}

	private void addToGathering(Gathering gathering, HumanEntity humanEntity, ItemStack itemStack) {
		int amount = itemStack.getAmount();
		if (gathering.add(amount)) {
			closeForEveryone(gathering);
			TranslatableComponent component = Components.translatable(
					"fabicraft.paper.survival.gathering.completed.broadcast",
					MessageType.SUCCESS,
					Components.player(humanEntity),
					gathering.displayName()
			);
			Bukkit.broadcast(component);
		}
		this.plugin.gatheringManager().save();
	}

	private void closeForEveryone(Gathering gathering) {
		this.plugin.getServer().getOnlinePlayers().forEach(player -> {
			Inventory topInventory = player.getOpenInventory().getTopInventory();
			if (!(topInventory.getHolder() instanceof GatheringInventoryHolder gatheringInventoryHolder)) {
				return;
			}
			if (!gatheringInventoryHolder.gathering().equals(gathering)) {
				return;
			}
			topInventory.close();
		});
	}
}
