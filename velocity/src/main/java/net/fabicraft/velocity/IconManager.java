package net.fabicraft.velocity;

import com.velocitypowered.api.util.Favicon;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public final class IconManager {
	private final FabiCraftVelocity plugin;
	private final Path iconDirectoryPath;
	private final Favicon defaultIcon;
	private final Random random = new Random();
	private List<Favicon> icons;

	public IconManager(FabiCraftVelocity plugin) {
		this.plugin = plugin;
		this.iconDirectoryPath = plugin.dataDirectory().resolve("icons");
		this.defaultIcon = defaultIcon();
	}

	public void load() {
		if (!Files.exists(this.iconDirectoryPath)) {
			try {
				Files.createDirectories(this.iconDirectoryPath);
			} catch (IOException e) {
				this.plugin.logger().error("Couldn't create icons directory", e);
				return;
			}
		}

		List<Favicon> icons = new ArrayList<>();
		try (DirectoryStream<Path> stream = Files.newDirectoryStream(this.iconDirectoryPath, "*.png")) {
			for (Path path : stream) {
				icons.add(Favicon.create(path));
			}
		} catch (IOException e) {
			this.plugin.logger().error("Couldn't load icons", e);
		}

		if (icons.isEmpty()) {
			this.icons = List.of(this.defaultIcon);
		} else {
			this.icons = List.copyOf(icons);
		}
	}

	public Favicon icon() {
		return this.icons.get(random.nextInt(this.icons.size()));
	}

	private Favicon defaultIcon() {
		try (InputStream stream = this.getClass().getClassLoader().getResourceAsStream("server-icon.png")) {
			if (stream == null) {
				throw new IllegalStateException("Couldn't find server-icon.png");
			}
			BufferedImage image = ImageIO.read(stream);
			if (image == null) {
				throw new IOException("Unable to read the image.");
			}
			return Favicon.create(image);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
}
