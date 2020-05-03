package com.wireless.memarize.utils

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.res.Configuration
import android.graphics.Typeface
import android.os.Build
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat.recreate
import androidx.core.content.res.ResourcesCompat
import com.wireless.memarize.R
import java.util.*

@RequiresApi(Build.VERSION_CODES.O)
fun displayChangeLanguage(context : Context, activity: Activity) {
    val listLang = arrayOf("English", "Thai")
    val font: Typeface = ResourcesCompat.getFont(context, R.font.prompt_bold) as Typeface
    val textView = TextView(context)
    textView.text = context.getString(R.string.Select_Language)
    textView.typeface = font
    textView.setTextColor(context.getColor(R.color.black))
    textView.textSize = 20F
    textView.setPadding(50,50,0,0);
    val mBuilder = AlertDialog.Builder(context)
    mBuilder.setCustomTitle(textView)
    mBuilder.setSingleChoiceItems(listLang, -1)
    { dialog, which ->
        if (which == 0) {
            setLocate("en", context)
            recreate(activity)
        } else {
            setLocate("th", context)
            recreate(activity)
        }
        dialog.dismiss()
    }
    val mDialog = mBuilder.create()
    mDialog.show()
}

fun setLocate(language: String, context : Context){
    val locale = Locale(language)
    Locale.setDefault(locale)
    val config = Configuration(context.resources.configuration)
    config.setLocale(locale)
    //context.createConfigurationContext(config)
    context.resources.updateConfiguration(config, context.resources.displayMetrics)
    setEncryptedSharePreferencesString("language", language, context)
}

fun loadLocate(context : Context) {
    setLocate(getEncryptedSharePreferencesString("language", context), context)
}