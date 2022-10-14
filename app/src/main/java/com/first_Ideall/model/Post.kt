package com.first_Ideall.model

import com.google.firebase.database.Exclude
import java.util.*
import kotlin.collections.ArrayList

class Post (val postTitle: String? = null, val postDescription: String? = null, val date: Date? = null, val userName: String? = null, val postImg: String? = null, val likes: ArrayList<String>? = arrayListOf()) {
    @Exclude
    @set:Exclude
    @get:Exclude
    var uid: String? = null
    constructor(): this (null,null,null, null, null,null)
}
