rootProject.name = "PluginTemplate"

dependencyResolutionManagement {
    versionCatalogs {
        create("libs") {
            // compileOnly dependencies
            library("paper-api", "io.papermc.paper:paper-api:26.1.2.build.+")
            library("daisylib", "uk.firedev:DaisyLib:3.1-SNAPSHOT")

            // implementation dependencies

            // paperLibrary dependencies

            // Gradle plugins
            plugin("shadow", "com.gradleup.shadow").version("9.4.1")
            plugin("plugin-yml", "de.eldoria.plugin-yml.paper").version("0.9.0")
        }
    }
}
