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

package com.ngapp.metanmobile.feature.stations.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.navDeepLink
import com.ngapp.metanmobile.core.ui.util.slideInLeftComposable
import com.ngapp.metanmobile.feature.stations.StationsRoute
import kotlinx.serialization.Serializable

private const val DEEP_LINK_URI_PATTERN = "https://metan.by/ecogas-map"

fun NavController.navigateToStations(navOptions: NavOptions) =
    navigate(route = StationsNavigation, navOptions)

fun NavGraphBuilder.stationsScreen(
    onNewsDetailClick: (String) -> Unit,
    onShowBottomBar: (Boolean) -> Unit,
) {
    slideInLeftComposable<StationsNavigation>(
        deepLinks = listOf(navDeepLink { uriPattern = DEEP_LINK_URI_PATTERN }),
    ) {
        StationsRoute(onNewsDetailClick, onShowBottomBar)
    }
}

@Serializable
data object StationsNavigation