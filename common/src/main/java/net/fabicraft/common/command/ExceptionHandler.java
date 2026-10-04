package net.fabicraft.common.command;

import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.TranslatableComponent;
import org.incendo.cloud.CommandManager;
import org.incendo.cloud.caption.Caption;
import org.incendo.cloud.caption.CaptionVariable;
import org.incendo.cloud.exception.*;
import org.incendo.cloud.exception.handling.ExceptionContext;
import org.incendo.cloud.exception.handling.ExceptionController;
import org.incendo.cloud.exception.parsing.ParserException;
import org.incendo.cloud.util.TypeUtils;
import org.slf4j.Logger;

import java.util.function.Function;
import java.util.stream.Collectors;

public class ExceptionHandler<C> {
	private final MinecraftCaptionFormatter<C> formatter = new MinecraftCaptionFormatter<>();
	private final Logger logger;
	private final Function<C, Audience> audienceMapper;

	public ExceptionHandler(Logger logger, Function<C, Audience> audienceMapper) {
		this.logger = logger;
		this.audienceMapper = audienceMapper;
	}

	public void register(CommandManager<C> manager) {
		ExceptionController<C> controller = manager.exceptionController();
		controller.clearHandlers();
		registerDefaultHandlers(controller);
	}

	private void registerDefaultHandlers(ExceptionController<C> controller) {
		controller.registerHandler(Throwable.class, context -> {
			send(context, "fabicraft.common.command.exception.unexpected");
			this.logger.error("An unhandled exception was thrown during command execution", context.exception());
		});
		controller.registerHandler(CommandExecutionException.class, context -> {
			send(context, "fabicraft.common.command.exception.unexpected");
			this.logger.error("Exception executing command handler", context.exception().getCause());
		});
		controller.registerHandler(ArgumentParseException.class, context -> {
			String message = context.exception().getCause().getMessage();
			send(context, "fabicraft.common.command.exception.invalid-argument", CaptionVariable.of("message", message));
		});
		controller.registerHandler(NoSuchCommandException.class, context ->
				send(context, "fabicraft.common.command.exception.no-such-command", CaptionVariable.of("command", context.exception().suppliedCommand()))
		);
		controller.registerHandler(NoPermissionException.class, context -> {
			String permission = context.exception().permissionResult().permission().permissionString();
			send(context, "fabicraft.common.command.exception.no-permission", CaptionVariable.of("permission", permission));
		});
		controller.registerHandler(InvalidCommandSenderException.class, context -> {
			final boolean multiple = context.exception().requiredSenderTypes().size() != 1;
			final String expected = multiple
					? context.exception().requiredSenderTypes().stream().<String>map(TypeUtils::simpleName)
					.collect(Collectors.joining(", "))
					: TypeUtils.simpleName(context.exception().requiredSenderTypes().iterator().next());
			send(context, multiple ? "fabicraft.common.command.exception.invalid-sender-list" : "fabicraft.common.command.exception.invalid-sender", CaptionVariable.of("expected", expected));
		});
		controller.registerHandler(InvalidSyntaxException.class, context ->
				send(context, "fabicraft.common.command.exception.invalid-syntax", CaptionVariable.of("syntax", context.exception().correctSyntax()))
		);
		controller.registerHandler(ParserException.class, context ->
				send(context, context.exception().errorCaption(), context.exception().captionVariables())
		);
	}

	protected void send(ExceptionContext<C, ?> context, Caption caption, CaptionVariable... variables) {
		TranslatableComponent component = context.context().formatCaption(this.formatter, caption, variables);
		this.audienceMapper.apply(context.context().sender()).sendMessage(component);
	}

	protected void send(ExceptionContext<C, ?> context, String key, CaptionVariable... variables) {
		send(context, Caption.of(key), variables);
	}
}
