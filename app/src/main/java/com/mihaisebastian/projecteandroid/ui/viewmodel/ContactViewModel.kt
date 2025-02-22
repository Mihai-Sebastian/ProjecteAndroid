package com.mihaisebastian.projecteandroid.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mihaisebastian.projecteandroid.data.ContactEntity
import com.mihaisebastian.projecteandroid.data.ContactDao
import kotlinx.coroutines.launch

class ContactViewModel(private val contactDao: ContactDao) : ViewModel() {

    // Llista de tots els contactes (observable)
    val allContacts: LiveData<List<ContactEntity>> = contactDao.getAllContacts()

    // Funció per afegir un nou contacte
    fun insert(contact: ContactEntity) = viewModelScope.launch {
        contactDao.insert(contact)
    }

    // Funció per actualitzar un contacte existent
    fun update(contact: ContactEntity) = viewModelScope.launch {
        contactDao.update(contact)
    }

    // Funció per eliminar un contacte
    fun delete(contact: ContactEntity) = viewModelScope.launch {
        contactDao.delete(contact)
    }

}