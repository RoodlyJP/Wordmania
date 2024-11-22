package com.example.wordmania

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.wordmania.ui.WordmaniaApp
import com.example.wordmania.ui.theme.WordmaniaTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            WordmaniaTheme {
                WordmaniaApp()
            }
        }
    }
}

