package com.phoenixtechno.devicecontacts

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.ContactsContract
import android.view.*
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.util.ArrayList

/**
 * Created by Ullas on 06/14/21.
 */
class MainActivity : AppCompatActivity() {
    var contactsInfoList: MutableList<ContactsInfo>? = null
    var recyclerView: RecyclerView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        recyclerView = findViewById<View>(R.id.recyclerView) as RecyclerView
        val linearLayoutManager = LinearLayoutManager(applicationContext)
        recyclerView!!.layoutManager = linearLayoutManager
        contacts
    }

    private val contacts: Unit
        private get() {
            var contactId: String? = null
            var displayName: String? = null
            contactsInfoList = ArrayList()
            val cursor = contentResolver.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " ASC")
            if (cursor!!.count > 0) {
                while (cursor.moveToNext()) {

                    val contactsInfo = ContactsInfo()
                    contactId = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID))
                    displayName = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME))
                    contactsInfo.contactId = contactId
                    contactsInfo.displayName = displayName
                    val phoneCursor = contentResolver.query(
                            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                            null,
                            ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?", arrayOf(contactId),
                            ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " ASC")
                    if (phoneCursor!!.moveToNext()) {
                        val phoneNumber = phoneCursor.getString(phoneCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))
                        contactsInfo.phoneNumber = phoneNumber
                    }
                    phoneCursor.close()
                    (contactsInfoList as ArrayList<ContactsInfo>).add(contactsInfo)

                }
            }
            cursor.close()
            val adapter: Adapter = Adapter(contactsInfoList as ArrayList<ContactsInfo>)
            recyclerView!!.adapter = adapter
        }

    inner class ContactsInfo {
        var contactId: String? = null
        var displayName: String? = null
        var phoneNumber: String? = null
    }

    inner class Adapter(var contactsInfoList: ArrayList<*>) : RecyclerView.Adapter<Adapter.ViewHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.contact_info, parent, false)
            return ViewHolder(view)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            var model = ContactsInfo()
            model = contactsInfoList[position] as ContactsInfo
            holder.Name.text = model.displayName
            holder.Phonenumber.text = model.phoneNumber
        }

        override fun getItemCount(): Int {
            return contactsInfoList.size
        }

        inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            var Name: TextView
            var Phonenumber: TextView

            init {
                Name = view.findViewById<View>(R.id.displayName) as TextView
                Phonenumber = view.findViewById<View>(R.id.phoneNumber) as TextView
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.mainmenu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        if (item.itemId == android.R.id.home) {
            Toast.makeText(applicationContext, "Exit", Toast.LENGTH_LONG).show()
        }
        if (id == R.id.addnew) {
            val `in` = Intent(this@MainActivity, Addnewcontact::class.java)
            startActivity(`in`)
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onResume() {
        super.onResume()
        contacts
    }

    override fun onBackPressed() {}
}

