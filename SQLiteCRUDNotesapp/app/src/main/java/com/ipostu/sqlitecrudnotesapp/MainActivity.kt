package com.ipostu.sqlitecrudnotesapp

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.ClipboardManager
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.ipostu.sqlitecrudnotesapp.databinding.ActivityMainBinding
import com.ipostu.sqlitecrudnotesapp.databinding.RowBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private var listNotes = ArrayList<Note>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return null // TODO
    }

    private fun loadQuery(title: String) {
        val dbManager = DatabaseManager(this)
        val projections = arrayOf("ID", "Title", "Description")
        val selectionArgs = arrayOf(title)
        val cursor = dbManager.query(projections, "Title like ?", selectionArgs, "Title")

        if (cursor.moveToFirst()) {
            do {
                val ID = cursor.getInt(cursor.getColumnIndexOrThrow("ID"))
                val noteTitle = cursor.getString(cursor.getColumnIndexOrThrow("Title"))
                val description = cursor.getString(cursor.getColumnIndexOrThrow("Description"))

                listNotes.add(Note(ID, noteTitle, description))
            } while (cursor.moveToNext())
        }

        var myNotesAdapter = MyNotesAdapter(this, listNotes)
    }

    inner class MyNotesAdapter(
        private val context: Context,
        private val listNotes: ArrayList<Note>
    ) : BaseAdapter() {
        override fun getCount(): Int {
            return listNotes.size
        }

        override fun getItem(position: Int): Any {
            return listNotes[position]
        }

        override fun getItemId(position: Int): Long {
            return position.toLong()
        }

        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
            // inflate layout row.xml
            val binding = RowBinding.inflate(layoutInflater, parent, false)
            val myNote = listNotes[position]

            binding.titleView.text = myNote.noteName
            binding.descriptionView.text = myNote.noteDescription
            binding.deleteButton.setOnClickListener {
                val dbManager = DatabaseManager(context)
                val selectionArgs = arrayOf(myNote.noteId.toString())
                dbManager.delete("ID=?", selectionArgs)
                loadQuery("%")
            }
            binding.editButton.setOnClickListener {
                goToUpdate(myNote)
            }
            binding.copyButton.setOnClickListener {
                val title = binding.titleView.text.toString()
                val description = binding.descriptionView.text.toString()
                val s = title + "\n" + description
                val clipBoard = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                clipBoard.text = s
                Toast.makeText(this@MainActivity, "Copied...", Toast.LENGTH_SHORT).show()
            }
            binding.shareButton.setOnClickListener {
                val title = binding.titleView.text.toString()
                val description = binding.descriptionView.text.toString()
                val s = title + "\n" + description
                val shareIntent = Intent()
                shareIntent.action = Intent.ACTION_SEND
                shareIntent.type = "text/plain"
                shareIntent.putExtra(Intent.EXTRA_TEXT, s)
                startActivity(Intent.createChooser(shareIntent, s))
            }

            return binding.root
        }
    }

    private fun goToUpdate(note: Note) {
        val intent = Intent(this, AddNoteActivity::class.java)
        intent.putExtra("ID", note.noteId)
        intent.putExtra("name", note.noteName)
        intent.putExtra("description", note.noteDescription)
        startActivity(intent)
    }
}