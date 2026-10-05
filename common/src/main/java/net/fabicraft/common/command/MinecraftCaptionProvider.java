package net.fabicraft.common.command;

import org.incendo.cloud.caption.Caption;
import org.incendo.cloud.caption.CaptionProvider;
import org.jspecify.annotations.NonNull;

public final class MinecraftCaptionProvider<C> implements CaptionProvider<C> {
	@Override
	public @NonNull String provide(@NonNull Caption caption, @NonNull C recipient) {
		return caption.key();
	}
}
