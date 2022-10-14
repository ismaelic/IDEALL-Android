package com.first_Ideall.model

var ideaList = mutableListOf<Idea>()

val IDEA_ID_EXTRA = "ideaExtra"

class Idea(
    var user: String,
    var name: String,
    var image: Int,
    val id: Int? = ideaList.size


    )