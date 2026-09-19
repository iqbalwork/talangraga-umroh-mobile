package com.talangraga.umrohmobile.ui.section

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material.icons.filled.People
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.talangraga.shared.Aqua
import com.talangraga.shared.Green
import com.talangraga.shared.Sage
import com.talangraga.shared.TalangragaTypography
import com.talangraga.umrohmobile.ui.component.IconBlock
import com.talangraga.umrohmobile.ui.theme.TalangragaTheme

@Composable
fun CardInfoSection(
    modifier: Modifier = Modifier,
    title: String,
    value: String,
    notes: String? = null,
    icon: ImageVector,
    notesColor: Color = Green,
    cardColor: Color = MaterialTheme.colorScheme.surfaceContainerHigh,
    illustrationIcon: ImageVector,
    startIconColor: Color,
    endIconColor: Color
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = cardColor
        ),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.4f)),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(Modifier.width(6.dp))
                    Text(
                        text = title,
                        style = TalangragaTypography.bodySmall.copy(fontWeight = FontWeight.Medium),
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                Text(
                    text = value,
                    style = TalangragaTypography.titleLarge.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.padding(top = 4.dp)
                )

                if (!notes.isNullOrBlank()) {
                    Text(
                        text = notes,
                        style = TalangragaTypography.bodySmall.copy(
                            color = notesColor,
                            fontWeight = FontWeight.SemiBold
                        ),
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }
            }

            IconBlock(
                icon = illustrationIcon,
                startColor = startIconColor,
                endColor = endIconColor,
                modifier = Modifier.padding(start = 12.dp)
            )
        }
    }
}

@Preview
@Composable
fun CardInfoSectionPreview() {
    TalangragaTheme {
        CardInfoSection(
            title = "Total Saldo",
            value = "Rp 380.000.000",
            illustrationIcon = Icons.Default.AttachMoney,
            startIconColor = Aqua,
            endIconColor = Aqua.copy(alpha = 0.5f),
            modifier = Modifier.padding(16.dp),
            icon = Icons.Default.People,
        )
    }
}
