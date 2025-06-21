package com.example.playlistmaker

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import retrofit2.Call
import retrofit2.Response



class SearchActivity : AppCompatActivity() {
    var searchString: String = STRING_DEF
    private var lastSearch: String?= null

    private val songs = mutableListOf<Song>()
    private val trackAdapter = TrackAdapter(songs)
    private lateinit var noNetworkPlaceholder: LinearLayout
    private lateinit var noResultPlaceholder: LinearLayout
    private lateinit var recyclerView: RecyclerView


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


        noResultPlaceholder = findViewById<LinearLayout>(R.id.noResultsPlaceholder)
        noNetworkPlaceholder = findViewById<LinearLayout>(R.id.noNetworkPlaceholder)
        val refreshButton = findViewById<Button>(R.id.renew_button)
        val inputMethodManager =
            getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager

        clearButton.setOnClickListener {
            editText.text?.clear()
            songs.clear()
            trackAdapter.notifyDataSetChanged()
            noResultPlaceholder.visibility = View.GONE
            noNetworkPlaceholder.visibility = View.GONE
            inputMethodManager?.hideSoftInputFromWindow(editText.windowToken, 0)
            editText.clearFocus()
        }

        recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = trackAdapter



        refreshButton.setOnClickListener {
            noNetworkPlaceholder.visibility = View.GONE
            lastSearch?.let { query -> performSearch(query) }
        }

        editText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE){
                noResultPlaceholder.visibility = View.GONE
                noNetworkPlaceholder.visibility = View.GONE
                val query = editText.text.toString()
                lastSearch = query
                performSearch(query)

                true
            }
            false
        }


    }

    private fun performSearch(query: String){
        RetrofitSettings.iTunesAPI.searchSong(query).enqueue(object : retrofit2.Callback<SearchResponse> {
            override fun onResponse(call : Call<SearchResponse>, response : Response<SearchResponse>){
                if (response.isSuccessful) {
                    val newSongs = response.body()?.results.orEmpty()
                    songs.clear()
                    songs.addAll(newSongs)
                    trackAdapter.notifyDataSetChanged()
                    if (songs.isEmpty()) {
                        noResultPlaceholder.visibility = View.VISIBLE
                        recyclerView.visibility = View.GONE
                    } else {
                        noResultPlaceholder.visibility = View.GONE
                        recyclerView.visibility = View.VISIBLE
                    }

                } else {
                    recyclerView.visibility = View.GONE
                    noResultPlaceholder.visibility = View.GONE
                    noNetworkPlaceholder.visibility = View.VISIBLE
                }
            }

            override fun onFailure(call: Call<SearchResponse>, t: Throwable) {
                t.printStackTrace()
                recyclerView.visibility = View.GONE
                noResultPlaceholder.visibility = View.GONE
                noNetworkPlaceholder.visibility = View.VISIBLE
            }
        })
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