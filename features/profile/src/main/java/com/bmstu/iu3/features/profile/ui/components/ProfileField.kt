package com.bmstu.iu3.features.profile.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bmstu.iu3.core.designsystem.AppGray
import com.bmstu.iu3.core.designsystem.AppLink
import com.bmstu.iu3.core.designsystem.AppSeparator

@Composable
internal fun ProfileField(
    label: String,
    value: String,
    edit: (() -> Unit)? = null,
) {
    Column {
        HorizontalDivider(color = AppSeparator)
        Row(
            Modifier.fillMaxWidth()
                .then(if (edit == null) Modifier else Modifier.clickable(onClick = edit))
                .padding(vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text(label, color = AppGray, fontSize = 12.sp)
                Text(value, fontSize = 16.sp, fontWeight = FontWeight.Medium)
            }
            if (edit != null) Icon(
                Icons.Default.Edit,
                null,
                tint = AppLink,
                modifier = Modifier.size(18.dp)
            )
        }
    }
}
