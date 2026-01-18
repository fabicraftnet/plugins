package net.fabicraft.common.locale;

import net.kyori.adventure.identity.Identity;
import net.kyori.adventure.pointer.Pointered;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TranslatableComponent;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static net.kyori.adventure.text.Component.text;

public final class Components {
	public static TranslatableComponent translatable(String key, MessageType type, Object... arguments) {
		List<Component> coloredArguments = new ArrayList<>();
		for (Object argument : arguments) {
			if (argument instanceof String stringArgument) {
				coloredArguments.add(text(stringArgument, type.highlightColor));
			} else if (argument instanceof Component componentArgument) {
				coloredArguments.add(componentArgument.colorIfAbsent(type.highlightColor));
			} else {
				coloredArguments.add(text(String.valueOf(argument), type.highlightColor));
			}
		}

		return Component.translatable()
				.key(key)
				.color(type.baseColor)
				.arguments(coloredArguments)
				.build();
	}

	public static Component player(Pointered pointered) {
		Component component = pointered.get(Identity.NAME)
				.<Component>map(Component::text)
				.orElse(Component.translatable("sog.common.component.unknown"));

		return pointered.get(Identity.UUID)
				.map(uuid -> component
						.hoverEvent(HoverEvent.showText(Component.text(uuid.toString())))
						.clickEvent(ClickEvent.copyToClipboard(uuid.toString()))
				)
				.orElse(component);
	}

	public static <P extends Pointered> Component playerCount(Collection<P> players) {
		Component component = Component.text(players.size());
		if (players.isEmpty()) {
			return component;
		}

		return component.hoverEvent(HoverEvent.showText(playerList(players)));
	}

	public static <P extends Pointered> Component playerList(Collection<P> players) {
		Component separator = Component.text(", ");
		Component unknown = Component.translatable("sog.common.component.unknown");

		return players.stream()
				.map(audience -> audience.get(Identity.NAME)
						.<Component>map(Component::text)
						.orElse(unknown))
				.collect(Component.toComponent(separator));
	}
}
