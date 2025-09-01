package com.example.learningcodingapp.app

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.compose.rememberNavController
import com.example.learningcodingapp.R
import com.example.learningcodingapp.core.navigation.AppNavHost
import com.example.learningcodingapp.ui.theme.LearningCodingAppTheme
import com.google.firebase.FirebaseApp
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            LearningCodingAppTheme {
                val nav = rememberNavController()
                AppNavHost(nav)

            }
        }
    }
}


