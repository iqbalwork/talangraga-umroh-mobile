package com.talangraga.umrohmobile.ui.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.talangraga.shared.Background
import com.talangraga.shared.BorderColor
import com.talangraga.shared.Sage
import com.talangraga.shared.TalangragaTypography
import com.talangraga.umrohmobile.ui.theme.TalangragaTheme
import org.jetbrains.compose.resources.stringResource
import talangragaumrohmobile.composeapp.generated.resources.Res
import talangragaumrohmobile.composeapp.generated.resources.hide_password
import talangragaumrohmobile.composeapp.generated.resources.label_username_or_email
import talangragaumrohmobile.composeapp.generated.resources.password
import talangragaumrohmobile.composeapp.generated.resources.show_password

@Composable
fun InputText(
    modifier: Modifier = Modifier,
    title: String? = null,
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    enabled: Boolean = true,
    backgroundColor: Color = Color.White,
    leadingIcon: ImageVector? = null,
    trailingIcon: ImageVector? = null,
    keyboardCapitalization: KeyboardCapitalization = KeyboardCapitalization.Unspecified,
    keyboardType: KeyboardType = KeyboardType.Text
) {
    val borderColor = if (value.isNotBlank()) Sage else BorderColor

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.Start
    ) {
        title?.let {
            Text(
                text = title,
                style = TalangragaTypography.titleSmall,
                textAlign = TextAlign.Start,
                modifier = Modifier.fillMaxWidth()
            )
        }
        Spacer(modifier = Modifier.height(4.dp))
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            placeholder = {
                Text(placeholder)
            },
            singleLine = true,
            enabled = enabled,
            keyboardOptions = KeyboardOptions(
                capitalization = keyboardCapitalization,
                keyboardType = keyboardType
            ),
            leadingIcon = if (leadingIcon != null) {
                {
                    Icon(
                        imageVector = leadingIcon,
                        contentDescription = null
                    )
                }
            } else null,
            trailingIcon = if (trailingIcon != null) {
                {
                    Icon(
                        imageVector = trailingIcon,
                        contentDescription = null
                    )
                }
            } else null,
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = borderColor,
                unfocusedBorderColor = BorderColor,
                focusedContainerColor = backgroundColor,
                unfocusedContainerColor = backgroundColor
            ),
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
fun InputTextWithStylingTitle(
    modifier: Modifier = Modifier,
    title: AnnotatedString? = null,
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    enabled: Boolean = true,
    backgroundColor: Color = Color.White,
    leadingIcon: ImageVector? = null,
    trailingIcon: ImageVector? = null,
    keyboardCapitalization: KeyboardCapitalization = KeyboardCapitalization.Unspecified,
    keyboardType: KeyboardType = KeyboardType.Text
) {
    val borderColor = if (value.isNotBlank()) Sage else BorderColor

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.Start
    ) {
        title?.let {
            Text(
                text = title,
                style = TalangragaTypography.titleSmall,
                textAlign = TextAlign.Start,
                modifier = Modifier.fillMaxWidth()
            )
        }
        Spacer(modifier = Modifier.height(4.dp))
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            placeholder = {
                Text(placeholder)
            },
            singleLine = true,
            enabled = enabled,
            keyboardOptions = KeyboardOptions(
                capitalization = keyboardCapitalization,
                keyboardType = keyboardType
            ),
            leadingIcon = if (leadingIcon != null) {
                {
                    Icon(
                        imageVector = leadingIcon,
                        contentDescription = null
                    )
                }
            } else null,
            trailingIcon = if (trailingIcon != null) {
                {
                    Icon(
                        imageVector = trailingIcon,
                        contentDescription = null
                    )
                }
            } else null,
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = borderColor,
                unfocusedBorderColor = BorderColor,
                focusedContainerColor = backgroundColor,
                unfocusedContainerColor = backgroundColor
            ),
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
fun CurrencyInputText(
    modifier: Modifier = Modifier,
    title: String? = null,
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    enabled: Boolean = true,
    backgroundColor: Color = Background
) {
    val borderColor = if (value.isNotBlank()) Sage else BorderColor
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.Start
    ) {
        title?.let {
            Text(
                text = title,
                style = TalangragaTypography.titleSmall,
                textAlign = TextAlign.Start,
                modifier = Modifier.fillMaxWidth()
            )
        }
        Spacer(modifier = Modifier.height(4.dp))
        OutlinedTextField(
            value = value,
            onValueChange = { newValue ->
                if (newValue.all { it.isDigit() }) {
                    onValueChange(newValue)
                }
            },
            placeholder = {
                Text(placeholder)
            },
            singleLine = true,
            enabled = enabled,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            visualTransformation = CurrencyVisualTransformation(),
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = borderColor,
                unfocusedBorderColor = BorderColor,
                focusedContainerColor = backgroundColor,
                unfocusedContainerColor = backgroundColor
            ),
            modifier = Modifier.fillMaxWidth()
        )
    }
}

class CurrencyVisualTransformation : VisualTransformation {
    override fun filter(text: AnnotatedString): TransformedText {
        val originalText = text.text
        if (originalText.isEmpty()) {
            return TransformedText(text, OffsetMapping.Identity)
        }

        val formattedText = formatCurrency(originalText)
        val newText = AnnotatedString(formattedText)

        val offsetMapping = object : OffsetMapping {
            override fun originalToTransformed(offset: Int): Int {
                if (offset <= 0) return 0
                val original = originalText.take(offset)
                val formatted = formatCurrency(original)
                return formatted.length
            }

            override fun transformedToOriginal(offset: Int): Int {
                if (offset <= 0) return 0
                val formatted = formattedText.take(offset)
                val original = formatted.replace(".", "").replace("Rp ", "")
                return original.length.coerceAtMost(originalText.length)
            }
        }

        return TransformedText(newText, offsetMapping)
    }

    private fun formatCurrency(text: String): String {
        if (text.isEmpty()) return ""
        val reversed = text.reversed()
        val formatted = StringBuilder()
        for (i in reversed.indices) {
            formatted.append(reversed[i])
            if ((i + 1) % 3 == 0 && i != reversed.lastIndex) {
                formatted.append(".")
            }
        }
        return "Rp " + formatted.reverse().toString()
    }
}

@Preview(showBackground = true)
@Composable
fun InputTextPreview() {
    var text by remember { mutableStateOf("") }
    TalangragaTheme {
        Column {
            InputText(
                modifier = Modifier.padding(16.dp),
                title = stringResource(Res.string.label_username_or_email),
                value = text,
                onValueChange = { text = it },
                placeholder = "Enter your username or email",
                leadingIcon = Icons.Default.AccountCircle
            )
            InputText(
                modifier = Modifier.padding(16.dp),
                title = stringResource(Res.string.label_username_or_email),
                value = text,
                onValueChange = { text = it },
                placeholder = "Enter your username or email",
            )
        }
    }
}

@Preview
@Composable
fun CurrencyInputTextPreview() {
    var text by remember { mutableStateOf("") }
    TalangragaTheme {
        CurrencyInputText(
            modifier = Modifier.padding(16.dp),
            title = "Jumlah Tabungan",
            value = text,
            onValueChange = { text = it },
            placeholder = "Rp xxx.xxx.xxx"
        )
    }
}

@Preview
@Composable
fun PasswordInputPreview() {
    var password by remember { mutableStateOf("") }
    TalangragaTheme {
        PasswordInput(
            modifier = Modifier.padding(16.dp),
            title = stringResource(Res.string.password),
            password = password,
            onPasswordChange = { password = it },
            placeholder = "Enter your password",
            leadingIcon = Icons.Default.Security
        )
    }
}

@Composable
fun PasswordInput(
    modifier: Modifier = Modifier,
    title: String,
    password: String,
    onPasswordChange: (String) -> Unit,
    placeholder: String,
    enabled: Boolean = true,
    leadingIcon: ImageVector? = null
) {
    var passwordVisibility by remember { mutableStateOf(false) }
    val borderColor = if (password.isNotBlank()) Sage else BorderColor

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.Start
    ) {
        Text(
            text = title,
            textAlign = TextAlign.Start,
            style = TalangragaTypography.titleSmall,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(4.dp))
        OutlinedTextField(
            value = password,
            onValueChange = onPasswordChange,
            placeholder = {
                Text(placeholder)
            },
            singleLine = true,
            enabled = enabled,
            visualTransformation = if (passwordVisibility) VisualTransformation.None else PasswordVisualTransformation(),
            leadingIcon = if (leadingIcon != null) {
                {
                    Icon(
                        imageVector = leadingIcon,
                        contentDescription = null
                    )
                }
            } else null,
            trailingIcon = {
                val image = if (passwordVisibility)
                    Icons.Filled.VisibilityOff
                else Icons.Filled.Visibility

                val description = if (passwordVisibility) {
                    stringResource(Res.string.hide_password)
                } else stringResource(
                    Res.string.show_password
                )

                IconButton(onClick = { passwordVisibility = !passwordVisibility }) {
                    Icon(imageVector = image, description)
                }
            },
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = borderColor,
                unfocusedBorderColor = BorderColor,
                focusedContainerColor = Color.White,
                unfocusedContainerColor = Color.White
            ),
            modifier = Modifier.fillMaxWidth()
        )
    }
}
