package net.fabicraft.paper.survival.placeholder.gathering;

import io.github.miniplaceholders.api.resolver.GlobalTagResolver;
import net.fabicraft.paper.survival.gathering.Gathering;
import net.fabicraft.paper.survival.gathering.GatheringManager;
import net.kyori.adventure.text.minimessage.Context;
import net.kyori.adventure.text.minimessage.tag.Tag;
import net.kyori.adventure.text.minimessage.tag.resolver.ArgumentQueue;
import org.jspecify.annotations.Nullable;

public abstract class GatheringPlaceholder implements GlobalTagResolver {
	protected final GatheringManager manager;

	public GatheringPlaceholder(GatheringManager manager) {
		this.manager = manager;
	}

	@Override
	public @Nullable Tag tag(ArgumentQueue queue, Context context) {
		if (!queue.hasNext()) {
			return Tag.preProcessParsed("You need to provide a name");
		}

		Gathering gathering = this.manager.gathering(queue.pop().value());
		if (gathering == null) {
			return Tag.preProcessParsed("Unknown gathering");
		}

		return resolve(gathering);
	}

	protected abstract Tag resolve(Gathering gathering);
}
