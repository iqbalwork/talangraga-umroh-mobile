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
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.talangraga.umrohmobile.ui.TalangragaTheme
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
    title: String,
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    enabled: Boolean = true,
    leadingIcon: @Composable (() -> Unit)? = null
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.Start
    ) {
        Text(
            text = title,
            textAlign = TextAlign.Start,
            fontSize = 16.sp,
            color = Color.Black,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(4.dp))
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            placeholder = {
                Text(placeholder)
            },
            singleLine = true,
            enabled = enabled,
            leadingIcon = leadingIcon,
            modifier = Modifier.fillMaxWidth()
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
    leadingIcon: @Composable (() -> Unit)? = null
) {

    var passwordVisibility by remember { mutableStateOf(false) }

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.Start
    ) {
        Text(
            text = title,
            textAlign = TextAlign.Start,
            fontSize = 16.sp,
            color = Color.Black,
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
            leadingIcon = leadingIcon,
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
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Preview
@Composable
fun InputTextComponentPreview(modifier: Modifier = Modifier) {
    TalangragaTheme {
        Column(
            modifier = Modifier
                .background(color = Color.White)
                .padding(16.dp)
                .fillMaxSize()
        ) {
            InputText(
                title = stringResource(Res.string.label_username_or_email),
                value = "",
                onValueChange = {},
                placeholder = "Input here",
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Filled.AccountCircle,
                        contentDescription = null
                    )
                },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))
            PasswordInput(
                title = stringResource(Res.string.password),
                password = "",
                onPasswordChange = {},
                placeholder = "Input password here",
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}