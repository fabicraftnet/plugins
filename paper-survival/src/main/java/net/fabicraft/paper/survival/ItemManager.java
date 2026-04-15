package net.fabicraft.paper.survival;

import org.bukkit.inventory.ItemFactory;
import org.bukkit.inventory.ItemStack;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class ItemManager {
	private final FabiCraftPaperSurvival plugin;
	private final Path path;
	private List<ItemStack> firstJoinItems = new ArrayList<>();
	private Map<String, ItemStack> customItems = new HashMap<>();

	public ItemManager(FabiCraftPaperSurvival plugin) {
		this.plugin = plugin;
		this.path = plugin.getDataPath().resolve("items.properties");
	}

	public void load() {
		copyCustomItemsToDataPath();
		loadCustomItemsFromDataPath();
		parseFirstJoinItems();
	}

	public ItemStack customItem(String key) {
		return this.customItems.get(key);
	}

	public List<ItemStack> firstJoinItems() {
		return this.firstJoinItems;
	}

	public Set<String> customItemKeys() {
		return this.customItems.keySet();
	}

	private void copyCustomItemsToDataPath() {
		if (Files.exists(this.path)) {
			return;
		}

		try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream("items.properties")) {
			if (inputStream == null) {
				throw new IllegalStateException("items.properties was not found in resources");
			}
			Files.copy(inputStream, this.path);
		} catch (IOException e) {
			this.plugin.getSLF4JLogger().error("Could not save items.properties", e);
		}
	}

	private void loadCustomItemsFromDataPath() {
		Properties props = new Properties();
		try (Reader reader = Files.newBufferedReader(path)) {
			props.load(reader);
		} catch (IOException e) {
			this.plugin.getSLF4JLogger().error("Could not load items.properties", e);
			return;
		}

		Map<String, ItemStack> map = new HashMap<>();
		ItemFactory factory = this.plugin.getServer().getItemFactory();
		for (String key : props.stringPropertyNames()) {
			map.put(key, factory.createItemStack(props.getProperty(key)));
		}

		this.customItems = Collections.unmodifiableMap(map);
		this.plugin.getSLF4JLogger().info("Loaded {} custom items", map.size());
	}

	private void parseFirstJoinItems() {
		Pattern pattern = Pattern.compile(".* (\\d+)$");
		ItemFactory factory = this.plugin.getServer().getItemFactory();
		List<ItemStack> items = new ArrayList<>();
		for (String item : this.plugin.config().firstJoinItems()) {
			Matcher matcher = pattern.matcher(item);
			int quantity = 1;
			if (matcher.matches()) {
				quantity = Integer.parseInt(matcher.group(1));
				item = item.substring(0, item.lastIndexOf(' '));
			}
			ItemStack customItem = this.customItems.get(item);
			if (customItem != null) {
				items.add(customItem.asQuantity(quantity));
			} else {
				items.add(factory.createItemStack(item).asQuantity(quantity));
			}
		}
		this.firstJoinItems = List.copyOf(items);
		this.plugin.getSLF4JLogger().info("Loaded {} first join items", items.size());
	}
}
