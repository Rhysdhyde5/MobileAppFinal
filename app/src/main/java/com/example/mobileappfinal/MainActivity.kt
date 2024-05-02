package com.example.mobileappfinal

import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Adapter
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationBarView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)


        supportFragmentManager.beginTransaction().apply {
            replace(R.id.FrameLayout, List())
            commit()
        }

        val navBar = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        navBar.setOnItemSelectedListener  { item ->
            when(item.itemId) {
                R.id.item_1 -> {
                    supportFragmentManager.beginTransaction().apply {
                        replace(R.id.FrameLayout, List())
                        commit()
                    }
                    true
                }
                R.id.item_2 -> {
                    supportFragmentManager.beginTransaction().apply {
                        replace(R.id.FrameLayout, new_task())
                        commit()
                    }
                    true
                }
                else -> false
            }
        }
    }
}
