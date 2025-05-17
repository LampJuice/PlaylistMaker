package com.example.playlistmaker

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.text.set

class SearchActivity : AppCompatActivity() {
    private var searchString: String = STRING_DEF
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_search)

        val backButton = findViewById<ImageView>(R.id.search_back)
        val editText = findViewById<EditText>(R.id.search_edittext)
        val clearButton = findViewById<ImageView>(R.id.clear_text)


        backButton.setOnClickListener { finish() }
        editText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s.isNullOrEmpty()) clearButton.visibility =
                    View.INVISIBLE else clearButton.visibility = View.VISIBLE
                searchString = s.toString()
            }

            override fun afterTextChanged(s: Editable?) {
            }

        })

        val inputMethodManager =
            getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager

        clearButton.setOnClickListener {
            editText.text?.clear()
            inputMethodManager?.hideSoftInputFromWindow(editText.windowToken, 0)
            editText.clearFocus()
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        val editText = findViewById<EditText>(R.id.search_edittext)
        super.onSaveInstanceState(outState)
        outState.putString(SEARCH_STRING, editText.text.toString())
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        val editText = findViewById<EditText>(R.id.search_edittext)

        searchString = savedInstanceState.getString(SEARCH_STRING, STRING_DEF)
        editText.setText(searchString)
    }

    companion object{
        private const val SEARCH_STRING = "SEARCH_STRING"
        private const val STRING_DEF = ""
    }

}