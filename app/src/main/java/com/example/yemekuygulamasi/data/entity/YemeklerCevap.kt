package com.example.yemekuygulamasi.data.entity

import com.google.gson.annotations.SerializedName

data class YemeklerCevap(var yemekler: List<Yemekler>, var success: Int){

}