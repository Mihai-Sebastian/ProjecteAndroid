package com.mihaisebastian.projecteandroid.ui.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.mihaisebastian.projecteandroid.R
import com.mihaisebastian.projecteandroid.data.AppDatabase
import com.mihaisebastian.projecteandroid.databinding.FragmentContactListBinding
import com.mihaisebastian.projecteandroid.ui.adapter.ContactAdapter
import com.mihaisebastian.projecteandroid.ui.viewmodel.ContactViewModel
import com.mihaisebastian.projecteandroid.ui.viewmodel.ContactViewModelFactory

class ContactListFragment : Fragment(R.layout.fragment_contact_list) {

    private var _binding: FragmentContactListBinding? = null
    private val binding get() = _binding!!

    private lateinit var contactViewModel: ContactViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _binding = FragmentContactListBinding.bind(view)

        val contactDao = AppDatabase.getDatabase(requireContext()).contactDao()
        val viewModelFactory = ContactViewModelFactory(contactDao)
        contactViewModel = ViewModelProvider(this, viewModelFactory).get(ContactViewModel::class.java)

        // Configurar RecyclerView amb adaptador i clic als contactes
        val contactAdapter = ContactAdapter { contact ->
            val fragment = ShowContactFragment().apply {
                arguments = Bundle().apply {
                    putString("name", contact.name)
                    putString("phone", contact.phone)
                    putString("email", contact.email)
                }
            }
            requireActivity().supportFragmentManager.commit {
                replace(R.id.fragment_container, fragment)
                addToBackStack(null)
            }
        }

        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = contactAdapter
        }

        contactViewModel.allContacts.observe(viewLifecycleOwner) { contacts ->
            contactAdapter.submitList(contacts)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
