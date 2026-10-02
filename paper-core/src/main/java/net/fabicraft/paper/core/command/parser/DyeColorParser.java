package net.fabicraft.paper.core.command.parser;

import org.bukkit.DyeColor;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.incendo.cloud.caption.Caption;
import org.incendo.cloud.caption.CaptionVariable;
import org.incendo.cloud.component.CommandComponent;
import org.incendo.cloud.context.CommandContext;
import org.incendo.cloud.context.CommandInput;
import org.incendo.cloud.exception.parsing.ParserException;
import org.incendo.cloud.parser.ArgumentParseResult;
import org.incendo.cloud.parser.ArgumentParser;
import org.incendo.cloud.parser.ParserDescriptor;
import org.incendo.cloud.suggestion.BlockingSuggestionProvider;

import java.util.Arrays;

public final class DyeColorParser<C> implements ArgumentParser<C, DyeColor>, BlockingSuggestionProvider.Strings<C> {
	public static <C> @NonNull ParserDescriptor<C, DyeColor> dyeColorParser() {
		return ParserDescriptor.of(new DyeColorParser<>(), DyeColor.class);
	}

	public static <C> CommandComponent.@NonNull Builder<C, DyeColor> dyeColorComponent() {
		return CommandComponent.<C, DyeColor>builder().parser(dyeColorParser());
	}

	@Override
	public @NonNull ArgumentParseResult<@NonNull DyeColor> parse(@NonNull CommandContext<@NonNull C> context, @NonNull CommandInput input) {
		final String inputString = input.peekString();
		DyeColor color;
		try {
			color = DyeColor.valueOf(inputString.toUpperCase());
		} catch (IllegalArgumentException e) {
			throw new DyeColorParseException(inputString, context);
		}

		input.readString();
		return ArgumentParseResult.success(color);
	}

	@Override
	public @NonNull Iterable<@NonNull String> stringSuggestions(@NonNull CommandContext<C> context, @NonNull CommandInput input) {
		return Arrays.stream(DyeColor.values()).map(Enum::toString).toList();
	}

	public static final class DyeColorParseException extends ParserException {
		private DyeColorParseException(final @NonNull String input, final @NonNull CommandContext<?> context) {
			super(
					DyeColorParser.class,
					context,
					Caption.of("fabicraft.paper.common.command.exception.dyecolor"),
					CaptionVariable.of("input", input)
			);
		}
	}
}
