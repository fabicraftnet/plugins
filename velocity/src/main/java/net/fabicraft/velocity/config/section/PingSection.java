package net.fabicraft.velocity.config.section;

public interface PingSection {
	default String description() {
		return "<#94a3b8><#a78bfa>FabiCraft</#a78bfa> | <white><version_supported_range></white> | <#5865F2>discord.gg/TJyAWjztUD</#5865F2>";
	}

	default String descriptionUnsupportedVersion() {
		return "<#94a3b8><#a78bfa>FabiCraft</#a78bfa> | <white><version_supported_range></white> | <#5865F2>discord.gg/TJyAWjztUD</#5865F2><newline><red>Vaihda Minecraftin versiota pelataksesi palvelimella";
	}

	default String icon() {
		return "random";
	}
}
