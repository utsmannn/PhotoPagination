package com.utsman.wallaz.data

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize


@Parcelize
data class Urls(@SerializedName("raw") val raw: String,
                @SerializedName("full") val full: String,
                @SerializedName("regular") val regular: String,
                @SerializedName("small") val small: String,
                @SerializedName("thumb") val thumb: String) : Parcelable

@Parcelize
data class Links(@SerializedName("self") val self: String,
                 @SerializedName("html") val html: String,
                 @SerializedName("download") val download: String,
                 @SerializedName("download_location") val downloadLocation: String) : Parcelable

@Parcelize
data class ProfileImage(@SerializedName("medium") val medium: String,
                       @SerializedName("small") val small: String) : Parcelable

@Parcelize
data class LinkUsers(@SerializedName("self") val self: String) : Parcelable

@Parcelize
data class User(@SerializedName("id") val id: String,
                @SerializedName("username") val username: String,
                @SerializedName("name") val name: String,
                @SerializedName("twitter_username") val twitter: String?,
                @SerializedName("instagram_username") val instagram: String?,
                @SerializedName("links") val links: LinkUsers,
                @SerializedName("profile_image") val profileImage: ProfileImage,
                @SerializedName("bio") val bio: String,
                @SerializedName("location") val location: String) : Parcelable

@Parcelize
data class Exif(@SerializedName("make") val make: String,
                @SerializedName("model") val model: String,
                @SerializedName("exposure_time") val exposureTime: String,
                @SerializedName("aperture") val aperture: String,
                @SerializedName("focal_length") val focalLength: String,
                @SerializedName("iso") val iso: Long) : Parcelable

@Parcelize
data class PhotoLocation(@SerializedName("title") val title: String,
                         @SerializedName("name") val name: String,
                         @SerializedName("city") val city: String,
                         @SerializedName("country") val country: String) : Parcelable

@Parcelize
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
                  @SerializedName("location") val location: PhotoLocation?) : Parcelable