package net.fabicraft.paper.core.command.parser;

import io.leangen.geantyref.TypeToken;
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
import java.util.Optional;
import java.util.stream.Stream;

public final class DyeColorParser<C> implements ArgumentParser<C, Optional<DyeColor>>, BlockingSuggestionProvider.Strings<C> {
	private static final String NONE = "NONE";

	public static <C> @NonNull ParserDescriptor<C, Optional<DyeColor>> dyeColorParser() {
		return ParserDescriptor.of(new DyeColorParser<>(), new TypeToken<>() {
		});
	}

	public static <C> CommandComponent.@NonNull Builder<C, Optional<DyeColor>> dyeColorComponent() {
		return CommandComponent.<C, Optional<DyeColor>>builder().parser(dyeColorParser());
	}

	@Override
	public @NonNull ArgumentParseResult<@NonNull Optional<DyeColor>> parse(@NonNull CommandContext<@NonNull C> context, @NonNull CommandInput input) {
		final String inputString = input.peekString();

		if (NONE.equalsIgnoreCase(inputString)) {
			input.readString();
			return ArgumentParseResult.success(Optional.empty());
		}

		DyeColor color;
		try {
			color = DyeColor.valueOf(inputString.toUpperCase());
		} catch (IllegalArgumentException e) {
			throw new DyeColorParseException(inputString, context);
		}

		input.readString();
		return ArgumentParseResult.success(Optional.of(color));
	}

	@Override
	public @NonNull Iterable<@NonNull String> stringSuggestions(@NonNull CommandContext<C> context, @NonNull CommandInput input) {
		return Stream.concat(
				Arrays.stream(DyeColor.values()).map(Enum::toString),
				Stream.of(NONE)
		).toList();
	}

	public static final class DyeColorParseException extends ParserException {
		private DyeColorParseException(final @NonNull String input, final @NonNull CommandContext<?> context) {
			super(
					DyeColorParser.class,
					context,
					Caption.of("fabicraft.paper.core.command.exception.dyecolor"),
					CaptionVariable.of("input", input)
			);
		}
	}
}
