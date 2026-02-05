package net.fabicraft.paper.survival.command.parser;

import net.fabicraft.paper.survival.command.SurvivalCommandContextKeys;
import net.fabicraft.paper.survival.gathering.Gathering;
import net.fabicraft.paper.survival.gathering.GatheringManager;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.incendo.cloud.caption.Caption;
import org.incendo.cloud.caption.CaptionVariable;
import org.incendo.cloud.context.CommandContext;
import org.incendo.cloud.context.CommandInput;
import org.incendo.cloud.exception.parsing.ParserException;
import org.incendo.cloud.parser.ArgumentParseResult;
import org.incendo.cloud.parser.ArgumentParser;
import org.incendo.cloud.parser.ParserDescriptor;
import org.incendo.cloud.suggestion.BlockingSuggestionProvider;

public final class GatheringParser<C> implements ArgumentParser<C, Gathering>, BlockingSuggestionProvider.Strings<C> {
	public static <C> @NonNull ParserDescriptor<C, Gathering> gatheringParser() {
		return ParserDescriptor.of(new GatheringParser<>(), Gathering.class);
	}

	@Override
	public @NonNull ArgumentParseResult<@NonNull Gathering> parse(@NonNull CommandContext<@NonNull C> context, @NonNull CommandInput input) {
		final GatheringManager gatheringManager = context.get(SurvivalCommandContextKeys.GATHERING_MANAGER_KEY);
		final String inputString = input.peekString();
		final Gathering gathering = gatheringManager.gathering(inputString);

		if (gathering == null) {
			throw new GatheringParseException(inputString, context);
		}

		input.readString();
		return ArgumentParseResult.success(gathering);
	}

	@Override
	public @NonNull Iterable<@NonNull String> stringSuggestions(@NonNull CommandContext<C> context, @NonNull CommandInput input) {
		final GatheringManager manager = context.get(SurvivalCommandContextKeys.GATHERING_MANAGER_KEY);
		return manager.gatherings().stream().map(Gathering::identifier).toList();
	}

	public static final class GatheringParseException extends ParserException {
		private GatheringParseException(
				final @NonNull String input,
				final @NonNull CommandContext<?> context
		) {
			super(
					GatheringParser.class,
					context,
					Caption.of("fabicraft.paper.survival.command.exception.gathering"),
					CaptionVariable.of("input", input)
			);
		}

	}
}
