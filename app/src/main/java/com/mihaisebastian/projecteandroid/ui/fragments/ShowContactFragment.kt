package com.mihaisebastian.projecteandroid.ui.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.fragment.app.commit
import com.mihaisebastian.projecteandroid.R
import com.mihaisebastian.projecteandroid.data.AppDatabase
import com.mihaisebastian.projecteandroid.data.ContactEntity
import com.mihaisebastian.projecteandroid.databinding.FragmentShowContactBinding
import com.mihaisebastian.projecteandroid.ui.viewmodel.ContactViewModel
import com.mihaisebastian.projecteandroid.ui.viewmodel.ContactViewModelFactory

class ShowContactFragment : Fragment(R.layout.fragment_show_contact) {

    private var _binding: FragmentShowContactBinding? = null
    private val binding get() = _binding!!
    private lateinit var contactViewModel: ContactViewModel
    private var contactId: Int? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _binding = FragmentShowContactBinding.bind(view)

        val contactDao = AppDatabase.getDatabase(requireContext()).contactDao()
        val viewModelFactory = ContactViewModelFactory(contactDao)
        contactViewModel = ViewModelProvider(this, viewModelFactory).get(ContactViewModel::class.java)

        contactId = arguments?.getInt("id")
        val name = arguments?.getString("name")
        val phone = arguments?.getString("phone")
        val email = arguments?.getString("email")

        binding.contactNameTextView.text = name
        binding.contactPhoneTextView.text = phone
        binding.contactEmailTextView.text = email

        binding.deleteButton.setOnClickListener {
            val contactId = arguments?.getInt("contactId")?.toLong() ?: 0L

            contactId?.let { id ->
                val contact = ContactEntity(id, name ?: "", phone ?: "", email ?: "")
                contactViewModel.delete(contact)

                requireActivity().supportFragmentManager.popBackStack()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
