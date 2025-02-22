package com.mihaisebastian.projecteandroid

import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.mihaisebastian.projecteandroid.ui.fragments.AddContactFragment
import com.mihaisebastian.projecteandroid.ui.fragments.ContactListFragment

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val btnFragment1 = findViewById<Button>(R.id.btnFragment1)
        val btnFragment2 = findViewById<Button>(R.id.btnFragment2)

        val fragmentContactList = ContactListFragment()
        val fragmentAddContact = AddContactFragment()

        supportFragmentManager.beginTransaction().apply {
            replace(R.id.fragment_container, fragmentContactList)
            commit()
        }

        btnFragment1.setOnClickListener {
            supportFragmentManager.beginTransaction().apply {
                replace(R.id.fragment_container, fragmentAddContact)
                commit()
            }
        }

        btnFragment2.setOnClickListener {
            supportFragmentManager.beginTransaction().apply {
                replace(R.id.fragment_container, fragmentContactList)
                commit()
            }
        }
    }
}
