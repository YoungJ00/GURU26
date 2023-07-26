package com.example.guru26

data class ContentDTO(var favoriteCount: Int =0,
var favorites : MutableMap<String,Boolean> = HashMap()){

}