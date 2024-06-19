package com.ipostu.sqlitecrudnotesapp

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.text.ClipboardManager
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import com.ipostu.sqlitecrudnotesapp.databinding.ActivityMainBinding
import com.ipostu.sqlitecrudnotesapp.databinding.RowBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private var listNotes = ArrayList<Note>()
    private lateinit var sharePreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sharePreferences = this.getSharedPreferences("My_Data", MODE_PRIVATE)
        loadQuery("%")
    }

    override fun onResume() {
        loadQuery("%")
        super.onResume()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        val search = menu!!.findItem(R.id.app_bar_search)
        val searchView = search.actionView as SearchView
        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager

        searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                loadQuery("%$query%")
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                loadQuery("%$newText%")
                return false
            }

        })

        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.add_note -> {
                startActivity(Intent(this, AddNoteActivity::class.java))
            }

            R.id.action_sort -> {
                showSortDialog()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun showSortDialog() {
        val sortOptions =
            arrayOf("Newest", "Oldest", "Title(Ascending)", "Title(Descending)")
        val alertBuilder = AlertDialog.Builder(this)
        val sortingOptionString =
            sharePreferences.getString("Sort", OrderOption.TITLE_ASC.name) as String
        val sortOption = OrderOption.valueOf(sortingOptionString)

        var checkedItem = -1
        if (OrderOption.ID_DESC == sortOption) {
            checkedItem = 0;
        }
        if (OrderOption.ID_ASC == sortOption) {
            checkedItem = 1;
        }
        if (OrderOption.TITLE_ASC == sortOption) {
            checkedItem = 2;
        }
        if (OrderOption.TITLE_DESC == sortOption) {
            checkedItem = 3;
        }

        alertBuilder.setTitle("Sort by")
        alertBuilder.setIcon(R.drawable.ic_action_sort)
        alertBuilder.setSingleChoiceItems(sortOptions, checkedItem) { dialogInterface, i ->
            if (i == 0) {
                Toast.makeText(this, sortOptions[i], Toast.LENGTH_SHORT).show()
                val editor = sharePreferences.edit()
                editor.putString("Sort", OrderOption.ID_DESC.name)
                editor.apply()
                loadQuery("%")
            }
            if (i == 1) {
                Toast.makeText(this, sortOptions[i], Toast.LENGTH_SHORT).show()
                val editor = sharePreferences.edit()
                editor.putString("Sort", OrderOption.ID_ASC.name)
                editor.apply()
                loadQuery("%")
            }
            if (i == 2) {
                Toast.makeText(this, sortOptions[i], Toast.LENGTH_SHORT).show()
                val editor = sharePreferences.edit()
                editor.putString("Sort", OrderOption.TITLE_ASC.name)
                editor.apply()
                loadQuery("%")
            }
            if (i == 3) {
                Toast.makeText(this, sortOptions[i], Toast.LENGTH_SHORT).show()
                val editor = sharePreferences.edit()
                editor.putString("Sort", OrderOption.TITLE_DESC.name)
                editor.apply()
                loadQuery("%")
            }
            dialogInterface.dismiss()
        }

        alertBuilder.create().show()
    }

    private fun loadQuery(title: String) {
        val dbManager = DatabaseManager(this)
        val projections = arrayOf("ID", "Title", "Description")
        val selectionArgs = arrayOf(title)
        val sortingOptionString =
            sharePreferences.getString("Sort", OrderOption.TITLE_ASC.name) as String
        val sortOption = OrderOption.valueOf(sortingOptionString)
        val cursor = dbManager.query(
            projections, "Title like ?", selectionArgs,
            sortOption.columnName + " " + sortOption.sortOrder.value
        )

        listNotes.clear()
        if (cursor.moveToFirst()) {
            do {
                val ID = cursor.getInt(cursor.getColumnIndexOrThrow("ID"))
                val noteTitle = cursor.getString(cursor.getColumnIndexOrThrow("Title"))
                val description = cursor.getString(cursor.getColumnIndexOrThrow("Description"))

                listNotes.add(Note(ID, noteTitle, description))
            } while (cursor.moveToNext())
        }

        val myNotesAdapter = MyNotesAdapter(this, listNotes)
        binding.notesListView.adapter = myNotesAdapter

        val total = listNotes.size
        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.subtitle = "You have $total note(s) in the list..."
        }
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

    enum class OrderOption(val columnName: String, val sortOrder: SortOrder) {
        TITLE_DESC("Title", SortOrder.DESC),
        TITLE_ASC("Title", SortOrder.ASC),
        ID_DESC("ID", SortOrder.DESC),
        ID_ASC("Description", SortOrder.ASC);
    }

    enum class SortOrder(val value: String) {
        ASC("ASC"), DESC("DESC")
    }
}