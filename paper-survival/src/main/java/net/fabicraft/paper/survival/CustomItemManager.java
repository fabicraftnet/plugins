package net.fabicraft.paper.survival;

import org.bukkit.inventory.ItemFactory;
import org.bukkit.inventory.ItemStack;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public final class CustomItemManager {
	private final FabiCraftPaperSurvival plugin;
	private final Path path;
	private Map<String, ItemStack> items = new HashMap<>();

	public CustomItemManager(FabiCraftPaperSurvival plugin) {
		this.plugin = plugin;
		this.path = plugin.getDataPath().resolve("items.properties");
	}

	public void load() {
		copyToDataPath();
		loadFromDataPath();
	}

	public ItemStack itemStack(String key) {
		return this.items.get(key);
	}

	public Set<String> itemKeys() {
		return this.items.keySet();
	}

	private void copyToDataPath() {
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

	private void loadFromDataPath() {
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

		this.items = Collections.unmodifiableMap(map);
	}
}
