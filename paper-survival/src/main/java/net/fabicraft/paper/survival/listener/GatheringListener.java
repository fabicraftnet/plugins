package net.fabicraft.paper.survival.listener;

import net.fabicraft.common.locale.Components;
import net.fabicraft.common.locale.MessageType;
import net.fabicraft.paper.survival.FabiCraftPaperSurvival;
import net.fabicraft.paper.survival.gathering.Gathering;
import net.kyori.adventure.text.TranslatableComponent;
import org.bukkit.Bukkit;
import org.bukkit.block.Container;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryMoveItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;

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
	public void onContainerOpen(PlayerInteractEvent event) {
		if (!event.hasBlock() || !(event.getClickedBlock() instanceof Container container)) {
			return;
		}
		Gathering gathering = plugin.gatheringManager().gathering(container.getLocation());
		if (gathering == null) {
			return;
		}
		if (gathering.isCompleted()) {
			event.getPlayer().sendMessage(COMPONENT_GATHERING_COMPLETED);
			event.setCancelled(true);
		}
	}

	@EventHandler
	public void onContainerAdd(InventoryMoveItemEvent event) {
		if (!(event.getInitiator().getHolder() instanceof Player player)) {
			return;
		}

		if (event.getSource().getHolder() instanceof Container container) {
			Gathering gathering = plugin.gatheringManager().gathering(container.getLocation());
			if (gathering != null) {
				event.setCancelled(true);
			}
		}

		if (!(event.getDestination().getHolder() instanceof Container container)) {
			return;
		}

		Gathering gathering = plugin.gatheringManager().gathering(container.getLocation());
		if (gathering == null) {
			return;
		}

		if (gathering.isCompleted()) {
			event.setCancelled(true);
			event.getDestination().close();
		}

		if (!event.getItem().getType().equals(gathering.gatherableMaterial())) {
			event.setCancelled(true);
		}

		int amount = event.getItem().getAmount();
		if (gathering.add(amount)) {
			TranslatableComponent component = Components.translatable(
					"fabicraft.paper.survival.gathering.completed.broadcast",
					MessageType.SUCCESS,
					Components.player(player),
					gathering.identifier()
			);
			Bukkit.broadcast(component);
		}
		Bukkit.getScheduler().runTaskLater(this.plugin, () -> event.getDestination().remove(event.getItem()), 20);
	}
}
