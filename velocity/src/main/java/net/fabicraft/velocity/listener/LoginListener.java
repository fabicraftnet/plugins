package net.fabicraft.velocity.listener;

import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.connection.PostLoginEvent;
import com.velocitypowered.api.event.connection.PreLoginEvent;
import com.velocitypowered.api.network.ProtocolVersion;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.util.ServerLink;
import net.fabicraft.velocity.FabiCraftVelocity;
import net.fabicraft.velocity.VersionRange;
import net.fabicraft.velocity.config.section.VersionSection;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;

import java.util.List;

public final class LoginListener {
	private final FabiCraftVelocity plugin;

	public LoginListener(FabiCraftVelocity plugin) {
		this.plugin = plugin;
	}

	@Subscribe
	public void onPreLogin(PreLoginEvent event) {
		VersionSection config = this.plugin.config().version();
		VersionRange range = new VersionRange(config.min(), config.max());
		ProtocolVersion incomingVersion = event.getConnection().getProtocolVersion();
		if (range.contains(incomingVersion)) {
			return;
		}

		MiniMessage miniMessage = this.plugin.miniMessageBuilder().editTags(tags -> tags.resolvers(
				Placeholder.unparsed("version_supported_range", range.toString()),
				Placeholder.unparsed("version_supported_min", range.min().getVersionIntroducedIn()),
				Placeholder.unparsed("version_supported_max", range.max().getMostRecentSupportedVersion()),
				Placeholder.unparsed("version_current", incomingVersion.toString())
		)).build();

		Component message = config.loginUnsupportedVersionMessage().stream().map(miniMessage::deserialize).collect(Component.toComponent(Component.newline()));

		event.setResult(PreLoginEvent.PreLoginComponentResult.denied(message));
	}

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
