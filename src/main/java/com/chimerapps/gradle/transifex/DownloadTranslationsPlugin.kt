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

import com.chimerapps.gradle.transifex.transifex.api.TransifexApi
import com.chimerapps.gradle.transifex.transifex.api.model.MoshiFactory
import com.chimerapps.gradle.transifex.utils.ApiTokenAuthInterceptor
import com.squareup.moshi.Moshi
import okhttp3.OkHttpClient
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.Task
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

/**
 * @author Nicola Verbeeck
 * @date 04/09/2017.
 */
open class DownloadTranslationsPlugin : Plugin<Project> {

    val authInterceptor = ApiTokenAuthInterceptor()

    private val httpClient = OkHttpClient.Builder()
            .addInterceptor(authInterceptor)
            .build()
    private val moshi = Moshi.Builder()
            .add(MoshiFactory())
            .build()

    val transifexApi = Retrofit.Builder()
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .client(httpClient)
            .baseUrl(TransifexApi.API_BASE)
            .build()
            .create(TransifexApi::class.java)

    override fun apply(target: Project) {
        val extension = target.extensions.create("transifex", DownloadTranslationsExtension::class.java)

        target.afterEvaluate {
            val tasks = arrayListOf<Task>()
            extension.configurations.forEach { configuration ->
                target.logger.debug("Creating task update${configuration.name.capitalize()}Translations")
                val task = target.tasks.create("update${configuration.name.capitalize()}Translations", UpdateTranslationsTask::class.java) {
                    it.configuration = configuration
                }
                task.group = "Translations"
                tasks.add(task)
            }

            if (extension.apiKey != null) {
                target.logger.debug("Creating task updateDefaultTranslations")
                val task = target.tasks.create("updateDefaultTranslations", UpdateTranslationsTask::class.java) {
                    it.configuration = extension
                }
                task.group = "Translations"
                tasks.add(task)
            }

            target.logger.debug("Creating task updateTranslations")
            val allTask = target.tasks.create("updateTranslations") {
                it.dependsOn.addAll(tasks)
            }
            allTask.group = "Translations"
        }

    }

}