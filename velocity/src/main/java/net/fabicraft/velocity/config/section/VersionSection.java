package net.fabicraft.velocity.config.section;

import com.velocitypowered.api.network.ProtocolVersion;
import space.arim.dazzleconf.engine.Comments;

import java.util.List;

public interface VersionSection {
	@Comments("Minimum protocol that can join")
	default ProtocolVersion min() {
		return ProtocolVersion.MINIMUM_VERSION;
	}

	@Comments("Maximum protocol that can join")
	default ProtocolVersion max() {
		return ProtocolVersion.MAXIMUM_VERSION;
	}

	default List<String> loginUnsupportedVersionMessage() {
		return List.of(
				"<red>You are using an unsupported Minecraft version!",
				"",
				"<red>This server supports Minecraft <version_supported_range>"
		);
	}
}
