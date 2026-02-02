package net.fabicraft.paper.survival.gathering;

import org.bukkit.Location;

import java.util.ArrayList;
import java.util.List;

public final class GatheringManager {
	private final List<Gathering> gatherings = new ArrayList<>();

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
			if (gathering.containerLocation().equals(chestLocation)) {
				return gathering;
			}
		}
		return null;
	}
}
