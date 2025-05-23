package com.example.yemekuygulamasi.data.entity

import com.google.gson.annotations.SerializedName

class SepetYemeklerCevap(
    @SerializedName("sepet_yemekler") var sepetYemekler: List<SepetYemekler>,
    @SerializedName("success") var success: Int
)
