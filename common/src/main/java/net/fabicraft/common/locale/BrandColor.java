package net.fabicraft.common.locale;

import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;

import java.util.Locale;

public enum BrandColor {
	ACCENT(0xa78bfa),
	GRAY(0x94a3b8),

	SUCCESS(0x4ade80),
	WARNING(0xfacc15),
	ERROR(0xef4444);

	public final TextColor textColor;

	BrandColor(int color) {
		this.textColor = TextColor.color(color);
	}

	public TagResolver toTagResolver() {
		String placeholder = toString().toLowerCase(Locale.ROOT);
		return Placeholder.styling(placeholder, textColor);
	}

	public static TagResolver resolver() {
		TagResolver.Builder builder = TagResolver.builder();
		for (BrandColor color : values()) {
			builder.resolver(color.toTagResolver());
		}
		return builder.build();
	}
}
