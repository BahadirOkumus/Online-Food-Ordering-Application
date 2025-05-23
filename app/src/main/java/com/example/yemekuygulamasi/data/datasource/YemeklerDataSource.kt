package com.example.yemekuygulamasi.data.datasource

import android.util.Log
import com.example.yemekuygulamasi.data.entity.CRUDCevap
import com.example.yemekuygulamasi.data.entity.SepetYemekler
import com.example.yemekuygulamasi.data.entity.SepetYemeklerCevap
import com.example.yemekuygulamasi.retrofit.YemeklerDao
import com.example.yemekuygulamasi.data.entity.Yemekler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class YemeklerDataSource @Inject constructor(var YemeklerDao : YemeklerDao) {
    suspend fun sepeteEkle(
                          yemek_adi: String,
                          yemek_resim_adi: String,
                          yemek_fiyat: Int,
                          yemek_siparis_adet: Int,
                          kullanici_adi: String) : CRUDCevap = withContext(Dispatchers.IO) {
        return@withContext YemeklerDao.sepeteEkle( yemek_adi, yemek_resim_adi, yemek_fiyat, yemek_siparis_adet, kullanici_adi)
    }
    suspend fun sil(sepet_yemek_id: Int, kullanici_adi: String) : CRUDCevap = withContext(Dispatchers.IO) {
        return@withContext YemeklerDao.sil(sepet_yemek_id, kullanici_adi)
    }
    suspend fun sepetiGetir(kullanici_adi: String) : SepetYemeklerCevap = withContext(Dispatchers.IO) {
        try {
            val response = YemeklerDao.sepetiGetir(kullanici_adi)
            Log.e("YemeklerDataSource", "API yanıtı - Success: ${response.success}")
            return@withContext response
        } catch (e: Exception) {
            Log.e("YemeklerDataSource", "API çağrısı: ${e.message}")
            e.printStackTrace()
            throw e
        }
    }
    suspend fun yemekleriYukle() : List<Yemekler> = withContext(Dispatchers.IO){
        return@withContext YemeklerDao.yemekleriYukle().yemekler
    }
    suspend fun ara(aramaKelimesi: String) : List<Yemekler> = withContext(Dispatchers.IO){
        return@withContext YemeklerDao.ara(aramaKelimesi).yemekler
    }
    suspend fun sepettekiYemekleriYukle(kullanici_adi: String): List<SepetYemekler> {
        return YemeklerDao.sepettekiYemekleriGetir(kullanici_adi).sepetYemekler
    }

}