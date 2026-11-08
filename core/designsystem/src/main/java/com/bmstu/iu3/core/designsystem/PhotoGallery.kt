package com.bmstu.iu3.core.designsystem

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties

@Composable
fun PhotoGallery(
    photos: List<Pair<String, String>>,
    index: Int,
    dismiss: () -> Unit,
) {
    if (photos.isEmpty()) return
    Dialog(
        dismiss,
        properties = DialogProperties(
            usePlatformDefaultWidth = false,
            decorFitsSystemWindows = false
        )
    ) {
        Surface(Modifier.fillMaxSize(), color = Color.Black) {
            val pager =
                rememberPagerState(initialPage = index.coerceIn(photos.indices)) { photos.size }
            Column(Modifier.fillMaxSize().safeDrawingPadding()) {
                Row(
                    Modifier.fillMaxWidth().padding(12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        stringResource(
                            R.string.ds_gallery_count,
                            pager.currentPage + 1,
                            photos.size
                        ), color = Color.White
                    )
                    TextButton(dismiss) {
                        Text(
                            stringResource(R.string.ds_gallery_close),
                            color = Color.White
                        )
                    }
                }
                HorizontalPager(
                    pager,
                    Modifier.weight(1f)
                ) { page ->
                    FacultyImage(
                        photos[page].first,
                        Modifier.fillMaxSize(),
                        crop = false,
                        background = Color.Black
                    )
                }
                Text(photos[pager.currentPage].second, Modifier.padding(16.dp), color = Color.White)
            }
        }
    }
}
