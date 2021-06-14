package com.phoenixtechno.devicecontacts

import android.content.ContentUris
import android.content.ContentValues
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.ContactsContract
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast

/**
 * Created by Ullas on 06/14/21.
 */
class Addnewcontact : AppCompatActivity() {
    private var Name: EditText? = null
    private var Number: EditText? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_addnewcontact)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowHomeEnabled(true)
        supportActionBar!!.title = "Add New Contact"
        Name = findViewById<View>(R.id.name) as EditText
        Number = findViewById<View>(R.id.number) as EditText
        val savePhoneContactButton = findViewById<View>(R.id.save) as Button
        savePhoneContactButton.setOnClickListener {
            val addContactsUri = ContactsContract.Data.CONTENT_URI
            val rowContactId = rawContactId
            val displayName = Name!!.text.toString()
            insertContactDisplayName(addContactsUri, rowContactId, displayName)
            val phoneNumber = Number!!.text.toString()
            val phoneTypeStr = "Mobile"
            insertContactPhoneNumber(addContactsUri, rowContactId, phoneNumber, phoneTypeStr)
            Toast.makeText(applicationContext, "Contact Added Succesfully", Toast.LENGTH_LONG).show()
            finish()
        }
    }

    private val rawContactId: Long
        private get() {
            val contentValues = ContentValues()
            val rawContactUri = contentResolver.insert(ContactsContract.RawContacts.CONTENT_URI, contentValues)
            return ContentUris.parseId(rawContactUri!!)
        }

    private fun insertContactDisplayName(addContactsUri: Uri, rawContactId: Long, displayName: String) {
        val contentValues = ContentValues()
        contentValues.put(ContactsContract.Data.RAW_CONTACT_ID, rawContactId)
        contentValues.put(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE)
        contentValues.put(ContactsContract.CommonDataKinds.StructuredName.GIVEN_NAME, displayName)
        contentResolver.insert(addContactsUri, contentValues)
    }

    private fun insertContactPhoneNumber(addContactsUri: Uri, rawContactId: Long, phoneNumber: String, phoneTypeStr: String) {
        val contentValues = ContentValues()
        contentValues.put(ContactsContract.Data.RAW_CONTACT_ID, rawContactId)
        contentValues.put(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE)
        contentValues.put(ContactsContract.CommonDataKinds.Phone.NUMBER, phoneNumber)
        var phoneContactType = ContactsContract.CommonDataKinds.Phone.TYPE_HOME
        phoneContactType = ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE
        contentValues.put(ContactsContract.CommonDataKinds.Phone.TYPE, phoneContactType)
        contentResolver.insert(addContactsUri, contentValues)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
        }
        return super.onOptionsItemSelected(item)
    }
}