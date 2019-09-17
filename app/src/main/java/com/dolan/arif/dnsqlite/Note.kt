package com.dolan.arif.dnsqlite

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * Created by Bencoleng on 17/09/2019.
 */
@Parcelize
data class Note(
    var id: Int,
    var title: String,
    var desc: String
) : Parcelable