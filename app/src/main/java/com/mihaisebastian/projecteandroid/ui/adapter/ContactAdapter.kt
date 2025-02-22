package com.mihaisebastian.projecteandroid.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.mihaisebastian.projecteandroid.R
import com.mihaisebastian.projecteandroid.data.ContactEntity

class ContactAdapter(private val onContactClick: (ContactEntity) -> Unit) :
    ListAdapter<ContactEntity, ContactAdapter.ContactViewHolder>(ContactDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_contact, parent, false)
        return ContactViewHolder(view, onContactClick)
    }

    override fun onBindViewHolder(holder: ContactViewHolder, position: Int) {
        val contact = getItem(position)
        holder.bind(contact)
    }

    class ContactViewHolder(itemView: View, private val onContactClick: (ContactEntity) -> Unit) :
        RecyclerView.ViewHolder(itemView) {
        private val nameTextView: TextView = itemView.findViewById(R.id.nameTextView)
        private val phoneTextView: TextView = itemView.findViewById(R.id.phoneTextView)
        private val emailTextView: TextView = itemView.findViewById(R.id.emailTextView)

        fun bind(contact: ContactEntity) {
            nameTextView.text = contact.name
            phoneTextView.text = contact.phone
            emailTextView.text = contact.email

            itemView.setOnClickListener { onContactClick(contact) }
        }
    }

    class ContactDiffCallback : DiffUtil.ItemCallback<ContactEntity>() {
        override fun areItemsTheSame(oldItem: ContactEntity, newItem: ContactEntity): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: ContactEntity, newItem: ContactEntity): Boolean {
            return oldItem == newItem
        }
    }
}
