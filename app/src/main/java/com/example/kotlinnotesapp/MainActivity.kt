package com.example.kotlinnotesapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.kotlinnotesapp.ui.theme.KotlinNotesAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            KotlinNotesAppTheme {
                NotesApp(modifier = Modifier.fillMaxSize())
            }
        }
    }
}

@Composable
fun NotesApp (modifier: Modifier = Modifier) {
    var notes by remember { mutableStateOf(mutableStateListOf<Note>()) }
    NotesScreen(notes = notes,
        modifier = modifier,
        onNoteAdd = { title, body -> notes.add(Note(title = title, body = body)) },
        onDeleteAll = {notes.clear()}
        )
}

data class Note (
    val title : String,
    val body: String
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotesScreen (notes : MutableList<Note>,
                 onNoteAdd : (String, String) -> Unit,
                 onDeleteAll : () -> Unit,
                 modifier: Modifier ) {
    Scaffold (
        containerColor = Color.White,

        modifier = modifier.fillMaxSize(),
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(text = "My Notes", color = Color.Black, fontSize = 14.sp,
                    fontWeight = FontWeight.Bold
                    ) },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Color.Transparent
                ),
                actions = {
                    IconButton(onClick = {
                        //delete all notes
                        onDeleteAll()
                    },) {
                            Icon(
                                imageVector = Icons.Default.Delete,
                                contentDescription = "Delete All Notes",
                                tint = Color.Black
                            )
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = {
                onNoteAdd("New Note", "This is the body of the new note.") // Add a new note
            },
                containerColor = Color.Black
                ) {
                Icon(imageVector = Icons.Default.Add,
                    contentDescription = "Add Note",
                        tint = Color.White
                    )
            }
        }
    ) { paddingValues ->
        LazyColumn (
            modifier = modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(20.dp)) {
            items(notes) {
                note -> NoteCard(title = note.title, body = note.body)
            }
        }
    }
}

@Composable
fun NoteCard (title: String, body: String) {
    Card (modifier = Modifier
        .fillMaxWidth()
        .padding(10.dp)
        .border(1.dp, Color.Black, RoundedCornerShape(5.dp)),
        shape = RoundedCornerShape(5.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.Transparent,
            contentColor = Color.Black,
        ),
    ) {
        Column ( modifier = Modifier.padding(16.dp)) {
            Text(text = title,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 8.dp),
                )
            Text(text = body, style = MaterialTheme.typography.bodySmall,
                    textAlign = TextAlign.Justify,
                )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun NotesAppPreview() {
    KotlinNotesAppTheme {
        NotesApp()
    }
}