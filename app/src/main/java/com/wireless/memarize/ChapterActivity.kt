package com.wireless.memarize

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_chapter.*

class ChapterActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_store)

        val ChapterRecyclerView: RecyclerView = findViewById(R.id.chapterRecyclerView)
        chapterRecyclerView.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)

        val chapter = ArrayList<Chapter>()

        chapter.add(Chapter("Chapter 1", 10))
        chapter.add(Chapter("Chapter 2", 20))
        chapter.add(Chapter("Chapter 3", 30))

        val adapter = ChapterRecyclerViewAdapter(chapter)
        chapterRecyclerView.adapter = adapter
    }
}