rootProject.name = "FabiCraft"

listOf(
	"paper-common",
	"paper-core",
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
	}
}