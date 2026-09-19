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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import com.talangraga.shared.TalangragaTypography
import com.talangraga.umrohmobile.ui.theme.TalangragaTheme

@Composable
fun TitleTextIcon(
    modifier: Modifier = Modifier,
    text: String,
    leadingIcon: ImageVector? = null,
    tint: Color = MaterialTheme.colorScheme.onSurface
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
    val backgroundColor = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceContainerHigh
    val borderColor = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)
    val textColor = if (isSelected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant
    val roundedCornerShape = RoundedCornerShape(12.dp)

    Text(
        text = text,
        style = TalangragaTypography.bodyMedium.copy(
            color = textColor,
            fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal
        ),
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
    val isEmptyText = text.isBlank() || text.contains("null")
    val backgroundColor = MaterialTheme.colorScheme.surfaceContainerHigh
    val borderColor = if (!isEmptyText) MaterialTheme.colorScheme.primary.copy(alpha = 0.5f) else MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)
    val textColor = if (!isEmptyText) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
    val roundedCornerShape = RoundedCornerShape(12.dp)

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = modifier
            .clip(roundedCornerShape)
            .clickable { onClick() }
            .background(backgroundColor)
            .border(width = 1.dp, color = borderColor, shape = roundedCornerShape)
            .padding(horizontal = 16.dp, vertical = 10.dp)
    ) {
        val label = if (isEmptyText) placeholder else text

        Text(
            text = label,
            style = TalangragaTypography.bodyMedium.copy(
                color = textColor,
                fontWeight = if (!isEmptyText) FontWeight.Medium else FontWeight.Normal
            ),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.weight(1f, fill = false)
        )
        Spacer(Modifier.width(12.dp))
        trailingIcon?.let {
            Icon(
                imageVector = it,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier
            )
        }
    }
}

@Preview
@Composable
fun TextRoundedPreview() {
    TalangragaTheme {
        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            TitleTextIcon(text = "Total Tabungan", leadingIcon = Icons.Default.Wallet)
            Row(
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
        }
    }
}
