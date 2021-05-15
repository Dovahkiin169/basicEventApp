package com.omens.basiceventapp.model
import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class RetrievedItem(
    @SerializedName("id") val id: Int,
    @SerializedName("title") val title: String?,
    @SerializedName("subtitle") val subtitle: String?,
    @SerializedName("date") val date: String?,
    @SerializedName("imageUrl") val imageUrl: String?,
    @SerializedName("videoUrl") val videoUrl: String?,
): Parcelable