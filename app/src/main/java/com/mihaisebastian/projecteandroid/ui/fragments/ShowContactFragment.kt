package com.mihaisebastian.projecteandroid.ui.fragments

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.mihaisebastian.projecteandroid.R
import com.mihaisebastian.projecteandroid.data.AppDatabase
import com.mihaisebastian.projecteandroid.databinding.FragmentShowContactBinding
import com.mihaisebastian.projecteandroid.ui.viewmodel.ContactViewModel
import com.mihaisebastian.projecteandroid.ui.viewmodel.ContactViewModelFactory
import kotlinx.coroutines.launch

class ShowContactFragment : Fragment(R.layout.fragment_show_contact) {

    private var _binding: FragmentShowContactBinding? = null
    private val binding get() = _binding!!
    private lateinit var contactViewModel: ContactViewModel
    private var contactId: Long = 0L

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentShowContactBinding.bind(view)

        setupViewModel()
        loadContact()
        setupButtons()
    }

    private fun setupViewModel() {
        val contactDao = AppDatabase.getDatabase(requireContext()).contactDao()
        val factory = ContactViewModelFactory(contactDao)
        contactViewModel = ViewModelProvider(this, factory)[ContactViewModel::class.java]
    }

    private fun loadContact() {
        contactId = arguments?.getLong("id") ?: 0L
        if (contactId == 0L) showErrorAndExit("Contacte no trobat")

        contactViewModel.getContactById(contactId).observe(viewLifecycleOwner) { contact ->
            contact?.let {
                binding.contactNameTextView.text = it.name
                binding.contactPhoneTextView.text = it.phone
                binding.contactEmailTextView.text = it.email
            } ?: showErrorAndExit("Contacte eliminat")
        }
    }

    private fun setupButtons() {
        // Botó Eliminar
        binding.deleteButton.setOnClickListener {
            lifecycleScope.launch {
                try {
                    contactViewModel.deleteContactById(contactId)
                    Toast.makeText(requireContext(), "Contacte eliminat", Toast.LENGTH_SHORT).show()
                    parentFragmentManager.popBackStack()
                } catch (e: Exception) {
                    Toast.makeText(requireContext(), "Error en eliminar: ${e.message}", Toast.LENGTH_LONG).show()
                }
            }
        }

        // Botó Tornar Enrere
        binding.backButton.setOnClickListener {
            parentFragmentManager.popBackStack()
        }

        // Botó Editar
        binding.editButton.setOnClickListener {
            val editFragment = EditContactFragment().apply {
                arguments = Bundle().apply { putLong("id", contactId) }
            }

            parentFragmentManager.commit {
                replace(R.id.fragment_container, editFragment)
                addToBackStack("show_to_edit")
                setReorderingAllowed(true)
            }
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