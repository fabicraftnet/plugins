package net.fabicraft.paper.survival.hook.miniplaceholders;

import io.github.miniplaceholders.api.Expansion;
import net.fabicraft.paper.survival.FabiCraftPaperSurvival;
import net.fabicraft.paper.survival.gathering.GatheringManager;
import net.fabicraft.paper.survival.hook.Hook;
import net.fabicraft.paper.survival.hook.miniplaceholders.gathering.*;
import net.fabicraft.paper.survival.player.PlayerData;
import net.fabicraft.paper.survival.player.PlayerDataManager;
import net.kyori.adventure.text.minimessage.tag.Tag;
import org.bukkit.entity.Player;

public final class MiniPlaceholdersHook implements Hook {
	private final Expansion expansion;

	public MiniPlaceholdersHook(FabiCraftPaperSurvival plugin) {
		Expansion.Builder builder = Expansion.builder("fabicraftpapersurvival");

		GatheringManager gatheringManager = plugin.gatheringManager();
		builder.globalPlaceholder("gathering_collected", new GatheringCollectedPlaceholder(gatheringManager));
		builder.globalPlaceholder("gathering_displayname", new GatheringDisplayNamePlaceholder(gatheringManager));
		builder.globalPlaceholder("gathering_goal", new GatheringGoalPlaceholder(gatheringManager));
		builder.globalPlaceholder("gathering_identifier", new GatheringIdentifierPlaceholder(gatheringManager));
		builder.globalPlaceholder("gathering_item", new GatheringItemPlaceholder(gatheringManager));

		PlayerDataManager playerDataManager = plugin.playerDataManager();
		builder.audiencePlaceholder(Player.class, "nickname", (player, queue, context) -> {
			PlayerData data = playerDataManager.data(player.getUniqueId());
			String nickname = data == null || data.rolePlayName() == null ? player.getName() : data.rolePlayName();
			return Tag.preProcessParsed(nickname);
		});

		this.expansion = builder.build();
	}

	@Override
	public void register() {
		this.expansion.register();
	}
}
