package net.fabicraft.paper.survival.placeholder.gathering;

import net.fabicraft.paper.survival.gathering.Gathering;
import net.fabicraft.paper.survival.gathering.GatheringManager;
import net.kyori.adventure.text.minimessage.tag.Tag;

public final class GatheringDisplayNamePlaceholder extends GatheringPlaceholder {
	public GatheringDisplayNamePlaceholder(GatheringManager manager) {
		super(manager);
	}

	@Override
	protected Tag resolve(Gathering gathering) {
		return Tag.selfClosingInserting(gathering.displayName());
	}
}
