package net.fabicraft.velocity.listener;

import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.connection.PostLoginEvent;
import com.velocitypowered.api.network.ProtocolVersion;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.util.ServerLink;

import java.util.List;

public final class LoginListener {
	@Subscribe
	public void onPostLogin(PostLoginEvent event) {
		sendServerLinks(event);
	}

	private void sendServerLinks(PostLoginEvent event) {
		Player player = event.getPlayer();

		if (player.getProtocolVersion().lessThan(ProtocolVersion.MINECRAFT_1_21)) {
			return;
		}

		player.setServerLinks(List.of(
				ServerLink.serverLink(ServerLink.Type.WEBSITE, "https://fabicraft.net"),
				ServerLink.serverLink(ServerLink.Type.SUPPORT, "https://discord.gg/TJyAWjztUD"),
				ServerLink.serverLink(ServerLink.Type.BUG_REPORT, "https://discord.gg/TJyAWjztUD"),
				ServerLink.serverLink(ServerLink.Type.COMMUNITY_GUIDELINES, "https://fabicraft.net/rules/")
		));
	}
}
