package net.fabicraft.paper.core.listener;

import net.william278.huskhomes.event.HomeCreateEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public final class HuskHomesListener implements Listener {

	@EventHandler
	public void onHomeCreate(HomeCreateEvent event) {
		//TODO If player has multiple home permission, don't limit
		event.setName("home");
	}
}
