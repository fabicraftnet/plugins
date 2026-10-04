package net.fabicraft.paper.core.command.parser;

import org.bukkit.inventory.MenuType;
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

import java.util.Locale;
import java.util.Map;


public final class MenuTypeParser<C> implements ArgumentParser<C, MenuType>, BlockingSuggestionProvider.Strings<C> {
	private final Map<String, MenuType> MENU_TYPE_MAP;

	public MenuTypeParser(Map<String, MenuType> types) {
		this.MENU_TYPE_MAP = types;
	}

	public static <C> @NonNull ParserDescriptor<C, MenuType> crafters() {
		Map<String, MenuType> types = Map.ofEntries(
				Map.entry("anvil", MenuType.ANVIL),
				Map.entry("blast_furnace", MenuType.BLAST_FURNACE),
				Map.entry("brewing_stand", MenuType.BREWING_STAND),
				Map.entry("cartography_table", MenuType.CARTOGRAPHY_TABLE),
				Map.entry("crafting", MenuType.CRAFTING),
				Map.entry("enchantment", MenuType.ENCHANTMENT),
				Map.entry("furnace", MenuType.FURNACE),
				Map.entry("grindstone", MenuType.GRINDSTONE),
				Map.entry("loom", MenuType.LOOM),
				Map.entry("smithing", MenuType.SMITHING),
				Map.entry("smoker", MenuType.SMOKER),
				Map.entry("stonecutter", MenuType.STONECUTTER)
		);

		return ParserDescriptor.of(new MenuTypeParser<>(types), MenuType.class);
	}

	@Override
	public @NonNull ArgumentParseResult<@NonNull MenuType> parse(@NonNull CommandContext<@NonNull C> context, @NonNull CommandInput input) {
		final String inputString = input.peekString();

		MenuType menuType = MENU_TYPE_MAP.get(inputString.toLowerCase(Locale.ROOT));
		if (menuType == null) {
			throw new MenuTypeParseException(inputString, context);
		}

		input.readString();
		return ArgumentParseResult.success(menuType);
	}

	@Override
	public @NonNull Iterable<@NonNull String> stringSuggestions(@NonNull CommandContext<C> context, @NonNull CommandInput input) {
		return MENU_TYPE_MAP.keySet();
	}

	public static final class MenuTypeParseException extends ParserException {
		private MenuTypeParseException(final @NonNull String input, final @NonNull CommandContext<?> context) {
			super(
					MenuTypeParser.class,
					context,
					Caption.of("fabicraft.paper.core.command.exception.menutype"),
					CaptionVariable.of("input", input)
			);
		}

	}
}
