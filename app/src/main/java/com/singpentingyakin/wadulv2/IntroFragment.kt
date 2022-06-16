package com.singpentingyakin.wadulv2

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_intro.*

class IntroFragment : Fragment() {
    var pageTitle : String = ""
    var pageContent : String = ""


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_intro, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        fragment_title.text = pageTitle
        fragment_content.text = pageContent
    }

    fun setData(title : String, content : String){
        pageTitle = title
        pageContent = content
    }
}