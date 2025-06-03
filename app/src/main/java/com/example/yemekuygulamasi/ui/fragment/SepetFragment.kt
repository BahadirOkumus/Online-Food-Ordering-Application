package com.example.yemekuygulamasi.ui.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.yemekuygulamasi.R
import com.example.yemekuygulamasi.databinding.FragmentSepetBinding
import com.example.yemekuygulamasi.ui.adapter.SepetAdapter
import com.example.yemekuygulamasi.ui.viewmodel.SepetViewModel
import dagger.hilt.android.AndroidEntryPoint
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import com.example.yemekuygulamasi.data.entity.SepetYemekler
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.serialization.descriptors.PrimitiveKind

@AndroidEntryPoint
class SepetFragment : Fragment(), SepetAdapter.ToplamSepet {
    private lateinit var binding: FragmentSepetBinding
    private val viewModel: SepetViewModel by viewModels()
    private lateinit var sepetAdapter: SepetAdapter

    override fun toplamBul(toplamAdet: Int,toplamFiyat: Int){

        binding.textViewFiyatSepet.text="${toplamFiyat} ₺"

    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.e("SepetFragment", "onCreateView başladı")
        binding = FragmentSepetBinding.inflate(inflater, container, false)
        sepetAdapter = SepetAdapter(requireContext(), listOf(), viewModel, this)
        binding.sepetRv.adapter = sepetAdapter

        binding.sepetRv.layoutManager = LinearLayoutManager(requireContext())

        binding.bottomNavigationDonus.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> {
                    findNavController().navigate(R.id.anaSayfaFragment)
                    true}
                else -> false
            }
        }


        viewModel.sepetYemeklerListesi.observe(viewLifecycleOwner) { liste ->
            if (liste.isNullOrEmpty()) {
                binding.sepetRv.visibility = View.GONE
                binding.lottieViewSepet.visibility = View.VISIBLE

            } else {
                binding.sepetRv.visibility = View.VISIBLE
                binding.lottieViewSepet.visibility = View.GONE
                sepetAdapter.updateList(liste)
            }
        }


        binding.buttonSepetOnay.setOnClickListener {
            Snackbar.make(it,"Sepet onaylandı", Snackbar.LENGTH_SHORT).show()
            lifecycleScope.launch {
                viewModel.sepetiTemizle()
                delay(100)
                findNavController().navigate(R.id.sepet_to_main)
            }
        }
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        viewModel.sepetiGetir()
    }
}