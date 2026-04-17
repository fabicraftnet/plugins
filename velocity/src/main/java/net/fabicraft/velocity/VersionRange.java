package net.fabicraft.velocity;

import com.velocitypowered.api.network.ProtocolVersion;
import org.jspecify.annotations.NonNull;

public record VersionRange(ProtocolVersion min, ProtocolVersion max) {
	public VersionRange {
		if (min.greaterThan(max)) {
			throw new IllegalArgumentException("min cannot be greater than max");
		}
	}

	@Override
	public @NonNull String toString() {
		if (isSingleVersion()) {
			return this.min.getVersionIntroducedIn();
		}
		return this.min.getVersionIntroducedIn() + "-" + this.max.getMostRecentSupportedVersion();
	}

	public boolean contains(ProtocolVersion version) {
		return version.noLessThan(this.min) && version.noGreaterThan(this.max);
	}

	public ProtocolVersion clamp(ProtocolVersion version) {
		if (version.lessThan(this.min)) {
			return this.min;
		}
		if (version.greaterThan(this.max)) {
			return this.max;
		}
		return version;
	}

	private boolean isSingleVersion() {
		return this.min.equals(this.max) && this.min.getVersionsSupportedBy().size() < 2;
	}
}
