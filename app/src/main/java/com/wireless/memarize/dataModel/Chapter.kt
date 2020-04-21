package com.wireless.memarize.dataModel

data class Chapter(val title:String, val wordsLearnt: Int, val wordsTotal: Int, val src: Int, val words: Map<String, String>)  {
}