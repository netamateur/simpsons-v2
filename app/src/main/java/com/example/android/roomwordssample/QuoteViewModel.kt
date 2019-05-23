package com.example.android.roomwordssample

/*
 * Copyright (C) 2017 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.google.gson.GsonBuilder
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.internal.schedulers.IoScheduler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

/**
 * View Model to keep a reference to the word repository and
 * an up-to-date list of all words.
 */

class QuoteViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: QuoteRepository
    // Using LiveData and caching what getAlphabetizedWords returns has several benefits:
    // - We can put an observer on the data (instead of polling for changes) and only update the
    //   the UI when the data actually changes.
    // - Repository is completely separated from the UI through the ViewModel.
    val allQuotes: LiveData<List<Quote>>

    init {
        val quotesDao = QuoteRoomDatabase.getDatabase(application, viewModelScope).QuoteDao()
        repository = QuoteRepository(quotesDao)
        allQuotes = repository.allQuotes
    }

    /**
     * Launching a new coroutine to insert the data in a non-blocking way
     */
    fun insert(quote: Quote) = viewModelScope.launch(Dispatchers.IO) {
        repository.insert(quote)
    }

    fun insertMultiple(quotes: List<Quote>) = viewModelScope.launch(Dispatchers.IO) {
        repository.insertFetchedQuotes(quotes)
    }

    //
    fun initialDownload() {
        repository.downloadData()
        //async?
        insertMultiple(repository.fetched)
    }

}
