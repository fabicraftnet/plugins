package net.fabicraft.common.command;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.incendo.cloud.caption.Caption;
import org.incendo.cloud.caption.CaptionProvider;
import org.jetbrains.annotations.NotNull;

public final class MinecraftCaptionProvider<C> implements CaptionProvider<C> {
	@Override
	public @NotNull String provide(@NonNull Caption caption, @NotNull C recipient) {
		return "fabicraft.common.command." + caption.key();
	}
}
