package com.singpentingyakin.wadulv2

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.ViewPager
import kotlinx.android.synthetic.main.activity_intro.*

class IntroActivity : AppCompatActivity() {

    val fragment1 = IntroFragment()
    val fragment2 = IntroFragment()
    val fragment3 = IntroFragment()
    lateinit var adapter : myPagerAdapter
    lateinit var activity : Activity
    lateinit var preference : SharedPreferences
    val pref_show_intro = "Intro"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_intro)
        supportActionBar?.hide()
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        activity = this
        preference = getSharedPreferences("IntroSlider" , Context.MODE_PRIVATE)

        if(!preference.getBoolean(pref_show_intro,true)){
            startActivity(Intent(activity, LoginActivity::class.java))
            finish()
        }
        fragment1.setData("Selamat Datang", "Lorem ipsum dolor sit amet")
        fragment2.setData("Di Wadul", "Consectetur adispiscing elit")
        fragment3.setData("Test", "Itulah isinya lorem ipsum")

//        val imageView = findViewById<ImageView>(R.id.imageView)
//        imageView.setImageResource(R.drawable.ic_wadul)

        adapter = myPagerAdapter(supportFragmentManager)
        adapter.list.add(fragment1)
        adapter.list.add(fragment2)
        adapter.list.add(fragment3)

        view_pager.adapter = adapter
        btn_next.setOnClickListener {
            view_pager.currentItem++
        }

        btn_skip.setOnClickListener { goToDashboard() }

        view_pager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener{
            override fun onPageScrollStateChanged(state: Int) {
            }

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
            }

            override fun onPageSelected(position: Int) {
                if(position == adapter.list.size-1){
                    //lastPage
                    btn_next.text = "MENGERTI"
                    btn_next.setOnClickListener {
                        goToDashboard()
                    }
                }else{
                    //has next
                    btn_next.text = "MAJU"
                    btn_next.setOnClickListener {
                        view_pager.currentItem++
                    }
                }

                when(view_pager.currentItem){
                    0->{
                        indicator1.setTextColor(Color.BLACK)
                        indicator2.setTextColor(Color.GRAY)
                        indicator3.setTextColor(Color.GRAY)
                    }
                    1->{
                        indicator1.setTextColor(Color.GRAY)
                        indicator2.setTextColor(Color.BLACK)
                        indicator3.setTextColor(Color.GRAY)
                    }
                    2->{
                        indicator1.setTextColor(Color.GRAY)
                        indicator2.setTextColor(Color.GRAY)
                        indicator3.setTextColor(Color.BLACK)
                    }
                }

            }

        })

    }

    fun goToDashboard(){
        startActivity(Intent(activity, LoginActivity::class.java))
        finish()
//        val editor = preference.edit()
//        editor.putBoolean(pref_show_intro,false)
//        editor.apply()
    }

    class myPagerAdapter(manager : FragmentManager) : FragmentPagerAdapter(manager){

        val list : MutableList<Fragment> = ArrayList()

        override fun getItem(position: Int): Fragment {
            return list[position]
        }

        override fun getCount(): Int {
            return list.size
        }

    }
}
