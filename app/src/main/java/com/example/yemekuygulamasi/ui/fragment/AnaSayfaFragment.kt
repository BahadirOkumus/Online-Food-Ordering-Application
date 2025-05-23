package com.example.yemekuygulamasi.ui.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.SearchView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.airbnb.lottie.LottieAnimationView
import com.bumptech.glide.Glide
import com.example.yemekuygulamasi.R
import com.example.yemekuygulamasi.databinding.FragmentAnaSayfaBinding
import com.example.yemekuygulamasi.ui.adapter.YemeklerAdapter
import com.example.yemekuygulamasi.ui.viewmodel.AnaSayfaViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AnaSayfaFragment : Fragment() {
    private lateinit var binding: FragmentAnaSayfaBinding
    private val viewModel: AnaSayfaViewModel by viewModels()
    private lateinit var yemeklerAdapter: YemeklerAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAnaSayfaBinding.inflate(inflater, container, false)


        /*binding.locationPinAnimation.setOnTouchListener { _, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    binding.locationPinAnimation.playAnimation()
                    true
                }
                MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                    binding.locationPinAnimation.pauseAnimation()
                    true
                }
                else -> false
            }
        }*/

        binding.yemeklerRv.layoutManager = GridLayoutManager(requireContext(), 2)

        viewModel.yemeklerListesi.observe(viewLifecycleOwner) {
            yemeklerAdapter = YemeklerAdapter(requireContext(), it, viewModel)
            binding.yemeklerRv.adapter = yemeklerAdapter
        }

        binding.locationPinAnimation.pauseAnimation()

        binding.bottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> {
                    findNavController().navigate(R.id.anaSayfaFragment)
                    true
                }
                R.id.nav_cart -> {
                    findNavController().navigate(R.id.sepetgecis)
                    true
                }
                R.id.nav_profile -> {
                    findNavController().navigate(R.id.profilGecis)
                    true
                }
                else -> false
            }
        }

        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextChange(newText: String?): Boolean {
                viewModel.ara(newText ?: "")
                return true
            }

            override fun onQueryTextSubmit(query: String?): Boolean {
                viewModel.ara(query ?: "")
                return true
            }
        })

        return binding.root
    }

    override fun onResume() {
        super.onResume()
        viewModel.yemekleriYukle()
    }
}



