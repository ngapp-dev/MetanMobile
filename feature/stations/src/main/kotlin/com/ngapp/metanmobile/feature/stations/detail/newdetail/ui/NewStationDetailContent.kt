/*
 * Copyright 2024 NGApps Dev (https://github.com/ngapp-dev). All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.ngapp.metanmobile.feature.stations.detail.newdetail.ui

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.ngapp.metanmobile.core.designsystem.component.MMDivider
import com.ngapp.metanmobile.core.designsystem.component.MMTab
import com.ngapp.metanmobile.core.designsystem.component.MMTabRow
import com.ngapp.metanmobile.core.designsystem.theme.Green
import com.ngapp.metanmobile.core.designsystem.theme.MMColors
import com.ngapp.metanmobile.core.designsystem.theme.MMShapes
import com.ngapp.metanmobile.core.designsystem.theme.MMTypography
import com.ngapp.metanmobile.core.designsystem.theme.cardBackgroundColor
import com.ngapp.metanmobile.core.designsystem.theme.dividerColor
import com.ngapp.metanmobile.core.model.news.UserNewsResource
import com.ngapp.metanmobile.core.model.price.PriceResource
import com.ngapp.metanmobile.core.model.station.UserStationResource
import com.ngapp.metanmobile.core.ui.ItemDetailImageView
import com.ngapp.metanmobile.core.ui.stations.StationStatusView
import com.ngapp.metanmobile.core.ui.stations.createLocationIntent
import com.ngapp.metanmobile.feature.stations.R
import com.ngapp.metanmobile.feature.stations.detail.newdetail.state.NewStationDetailAction
import com.ngapp.metanmobile.feature.stations.detail.newdetail.ui.MenuTabs.OVERVIEW
import com.ngapp.metanmobile.feature.stations.detail.newdetail.ui.MenuTabs.PAYMENTS
import com.ngapp.metanmobile.feature.stations.detail.newdetail.ui.MenuTabs.PHOTOS
import com.ngapp.metanmobile.feature.stations.detail.newdetail.ui.MenuTabs.UPDATES


@Composable
internal fun NewStationDetailContent(
    modifier: Modifier = Modifier,
    stationDetail: UserStationResource,
    cngPrice: PriceResource?,
    relatedNewsList: List<UserNewsResource>,
    onAction: (NewStationDetailAction) -> Unit,
    onNewsDetailClick: (String) -> Unit,
    onBackClick: () -> Unit,
) {
    val context = LocalContext.current
    val listState = rememberLazyListState()
    val uriHandler = LocalUriHandler.current
    val phoneNumber = stationDetail.phones.split(",").first().trim()
    val hasUnread = relatedNewsList.any { !it.hasBeenViewed }

    LazyColumn(
        state = listState,
        modifier = modifier.fillMaxSize()
    ) {
        item("header") {
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                HeaderWithStatusAndActions(
                    title = stationDetail.title,
                    onShareClick = { onAction(NewStationDetailAction.ShareStation(stationDetail)) },
                    onCloseClick = onBackClick,
                    modifier = Modifier.padding(bottom = 2.dp)
                ) {
                    StationStatusView(stationDetail)
                }
                HeaderObjectType(
                    objectType = stationDetail.type,
                    distanceBetween = stationDetail.distanceBetween
                )
                HeaderObjectWorkTime(stationDetail.workingTime)
                if (cngPrice != null) {
                    HeaderObjectCNGPrice(cngPrice.content)
                }
                HeaderButtons(
                    isFavorite = stationDetail.isFavorite,
                    onDirectionsClick = { context.createLocationIntent(stationDetail) },
                    onCallClick = { uriHandler.openUri("tel:$phoneNumber") },
                    onToggleBookmark = {
                        onAction(
                            NewStationDetailAction.UpdateStationFavorite(
                                stationDetail.code, !stationDetail.isFavorite
                            )
                        )
                    },
                    onShareClick = { onAction(NewStationDetailAction.ShareStation(stationDetail)) }
                )
                Spacer(modifier = Modifier.padding(top = 8.dp))
            }
        }
        item("detailImage") {
            ItemDetailImageView(
                imageUrl = stationDetail.detailPicture,
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .clip(MMShapes.extraLarge)
            )
        }
        item("content") {
            Column {
                val tabsName = rememberSaveable { MenuTabs.entries.map { it.titleResId } }
                var selectedIndex by rememberSaveable { mutableIntStateOf(OVERVIEW.ordinal) }
                MMTabRow(
                    selectedTabIndex = selectedIndex,
                    indicatorHeight = 3.dp,
                    indicatorModifier = Modifier
                        .clip(RoundedCornerShape(topStart = 1.dp, topEnd = 1.dp))
                        .offset(y = (-2).dp),
                    divider = {
                        MMDivider(
                            thickness = 2.dp,
                            color = MMColors.dividerColor
                        )
                    }
                ) {
                    tabsName.forEachIndexed { index, stringResourceId ->
                        val extraModifier = if (index == UPDATES.ordinal) {
                            if (hasUnread) Modifier.notificationDot() else Modifier
                        } else {
                            Modifier
                        }
                        MMTab(
                            modifier = extraModifier.background(color = MMColors.cardBackgroundColor),
                            selected = index == selectedIndex,
                            onClick = { selectedIndex = index },
                            text = {
                                Text(
                                    text = stringResource(stringResourceId),
                                    style = MMTypography.headlineMedium,
                                )
                            }
                        )
                    }
                }
                Crossfade(
                    targetState = selectedIndex,
                    label = "contentMenu",
                ) { page ->
                    when (MenuTabs.entries[page]) {
                        OVERVIEW -> StationDetailOverview(stationDetail)
                        UPDATES -> StationDetailUpdates(relatedNewsList, onNewsDetailClick)
                        PAYMENTS -> StationDetailPayments(stationDetail)
                        PHOTOS -> StationDetailPhotos(stationDetail.detailPicture)
                    }
                }
            }
        }
    }
}

private enum class MenuTabs(val titleResId: Int) {
    OVERVIEW(R.string.feature_stations_title_overview),
    UPDATES(R.string.feature_stations_title_updates),
    PAYMENTS(R.string.feature_stations_title_payments),
    PHOTOS(R.string.feature_stations_title_photos),
}

private fun Modifier.notificationDot(): Modifier = composed {
    val tertiaryColor = Green
    drawWithContent {
        drawContent()
        drawCircle(
            tertiaryColor,
            radius = 5.dp.toPx(),
            center = center + Offset(
                80.dp.toPx() * .45f,
                12.dp.toPx() * -.45f - 6.dp.toPx(),
            ),
        )
    }
}