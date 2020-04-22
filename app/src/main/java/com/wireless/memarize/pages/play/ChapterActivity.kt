package com.wireless.memarize.pages.play

import android.content.Context
import android.os.Bundle
import android.util.Log
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
import com.wireless.memarize.viewAdapter.ChapterRecyclerViewAdapter
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap


class ChapterActivity : AppCompatActivity() {

    private lateinit var database: FirebaseDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chapter)
        database = FirebaseDatabase.getInstance()
        getChapters(this)
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
            chapters.add(Chapter("chapter $x", 10, 30, getResourceByName("chapter1", "drawable"), words))
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
        //setEncryptedSharePreferences(name, email, uid, petName, petType, 0)
    }

    private fun getChapters(context : Context) {
        val chapterList = ArrayList<Chapter>()
        val database = FirebaseDatabase.getInstance().reference
        database.child("chapters").addListenerForSingleValueEvent(
            object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    val chapters : HashMap<*,*> = dataSnapshot.value as HashMap<*, *>
                    val sortedChapters: TreeMap<*, *> = TreeMap(chapters)
                    for ((chapterName, words) in sortedChapters ) {
                        chapterList.add(Chapter("$chapterName", 10, 30, getResourceByName("chapter1", "drawable"),
                            words as Map<String, String>
                        ))
                       //Log.e(this.toString(), "$chapterList")
                    }
                    val chapterRecyclerView: RecyclerView = findViewById(R.id.chapterRecyclerView)
                    chapterRecyclerView.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
                    val adapter = ChapterRecyclerViewAdapter(context, chapterList)
                    chapterRecyclerView.adapter = adapter

                }
                override fun onCancelled(databaseError: DatabaseError) {
                    Log.w("TAG", "loadPost:onCancelled", databaseError.toException())
                }
            })
    }
}