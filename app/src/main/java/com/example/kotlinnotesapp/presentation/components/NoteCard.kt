package com.example.kotlinnotesapp.presentation.components

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@Composable
fun NoteCard(title: String, body: String) {
  Card(
      modifier =
          Modifier.fillMaxWidth()
              .padding(10.dp)
              .border(1.dp, Color.Black, RoundedCornerShape(5.dp)),
      shape = RoundedCornerShape(5.dp),
      colors =
          CardDefaults.cardColors(
              containerColor = Color.Transparent,
              contentColor = Color.Black,
          ),
  ) {
    Column(modifier = Modifier.padding(10.dp)) {
      Text(
          text = title,
          style = MaterialTheme.typography.bodyLarge,
          fontWeight = FontWeight.Bold,
          modifier = Modifier.padding(bottom = 8.dp),
      )
      Text(
          text = body,
          style = MaterialTheme.typography.bodySmall,
          textAlign = TextAlign.Justify,
      )
    }
  }
}
