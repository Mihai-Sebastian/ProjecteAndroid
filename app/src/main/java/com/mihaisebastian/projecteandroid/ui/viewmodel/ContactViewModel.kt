package com.mihaisebastian.projecteandroid.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mihaisebastian.projecteandroid.data.ContactDao
import com.mihaisebastian.projecteandroid.data.ContactEntity
import kotlinx.coroutines.launch

class ContactViewModel(private val contactDao: ContactDao) : ViewModel() {

    val allContacts: LiveData<List<ContactEntity>> = contactDao.getAllContacts()

    fun getContactById(id: Long): LiveData<ContactEntity> = contactDao.getContactById(id)

    fun insertContact(contact: ContactEntity) = viewModelScope.launch {
        contactDao.insert(contact)
    }

    fun updateContact(contact: ContactEntity) = viewModelScope.launch {
        contactDao.update(contact)
    }

    fun deleteContactById(id: Long) = viewModelScope.launch {
        contactDao.delete(id)
    }
}