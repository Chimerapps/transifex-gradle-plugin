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

package com.chimerapps.gradle.transifex.transifex.api.model

import com.chimerapps.moshigenerator.GenerateMoshi
import com.chimerapps.moshigenerator.GenerateMoshiFactory
import com.squareup.moshi.Json

/**
 * @author Nicola Verbeeck
 * @date 04/09/2017.
 */
@GenerateMoshi
data class ProjectResource(
        @Json(name = "source_language_code") val sourceLanguageCode: String,
        val name: String,
        @Json(name = "i18n_type") val type: String,
        val slug: String,
        @Json(name = "available_languages") val availableLanguages: List<ProjectLanguage>?
)

@GenerateMoshi
data class ProjectLanguage(
        val code: String
)

@GenerateMoshiFactory(ProjectResource::class, ProjectLanguage::class)
interface ModelFactoryHolder