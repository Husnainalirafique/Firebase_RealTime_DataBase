package com.example.firebase.note

data class Note(
    val noteTitle: String?,
    val noteDescription:String?
){
    constructor() : this("","")
}
