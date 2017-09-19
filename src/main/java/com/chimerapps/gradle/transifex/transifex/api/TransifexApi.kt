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

package com.chimerapps.gradle.transifex.transifex.api

import com.chimerapps.gradle.transifex.transifex.api.model.ProjectResource
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * @author Nicola Verbeeck
 * @date 04/09/2017.
 */
interface TransifexApi {

    companion object {
        const val API_BASE = "https://www.transifex.com/api/2/"
    }

    @GET("project/{projectSlug}/resources")
    fun getProjectResources(@Path("projectSlug") projectSlug: String): Call<List<ProjectResource>>

    @GET("project/{projectSlug}/resource/{resourceSlug}?details")
    fun getProjectResourceDetails(@Path("projectSlug") projectSlug: String,
                                  @Path("resourceSlug") resourceSlug: String): Call<ProjectResource>

    @GET("project/{projectSlug}/resource/{resourceSlug}/translation/{languageId}")
    fun getProjectTranslation(@Path("projectSlug") projectSlug: String,
                              @Path("resourceSlug") resourceSlug: String,
                              @Path("languageId") languageId: String,
                              @Query("file") file: String): Call<ResponseBody>

}