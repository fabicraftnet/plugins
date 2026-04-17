package net.fabicraft.paper.survival.hook.miniplaceholders;

import io.github.miniplaceholders.api.Expansion;
import net.fabicraft.paper.survival.FabiCraftPaperSurvival;
import net.fabicraft.paper.survival.gathering.GatheringManager;
import net.fabicraft.paper.survival.hook.Hook;
import net.fabicraft.paper.survival.hook.miniplaceholders.gathering.GatheringCollectedPlaceholder;
import net.fabicraft.paper.survival.hook.miniplaceholders.gathering.GatheringDisplayNamePlaceholder;
import net.fabicraft.paper.survival.hook.miniplaceholders.gathering.GatheringGoalPlaceholder;
import net.fabicraft.paper.survival.hook.miniplaceholders.gathering.GatheringIdentifierPlaceholder;
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

		PlayerDataManager playerDataManager = plugin.playerDataManager();
		builder.audiencePlaceholder(Player.class, "character_name", (player, queue, context) -> {
			PlayerData data = playerDataManager.data(player);
			String characterName = data == null || data.characterName() == null ? player.getName() : data.characterName();
			return Tag.preProcessParsed(characterName);
		});

		this.expansion = builder.build();
	}

	@Override
	public void register() {
		this.expansion.register();
	}
}
