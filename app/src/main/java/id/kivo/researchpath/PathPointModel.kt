package id.kivo.researchpath

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class PathPointModel(
    var x: Float = 0f,
    var y: Float = 0f
): Parcelable