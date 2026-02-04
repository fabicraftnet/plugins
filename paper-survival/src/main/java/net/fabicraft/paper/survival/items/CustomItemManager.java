package net.fabicraft.paper.survival.items;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import net.fabicraft.paper.survival.FabiCraftPaperSurvival;
import org.bukkit.inventory.ItemStack;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public final class CustomItemManager {
	private final FabiCraftPaperSurvival plugin;
	private final Path path;
	private final Gson gson = new Gson();
	private Map<String, String> items = new HashMap<>();

	public CustomItemManager(FabiCraftPaperSurvival plugin) {
		this.plugin = plugin;
		this.path = plugin.getDataPath().resolve("items.json");
	}

	public void load() throws IOException {
		copyToDataPath();
		loadFromDataPath();
	}

	public ItemStack itemStack(String key) {
		String itemInput = this.items.get(key);
		if (itemInput == null) {
			return null;
		}
		return this.plugin.getServer().getItemFactory().createItemStack(itemInput);
	}

	public Set<String> itemKeys() {
		return Set.copyOf(this.items.keySet());
	}

	private void copyToDataPath() throws IOException {
		if (Files.exists(this.path)) {
			return;
		}

		ClassLoader loader = getClass().getClassLoader();
		try (InputStream inputStream = loader.getResourceAsStream("items.json")) {
			if (inputStream == null) {
				throw new IllegalStateException("items.json was not found in resources");
			}
			Files.copy(inputStream, this.path);
		} catch (IOException e) {
			this.plugin.getSLF4JLogger().error("Could not save items.json", e);
			throw e;
		}
	}

	private void loadFromDataPath() throws IOException {
		try {
			this.items = Map.copyOf(this.gson.fromJson(Files.newBufferedReader(this.path), new TypeToken<Map<String, String>>() {
			}.getType()));
		} catch (IOException e) {
			this.plugin.getSLF4JLogger().error("Could not read items.json", e);
			throw e;
		}
	}
}
