package com.example.yemekuygulamasi.ui.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.yemekuygulamasi.R
import com.example.yemekuygulamasi.data.entity.SepetYemekler
import com.example.yemekuygulamasi.data.entity.Yemekler
import com.example.yemekuygulamasi.databinding.CardSepetBinding
import com.example.yemekuygulamasi.ui.fragment.AnaSayfaFragmentDirections
import com.example.yemekuygulamasi.ui.fragment.DetayFragment
import com.example.yemekuygulamasi.ui.fragment.SepetFragment
import com.example.yemekuygulamasi.ui.fragment.SepetFragmentDirections
import com.example.yemekuygulamasi.ui.viewmodel.SepetViewModel
import com.example.yemekuygulamasi.utils.gecisYap
import com.google.android.material.snackbar.Snackbar

class SepetAdapter(var mContext: Context, var yemeklerListesi: List<SepetYemekler>?, var viewModel: SepetViewModel,var listener: ToplamSepet)
    : RecyclerView.Adapter<SepetAdapter.CardSepetTutucu>() {

        var sepetYemeklerListesi: List<SepetYemekler> = listOf()

    interface ToplamSepet{
        fun toplamBul(toplamAdet: Int,toplamFiyat: Int)
    }

    inner class CardSepetTutucu(var binding: CardSepetBinding) : RecyclerView.ViewHolder(binding.root)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardSepetTutucu {
        val binding= CardSepetBinding.inflate(LayoutInflater.from(mContext), parent, false)
        return CardSepetTutucu(binding)
    }

    private fun toplamıHesapla() {
        var toplamAdet = 0
        var toplamFiyat = 0
        for (item in sepetYemeklerListesi) {
            toplamAdet += item.yemek_siparis_adet.toInt()
            toplamFiyat += item.yemek_fiyat.toInt() * item.yemek_siparis_adet.toInt()
        }
        listener.toplamBul(toplamAdet,toplamFiyat)


    }

    fun updateList(yeniListe: List<SepetYemekler>) {
        this.sepetYemeklerListesi = yeniListe
        toplamıHesapla()
        notifyDataSetChanged()
    }


    override fun onBindViewHolder(holder: CardSepetTutucu, position: Int) {
        var sepetYemek = sepetYemeklerListesi.get(position)
        var t = holder.binding


        t.textViewSepet.text = sepetYemek.yemek_adi
        t.textViewSepetFiyat.text = "${sepetYemek.yemek_fiyat} ₺"
        t.textViewSepetAdet.text = "${sepetYemek.yemek_siparis_adet}"

        val resimUrl = "http://kasimadalan.pe.hu/yemekler/resimler/${sepetYemek.yemek_resim_adi}"

        try {
            Glide.with(mContext)
                .load(resimUrl)
                .centerCrop()
                .into(t.imageViewSepetYemek)
        } catch (e: Exception) {

        }

        t.imageViewSil.setOnClickListener {
            Snackbar.make(it, "${sepetYemek.yemek_adi} silinsin mi?", Snackbar.LENGTH_SHORT)
                .setAction("EVET") {
                    viewModel.sil(sepet_yemek_id = "${sepetYemek.sepet_yemek_id}", kullanici_adi = "${sepetYemek.kullanici_adi}")
                }.show()
        }


    }

    override fun getItemCount(): Int {
        val boyut = sepetYemeklerListesi?.size ?: 0
        Log.e("SepetAdapter", "getItemCount çağrıldı - Liste boyutu: $boyut")
        return boyut
    }
}


