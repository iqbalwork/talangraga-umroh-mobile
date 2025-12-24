package com.talangraga.umrohmobile.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Wallet
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import com.talangraga.shared.Background
import com.talangraga.shared.BorderColor
import com.talangraga.shared.Sage
import com.talangraga.shared.TalangragaTypography
import com.talangraga.shared.TextOnColor
import com.talangraga.shared.TextSecondaryDark
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun TitleTextIcon(
    modifier: Modifier = Modifier,
    text: String,
    leadingIcon: ImageVector? = null,
    tint: Color = TextOnColor
) {
    ConstraintLayout(modifier = modifier) {
        val (iconRef, textRef) = createRefs()
        leadingIcon?.let {
            Icon(
                imageVector = leadingIcon,
                contentDescription = null,
                tint = tint,
                modifier = Modifier.constrainAs(iconRef) {
                    top.linkTo(textRef.top)
                    bottom.linkTo(textRef.bottom)
                    start.linkTo(parent.start)
                }
            )
        }
        Text(
            text = text,
            style = TalangragaTypography.titleMedium.copy(color = tint),
            modifier = Modifier.constrainAs(textRef) {
                top.linkTo(parent.top)
                start.linkTo(
                    if (leadingIcon != null) iconRef.end else parent.start,
                    if (leadingIcon != null) 8.dp else 0.dp
                )
            }
        )
    }
}

@Composable
fun TextButton(
    modifier: Modifier = Modifier,
    text: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val backgroundColor = if (isSelected) Sage else Background
    val borderColor = if (isSelected) Sage else BorderColor
    val textColor = if (isSelected) TextOnColor else TextSecondaryDark
    val roundedCornerShape = RoundedCornerShape(12.dp)

    Text(
        text = text,
        style = TalangragaTypography.bodyMedium.copy(color = textColor),
        modifier = modifier
            .clip(roundedCornerShape)
            .clickable { onClick() }
            .background(backgroundColor)
            .border(width = 1.dp, color = borderColor, shape = roundedCornerShape)
            .padding(horizontal = 16.dp, vertical = 10.dp),
    )

}

@Composable
fun TextButtonOption(
    modifier: Modifier = Modifier,
    text: String,
    placeholder: String,
    trailingIcon: ImageVector? = Icons.Default.ArrowDropDown,
    onClick: () -> Unit
) {
    val backgroundColor = if (text.isNotBlank()) Sage else Background
    val borderColor = if (text.isNotBlank()) Sage else BorderColor
    val selectedTextColor = if (text.isNotBlank()) TextOnColor else TextSecondaryDark
    val roundedCornerShape = RoundedCornerShape(12.dp)

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = modifier
            .clip(roundedCornerShape)
            .clickable { onClick() }
            .background(backgroundColor)
            .border(width = 1.dp, color = borderColor, shape = roundedCornerShape)
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {

        val label = text.ifBlank { placeholder }

        Text(
            text = label,
            style = TalangragaTypography.bodyMedium.copy(color = selectedTextColor),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier
        )
        Spacer(Modifier.width(12.dp))
        trailingIcon?.let {
            Icon(
                imageVector = it,
                contentDescription = null,
                tint = selectedTextColor,
                modifier = Modifier
            )
        }
    }

}

@Preview
@Composable
fun TextRoundedPreview() {
    Column {
        TitleTextIcon(text = "Total Tabungan", leadingIcon = Icons.Default.Wallet)
        Row(
            modifier = Modifier.padding(8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextButton(text = "Semua", isSelected = true, modifier = Modifier) { }
            TextButtonOption(
                text = "Bulan ke 31: 6 Nov - 5 Des 2025",
                placeholder = "Pilih Bulan",
                trailingIcon = Icons.Default.ArrowDropDown,
                modifier = Modifier.weight(1f)
            ) { }
        }
        TextButtonOption(
            text = "",
            placeholder = "Pilih Bulan",
            trailingIcon = Icons.Default.ArrowDropDown,
            modifier = Modifier.fillMaxWidth().padding(8.dp)
        ) { }
    }
}
