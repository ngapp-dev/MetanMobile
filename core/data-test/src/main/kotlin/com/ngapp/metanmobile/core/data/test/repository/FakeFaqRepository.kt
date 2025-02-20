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

package com.ngapp.metanmobile.core.data.test.repository

import com.ngapp.metanmobile.core.common.network.Dispatcher
import com.ngapp.metanmobile.core.common.network.MMDispatchers.IO
import com.ngapp.metanmobile.core.data.Synchronizer
import com.ngapp.metanmobile.core.data.model.faq.asEntity
import com.ngapp.metanmobile.core.data.repository.faq.FaqRepository
import com.ngapp.metanmobile.core.data.repository.faq.FaqResourceQuery
import com.ngapp.metanmobile.core.database.model.faq.FaqResourceEntity
import com.ngapp.metanmobile.core.database.model.faq.asExternalModel
import com.ngapp.metanmobile.core.model.faq.FaqResource
import com.ngapp.metanmobile.core.network.model.faq.NetworkFaqResource
import com.ngapp.metanmobile.core.network.network.MetanMobileParser
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

/**
 * Fake implementation of the [FaqRepository] that retrieves the faq resources from a JSON String.
 *
 * This allows us to run the app with fake data, without needing an internet connection or working
 * backend.
 */
internal class FakeFaqRepository @Inject constructor(
    @Dispatcher(IO) private val ioDispatcher: CoroutineDispatcher,
    private val parser: MetanMobileParser,
) : FaqRepository {

    override fun getFaqList(query: FaqResourceQuery): Flow<List<FaqResource>> = flow {
        val faqList = parser.getFaqList()
            .map(NetworkFaqResource::asEntity)
            .map(FaqResourceEntity::asExternalModel)
            .applyQueryFilters(query)
        emit(faqList)
    }.flowOn(ioDispatcher)

    override suspend fun syncWith(synchronizer: Synchronizer) = true

    private fun List<FaqResource>.applyQueryFilters(query: FaqResourceQuery): List<FaqResource> {
        return this.filter { faq -> !query.filterFaqListPinned || faq.isPinned == 1 }
    }
}
