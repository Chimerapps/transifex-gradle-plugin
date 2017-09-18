/*
 * Copyright 2017 - Chimerapps BVBA
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.chimerapps.gradle.transifex.transifex

import com.chimerapps.gradle.transifex.transifex.api.TransifexApi
import groovy.lang.Closure
import org.gradle.api.logging.Logger
import java.io.File
import java.io.FileOutputStream

/**
 * @author Nicola Verbeeck
 * @date 04/09/2017.
 */
class TranslationDownloader(private val transifexApi: TransifexApi, private val logger: Logger) {

    fun download(projectSlug: String, outputRoot: String, stringsFileName: String, languageRename: Closure<String>?) {
        logger.debug("Downloading translations for project ($projectSlug)")

        val projectResourcesResponse = transifexApi.getProjectResources(projectSlug).execute()
        val resourceSlug = projectResourcesResponse.body()?.find { it.type == "ANDROID" }?.slug ?: throw IllegalArgumentException("Could not find android resources in transifex response")
        val projectDetailsResponse = transifexApi.getProjectResourceDetails(projectSlug, resourceSlug).execute()

        logger.debug("Project download result: ${projectDetailsResponse.message()} and code ${projectDetailsResponse.code()}. Body: ${projectDetailsResponse.body()}")

        val projectLanguages = projectDetailsResponse.body()?.availableLanguages ?: throw IllegalArgumentException("Failed to load list of languages")
        logger.debug("Got ${projectLanguages.size} language files")
        projectLanguages.forEach {
            logger.debug("Downloading file: ${it.code}")

            val translationFile = transifexApi.getProjectTranslation(projectSlug, resourceSlug, it.code).execute()
            val body = translationFile.body()
            if (body == null) {
                logger.warn("Failed to download file for locale ${it.code}: ${translationFile.message()}")
                return@forEach
            }
            val folderName = "values-${languageRename?.call(it.code) ?: it.code}"

            val dir = File(outputRoot.replace("{language}", languageRename?.call(it.code) ?: it.code), folderName)
            dir.mkdirs()
            FileOutputStream(File(dir, stringsFileName)).apply {
                body.byteStream().copyTo(this)
            }
        }
    }

}