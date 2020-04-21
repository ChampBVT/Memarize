package com.wireless.memarize.viewAdapter

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.wireless.memarize.R
import com.wireless.memarize.dataModel.Chapter
import com.wireless.memarize.pages.question.QuestionActivity


class ChapterRecyclerViewAdapter (val context: Context, private val chapterList: ArrayList<Chapter>) :

    RecyclerView.Adapter<ChapterRecyclerViewAdapter.ViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.chapter, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return chapterList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val chapter: Chapter = chapterList[position]
        val wordsL = chapter.wordsLearnt
        val wordsT = chapter.wordsTotal
        val learntText = "$wordsL/$wordsT Learned"
        holder.wordsL.text = learntText
        holder.title.text = chapter.title
        holder.img.setImageResource(chapter.src)
        holder.itemView.setOnClickListener {
            val intent = Intent(context, QuestionActivity::class.java)
            val words: HashMap<*, *> = chapter.words as HashMap<*, *>
            intent.putExtra("words", words);
            intent.putExtra("chapterTitle", chapter.title);
            context.startActivity(intent)
            (context as Activity).finish()
        }
    }

    class ViewHolder(item: View) : RecyclerView.ViewHolder(item){
        var title:TextView = item.findViewById(R.id.chapterTitle)
        var wordsL:TextView = item.findViewById(R.id.wordsLearnt)
        var img:ImageView = item.findViewById(R.id.chapterImg)
    }

}

