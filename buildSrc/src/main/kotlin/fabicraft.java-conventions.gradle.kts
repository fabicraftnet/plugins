plugins {
	`java-library`
	id("com.diffplug.spotless")
}

group = rootProject.group

java.toolchain.languageVersion.set(JavaLanguageVersion.of(21))

tasks {
	compileJava {
		options.encoding = Charsets.UTF_8.name()
	}
}

spotless {
	java {
		endWithNewline()
		formatAnnotations()
		leadingSpacesToTabs()
		removeUnusedImports()
		trimTrailingWhitespace()
	}
}