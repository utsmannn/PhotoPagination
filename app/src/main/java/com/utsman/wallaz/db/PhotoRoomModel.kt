package com.utsman.wallaz.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

// @Parcelize
//data class Urls(@SerializedName("raw") val raw: String,
//                @SerializedName("full") val full: String,
//                @SerializedName("regular") val regular: String,
//                @SerializedName("small") val small: String,
//                @SerializedName("thumb") val thumb: String) : Parcelable
//
//@Parcelize
//data class Links(@SerializedName("self") val self: String,
//                 @SerializedName("html") val html: String,
//                 @SerializedName("download") val download: String,
//                 @SerializedName("download_location") val downloadLocation: String) : Parcelable
//
//@Parcelize
//data class ProfileImage(@SerializedName("medium") val medium: String,
//                       @SerializedName("small") val small: String) : Parcelable
//
//@Parcelize
//data class LinkUsers(@SerializedName("self") val self: String) : Parcelable
//
//@Parcelize
//data class User(@SerializedName("id") val id: String,
//                @SerializedName("username") val username: String,
//                @SerializedName("name") val name: String,
//                @SerializedName("twitter_username") val twitter: String?,
//                @SerializedName("instagram_username") val instagram: String?,
//                @SerializedName("links") val links: LinkUsers,
//                @SerializedName("profile_image") val profileImage: ProfileImage,
//                @SerializedName("bio") val bio: String,
//                @SerializedName("location") val location: String) : Parcelable
//
//@Parcelize
//data class Photos(@SerializedName("id") val id: String,
//                  @SerializedName("created_at") val createdAt: String,
//                  @SerializedName("width") val w: String,
//                  @SerializedName("height") val h: String,
//                  @SerializedName("color") val color: String,
//                  @SerializedName("alt_description") val description: String?,
//                  @SerializedName("urls") val url: Urls,
//                  @SerializedName("links") val links: Links,
//                  @SerializedName("user") val user: User,
//                  @SerializedName("likes") val likes: Long) : Parcelable


@Entity
data class PhotoRoomModel(
        @PrimaryKey val uid: Int,
        @ColumnInfo(name = "id") val id: String,
        @ColumnInfo(name = "created_at") val createdAt: String,
        @ColumnInfo(name = "width") val w: Int,
        @ColumnInfo(name = "height") val h: Int,
        @ColumnInfo(name = "color") val color: String,
        @ColumnInfo(name = "url") val url: String,
        @ColumnInfo(name = "user_name") val userName: String)