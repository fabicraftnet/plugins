package net.fabicraft.paper.survival.placeholder;

import io.github.miniplaceholders.api.Expansion;
import net.fabicraft.paper.survival.FabiCraftPaperSurvival;
import net.fabicraft.paper.survival.gathering.Gathering;
import net.kyori.adventure.text.minimessage.tag.Tag;

public final class MiniPlaceholders {
	private final Expansion expansion;

	public MiniPlaceholders(FabiCraftPaperSurvival plugin) {
		Expansion.Builder builder = Expansion.builder("fabicraft_paper_survival");
		builder.globalPlaceholder("gathering_amount", ((queue, context) -> {
			if (!queue.hasNext()) {
				return Tag.preProcessParsed("You need to provide a name");
			}
			String name = queue.pop().value();
			Gathering gathering = plugin.gatheringManager().gathering(name);
			if (gathering == null) {
				return Tag.preProcessParsed("Unknown gathering");
			}

			return Tag.preProcessParsed(String.valueOf(gathering.gatheredAmount()));
		}));
		builder.globalPlaceholder("gathering_goal", ((queue, context) -> {
			if (!queue.hasNext()) {
				return Tag.preProcessParsed("You need to provide a name");
			}
			String name = queue.pop().value();
			Gathering gathering = plugin.gatheringManager().gathering(name);
			if (gathering == null) {
				return Tag.preProcessParsed("Unknown gathering");
			}

			return Tag.preProcessParsed(String.valueOf(gathering.gatheredGoal()));
		}));
		this.expansion = builder.build();
	}

	public void register() {
		this.expansion.register();
	}
}
