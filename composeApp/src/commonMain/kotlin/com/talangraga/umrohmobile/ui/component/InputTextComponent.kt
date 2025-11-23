package com.talangraga.umrohmobile.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.talangraga.umrohmobile.ui.TalangragaTheme
import com.talangraga.umrohmobile.ui.TalangragaTypography
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
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
) {
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
            leadingIcon = {
                leadingIcon?.let {
                    Icon(
                        imageVector = leadingIcon,
                        contentDescription = null
                    )
                }
            },
            trailingIcon = {
                trailingIcon?.let {
                    Icon(
                        imageVector = trailingIcon,
                        contentDescription = null
                    )
                }
            },
            modifier = Modifier.fillMaxWidth().background(color = backgroundColor)
        )
    }
}

@Preview
@Composable
fun InputTextPreview() {
    var text by remember { mutableStateOf("") }
    TalangragaTheme {
        InputText(
            modifier = Modifier.padding(16.dp),
            title = stringResource(Res.string.label_username_or_email),
            value = text,
            onValueChange = { text = it },
            placeholder = "Enter your username or email",
            leadingIcon = Icons.Default.AccountCircle
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
            leadingIcon = {
                leadingIcon?.let {
                    Icon(
                        imageVector = leadingIcon,
                        contentDescription = null
                    )
                }
            },
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
            modifier = Modifier.fillMaxWidth().background(color = Color.White)
        )
    }
}
