package net.fabicraft.common.command.exception;

import net.kyori.adventure.text.TranslatableComponent;
import org.incendo.cloud.CommandManager;
import org.incendo.cloud.exception.handling.ExceptionContext;
import org.incendo.cloud.minecraft.extras.AudienceProvider;

import java.util.function.Function;

public final class ExceptionHandler<C, T extends Throwable> {

	private final Class<T> type;
	private final Function<ExceptionContext<C, T>, TranslatableComponent> factory;

	public ExceptionHandler(Class<T> type, Function<ExceptionContext<C, T>, TranslatableComponent> factory) {
		this.type = type;
		this.factory = factory;
	}

	void register(CommandManager<C> manager, AudienceProvider<C> audienceProvider) {
		manager.exceptionController().registerHandler(type, ctx -> {
			TranslatableComponent message = factory.apply(ctx);
			audienceProvider.apply(ctx.context().sender()).sendMessage(message);
		});
	}
}
