package com.example.yemekuygulamasi.ui.viewmodel

import androidx.lifecycle.ViewModel
import com.example.yemekuygulamasi.data.repo.YemeklerRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ProfilViewModel @Inject constructor(var yemeklerRepository: YemeklerRepository) : ViewModel(){
}