rootProject.name = "FabiCraft"

includeBuild("build-logic")

listOf(
	"paper-core",
	"paper-bedwars",
	"paper-survival",
	"common",
	"velocity",
	":typewriter:survival"
).forEach { include(it) }

dependencyResolutionManagement {
	repositoriesMode.set(RepositoriesMode.PREFER_PROJECT)
	repositories {
		mavenCentral()
		maven("https://repo.papermc.io/repository/maven-public/") // Paper, Velocity
		maven("https://maven.enginehub.org/repo/") // WorldGuard
		maven("https://repo.william278.net/releases") // HuskHomes, HuskClaims
		maven("https://maven.citizensnpcs.co/repo") // Citizens
		maven("https://repo.marcely.de/repository/maven-public/") // MBedwars
	}
}

pluginManagement {
	repositories {
		gradlePluginPortal()
		mavenCentral()
		maven("https://maven.typewritermc.com/beta")
	}
}