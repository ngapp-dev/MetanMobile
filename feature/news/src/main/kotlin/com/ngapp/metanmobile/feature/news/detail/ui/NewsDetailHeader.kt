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

package com.ngapp.metanmobile.feature.news.detail.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.ngapp.metanmobile.core.common.util.isNewsNew
import com.ngapp.metanmobile.core.designsystem.component.MMDivider
import com.ngapp.metanmobile.core.designsystem.component.htmltext.HtmlText
import com.ngapp.metanmobile.core.designsystem.theme.Blue
import com.ngapp.metanmobile.core.designsystem.theme.Gray400
import com.ngapp.metanmobile.core.designsystem.theme.MMTypography
import com.ngapp.metanmobile.core.designsystem.theme.White
import com.ngapp.metanmobile.core.model.news.NewsResource
import com.ngapp.metanmobile.core.ui.util.dateFormatted
import com.ngapp.metanmobile.feature.news.R

@Composable
internal fun NewsDetailHeader(news: NewsResource) {
    Column(
        verticalArrangement = Arrangement.spacedBy(12.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp),
    ) {
        Text(
            text = news.title,
            style = MMTypography.displayMedium,
        )
        NewsDetailDateAndStatus(
            dateCreated = news.dateCreated,
        )
        if (news.description.isNotEmpty()) {
            HtmlText(
                text = news.description,
                style = MMTypography.headlineMedium,
            )
        }
        MMDivider()
    }
}

@Composable
private fun NewsDetailDateAndStatus(
    dateCreated: Long,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = dateFormatted(dateCreated),
            modifier = Modifier.weight(1f),
            style = MMTypography.headlineMedium,
            color = Gray400
        )
        NewsDetailStatus(isNew = isNewsNew(dateCreated))
    }
}

@Composable
private fun NewsDetailStatus(isNew: Boolean) {
    if (isNew) {
        Text(
            text = stringResource(id = R.string.feature_news_text_new),
            style = MMTypography.displaySmall,
            color = White,
            modifier = Modifier
                .clip(RoundedCornerShape(12.dp, 0.dp, 12.dp, 0.dp))
                .background(color = Blue)
                .padding(vertical = 2.dp, horizontal = 8.dp)
        )
    }
}