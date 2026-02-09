package net.fabicraft.paper.survival.placeholder;

import io.github.miniplaceholders.api.resolver.AudienceTagResolver;
import io.github.miniplaceholders.api.utils.Tags;
import net.fabicraft.common.locale.BrandColor;
import net.fabicraft.paper.survival.afk.AfkManager;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.Context;
import net.kyori.adventure.text.minimessage.tag.Tag;
import net.kyori.adventure.text.minimessage.tag.resolver.ArgumentQueue;
import org.bukkit.entity.Player;
import org.jspecify.annotations.NonNull;

public final class AfkPlaceholder implements AudienceTagResolver<Player> {
	private static final Tag TAG_AFK = Tag.selfClosingInserting(Component.text(" AFK", BrandColor.GRAY.textColor));
	private final AfkManager afkManager;

	public AfkPlaceholder(AfkManager afkManager) {
		this.afkManager = afkManager;
	}

	@Override
	public @NonNull Tag tag(Player player, ArgumentQueue queue, Context ctx) {
		return this.afkManager.afk(player.getUniqueId()) ? TAG_AFK : Tags.EMPTY_TAG;
	}
}
