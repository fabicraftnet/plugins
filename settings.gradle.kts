rootProject.name = "FabiCraft"

includeBuild("build-logic")

listOf(
	"paper-core",
	"paper-bedwars",
	"paper-survival",
	"common",
	"velocity"
).forEach { include(it) }

dependencyResolutionManagement {
	repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
	repositories {
		mavenCentral()
		maven("https://repo.papermc.io/repository/maven-public/") // Paper, Velocity
		maven("https://maven.enginehub.org/repo/") // WorldGuard
		maven("https://repo.william278.net/releases") // HuskHomes, HuskClaims
		maven("https://maven.citizensnpcs.co/repo") // Citizens
		maven("https://repo.marcely.de/repository/maven-public/") // MBedwars
	}
}