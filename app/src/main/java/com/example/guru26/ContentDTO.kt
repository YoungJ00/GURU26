package com.example.guru26

import java.sql.Time
import java.util.Calendar

data class ContentDTO(var explain : String?=null,
                      var imageUrl : String? = null,
                      var uid:String? =null,
                      var usrId : String? = null,
                      var exhName : String?=null,
                      var exhPlace : String?=null,
                      var exhStartDay : String?=null,
                      var exhEndDay : String?=null,
                      var exhTime : String?= null,
                      var exhLink : String? = null,
                      var timeStamp : Long? = null,
                      var documentId: String? = null,
                      var favoriteCount: Int =0,
                      var favorites : MutableMap<String,Boolean> = HashMap()){
}