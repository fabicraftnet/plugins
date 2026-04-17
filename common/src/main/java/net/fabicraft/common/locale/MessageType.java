package net.fabicraft.common.locale;

import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;

public enum MessageType {
	INFO(NamedTextColor.WHITE, BrandColor.ACCENT.textColor),
	SUCCESS(BrandColor.GREEN.textColor, NamedTextColor.WHITE),
	WARNING(BrandColor.YELLOW.textColor, BrandColor.RED.textColor),
	ERROR(BrandColor.RED.textColor, BrandColor.YELLOW.textColor);

	public final TextColor baseColor;
	public final TextColor highlightColor;

	MessageType(TextColor baseColor, TextColor highlightColor) {
		this.baseColor = baseColor;
		this.highlightColor = highlightColor;
	}
}
