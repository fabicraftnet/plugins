package net.fabicraft.paper.survival.player;

import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.entity.Player;

public final class PlayerHeightController {
	public void reset(Player player) {
		AttributeInstance attribute = player.getAttribute(Attribute.SCALE);
		if (attribute == null) {
			throw new IllegalStateException("Player doesn't have SCALE attribute");
		}
		attribute.setBaseValue(attribute.getDefaultValue());
	}

	public void set(Player player, int height) {
		AttributeInstance attribute = player.getAttribute(Attribute.SCALE);
		if (attribute == null) {
			throw new IllegalStateException("Player doesn't have SCALE attribute");
		}
		attribute.setBaseValue((height / 180.0) * attribute.getDefaultValue());
	}
}
