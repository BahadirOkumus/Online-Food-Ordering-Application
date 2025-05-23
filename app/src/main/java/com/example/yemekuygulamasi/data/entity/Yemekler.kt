package com.example.yemekuygulamasi.data.entity

import java.io.Serializable

data class Yemekler(var yemek_id: Int,
                    var sepet_yemek_id : Int,
                    var yemek_adi: String,
                    var yemek_resim_adi: String,
                    var yemek_fiyat: Int,
                    var yemek_siparis_adet: Int,
                    var kullanici_adi: String? = null) : Serializable {
}