package com.example.yemekuygulamasi.ui.fragment

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.navigation.fragment.findNavController
import com.example.yemekuygulamasi.R
import com.example.yemekuygulamasi.databinding.FragmentProfilBinding
import com.example.yemekuygulamasi.ui.viewmodel.ProfilViewModel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class ProfilFragment : Fragment() {

    private lateinit var binding: FragmentProfilBinding
    private lateinit var viewModel: ProfilViewModel
    private val PREFS_NAME = "profil_prefs"
    private val KEY_AD = "key_ad"
    private val KEY_TEL = "key_tel"
    private val KEY_IMAGE_URI = "key_image_uri"

    private val imagePickerLauncher = registerForActivityResult(ActivityResultContracts.OpenDocument()) { uri: Uri? ->
        uri?.let {
            val takeFlags: Int = Intent.FLAG_GRANT_READ_URI_PERMISSION
            requireContext().contentResolver.takePersistableUriPermission(it, takeFlags)

            binding.imageViewProfil.setImageURI(it)
            requireContext().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
                .edit()
                .putString(KEY_IMAGE_URI, it.toString())
                .apply()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentProfilBinding.inflate(inflater, container, false)

        binding.imageButtonProfilSil.setOnClickListener { view ->
            Snackbar.make(view, "Profil resminizi silmek istiyor musunuz?", Snackbar.LENGTH_LONG)
                .setAction("EVET") {
                    val sharedPrefs = requireContext().getSharedPreferences("profil_prefs", Context.MODE_PRIVATE)
                    sharedPrefs.edit().remove("key_image_uri").apply()

                    binding.imageViewProfil.setImageResource(R.drawable.profil_resim)
                }
                .setActionTextColor(Color.WHITE)
                .addCallback(object : Snackbar.Callback() {
                    override fun onDismissed(transientBottomBar: Snackbar?, event: Int) {

                    }
                })
                .show()
        }

        binding.bottomNavigationDonus.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> {
                    findNavController().navigate(R.id.anaSayfaFragment)
                    true
                }

                else -> false
            }
        }

        val sharedPrefs = requireContext().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val kayitliAd = sharedPrefs.getString(KEY_AD, "")
        val kayitliTel = sharedPrefs.getString(KEY_TEL, "")
        val kayitliResimUri = sharedPrefs.getString(KEY_IMAGE_URI, null)

        binding.textViewKisiAdi.text = kayitliAd
        binding.textViewTelNo.text = kayitliTel
        binding.textViewKisiAdi2.text = kayitliAd

        kayitliResimUri?.let {
            binding.imageViewProfil.setImageURI(Uri.parse(it))
        }




        binding.buttonProfilDuzenle.setOnClickListener {
            val dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_profil_duzenle, null)

            val adEditText = dialogView.findViewById<EditText>(R.id.etAd)
            val telEditText = dialogView.findViewById<EditText>(R.id.etTel)
            val btnResimYukle = dialogView.findViewById<Button>(R.id.btnProfilResmiYukle)

            adEditText.setText(binding.textViewKisiAdi.text)
            telEditText.setText(binding.textViewTelNo.text)

            btnResimYukle.setOnClickListener {
                imagePickerLauncher.launch(arrayOf("image/*"))
            }

            AlertDialog.Builder(requireContext())
                .setTitle("Profili Düzenle")
                .setView(dialogView)
                .setPositiveButton("Kaydet") { _, _ ->
                    val ad = adEditText.text.toString()
                    val tel = telEditText.text.toString()

                    binding.textViewKisiAdi.text = ad
                    binding.textViewTelNo.text = tel
                    binding.textViewKisiAdi2.text = ad

                    requireContext().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
                        .edit()
                        .putString(KEY_AD, ad)
                        .putString(KEY_TEL, tel)
                        .apply()
                }
                .setNegativeButton("İptal", null)
                .show()
        }

        return binding.root
    }
}
