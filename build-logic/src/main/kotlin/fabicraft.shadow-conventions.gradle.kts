import com.github.jengelman.gradle.plugins.shadow.transformers.PropertiesFileTransformer

plugins {
	id("fabicraft.java-conventions")
	id("com.gradleup.shadow")
}

tasks {
	build {
		dependsOn(shadowJar)
	}
	shadowJar {
		archiveBaseName.set(project.prefixedPluginName)
		destinationDirectory.set(rootProject.layout.buildDirectory.dir("libs"))
		archiveClassifier.set("")
		duplicatesStrategy = DuplicatesStrategy.INCLUDE
		mergeServiceFiles()
		transform(PropertiesFileTransformer::class.java)
	}
}