package net.fabicraft.paper.survival.placeholder.gathering;

import net.fabicraft.paper.survival.gathering.Gathering;
import net.fabicraft.paper.survival.gathering.GatheringManager;
import net.kyori.adventure.text.minimessage.tag.Tag;

public final class GatheringIdentifierPlaceholder extends GatheringPlaceholder {
	public GatheringIdentifierPlaceholder(GatheringManager manager) {
		super(manager);
	}

	@Override
	protected Tag resolve(Gathering gathering) {
		return Tag.preProcessParsed(gathering.identifier());
	}
}
