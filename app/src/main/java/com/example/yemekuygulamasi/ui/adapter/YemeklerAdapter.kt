package com.example.yemekuygulamasi.ui.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.yemekuygulamasi.data.entity.Yemekler
import com.example.yemekuygulamasi.databinding.CardSepetBinding
import com.example.yemekuygulamasi.databinding.CardTasarimBinding
import com.example.yemekuygulamasi.retrofit.YemeklerDao
import com.example.yemekuygulamasi.ui.fragment.AnaSayfaFragmentDirections
import com.example.yemekuygulamasi.ui.viewmodel.AnaSayfaViewModel
import com.example.yemekuygulamasi.ui.viewmodel.SepetViewModel
import com.example.yemekuygulamasi.utils.gecisYap
import com.google.android.material.snackbar.Snackbar

class YemeklerAdapter (var mContext: Context,var yemeklerListesi: List<Yemekler>,var viewModel: AnaSayfaViewModel)
    : RecyclerView.Adapter<YemeklerAdapter.CardTasarimTutucu>(){
    inner class CardTasarimTutucu(var tasarim: CardTasarimBinding) : RecyclerView.ViewHolder(tasarim.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardTasarimTutucu {
        val tasarim = CardTasarimBinding.inflate(LayoutInflater.from(mContext),parent,false)
        return CardTasarimTutucu(tasarim)
    }

    override fun onBindViewHolder(holder: CardTasarimTutucu, position: Int) {
        val yemek = yemeklerListesi.get(position)
        val t = holder.tasarim

        t.textViewResimIsim.text = yemek.yemek_adi
        t.textViewFiyat.text = "${yemek.yemek_fiyat} ₺"

        val resimUrl = "http://kasimadalan.pe.hu/yemekler/resimler/${yemek.yemek_resim_adi}"

        Glide.with(mContext)
            .load(resimUrl)
            .centerCrop()
            .into(t.imageViewYemek)

        t.imageViewYemek.setOnClickListener {
            try {
                yemek.kullanici_adi = "okumu"
                val gecis = AnaSayfaFragmentDirections.detayGecis(yemek = yemek)
                Navigation.gecisYap(it, gecis)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        t.imageButtonSepeteEkle.setOnClickListener {
            viewModel.sepeteEkle(
                yemek_adi = yemek.yemek_adi,
                yemek_resim_adi = yemek.yemek_resim_adi,
                yemek_fiyat = yemek.yemek_fiyat,
                yemek_siparis_adet = 1,
                kullanici_adi = "okumu"
        )
            Snackbar.make(it,"${yemek.yemek_adi} sepete eklendi", Snackbar.LENGTH_SHORT).show()
        }
    }

    override fun getItemCount(): Int {
        Log.e("YemeklerAdapter", "Liste boyutu: ${yemeklerListesi.size}")
        return yemeklerListesi.size
    }

}
