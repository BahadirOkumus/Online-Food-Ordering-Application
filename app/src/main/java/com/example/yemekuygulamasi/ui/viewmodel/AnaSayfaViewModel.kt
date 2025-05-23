package com.example.yemekuygulamasi.ui.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.yemekuygulamasi.data.entity.Yemekler
import com.example.yemekuygulamasi.data.repo.YemeklerRepository
import com.example.yemekuygulamasi.ui.adapter.SepetAdapter
import com.example.yemekuygulamasi.ui.fragment.SepetFragment
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AnaSayfaViewModel @Inject constructor(var yemeklerRepository: YemeklerRepository) : ViewModel() {
    var yemeklerListesi = MutableLiveData<List<Yemekler>>()
    private var tumYemekler = listOf<Yemekler>()

    init {
        yemekleriYukle()
    }

    fun yemekleriYukle(){
        CoroutineScope(Dispatchers.Main).launch {
            try {
                val yemekler = yemeklerRepository.yemekleriYukle()
                Log.e("AnaSayfaViewModel", "Yüklenen yemek sayısı: ${yemekler.size}")
                tumYemekler = yemekler
                yemeklerListesi.value = yemekler
            }catch (e: Exception){
                Log.e("AnaSayfaViewModel", "Hata oluştu: ${e.message}")
            }
        }
    }

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
                    emptyList() // Hata durumunda boş liste dön
                }

                val mevcutUrun = sepetListesi.find { it.yemek_adi == yemek_adi }

                if (mevcutUrun != null) {
                    val yeniAdet = mevcutUrun.yemek_siparis_adet + 1
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

    fun ara(aramaKelimesi: String) {
        if (aramaKelimesi.isEmpty()) {
            yemeklerListesi.value = tumYemekler
        } else {
            val filtreliListe = tumYemekler.filter { yemek ->
                yemek.yemek_adi.contains(aramaKelimesi, ignoreCase = true)
            }
            yemeklerListesi.value = filtreliListe
        }
    }
}