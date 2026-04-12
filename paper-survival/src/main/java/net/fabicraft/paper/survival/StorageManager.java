package net.fabicraft.paper.survival;

import org.flywaydb.core.Flyway;
import org.flywaydb.core.api.FlywayException;
import org.sqlite.SQLiteDataSource;

import java.nio.file.Path;
import java.sql.Connection;
import java.sql.SQLException;

public final class StorageManager {
	private final SQLiteDataSource source;
	private final Flyway flyway;

	public StorageManager(Path dataDirectory) {
		SQLiteDataSource source = new SQLiteDataSource();
		source.setEnforceForeignKeys(true);
		source.setUrl("jdbc:sqlite:" + dataDirectory.resolve("database.db").toAbsolutePath());
		this.source = source;

		this.flyway = Flyway.configure(getClass().getClassLoader())
				.dataSource(this.source)
				.locations("classpath:db/migration")
				.communityDBSupportEnabled(true)
				.load();
	}

	public void migrate() throws FlywayException {
		this.flyway.migrate();
	}

	public Connection connection() throws SQLException {
		return this.source.getConnection();
	}
}
