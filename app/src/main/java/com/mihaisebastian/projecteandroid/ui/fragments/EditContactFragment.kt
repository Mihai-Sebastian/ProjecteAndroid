package com.mihaisebastian.projecteandroid.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.commit
import androidx.lifecycle.ViewModelProvider
import com.mihaisebastian.projecteandroid.R
import com.mihaisebastian.projecteandroid.data.AppDatabase
import com.mihaisebastian.projecteandroid.data.ContactEntity
import com.mihaisebastian.projecteandroid.databinding.FragmentEditContactBinding
import com.mihaisebastian.projecteandroid.ui.viewmodel.ContactViewModel
import com.mihaisebastian.projecteandroid.ui.viewmodel.ContactViewModelFactory

class EditContactFragment : Fragment(R.layout.fragment_edit_contact) {

    private var _binding: FragmentEditContactBinding? = null
    private val binding get() = _binding!!
    private lateinit var contactViewModel: ContactViewModel
    private var contactId: Long = 0L

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentEditContactBinding.bind(view)

        setupViewModel()
        setupUI()
        loadContact()
        setupSaveButton()
    }

    private fun setupViewModel() {
        val contactDao = AppDatabase.getDatabase(requireContext()).contactDao()
        val factory = ContactViewModelFactory(contactDao)
        contactViewModel = ViewModelProvider(this, factory)[ContactViewModel::class.java]
    }

    private fun setupUI() {
        binding.nameEditText.isEnabled = true
        binding.nameEditText.isFocusable = true
        binding.nameEditText.isFocusableInTouchMode = true

        Log.d("EditContactFragment", "nameEditText enabled: ${binding.nameEditText.isEnabled}, focusable: ${binding.nameEditText.isFocusable}")
    }

    private fun loadContact() {
        contactId = arguments?.getLong("id") ?: 0L
        if (contactId == 0L) showErrorAndExit("ID invàlid")

        contactViewModel.getContactById(contactId).observe(viewLifecycleOwner) { contact ->
            contact?.let {
                binding.nameEditText.setText(it.name ?: "")
                binding.phoneEditText.setText(it.phone ?: "")
                binding.emailEditText.setText(it.email ?: "")
            } ?: showErrorAndExit("Contacte no trobat")
        }
    }

    private fun setupSaveButton() {
        binding.saveButton.setOnClickListener {
            val newName = binding.nameEditText.text.toString().trim()
            val newPhone = binding.phoneEditText.text.toString().trim()
            val newEmail = binding.emailEditText.text.toString().trim()

            if (validateInput(newName, newPhone, newEmail)) {
                val updatedContact = ContactEntity(
                    id = contactId,
                    name = newName,
                    phone = newPhone,
                    email = newEmail
                )

                contactViewModel.updateContact(updatedContact)
                Toast.makeText(requireContext(), "Canvis guardats", Toast.LENGTH_SHORT).show()

                parentFragmentManager.popBackStack("show_to_edit", FragmentManager.POP_BACK_STACK_INCLUSIVE)
            }
        }
    }

    private fun validateInput(name: String, phone: String, email: String): Boolean {
        return when {
            name.isEmpty() -> {
                binding.nameEditText.error = "El nom és obligatori"
                false
            }
            phone.isEmpty() -> {
                binding.phoneEditText.error = "El telèfon és obligatori"
                false
            }
            !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches() -> {
                binding.emailEditText.error = "Format d'email invàlid"
                false
            }
            else -> true
        }
    }

    private fun showErrorAndExit(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
        parentFragmentManager.popBackStack()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
