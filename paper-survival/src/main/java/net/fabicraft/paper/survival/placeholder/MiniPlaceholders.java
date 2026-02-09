package net.fabicraft.paper.survival.placeholder;

import io.github.miniplaceholders.api.Expansion;
import net.fabicraft.paper.survival.FabiCraftPaperSurvival;
import net.fabicraft.paper.survival.gathering.GatheringManager;
import net.fabicraft.paper.survival.placeholder.gathering.*;
import org.bukkit.entity.Player;

public final class MiniPlaceholders {
	private final Expansion expansion;

	public MiniPlaceholders(FabiCraftPaperSurvival plugin) {
		Expansion.Builder builder = Expansion.builder("fabicraftpapersurvival");

		GatheringManager gatheringManager = plugin.gatheringManager();
		builder.globalPlaceholder("gathering_collected", new GatheringCollectedPlaceholder(gatheringManager));
		builder.globalPlaceholder("gathering_displayname", new GatheringDisplayNamePlaceholder(gatheringManager));
		builder.globalPlaceholder("gathering_goal", new GatheringGoalPlaceholder(gatheringManager));
		builder.globalPlaceholder("gathering_identifier", new GatheringIdentifierPlaceholder(gatheringManager));
		builder.globalPlaceholder("gathering_item", new GatheringItemPlaceholder(gatheringManager));
		builder.audiencePlaceholder(Player.class, "afk", new AfkPlaceholder(plugin.afkManager()));

		this.expansion = builder.build();
	}

	public void register() {
		this.expansion.register();
	}
}
