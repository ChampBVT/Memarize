package com.wireless.memarize.pages.play

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.wireless.memarize.R
import com.wireless.memarize.dataModel.Chapter
import com.wireless.memarize.utils.*
import com.wireless.memarize.viewAdapter.ChapterRecyclerViewAdapter
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap


class ChapterActivity : AppCompatActivity() {

    private lateinit var database: FirebaseDatabase
    private lateinit var changeLanguageBtn : Button
    private lateinit var backButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        loadLocate(this)
        setContentView(R.layout.activity_chapter)
        database = FirebaseDatabase.getInstance()
        changeLanguageBtn = findViewById(R.id.changeLanguage)
        backButton = findViewById(R.id.back)

        getChapters(this)
        changeLanguageBtn.setOnClickListener {
            displayChangeLanguage(this, this)
        }
        backButton.setOnClickListener{
            onBackPressed();
        }
    }
    private fun getResourceByName(name: String, type: String): Int {
        return resources.getIdentifier(name , type, packageName)
    }

    private fun createChapter(){
        val chapters = ArrayList<Chapter>()
        val words = mutableMapOf<String,String>()
        for(x in 0 until 10)
            words["ans$x"] = "values$x"
        for(x in 0 until 10)
            chapters.add(Chapter("chapter $x", 10, getResourceByName("chapter1", "drawable"), words))
        val databaseRef = database.reference
        for(chapter in chapters){
            databaseRef.child("chapters").child(chapter.title).setValue(chapter.words)
                .addOnCompleteListener(this, OnCompleteListener { task ->
                    Toast.makeText(
                        this, "Failed: " + (task.exception?.message
                            ?: "No error message"), Toast.LENGTH_SHORT
                    ).show();
                });
        }
    }

    private fun getChapters(context : Context) {
        val chapterList = ArrayList<Chapter>()
        val database = FirebaseDatabase.getInstance().reference
        database.child("chapters").addListenerForSingleValueEvent(
            object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    val chapters : HashMap<*,*> = dataSnapshot.value as HashMap<*, *>
                    val sortedChapters: TreeMap<*, *> = TreeMap(chapters)
                    for ((chapterName, wordsList ) in sortedChapters ) {
                        val words = wordsList as Map<String, String>
                        chapterList.add(Chapter("$chapterName", words.size,
                            getResourceByName("chapter"+chapterName.toString().split(" ")[1], "drawable"),
                            words
                        ))
                    }
                    val chapterRecyclerView: RecyclerView = findViewById(R.id.chapterRecyclerView)
                    chapterRecyclerView.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
                    val adapter = ChapterRecyclerViewAdapter(context, chapterList)
                    chapterRecyclerView.adapter = adapter
                }
                override fun onCancelled(databaseError: DatabaseError) {
                    Log.e("RealtimeDatabase", "loadPost:onCancelled", databaseError.toException())
                }
            })
    }
}