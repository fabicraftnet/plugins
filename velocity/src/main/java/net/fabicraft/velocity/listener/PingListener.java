package net.fabicraft.velocity.listener;

import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyPingEvent;
import com.velocitypowered.api.network.ProtocolVersion;
import com.velocitypowered.api.proxy.server.ServerPing;
import net.fabicraft.velocity.FabiCraftVelocity;
import net.fabicraft.velocity.IconManager;
import net.fabicraft.velocity.VersionRange;
import net.fabicraft.velocity.config.VelocityConfig;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;

public final class PingListener {
	private final FabiCraftVelocity plugin;
	private final IconManager iconManager;

	public PingListener(FabiCraftVelocity plugin) {
		this.plugin = plugin;
		this.iconManager = plugin.iconManager();
	}

	@Subscribe
	public void onPing(ProxyPingEvent event) {
		VelocityConfig config = this.plugin.config();
		ProtocolVersion incomingVersion = event.getConnection().getProtocolVersion();
		VersionRange range = new VersionRange(config.version().min(), config.version().max());

		String description;
		if (range.contains(incomingVersion)) {
			description = config.ping().description();
		} else {
			description = config.ping().descriptionUnsupportedVersion();
		}

		Component descriptionComponent = this.plugin.miniMessage().deserialize(
				description,
				Placeholder.unparsed("version_supported_range", range.toString()),
				Placeholder.unparsed("version_supported_min", range.min().getVersionIntroducedIn()),
				Placeholder.unparsed("version_supported_max", range.max().getMostRecentSupportedVersion()),
				Placeholder.unparsed("version_current", incomingVersion.toString())
		);

		event.setPing(event.getPing().asBuilder()
				.favicon(this.iconManager.icon())
				.version(new ServerPing.Version(
						range.clamp(incomingVersion).getProtocol(),
						range.toString()
				))
				.description(descriptionComponent)
				.build()
		);
	}
}
