package net.fabicraft.paper.survival.afk;

public final class AfkStatus {
	private long afkSince;
	private boolean warned = false;

	public AfkStatus() {
		this.afkSince = System.nanoTime();
	}

	public void markAsActive() {
		this.afkSince = System.nanoTime();
		this.warned = false;
	}

	public long afkSince() {
		return this.afkSince;
	}

	public long hasBeenAfkFor() {
		return System.nanoTime() - this.afkSince;
	}

	public boolean warned() {
		return this.warned;
	}

	public boolean warned(boolean warned) {
		return this.warned = warned;
	}
}
