rootProject.name = "build-logic"

dependencyResolutionManagement {
	versionCatalogs {
		create("libs") {
			from(files("../gradle/libs.versions.toml"))
		}
	}
	repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
	repositories {
		mavenCentral()
		gradlePluginPortal()
		maven("https://maven.typewritermc.com/beta")
	}
}