package net.fabicraft.paper.survival.gathering;

import io.papermc.paper.math.BlockPosition;
import io.papermc.paper.math.Position;
import net.fabicraft.paper.survival.FabiCraftPaperSurvival;
import net.fabicraft.paper.survival.StorageManager;
import org.bukkit.Location;
import org.bukkit.Material;
import org.intellij.lang.annotations.Language;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;

//TODO Figure out why null storageManager wasn't throwing an NPE (or it was swallowed)
public final class GatheringManager {
	@Language("SQLite")
	private static final String STATEMENT_SELECT = "SELECT * FROM fcps_gatherings";
	@Language("SQLite")
	private static final String STATEMENT_UPSERT = """
			INSERT INTO fcps_gatherings (
				identifier,
				position_x,
				position_y,
				position_z,
				material,
				goal,
				collected,
				display_name
			) VALUES (?, ?, ?, ?, ?, ?, ?, ?)
			ON CONFLICT(identifier) DO UPDATE SET
				position_x = excluded.position_x,
				position_y = excluded.position_y,
				position_z = excluded.position_z,
				material = excluded.material,
				goal = excluded.goal,
				collected = excluded.collected,
				display_name = excluded.display_name
			""";
	@Language("SQLite")
	private static final String STATEMENT_DELETE = """
			DELETE FROM fcps_gatherings
			WHERE identifier = ?
			""";
	private final StorageManager storageManager;
	private final FabiCraftPaperSurvival plugin;
	private final Executor executor;
	private final List<Gathering> gatherings = new ArrayList<>();

	public GatheringManager(FabiCraftPaperSurvival plugin) {
		this.plugin = plugin;
		this.executor = plugin.executor();
		this.storageManager = plugin.storageManager();
	}

	public void load() {
		this.gatherings.clear();
		this.executor.execute(() -> {
			try (
					Connection connection = this.storageManager.connection();
					PreparedStatement statement = connection.prepareStatement(STATEMENT_SELECT);
					ResultSet set = statement.executeQuery()
			) {
				while (set.next()) {
					String identifier = set.getString("identifier");

					BlockPosition position = Position.block(
							set.getInt("position_x"),
							set.getInt("position_y"),
							set.getInt("position_z")
					);

					Material material = Material.valueOf(set.getString("material"));
					int collected = set.getInt("collected");
					int goal = set.getInt("goal");

					Gathering gathering = new Gathering(identifier, position, material, collected, goal);

					String displayName = set.getString("display_name");
					if (displayName != null) {
						gathering.displayName(this.plugin.miniMessage().deserialize(displayName));
					}
					this.gatherings.add(gathering);
				}
				this.plugin.getSLF4JLogger().info("Loaded {} gatherings", this.gatherings.size());
			} catch (SQLException exception) {
				this.plugin.getSLF4JLogger().error("Could not load gatherings", exception);
			}
		});
	}

	public void add(Gathering gathering) {
		this.gatherings.add(gathering);
		save(gathering);
	}

	public void remove(Gathering gathering) {
		this.gatherings.remove(gathering);
		this.executor.execute(() -> {
			try (Connection connection = this.storageManager.connection();
				PreparedStatement statement = connection.prepareStatement(STATEMENT_DELETE)
			) {
				statement.setString(1, gathering.identifier());
				statement.executeUpdate();
			} catch (SQLException exception) {
				this.plugin.getSLF4JLogger().error("Could not delete gathering from the database", exception);
			}
		});
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

	public void save(Gathering gathering) {
		this.executor.execute(() -> {
			try (
					Connection connection = this.storageManager.connection();
					PreparedStatement statement = connection.prepareStatement(STATEMENT_UPSERT)
			) {
				statement.setString(1, gathering.identifier());
				statement.setInt(2, gathering.containerPosition().blockX());
				statement.setInt(3, gathering.containerPosition().blockY());
				statement.setInt(4, gathering.containerPosition().blockZ());
				statement.setString(5, gathering.material().toString());
				statement.setInt(6, gathering.goal());
				statement.setInt(7, gathering.collected());
				statement.setString(8, this.plugin.miniMessage().serialize(gathering.displayName()));
				statement.executeUpdate();
			} catch (SQLException exception) {
				this.plugin.getSLF4JLogger().error("Could not save gathering to the database {}", gathering.identifier(), exception);
			}
		});
	}

	public Gathering nether() {
		return gathering("nether");
	}

	public Gathering end() {
		return gathering("end");
	}
}
