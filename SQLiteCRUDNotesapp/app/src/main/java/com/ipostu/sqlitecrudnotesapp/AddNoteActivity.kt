package com.ipostu.sqlitecrudnotesapp

import android.content.ContentValues
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.ipostu.sqlitecrudnotesapp.databinding.ActivityAddNoteBinding
import com.ipostu.sqlitecrudnotesapp.databinding.ActivityMainBinding

class AddNoteActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddNoteBinding

    var dbTable = "Notes"
    var id = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddNoteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        try {
            val bundle: Bundle? = intent.extras
            id = bundle!!.getInt("ID", 0)
            if (id != 0) {

                supportActionBar!!.title = "Update Note"
                binding.addButton.text = "Update"

                binding.titleEditText.setText(bundle.getString("name"))
                binding.descriptionEditText.setText(bundle.getString("description"))
            }

        } catch (e: Exception) {
            Log.i(
                AddNoteActivity::class.java.simpleName,
                "Was not possible to extract data from intent, using defaults"
            )
        }

        binding.addButton.setOnClickListener {
            addFunc(it)
        }
    }

    fun addFunc(view: View) {
        val dbManager = DatabaseManager(this)
        val values = ContentValues()
        values.put("Title", binding.titleEditText.text.toString())
        values.put("Description", binding.descriptionEditText.text.toString())

        if (id == 0) {
            val ID = dbManager.insert(values)
            if (ID > 0) {
                Toast.makeText(this, "Note is updated", Toast.LENGTH_SHORT).show()
                finish()
            } else {
                Toast.makeText(this, "Error updating note", Toast.LENGTH_SHORT).show()
            }
        } else {
            val selectionArgs = arrayOf(id.toString())
            val ID = dbManager.update(values, "ID=?", selectionArgs)
            if (ID > 0) {
                Toast.makeText(this, "Note is added", Toast.LENGTH_SHORT).show()
                finish()
            } else {
                Toast.makeText(this, "Error adding note", Toast.LENGTH_SHORT).show()
            }
        }
    }
}