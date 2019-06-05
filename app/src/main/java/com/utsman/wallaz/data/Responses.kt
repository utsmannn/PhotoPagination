/*
 * Copyright 2019 Muhammad Utsman. All rights reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.utsman.wallaz.data

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

data class Urls(@SerializedName("raw") val raw: String,
                @SerializedName("full") val full: String,
                @SerializedName("regular") val regular: String,
                @SerializedName("small") val small: String,
                @SerializedName("thumb") val thumb: String)

data class Links(@SerializedName("self") val self: String,
                 @SerializedName("html") val html: String,
                 @SerializedName("download") val download: String,
                 @SerializedName("download_location") val downloadLocation: String)

data class ProfileImage(@SerializedName("medium") val medium: String,
                       @SerializedName("small") val small: String)

data class LinkUsers(@SerializedName("self") val self: String)

data class User(@SerializedName("id") val id: String,
                @SerializedName("username") val username: String,
                @SerializedName("name") val name: String,
                @SerializedName("twitter_username") val twitter: String?,
                @SerializedName("instagram_username") val instagram: String?,
                @SerializedName("links") val links: LinkUsers,
                @SerializedName("profile_image") val profileImage: ProfileImage,
                @SerializedName("bio") val bio: String,
                @SerializedName("location") val location: String)

data class Exif(@SerializedName("make") val make: String,
                @SerializedName("model") val model: String,
                @SerializedName("exposure_time") val exposureTime: String,
                @SerializedName("aperture") val aperture: String,
                @SerializedName("focal_length") val focalLength: String,
                @SerializedName("iso") val iso: Long)

data class PhotoLocation(@SerializedName("title") val title: String,
                         @SerializedName("name") val name: String,
                         @SerializedName("city") val city: String,
                         @SerializedName("country") val country: String)

@Parcelize
data class Tag(@SerializedName("title") val title: String) : Parcelable

data class Photos(@SerializedName("id") val id: String,
                  @SerializedName("created_at") val createdAt: String,
                  @SerializedName("width") val w: Int,
                  @SerializedName("height") val h: Int,
                  @SerializedName("color") val color: String,
                  @SerializedName("alt_description") val description: String?,
                  @SerializedName("urls") val url: Urls,
                  @SerializedName("links") val links: Links,
                  @SerializedName("user") val user: User,
                  @SerializedName("likes") val likes: Long,
                  @SerializedName("exif") val exif: Exif?,
                  @SerializedName("location") val location: PhotoLocation?,
                  @SerializedName("tags") val tags: List<Tag>? = null)

data class Search(@SerializedName("total") val total: Long,
                  @SerializedName("results") val results: List<Photos>)

data class DownloadLocation(@SerializedName("url") val url: String)