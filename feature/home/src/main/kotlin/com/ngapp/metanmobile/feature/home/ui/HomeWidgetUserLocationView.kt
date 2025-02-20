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

package com.ngapp.metanmobile.feature.home.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.ngapp.metanmobile.core.common.util.shortFormatUnixDataToString
import com.ngapp.metanmobile.core.designsystem.icon.MMIcons
import com.ngapp.metanmobile.core.designsystem.theme.Blue
import com.ngapp.metanmobile.core.designsystem.theme.MMTypography
import com.ngapp.metanmobile.core.designsystem.theme.White
import com.ngapp.metanmobile.core.model.price.PriceResource
import com.ngapp.metanmobile.core.model.station.UserStationResource
import com.ngapp.metanmobile.core.ui.WidgetPriceAndLocationView
import com.ngapp.metanmobile.feature.home.R
import kotlinx.datetime.Clock
import com.ngapp.metanmobile.core.ui.R as CoreUiR

@Composable
internal fun HomeWidgetUserLocationView(
    modifier: Modifier = Modifier,
    isEditingUi: Boolean,
    nearestStation: UserStationResource?,
    cngPrice: PriceResource?,
    onStationDetailClick: (String) -> Unit = {},
) {
    Box {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(color = Blue)
                .padding(16.dp)
        ) {
            Text(
                text = stringResource(id = R.string.feature_home_text_hello_user),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                color = White,
                style = MMTypography.displayMedium,
            )
            Text(
                text = stringResource(
                    id = R.string.feature_home_text_today_date,
                    shortFormatUnixDataToString(Clock.System.now().toEpochMilliseconds() / 1000)
                ),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.padding(top = 4.dp),
                color = White,
                style = MMTypography.displaySmall
            )
            Spacer(Modifier.height(12.dp))
            WidgetPriceAndLocationView(
                nearestStation = nearestStation,
                cngPrice = cngPrice,
                onStationDetailClick = onStationDetailClick,
            )
        }
        AnimatedVisibility(
            visible = isEditingUi,
            enter = slideInHorizontally(initialOffsetX = { it }) + fadeIn(),
            exit = slideOutHorizontally(targetOffsetX = { it }) + fadeOut(),
            modifier = Modifier.align(Alignment.TopEnd),
        ) {
            IconButton(
                modifier = modifier.padding(start = 8.dp, top = 8.dp),
                onClick = {},
            ) {
                Icon(
                    imageVector = MMIcons.DragHandle,
                    contentDescription = "User location widget ${stringResource(CoreUiR.string.core_ui_description_reorder_drag_handle_icon)}",
                    tint = White,
                )
            }
        }
    }
}