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

package com.chimerapps.gradle.transifex

import com.chimerapps.gradle.transifex.transifex.TranslationDownloader
import com.chimerapps.gradle.transifex.transifex.api.TransifexApi
import com.chimerapps.gradle.transifex.utils.ApiTokenAuthInterceptor
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction

/**
 * @author Nicola Verbeeck
 * @date 04/09/2017.
 */
open class UpdateTranslationsTask : DefaultTask() {

    private val apiAuth: ApiTokenAuthInterceptor by lazy { project.plugins.findPlugin(DownloadTranslationsPlugin::class.java).authInterceptor }
    private val api: TransifexApi by lazy { project.plugins.findPlugin(DownloadTranslationsPlugin::class.java).transifexApi }
    lateinit var configuration: TranslationConfiguration

    @TaskAction
    fun updateTranslations() {
        logger.debug("Update translations task running for config ${configuration.name}")

        val apiKey = configuration.apiKey ?: throw IllegalArgumentException("No api key provided for transifex")
        apiAuth.apiToken = apiKey

        TranslationDownloader(api, logger).download(configuration)
    }

}