package com.example.yemekuygulamasi.ui.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.yemekuygulamasi.data.repo.YemeklerRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.DEFAULT_CONCURRENCY
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetayViewModel @Inject constructor(var yemeklerRepository: YemeklerRepository) : ViewModel() {
    fun sepeteEkle(
        yemek_adi: String,
        yemek_resim_adi: String,
        yemek_fiyat: Int,
        yemek_siparis_adet: Int,
        kullanici_adi: String
    ) {
        CoroutineScope(Dispatchers.Main).launch {
            try {
                val sepetListesi = try {
                    yemeklerRepository.sepettekiYemekleriYukle(kullanici_adi)
                } catch (e: Exception) {
                    Log.e("AnaSayfaViewModel", "Sepet verisi alınamadı: ${e.message}")
                    emptyList() // Hata durumunda boş liste dön
                }

                val mevcutUrun = sepetListesi.find { it.yemek_adi == yemek_adi }

                if (mevcutUrun != null) {
                    val yeniAdet = mevcutUrun.yemek_siparis_adet + yemek_siparis_adet
                    yemeklerRepository.sil(mevcutUrun.sepet_yemek_id, kullanici_adi)
                    yemeklerRepository.sepeteEkle(
                        yemek_adi,
                        yemek_resim_adi,
                        yemek_fiyat,
                        yeniAdet,
                        kullanici_adi
                    )
                } else {
                    yemeklerRepository.sepeteEkle(
                        yemek_adi,
                        yemek_resim_adi,
                        yemek_fiyat,
                        yemek_siparis_adet,
                        kullanici_adi
                    )
                }

            } catch (e: Exception) {
                Log.e("AnaSayfaViewModel", "Sepete ekleme hatası: ${e.message}")
            }
        }
    }
}