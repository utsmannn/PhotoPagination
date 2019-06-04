/*
 * Copyright 2019 Muhammad Utsman. All rights reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.utsman.wallaz.di

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.utsman.wallaz.db.ChipHistoryDatabase
import com.utsman.wallaz.db.ChipHistoryRepository
import com.utsman.wallaz.viewmodel.SearchViewModel
import com.utsman.wallaz.viewmodel.SearchViewModelFactory

object SearchInjector {

    private fun getChipRepo(context: Context): ChipHistoryRepository = ChipHistoryRepository.getInstance(ChipHistoryDatabase.getInstance(context).chipHistoryDao())
    private fun proviceChipsViewModel(context: Context): SearchViewModelFactory = SearchViewModelFactory(getChipRepo(context))
    fun injectSearchViewModel(fragment: Fragment, context: Context) = ViewModelProviders.of(fragment, proviceChipsViewModel(context)).get(SearchViewModel::class.java)

}