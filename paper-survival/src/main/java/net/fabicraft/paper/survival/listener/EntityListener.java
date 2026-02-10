package net.fabicraft.paper.survival.listener;

import net.fabicraft.paper.survival.FabiCraftPaperSurvival;
import net.fabicraft.paper.survival.gathering.Gathering;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityTeleportEvent;

import java.util.Locale;

public final class EntityListener implements Listener {
	private final FabiCraftPaperSurvival plugin;

	public EntityListener(FabiCraftPaperSurvival plugin) {
		this.plugin = plugin;
	}

	@EventHandler
	public void onTeleport(EntityTeleportEvent event) {
		World from = event.getFrom().getWorld();
		Location toLocation = event.getTo();
		if (toLocation == null) {
			return;
		}

		World to = toLocation.getWorld();

		if (from.getName().equalsIgnoreCase(to.getName())) {
			return;
		}

		Gathering gathering;
		switch (to.getName().toLowerCase(Locale.ROOT)) {
			case "world_nether" -> gathering = this.plugin.gatheringManager().nether();
			case "world_the_end" -> gathering = this.plugin.gatheringManager().end();
			default -> gathering = null;
		}

		if (gathering == null || gathering.isCompleted()) {
			return;
		}

		event.setCancelled(true);
	}
}
