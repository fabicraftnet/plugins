package net.fabicraft.paper.core.listener;

import net.william278.huskhomes.api.HuskHomesAPI;
import net.william278.huskhomes.event.HomeCreateEvent;
import net.william278.huskhomes.user.OnlineUser;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public final class HuskHomesListener implements Listener {

	@EventHandler
	public void onHomeCreate(HomeCreateEvent event) {
		if (HuskHomesAPI.getInstance().getMaxHomeSlots((OnlineUser) event.getOwner()) > 1) {
			return;
		}
		event.setName("home");
	}
}
