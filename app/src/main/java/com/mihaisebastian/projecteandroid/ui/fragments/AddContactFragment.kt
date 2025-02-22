package com.mihaisebastian.projecteandroid.ui.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.mihaisebastian.projecteandroid.R
import com.mihaisebastian.projecteandroid.data.ContactEntity
import com.mihaisebastian.projecteandroid.data.AppDatabase
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

        val contactDao = AppDatabase.getDatabase(requireContext()).contactDao()
        val viewModelFactory = ContactViewModelFactory(contactDao)
        contactViewModel = ViewModelProvider(this, viewModelFactory).get(ContactViewModel::class.java)

        binding.saveButton.setOnClickListener {
            val name = binding.nameEditText.text.toString()
            val phone = binding.phoneEditText.text.toString()
            val email = binding.emailEditText.text.toString()

            if (name.isNotEmpty() && phone.isNotEmpty() && email.isNotEmpty()) {
                val newContact = ContactEntity(
                    name = name,
                    phone = phone,
                    email = email
                )

                // Afegir el contacte a la base de dades
                contactViewModel.insert(newContact)

                // Netejar els camps d'entrada
                binding.nameEditText.text.clear()
                binding.phoneEditText.text.clear()
                binding.emailEditText.text.clear()

                // Tornar a la llista de contactes
                requireActivity().supportFragmentManager.popBackStack()
            } else {
                android.widget.Toast.makeText(
                    requireContext(),
                    "Si us plau, omple tots els camps",
                    android.widget.Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
