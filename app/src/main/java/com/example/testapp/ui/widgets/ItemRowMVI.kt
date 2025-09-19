package com.example.testapp.ui.widgets

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.testapp.data.Item


@Composable
fun ItemRowMVI(
    modifier: Modifier = Modifier,
    item: Item,
    onClick: () -> Unit,
    onCheckedChange: (Boolean) -> Unit
) {
    Surface(
        modifier = modifier,
        onClick = onClick,
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(checked = item.checked, onCheckedChange = { onCheckedChange(it) })
            Spacer(
                modifier = Modifier.width(12.dp)
            )
            Text(item.text)
        }
    }
}
