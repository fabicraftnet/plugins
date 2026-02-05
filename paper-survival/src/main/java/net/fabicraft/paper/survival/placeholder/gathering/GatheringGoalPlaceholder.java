package net.fabicraft.paper.survival.placeholder.gathering;

import net.fabicraft.paper.survival.gathering.Gathering;
import net.fabicraft.paper.survival.gathering.GatheringManager;
import net.kyori.adventure.text.minimessage.tag.Tag;

public final class GatheringGoalPlaceholder extends GatheringPlaceholder {
	public GatheringGoalPlaceholder(GatheringManager manager) {
		super(manager);
	}

	@Override
	protected Tag resolve(Gathering gathering) {
		return Tag.preProcessParsed(String.valueOf(gathering.goal()));
	}
}
