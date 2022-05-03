package com.example.classlib

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
class Books(
    var title: String? = null,
    var author: String? = null,
    var genre: String? = null,
    var lexileLevel: String? = null,
    var age: String? = null,
    var available: String? = null
) : Parcelable