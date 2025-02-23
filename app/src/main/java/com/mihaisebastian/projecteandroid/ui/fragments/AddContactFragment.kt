package com.mihaisebastian.projecteandroid.ui.fragments

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.mihaisebastian.projecteandroid.R
import com.mihaisebastian.projecteandroid.data.AppDatabase
import com.mihaisebastian.projecteandroid.data.ContactEntity
import com.mihaisebastian.projecteandroid.databinding.FragmentAddContactBinding
import com.mihaisebastian.projecteandroid.ui.viewmodel.ContactViewModel
import com.mihaisebastian.projecteandroid.ui.viewmodel.ContactViewModelFactory

class AddContactFragment : Fragment(R.layout.fragment_add_contact) {

    private var _binding: FragmentAddContactBinding? = null
    private val binding get() = _binding!!
    private lateinit var contactViewModel: ContactViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentAddContactBinding.bind(view)

        setupViewModel()
        setupSaveButton()
    }

    private fun setupViewModel() {
        val contactDao = AppDatabase.getDatabase(requireContext()).contactDao()
        val factory = ContactViewModelFactory(contactDao)
        contactViewModel = ViewModelProvider(this, factory)[ContactViewModel::class.java]
    }

    private fun setupSaveButton() {
        binding.saveButton.setOnClickListener {
            val name = binding.nameEditText.text.toString().trim()
            val phone = binding.phoneEditText.text.toString().trim()
            val email = binding.emailEditText.text.toString().trim()

            if (validateInput(name, phone, email)) {
                val newContact = ContactEntity(
                    name = name,
                    phone = phone,
                    email = email
                )

                contactViewModel.insertContact(newContact)
                clearInputFields()
                showSuccessMessage()
            }
        }
    }

    private fun validateInput(name: String, phone: String, email: String): Boolean {
        return when {
            name.isEmpty() -> {
                binding.nameEditText.error = "Introdueix un nom"
                false
            }
            phone.isEmpty() -> {
                binding.phoneEditText.error = "Introdueix un telèfon"
                false
            }
            !isValidEmail(email) -> {
                binding.emailEditText.error = "Email invàlid"
                false
            }
            else -> true
        }
    }

    private fun isValidEmail(email: String): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    private fun clearInputFields() {
        binding.nameEditText.text?.clear()
        binding.phoneEditText.text?.clear()
        binding.emailEditText.text?.clear()
        binding.nameEditText.clearFocus()
        binding.phoneEditText.clearFocus()
        binding.emailEditText.clearFocus()
    }

    private fun showSuccessMessage() {
        Toast.makeText(
            requireContext(),
            "Contacte afegit correctament",
            Toast.LENGTH_SHORT
        ).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}