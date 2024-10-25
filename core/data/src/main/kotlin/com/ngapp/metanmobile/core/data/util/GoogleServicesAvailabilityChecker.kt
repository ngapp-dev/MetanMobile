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

package com.ngapp.metanmobile.core.data.util

import android.content.Context
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject


class GoogleServicesAvailabilityChecker @Inject constructor(
    @ApplicationContext private val context: Context,
) : GoogleServicesChecker {

    override val isGoogleServicesAvailable: Boolean
        get() {
            val playServicesStatus =
                GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(context)
            return when (playServicesStatus) {
                ConnectionResult.SUCCESS -> {
                    // Google Play Services available
                    true
                }

                ConnectionResult.SERVICE_VERSION_UPDATE_REQUIRED -> {
                    // Google Play Services update required
                    throw Exception("Google services version is outdated, please update")
                    false
                }

                else -> {
                    // Google Play Services unavailable
                    throw Exception("Seems that this device does not have Google services, functionality may be limited")
                    false
                }
            }
        }
}