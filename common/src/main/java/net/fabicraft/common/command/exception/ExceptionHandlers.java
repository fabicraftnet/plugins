package net.fabicraft.common.command.exception;

import net.fabicraft.common.locale.BrandColor;
import net.fabicraft.common.locale.Components;
import net.fabicraft.common.locale.MessageType;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import org.incendo.cloud.CommandManager;
import org.incendo.cloud.exception.*;
import org.incendo.cloud.minecraft.extras.AudienceProvider;
import org.incendo.cloud.util.TypeUtils;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static net.kyori.adventure.text.Component.newline;
import static net.kyori.adventure.text.Component.text;

public final class ExceptionHandlers<C> {
	private final List<ExceptionHandler<C, ?>> entries = new ArrayList<>();

	public ExceptionHandlers() {
		this.entries.add(new ExceptionHandler<>(InvalidSyntaxException.class, ctx -> Components.translatable(
				"fabicraft.command.exception.invalid-syntax",
				MessageType.ERROR,
				"/" + ctx.exception().correctSyntax())
		));

		this.entries.add(new ExceptionHandler<>(InvalidCommandSenderException.class, ctx -> {
			final boolean multiple = ctx.exception().requiredSenderTypes().size() > 1;

			String expected = ctx.exception().requiredSenderTypes().stream()
					.map(TypeUtils::simpleName)
					.collect(Collectors.joining(", "));

			String key = multiple ? "fabicraft.command.exception.invalid-sender-list" : "fabicraft.command.exception.invalid-sender";
			return Components.translatable(
					key,
					MessageType.ERROR,
					TypeUtils.simpleName(ctx.context().sender().getClass()),
					Component.text(expected, BrandColor.GRAY.textColor)
			);
		}));

		this.entries.add(new ExceptionHandler<>(NoPermissionException.class, ctx -> Components.translatable(
				"fabicraft.command.exception.no-permission",
				MessageType.ERROR,
				ctx.exception().permissionResult().permission().permissionString()
		)));

		this.entries.add(new ExceptionHandler<>(ArgumentParseException.class, ctx -> Components.translatable(
				"fabicraft.command.exception.invalid-argument",
				MessageType.ERROR,
				ctx.exception().getCause().getMessage()
		)));

		this.entries.add(new ExceptionHandler<>(CommandExecutionException.class, ctx -> {
			final Throwable cause = ctx.exception().getCause();

			final StringWriter writer = new StringWriter();
			cause.printStackTrace(new PrintWriter(writer));
			final String stackTrace = writer.toString().replaceAll("\t", "    ");

			final HoverEvent<Component> hover = HoverEvent.showText(text()
					.append(Component.text(cause.getMessage()))
					.append(newline())
					.append(text(stackTrace, BrandColor.GRAY.textColor))
			);
			final ClickEvent click = ClickEvent.copyToClipboard(stackTrace);

			return Components.translatable(
					"fabicraft.command.exception.unexpected",
					MessageType.ERROR
			).hoverEvent(hover).clickEvent(click);
		}));
	}

	public void register(CommandManager<C> manager, AudienceProvider<C> audienceProvider) {
		entries.forEach(entry -> entry.register(manager, audienceProvider));
	}
}
