package ai.nextbillion.compose.example.kotlin.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material.Button
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material.ButtonDefaults
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material.Checkbox
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material.Icon
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material.Switch
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
@Composable
public fun SwitchItem(title: String, check: Boolean, onCheckedChange: ((Boolean) -> Unit)?) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(title)
        Switch(
            checked = check,
            onCheckedChange = onCheckedChange
        )
    }
}

@Composable
public fun CheckboxItem(title: String, check: Boolean, onCheckedChange: ((Boolean) -> Unit)?) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 32.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start
    ) {
        Checkbox(checked = check, onCheckedChange = onCheckedChange)
        Text(title)

    }
}

@Composable
fun ItemCell(title: String, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .clickable(onClick = onClick),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(title)
        Icon(
            imageVector = Icons.Default.KeyboardArrowRight,
            contentDescription = ""
        )
    }
}

@Composable
fun HorizontalButtonsWithTitle(
    buttonTexts: List<String>,
    title: String,
    select: Int,
    onSelect: ((Int) -> Unit)
) {
    var selectedButtonIndex by remember { mutableStateOf(select) }
    Column(
        modifier = Modifier
            .padding(16.dp)
            .height(IntrinsicSize.Min)
    ) {
        Text(
            text = title,
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
        ) {
            buttonTexts.forEachIndexed { index, buttonText ->
                Button(
                    onClick = {
                        onSelect(index)
                        selectedButtonIndex = index
                    },
                    modifier = Modifier.padding(horizontal = 8.dp),
                    colors = ButtonDefaults.buttonColors(backgroundColor = if (index == selectedButtonIndex) Color.Green else Color.White)
                ) {
                    Text(buttonText)
                }
            }
        }
    }
}