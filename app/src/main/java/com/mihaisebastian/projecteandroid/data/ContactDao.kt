package com.mihaisebastian.projecteandroid.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import androidx.room.Delete
import androidx.lifecycle.LiveData

@Dao
interface ContactDao {

    @Insert
    suspend fun insert(contact: ContactEntity)

    @Update
    suspend fun update(contact: ContactEntity)

    @Delete
    suspend fun delete(contact: ContactEntity)

    @Query("SELECT * FROM contacts ORDER BY name ASC")
    fun getAllContacts(): LiveData<List<ContactEntity>>

    @Query("SELECT * FROM contacts WHERE id = :contactId")
    fun getContactById(contactId: Long): LiveData<ContactEntity>
}
