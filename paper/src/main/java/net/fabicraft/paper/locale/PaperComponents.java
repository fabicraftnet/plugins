package net.fabicraft.paper.locale;

import net.fabicraft.common.locale.Components;
import net.kyori.adventure.text.ComponentLike;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public final class PaperComponents {
	public static ComponentLike commandSender(CommandSender sender) {
		if (sender instanceof Player) {
			return Components.player(sender);
		}
		return sender.name();
	}
}
