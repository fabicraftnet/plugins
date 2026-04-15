package net.fabicraft.paper.survival.player;

import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.entity.Player;

import java.util.Objects;

public final class PlayerHeightController {
	public static final int DEFAULT_HEIGHT = 180;

	public void reset(Player player) {
		AttributeInstance attribute = Objects.requireNonNull(
				player.getAttribute(Attribute.SCALE),
				"player is missing the scale attribute"
		);
		attribute.setBaseValue(attribute.getDefaultValue());
	}

	public void set(Player player, int height) {
		AttributeInstance attribute = Objects.requireNonNull(
				player.getAttribute(Attribute.SCALE),
				"player is missing the scale attribute"
		);
		attribute.setBaseValue(((double) height / DEFAULT_HEIGHT) * attribute.getDefaultValue());
	}
}
