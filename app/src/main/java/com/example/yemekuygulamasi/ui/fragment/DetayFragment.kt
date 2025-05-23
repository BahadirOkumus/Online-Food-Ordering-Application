package com.example.yemekuygulamasi.ui.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.example.yemekuygulamasi.R
import com.example.yemekuygulamasi.databinding.FragmentDetayBinding
import com.example.yemekuygulamasi.ui.viewmodel.DetayViewModel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetayFragment : Fragment() {
    private lateinit var binding: FragmentDetayBinding
    private lateinit var viewModel: DetayViewModel
    private val args: DetayFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        try {
            binding = FragmentDetayBinding.inflate(inflater, container, false)

            binding.bottomNavigationDonus.setOnItemSelectedListener { item ->
                when (item.itemId) {
                    R.id.nav_home -> {
                        findNavController().navigate(R.id.anaSayfaFragment)
                        true
                    }
                    R.id.nav_cart -> {
                        findNavController().navigate(R.id.detay_to_sepet)
                        true
                    }
                    else -> false
                }
            }

            binding.textViewDetayFiyat.text = "${args.yemek.yemek_fiyat} ₺"
            binding.textViewDetayisim.text = args.yemek.yemek_adi


            var baslangicAdet = 1
            binding.textViewDetayAdet.text = baslangicAdet.toString()
            binding.imageButtonDetayArti.setOnClickListener {
                baslangicAdet += 1
                binding.textViewDetayAdet.text = baslangicAdet.toString()
            }

            binding.imageButtonDetayEksi.setOnClickListener {
                if (baslangicAdet >1){
                    baslangicAdet -= 1
                    binding.textViewDetayAdet.text = baslangicAdet.toString()
                }else{
                    Snackbar.make(it,"Adet miktari 0 olamaz", Snackbar.LENGTH_SHORT).show()
                }
            }

            binding.buttonDetaySepeteEkle.setOnClickListener{
                val yemek_adi = args.yemek.yemek_adi
                val yemek_resim_adi = args.yemek.yemek_resim_adi
                val yemek_fiyat = args.yemek.yemek_fiyat
                val yemek_siparis_adet = baslangicAdet
                val kullanici_adi = "okumu"

                viewModel.sepeteEkle(yemek_adi, yemek_resim_adi, yemek_fiyat, yemek_siparis_adet, kullanici_adi)

                Snackbar.make(binding.root, "${yemek_adi} sepete eklendi", Snackbar.LENGTH_SHORT).show()
            }

            val resimUrl = "http://kasimadalan.pe.hu/yemekler/resimler/${args.yemek.yemek_resim_adi}"
            
            Glide.with(this)
                .load(resimUrl)
                .into(binding.imageViewDetayYemek)

            return binding.root
        } catch (e: Exception) {
            e.printStackTrace()
            throw e
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        try {
            val tempViewModel: DetayViewModel by viewModels()
            viewModel = tempViewModel
        } catch (e: Exception) {
            e.printStackTrace()
            throw e
        }
    }
}