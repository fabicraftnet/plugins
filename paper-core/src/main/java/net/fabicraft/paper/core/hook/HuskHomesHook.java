package net.fabicraft.paper.core.hook;

import net.fabicraft.paper.core.FabiCraftPaperCore;
import net.william278.huskhomes.BukkitHuskHomes;
import net.william278.huskhomes.api.HuskHomesAPI;
import net.william278.huskhomes.event.HomeCreateEvent;
import net.william278.huskhomes.position.Position;
import net.william278.huskhomes.user.OnlineUser;
import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.slf4j.Logger;

public final class HuskHomesHook implements Listener {
	private final Logger logger;
	private final FabiCraftPaperCore plugin;
	private Location spawnLocation;

	public HuskHomesHook(FabiCraftPaperCore plugin) {
		this.plugin = plugin;
		this.logger = plugin.getSLF4JLogger();
	}

	public void load() {
		HuskHomesAPI huskhomes = HuskHomesAPI.getInstance();
		huskhomes.getSpawn().thenAccept(positionOptional -> {
			if (positionOptional.isEmpty()) {
				return;
			}

			Position position = positionOptional.get();
			if (!position.getServer().equalsIgnoreCase(huskhomes.getServer())) {
				return;
			}
			this.spawnLocation = BukkitHuskHomes.Adapter.adapt(position);
			this.logger.info("Got spawn location from HuskHomes");
		});
	}

	public Location spawnLocation() {
		return this.spawnLocation;
	}

	public void registerListeners() {
		this.plugin.getServer().getPluginManager().registerEvents(this, this.plugin);
	}

	@EventHandler
	public void onHomeCreate(HomeCreateEvent event) {
		if (HuskHomesAPI.getInstance().getMaxHomeSlots((OnlineUser) event.getOwner()) > 1) {
			return;
		}
		event.setName("home");
	}
}
