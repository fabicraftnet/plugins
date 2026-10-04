package net.fabicraft.common.command;

import net.fabicraft.common.locale.Components;
import net.fabicraft.common.locale.MessageType;
import net.kyori.adventure.text.TranslatableComponent;
import org.incendo.cloud.caption.Caption;
import org.incendo.cloud.caption.CaptionFormatter;
import org.incendo.cloud.caption.CaptionVariable;
import org.jspecify.annotations.NonNull;

import java.util.List;

public final class MinecraftCaptionFormatter<C> implements CaptionFormatter<C, TranslatableComponent> {
	@Override
	public @NonNull TranslatableComponent formatCaption(@NonNull Caption key, @NonNull C recipient, @NonNull String caption, @NonNull List<@NonNull CaptionVariable> variables) {
		return Components.translatable(key.key(), MessageType.ERROR, variables.stream().map(CaptionVariable::value).toArray());
	}
}
