package net.fabicraft.common.command;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.ComponentLike;
import net.kyori.adventure.text.TranslatableComponent;
import net.kyori.adventure.text.minimessage.translation.Argument;
import org.incendo.cloud.caption.Caption;
import org.incendo.cloud.caption.CaptionFormatter;
import org.incendo.cloud.caption.CaptionVariable;
import org.jspecify.annotations.NonNull;

import java.util.List;

public final class MinecraftCaptionFormatter<C> implements CaptionFormatter<C, TranslatableComponent> {
	@Override
	public @NonNull TranslatableComponent formatCaption(@NonNull Caption key, @NonNull C recipient, @NonNull String caption, @NonNull List<@NonNull CaptionVariable> variables) {
		List<ComponentLike> arguments = variables.stream().map(variable -> Argument.string(variable.key(), variable.value())).toList();
		return Component.translatable(key.key()).arguments(arguments);
	}
}
