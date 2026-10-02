package net.fabicraft.paper.common.dialog;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import net.kyori.adventure.translation.GlobalTranslator;
import org.bukkit.entity.Player;

public abstract class DialogFactory {
	protected final Player player;
	private final PlainTextComponentSerializer plainTextComponentSerializer = PlainTextComponentSerializer.plainText();

	public DialogFactory(Player player) {
		this.player = player;
	}

	public abstract void show();

	protected Component render(Component component) {
		return GlobalTranslator.render(component, this.player.locale());
	}

	protected String renderPlainText(Component component) {
		return this.plainTextComponentSerializer.serialize(render(component));
	}
}
