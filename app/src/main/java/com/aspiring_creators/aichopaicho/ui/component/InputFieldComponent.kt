package com.aspiring_creators.aichopaicho.ui.component

import android.icu.util.Calendar
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun StringInputField(
    label: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var currentValue by remember {
        mutableStateOf("")
    }
    OutlinedTextField(
        value = currentValue,
        onValueChange = {
            currentValue = it
            onValueChange(it)
                        },
        label = { Text(label) },
        modifier = modifier.fillMaxWidth(),
        singleLine = true
    )
}

@Preview(showBackground = true, name = "String Input Field")
@Composable
fun StringInputFieldPreview() {
    Column(modifier = Modifier.padding(16.dp)) {
        StringInputField(
            label = "Name",
            onValueChange = { it }
        )
    }
}

@Composable
fun AmountInputField(
    label: String,
    onAmountTextChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    isError: Boolean = false,
    errorMessage: String? = null,
    value: String
) {
    var currentValue by remember(value) {
        mutableStateOf(value)
    }
    Column(modifier = modifier) {
        OutlinedTextField(
            value = currentValue,
            onValueChange = {
                currentValue = it
                onAmountTextChange(it)
            },
            label = { Text(label) },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            singleLine = true,
            isError = isError
        )
        if (isError && errorMessage != null) {
            Text(
                text = errorMessage,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(start = 16.dp, top = 4.dp)
            )
        }
    }
}

@Preview(showBackground = true, name = "Amount Input Field")
@Composable
fun AmountInputFieldPreview() {
    var amountInputText by remember { mutableStateOf("123") }
    val amountInt = amountInputText.toIntOrNull()
    val isError = amountInputText.isNotEmpty() && amountInt == null

    Column(modifier = Modifier.padding(16.dp)) {
        AmountInputField(
            label = "Transaction Amount",
            onAmountTextChange = {  it },
            isError = isError,
            errorMessage = if (isError) "Please enter a valid number" else null,
            value = amountInputText
        )
        Text(text = "Parsed Int: ${amountInt ?: "Invalid"}", modifier = Modifier.padding(top = 8.dp))
    }
}

@Preview(showBackground = true, name = "Amount Input Field - Error")
@Composable
fun AmountInputFieldErorPreview() {
    var amountInputText by remember { mutableStateOf("abc") }
    val amountInt = amountInputText.toIntOrNull()
    val isError = amountInputText.isNotEmpty() && amountInt == null

    Column(modifier = Modifier.padding(16.dp)) {
        AmountInputField(
            label = "Transaction Amount",
            onAmountTextChange = {  it },
            isError = isError,
            errorMessage = if (isError) "Please enter a valid number" else null,
            value = amountInputText
        )
        Text(text = "Parsed Int: ${amountInt ?: "Invalid"}", modifier = Modifier.padding(top = 8.dp))
    }
}

@Composable
fun DateInputField(
    label: String,
    onDateSelected: (Long?) -> Unit,
    modifier: Modifier = Modifier,
    initializeWithCurrentDate: Boolean = false,
    selectedDate: Long?
) {
    var selectedTimestamp by remember(selectedDate) {
        mutableStateOf(selectedDate)
    }
    var currentDate by remember { mutableStateOf("") }

    val context = LocalContext.current
    val calendar = Calendar.getInstance()

    LaunchedEffect(initializeWithCurrentDate) {
        if (initializeWithCurrentDate && selectedTimestamp == null) {
            val now = System.currentTimeMillis()
            selectedTimestamp = now
            onDateSelected(now)
        }
    }

    selectedTimestamp?.let {
        calendar.timeInMillis = it
        val dateFormatter = SimpleDateFormat("dd MMMM yyyy", java.util.Locale.getDefault())
        currentDate = dateFormatter.format(Date(it))
    }

    val showDatePicker = {
        android.app.DatePickerDialog(
            context,
            { _: android.widget.DatePicker, year: Int, month: Int, dayOfMonth: Int ->
                val selectedCalendar = Calendar.getInstance()
                selectedCalendar.set(year, month, dayOfMonth)
                val millis = selectedCalendar.timeInMillis
                selectedTimestamp = millis
                onDateSelected(millis)
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        ).show()
    }

    Column(modifier = modifier) {
        OutlinedTextField(
            value = currentDate,
            onValueChange = { /* No manual text input */ },
            label = { Text(label) },
            modifier = Modifier
                .fillMaxWidth()
                .clickable { showDatePicker() },
            readOnly = true,
            trailingIcon = {
                Icon(
                    imageVector = Icons.Filled.DateRange,
                    contentDescription = "Select Date",
                    modifier = Modifier.clickable { showDatePicker() }
                )
            },
        )
    }
}


@Preview(showBackground = true, name = "Date Input Field - Empty")
@Composable
fun DateInputFieldPreviewEmpty() {
    var date: Long? by remember { mutableStateOf(null) }
    MaterialTheme {
        Column(modifier = Modifier.padding(16.dp)) {
            DateInputField(
                label = "Transaction Date",
                onDateSelected = { it },
                initializeWithCurrentDate = true,
                selectedDate = date
            )
            Text(
                text = "Selected: ${date?.let { SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date(it)) } ?: "None"}",
                modifier = Modifier.padding(top = 8.dp),
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}


@Composable
fun MultiLineTextInputField(
    label: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    minLines: Int = 3,
    maxLines: Int = 5,
    value: String
) {
    var currentValue by remember(value) {
        mutableStateOf(value)
    }
    OutlinedTextField(
        value = currentValue,
        onValueChange = {
            currentValue = it
            onValueChange(it)
                        },
        label = { Text(label) },
        modifier = modifier
            .fillMaxWidth()
            .defaultMinSize(minHeight = (minLines * 24).dp), // Approximate height based on lines
        maxLines = maxLines
    )
}

@Preview(showBackground = true, name = "Multi-line Text Input Field")
@Composable
fun MultiLineTextInputFieldPreview() {
    var notes by remember { mutableStateOf("This is a note.\nIt can span multiple lines.") }
    Column(modifier = Modifier.padding(16.dp)) {
        MultiLineTextInputField(
            label = "Notes",
            onValueChange = {  it },
            value = notes
        )
        Text(
            text = "Notes: $notes",
            modifier = Modifier.padding(top = 8.dp),
            style = MaterialTheme.typography.bodySmall
        )
    }
}