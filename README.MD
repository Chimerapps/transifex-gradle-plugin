# transifex android gradle plugin

A simple plugin which downloads translations from transifex and saves the files to the project.

Usage:
```
transifex {
    apiKey 'your-api-key-here'
    projectSlug 'project-identifier-slub'
    fileName 'strings.xml'       //Default
    sourceRoot 'src/main/res'    //Default
    languageRename { languageCode ->
        return languageCode.replace('_','-r')
    }

    configuration("test") {
        apiKey 'read-only-key2'     //Uses the default fileName and sourceRoot. Not inherited
    }

    configuration("release") {
        apiKey 'read-only-key3'     //Uses the default fileName and sourceRoot. Not inherited
    }
}
```

You can then call the tasks from the command line:
```
./gradlew updateTranslations            //Runs all translations tasks
./gradlew updateDefaultTranslations     //Runs the translation task defined by the outer scope
./gradlew updateTestTranslations        //Runs the translation task defined by the 'test' configuration
./gradlew updateReleaseTranslations     //Runs the translation task defined by the 'release' configuration
```

**NOTE:** If the api key is missing from the outer scope, the 'default' configuration is not included as task

**NOTE:** You can use '{language}' in the sourceRoot definition to have it replaced with the (optionally renamed by languageRename) language.
Eg:
```
transifex {
    sourceRoot 'src/{language}/res'    //Default
    languageRename { languageCode ->
        return languageCode.replace('_','-r')
    }
}
```