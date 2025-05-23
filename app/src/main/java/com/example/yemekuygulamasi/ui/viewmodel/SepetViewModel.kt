package com.example.yemekuygulamasi.ui.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.yemekuygulamasi.data.entity.SepetYemekler
import com.example.yemekuygulamasi.data.entity.Yemekler
import com.example.yemekuygulamasi.data.repo.YemeklerRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SepetViewModel @Inject constructor(var yemeklerRepository: YemeklerRepository) : ViewModel(){
    var sepetYemeklerListesi = MutableLiveData<List<SepetYemekler>>()
    //private var tumYemekler = listOf<SepetYemekler>()

    init {
        if (sepetYemeklerListesi.value == null) {
            sepetiGetir()
        }
    }

    fun sil(sepet_yemek_id: String, kullanici_adi: String){
        CoroutineScope(Dispatchers.Main).launch {
            try {
                val response = yemeklerRepository.sil(sepet_yemek_id.toInt(), kullanici_adi)
                Log.e("SepetViewModel", "Silme işlemi başarılı: ${response.success}")
                sepetiGetir()
            } catch (e: Exception) {
                Log.e("SepetViewModel", "Silme işlemi hatası: ${e.message}")
            }
        }
    }

    fun sepetiGetir(kullanici_adi: String = "okumu"){
        CoroutineScope(Dispatchers.Main).launch {
            try {
                val response = yemeklerRepository.sepetiGetir(kullanici_adi)
                Log.e("SepetViewModel", "API Response - Success: ${response.success}")

                if (response.success == 1 && response.sepetYemekler != null) {
                    sepetYemeklerListesi.value = response.sepetYemekler
                } else {
                    sepetYemeklerListesi.value = listOf()
                }

            }catch (e: Exception){
                Log.e("SepetViewModel", "API çağrısı sırasında hata oluştu: ${e.message}")
                e.printStackTrace()
            }
        }
    }
    fun sepetiTemizle(kullanici_adi: String = "okumu") {
        viewModelScope.launch {
            sepetYemeklerListesi.value?.forEach { yemek ->
                yemeklerRepository.sil(yemek.sepet_yemek_id, kullanici_adi)
            }
            sepetYemeklerListesi.value = listOf() // Local listeyi de temizle
        }
    }

}