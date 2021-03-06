# transifex android gradle plugin

A simple plugin which downloads translations from transifex and saves the files to the project.

Usage:
```
transifex {
    apiKey 'your-api-key-here'
    projectSlug 'project-identifier-slub'
    fileName 'strings.xml'       //Default
    sourceRoot 'src/main/res'    //Default, will replace occurences of {language} with the (transformed) language
    fileType 'xml'               //Default, one of the file types supported by transifex
    
    languageRename { languageCode ->                //Default is identity transformation (return languageCode)
        return languageCode.replace('_','-r')
    }
    sourceRootProvider { languageCode ->            //Function that provides source root based on language code (this is the raw language code)
        def renamed = languageRename(languageCode)
        return sourceRoot.replace("{language}", (renamed == null) ? code : renamed)
    }
    
    fileNameProvider { languageCode ->      //Function that can generate different file names based on language code (this is the raw language code)
        return fileName
    }
    
    folderProvider { languageCode ->        //Function that provides the folder based on the (raw) language code
        def renamed = languageRename(languageCode)
        return "values-"+(renamed == null) ? code : renamed)
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
