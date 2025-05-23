package com.example.yemekuygulamasi.data.repo

import android.util.Log
import com.example.yemekuygulamasi.data.datasource.YemeklerDataSource
import com.example.yemekuygulamasi.data.entity.CRUDCevap
import com.example.yemekuygulamasi.data.entity.SepetYemekler
import com.example.yemekuygulamasi.data.entity.SepetYemeklerCevap
import com.example.yemekuygulamasi.data.entity.Yemekler
import javax.inject.Inject

class YemeklerRepository @Inject constructor(var yemeklerDataSource: YemeklerDataSource) {

    suspend fun yemekleriYukle() : List<Yemekler> = yemeklerDataSource.yemekleriYukle()

    suspend fun sepeteEkle(
                          yemek_adi: String,
                          yemek_resim_adi: String,
                          yemek_fiyat: Int,
                          yemek_siparis_adet: Int,
                          kullanici_adi: String) : CRUDCevap = yemeklerDataSource.sepeteEkle( yemek_adi, yemek_resim_adi, yemek_fiyat, yemek_siparis_adet, kullanici_adi)

    suspend fun sepetiGetir(kullanici_adi: String) : SepetYemeklerCevap {
        try {
            val response = yemeklerDataSource.sepetiGetir(kullanici_adi)
            Log.e("YemeklerRepository", "Sepet getir çalışıyor - Success: ${response.success}")
            return response
        } catch (e: Exception) {
            Log.e("YemeklerRepository", "Sepet gelmiyor hatası: ${e.message}")
            throw e
        }
    }

    suspend fun sil(sepet_yemek_id: Int, kullanici_adi: String) : CRUDCevap = yemeklerDataSource.sil(sepet_yemek_id, kullanici_adi)

    suspend fun ara(aramaKelimesi: String) : List<Yemekler> = yemeklerDataSource.ara(aramaKelimesi)

    suspend fun sepettekiYemekleriYukle(kullanici_adi: String): List<SepetYemekler> = yemeklerDataSource.sepettekiYemekleriYukle(kullanici_adi)



}


