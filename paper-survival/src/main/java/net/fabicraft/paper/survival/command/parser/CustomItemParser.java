package net.fabicraft.paper.survival.command.parser;

import net.fabicraft.paper.survival.command.SurvivalCommandContextKeys;
import net.fabicraft.paper.survival.items.CustomItemManager;
import org.bukkit.inventory.ItemStack;
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

public final class CustomItemParser<C> implements ArgumentParser<C, ItemStack>, BlockingSuggestionProvider.Strings<C> {
	public static <C> @NonNull ParserDescriptor<C, ItemStack> customItemParser() {
		return ParserDescriptor.of(new CustomItemParser<>(), ItemStack.class);
	}

	@Override
	public @NonNull ArgumentParseResult<@NonNull ItemStack> parse(@NonNull CommandContext<@NonNull C> context, @NonNull CommandInput input) {
		final CustomItemManager itemManager = context.get(SurvivalCommandContextKeys.CUSTOM_ITEM_MANAGER_KEY);
		final String inputString = input.peekString();
		final ItemStack itemStack = itemManager.itemStack(inputString);

		if (itemStack == null) {
			throw new CustomItemParseException(inputString, context);
		}

		input.readString();
		return ArgumentParseResult.success(itemStack);
	}

	@Override
	public @NonNull Iterable<@NonNull String> stringSuggestions(@NonNull CommandContext<C> context, @NonNull CommandInput input) {
		final CustomItemManager itemManager = context.get(SurvivalCommandContextKeys.CUSTOM_ITEM_MANAGER_KEY);
		return itemManager.itemKeys();
	}

	public static final class CustomItemParseException extends ParserException {
		private CustomItemParseException(
				final @NonNull String input,
				final @NonNull CommandContext<?> context
		) {
			super(
					CustomItemParser.class,
					context,
					Caption.of("fabicraft.paper.survival.command.exception.custom-item"),
					CaptionVariable.of("input", input)
			);
		}

	}
}
