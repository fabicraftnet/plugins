package net.fabicraft.paper.survival.placeholder.gathering;

import net.fabicraft.paper.survival.gathering.Gathering;
import net.fabicraft.paper.survival.gathering.GatheringManager;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.tag.Tag;
import net.kyori.adventure.text.object.ObjectContents;

public final class GatheringItemPlaceholder extends GatheringPlaceholder {
	public GatheringItemPlaceholder(GatheringManager manager) {
		super(manager);
	}

	@Override
	protected Tag resolve(Gathering gathering) {
		return Tag.selfClosingInserting(Component.object(ObjectContents.sprite(Key.key("items"), Key.key("item/" + gathering.material().getKey().value()))));
	}
}
