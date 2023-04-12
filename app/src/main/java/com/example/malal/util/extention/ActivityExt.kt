package com.example.malal.util.extention


import com.example.malal.R
import com.example.malal.presentation.MainActivity
import com.google.android.material.bottomnavigation.BottomNavigationView

fun MainActivity.showBottomNav(){
    val navigation = findViewById<BottomNavigationView>(R.id.bottomNavigationView)
    if(!navigation.isShown)
        navigation.show()
}

fun MainActivity.hideBottomNav(){
    val navigation = findViewById<BottomNavigationView>(R.id.bottomNavigationView)
    navigation.hide()
}