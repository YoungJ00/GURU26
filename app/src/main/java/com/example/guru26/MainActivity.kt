package com.example.guru26

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.tabs.TabLayout


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val bnv_main = findViewById<BottomNavigationView>(R.id.bnv_main)
        bnv_main.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.first -> {
                    val homeFragment = HomeFragment()
                    supportFragmentManager.beginTransaction().replace(R.id.fl_container, homeFragment).commit()
                }
                R.id.second -> {
                    val boardFragment = BoardFragment()
                    supportFragmentManager.beginTransaction().replace(R.id.fl_container, boardFragment).commit()
                }
                R.id.third -> {
                    val settingFragment = SettingFragment()
                    supportFragmentManager.beginTransaction().replace(R.id.fl_container, settingFragment).commit()
                }
            }
            true
        }

        bnv_main.selectedItemId = R.id.first
    }

}
