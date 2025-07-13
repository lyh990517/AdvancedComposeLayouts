package com.yunho.pull.to.refresh

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.yunho.common.samples
import com.yunho.pull.to.refresh.RefreshState.Companion.onRefresh
import com.yunho.pull.to.refresh.RefreshState.Companion.rememberRefreshState
import kotlinx.coroutines.delay

@Composable
fun PullToRefresh(
    modifier: Modifier = Modifier
) {
    val parentListState = rememberLazyListState()
    val refreshState = rememberRefreshState {
        delay(1000)

        reset()
    }
    val nestedScrollConnection = parentListState.rememberNestedScrollConnectionWith(refreshState)

    LazyColumn(
        state = parentListState,
        modifier = modifier
    ) {
        item {
            Box(
                modifier = Modifier
                    .statusBarsPadding()
                    .fillMaxWidth()
                    .height(60.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Topbar",
                    style = TextStyle(
                        fontSize = 32.sp,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center
                    )
                )
            }
        }

        item {
            RefreshIndicator(
                modifier = Modifier
                    .fillMaxWidth()
                    .onRefresh(refreshState)
            )
        }

        item {
            LazyColumn(
                modifier = Modifier
                    .navigationBarsPadding()
                    .nestedScroll(nestedScrollConnection)
                    .fillMaxWidth()
                    .height(1500.dp),
                verticalArrangement = Arrangement.spacedBy(40.dp),
                contentPadding = PaddingValues(
                    top = 20.dp,
                    bottom = 20.dp,
                    start = 30.dp,
                    end = 30.dp
                )
            ) {
                items(samples) {
                    Image(
                        modifier = Modifier
                            .aspectRatio(2f / 2.5f)
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(40.dp)),
                        painter = painterResource(it),
                        contentDescription = null,
                        contentScale = ContentScale.Crop
                    )
                }
            }
        }
    }
}