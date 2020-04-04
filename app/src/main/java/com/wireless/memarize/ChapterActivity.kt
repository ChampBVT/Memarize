package com.wireless.memarize

import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class ChapterActivity : AppCompatActivity() {



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chapter)

        val chapterRecyclerView: RecyclerView = findViewById(R.id.chapterRecyclerView)
        chapterRecyclerView.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)

        val chapter = ArrayList<Chapter>()

        chapter.add(Chapter("Chapter 1", 10, 30, getResourceByName("chapter1", "drawable")))
        chapter.add(Chapter("Chapter 2", 20, 40, getResourceByName("chapter2", "drawable")))
        chapter.add(Chapter("Chapter 3", 30, 999, getResourceByName("chapter3", "drawable")))
        val adapter = ChapterRecyclerViewAdapter(this, chapter)
        chapterRecyclerView.adapter = adapter
    }

    private fun getResourceByName(name: String, type: String): Int {
        val id = resources.getIdentifier(name , type, packageName);
        return id
    }
}