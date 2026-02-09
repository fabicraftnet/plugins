package net.fabicraft.paper.survival.listener;

import net.fabicraft.common.locale.Components;
import net.fabicraft.common.locale.MessageType;
import net.fabicraft.paper.survival.FabiCraftPaperSurvival;
import net.fabicraft.paper.survival.gathering.Gathering;
import net.kyori.adventure.text.TranslatableComponent;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInputEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.Locale;

public final class PlayerListener implements Listener {
	private final FabiCraftPaperSurvival plugin;

	public PlayerListener(FabiCraftPaperSurvival plugin) {
		this.plugin = plugin;
	}

	@EventHandler
	public void onFirstJoin(PlayerJoinEvent event) {
		Player player = event.getPlayer();
		if (player.hasPlayedBefore()) {
			return;
		}

		Inventory inventory = player.getInventory();
		ItemStack claimShovel = this.plugin.customItemManager().itemStack("tonttilapio");
		if (claimShovel != null) {
			inventory.addItem(claimShovel);
		} else {
			this.plugin.getSLF4JLogger().warn("Couldn't find custom item named tonttilapio. Giving normal golden shovel instead. Please check items.json!");
			inventory.addItem(ItemStack.of(Material.GOLDEN_SHOVEL));
		}
		inventory.addItem(ItemStack.of(Material.BREAD, 16));
		inventory.addItem(ItemStack.of(Material.BAMBOO_RAFT));
	}

	@EventHandler
	public void onWorldChange(PlayerTeleportEvent event) {
		World from = event.getFrom().getWorld();
		World to = event.getTo().getWorld();

		if (from.getName().equalsIgnoreCase(to.getName())) {
			return;
		}

		if (event.getPlayer().hasPermission("fabicraft.paper.survival.bypass.world-teleport")) {
			return;
		}

		Gathering gathering;
		switch (to.getName().toLowerCase(Locale.ROOT)) {
			case "world_nether" -> gathering = this.plugin.gatheringManager().gathering("nether");
			case "world_the_end" -> gathering = this.plugin.gatheringManager().gathering("end");
			default -> gathering = null;
		}

		if (gathering == null || gathering.isCompleted()) {
			return;
		}

		event.setCancelled(true);
		TranslatableComponent component = Components.translatable("fabicraft.paper.survival.gathering.world-teleport-disallowed", MessageType.ERROR, gathering.displayName());
		event.getPlayer().sendMessage(component);
	}

	@EventHandler
	public void onInput(PlayerInputEvent event) {
		this.plugin.afkManager().update(event.getPlayer().getUniqueId());
	}

	@EventHandler
	public void onQuit(PlayerQuitEvent event) {
		this.plugin.afkManager().remove(event.getPlayer().getUniqueId());
	}
}
