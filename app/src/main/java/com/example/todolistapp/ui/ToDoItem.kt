package com.example.todolistapp.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import com.example.todolistapp.R
import com.example.todolistapp.data.ToDoItemData

@Composable
fun ToDoItem(
    item: ToDoItemData,
    editExistingTask: (ToDoItemData) -> Unit,
    toggleCompleted: (Long) -> Unit,
    deleteItem: (Long) -> Unit
) {
    OutlinedCard(
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(4.dp)
                .padding(start = 6.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            val titleText =
                if (item.title.length > 20) item.title.slice(0..20) + ". . ." else item.title
            Row(verticalAlignment = Alignment.CenterVertically) {
                Checkbox(
                    checked = item.completed,
                    onCheckedChange = {
                        toggleCompleted(item.id)
                    }
                )
                Text(
                    text = titleText,
                    style = TextStyle(
                        textDecoration = if (item.completed) TextDecoration.LineThrough else null,
                    )
                )
            }
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = { editExistingTask(item) }) {
                    Icon(
                        painter = painterResource(id = R.drawable.edit_24),
                        contentDescription = "Edit icon"
                    )
                }
                IconButton(onClick = { deleteItem(item.id) }) {
                    Icon(
                        painter = painterResource(id = R.drawable.delete_24),
                        contentDescription = "Delete icon"
                    )
                }
            }
        }
    }
}