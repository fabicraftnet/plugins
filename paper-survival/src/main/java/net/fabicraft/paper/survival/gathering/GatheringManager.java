package net.fabicraft.paper.survival.gathering;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import net.fabicraft.paper.survival.FabiCraftPaperSurvival;
import org.bukkit.Location;

import java.io.BufferedReader;
import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public final class GatheringManager {
	private final Gson gson = new Gson();
	private final Path path;
	private final FabiCraftPaperSurvival plugin;
	private List<Gathering> gatherings = new ArrayList<>();

	public GatheringManager(FabiCraftPaperSurvival plugin) {
		this.plugin = plugin;
		this.path = plugin.getDataPath().resolve("gatherings.json");
	}

	public void load() throws IOException {
		if (!Files.exists(this.path)) {
			return;
		}
		try (BufferedReader reader = Files.newBufferedReader(this.path)) {
			Type type = new TypeToken<List<Gathering>>() {
			}.getType();
			this.gatherings = this.gson.fromJson(reader, type);
		} catch (IOException e) {
			this.plugin.getSLF4JLogger().error("Failed to read firstspawn.json", e);
			throw e;
		}
	}

	public void add(Gathering gathering) {
		this.gatherings.add(gathering);
	}

	public void remove(Gathering gathering) {
		this.gatherings.remove(gathering);
	}

	public List<Gathering> gatherings() {
		return List.copyOf(this.gatherings);
	}

	public Gathering gathering(String name) {
		for (Gathering gathering : this.gatherings) {
			if (gathering.identifier().equalsIgnoreCase(name)) {
				return gathering;
			}
		}
		return null;
	}

	public Gathering gathering(Location chestLocation) {
		for (Gathering gathering : this.gatherings) {
			if (gathering.containerPosition().equals(chestLocation.toBlock())) {
				return gathering;
			}
		}
		return null;
	}
}
