package com.talangraga.umrohmobile.ui.section

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material.icons.filled.People
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import com.talangraga.shared.Aqua
import com.talangraga.shared.Green
import com.talangraga.shared.Sage
import com.talangraga.umrohmobile.ui.TalangragaTheme
import com.talangraga.shared.TalangragaTypography
import com.talangraga.shared.TextOnColor
import com.talangraga.umrohmobile.ui.component.IconBlock
import com.talangraga.umrohmobile.ui.component.TitleTextIcon
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun CardInfoSection(
    modifier: Modifier = Modifier,
    title: String,
    value: String,
    notes: String? = null,
    icon: ImageVector,
    notesColor: Color = Green,
    cardColor: Color = Sage,
    illustrationIcon: ImageVector,
    startIconColor: Color,
    endIconColor: Color
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = cardColor
        ),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        ConstraintLayout(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            val (valueRef, notesRef) = createRefs()

            Column(modifier = Modifier.constrainAs(valueRef) {
                top.linkTo(parent.top)
                bottom.linkTo(parent.bottom)
                start.linkTo(parent.start)
            }) {
                TitleTextIcon(
                    text = title,
                    leadingIcon = icon,
                )
                Text(
                    text = value,
                    style = TalangragaTypography.titleLarge.copy(color = TextOnColor),
                )
            }

            notes?.let {
                Text(
                    text = notes,
                    style = TalangragaTypography.bodySmall.copy(
                        color = notesColor
                    ),
                    modifier = Modifier.constrainAs(notesRef) {
                        top.linkTo(valueRef.bottom, 4.dp)
                        start.linkTo(parent.start)
                    }
                )
            }

            IconBlock(
                icon = illustrationIcon,
                startColor = startIconColor,
                endColor = endIconColor,
                modifier = Modifier.constrainAs(createRef()) {
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                    end.linkTo(parent.end)
                }
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
            cardColor = Sage,
            illustrationIcon = Icons.Default.AttachMoney,
            startIconColor = Aqua,
            endIconColor = Aqua.copy(alpha = 0.5f),
            modifier = Modifier.padding(16.dp),
            icon = Icons.Default.People,
        )
    }
}

