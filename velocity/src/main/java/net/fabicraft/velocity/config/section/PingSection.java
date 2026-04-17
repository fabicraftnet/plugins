package net.fabicraft.velocity.config.section;

public interface PingSection {
	default String description() {
		return "<accent><#a78bfa>FabiCraft</accent> | <white>1.21.11</white> | <#5865F2>discord.gg/TJyAWjztUD</#5865F2>";
	}

	default String descriptionUnsupportedVersion() {
		return "<accent><#a78bfa>FabiCraft</accent> | <white>1.21.11</white> | <#5865F2>discord.gg/TJyAWjztUD</#5865F2><newline><red>Please change your Minecraft version!";
	}

	default String icon() {
		return "random";
	}
}
