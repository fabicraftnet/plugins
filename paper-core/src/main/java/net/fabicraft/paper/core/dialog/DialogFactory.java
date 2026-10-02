package net.fabicraft.paper.core.dialog;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.translation.GlobalTranslator;
import org.bukkit.entity.Player;

public abstract class DialogFactory {
	protected final Player player;

	public DialogFactory(Player player) {
		this.player = player;
	}

	public abstract void show();

	protected Component render(Component component) {
		return GlobalTranslator.render(component, this.player.locale());
	}
}
