package com.mindpalace.app.presentation.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.mindpalace.app.R

@Composable
fun AppTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    modifier: Modifier = Modifier,
    isPassword: Boolean = false,
    isDisabled: Boolean = false,
    isMultiline: Boolean = false
) {
    var passwordVisible = remember { mutableStateOf(false) }

    Column(modifier = modifier) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = if (isDisabled) MaterialTheme.colorScheme.outline else MaterialTheme.colorScheme.secondary,
            modifier = Modifier.padding(bottom = 6.dp)
        )
        OutlinedTextField(
            readOnly = isDisabled,
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier
                .fillMaxWidth()
                .then(if (isMultiline) Modifier.heightIn(min = 100.dp) else Modifier.height(52.dp)),
            textStyle = MaterialTheme.typography.titleSmall,
            singleLine = !isMultiline,
            shape = RoundedCornerShape(4.dp),
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = if (isPassword) KeyboardType.Password else KeyboardType.Email,
                imeAction = if (isMultiline) ImeAction.Default else ImeAction.Next
            ),
            visualTransformation = if (isPassword && !passwordVisible.value) PasswordVisualTransformation() else VisualTransformation.None,
            trailingIcon = {
                if (isPassword) {
                    val icon =
                        if (passwordVisible.value) R.drawable.eye_line else R.drawable.eye_close_line
                    IconButton(onClick = { passwordVisible.value = !passwordVisible.value }) {
                        Icon(
                            painter = painterResource(id = icon), contentDescription = null
                        )
                    }
                }
            },
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                focusedContainerColor = MaterialTheme.colorScheme.surface,
                focusedBorderColor = if (isDisabled) MaterialTheme.colorScheme.background else MaterialTheme.colorScheme.outlineVariant,
                unfocusedBorderColor = if (isDisabled) MaterialTheme.colorScheme.background else MaterialTheme.colorScheme.outline,
                cursorColor = MaterialTheme.colorScheme.primary,
                unfocusedTextColor = if (isDisabled) MaterialTheme.colorScheme.outline else MaterialTheme.colorScheme.primary,
                focusedTextColor = if (isDisabled) MaterialTheme.colorScheme.outline else MaterialTheme.colorScheme.primary
            )
        )
    }
}