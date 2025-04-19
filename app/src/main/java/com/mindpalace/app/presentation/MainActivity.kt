package com.mindpalace.app.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.mindpalace.app.core.SupabaseClient
import com.mindpalace.app.presentation.navigation.MindNavigator
import com.mindpalace.app.presentation.theme.MindPalaceTheme
import dagger.hilt.android.AndroidEntryPoint

val supabase = SupabaseClient.client

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            MindPalaceTheme {
                // Setting up the NavController
                val navController = rememberNavController()

                // Passing everything into the NotesApp composable
                MindNavigator(
                    navController = navController,
                    modifier = Modifier.fillMaxSize()
                )
            }
        }
    }
}
