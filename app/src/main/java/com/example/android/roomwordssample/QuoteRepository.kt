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

import androidx.lifecycle.LiveData
import androidx.annotation.WorkerThread
import androidx.lifecycle.MutableLiveData
import com.google.gson.GsonBuilder
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.internal.schedulers.IoScheduler
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Abstracted Repository as promoted by the Architecture Guide.
 * https://developer.android.com/topic/libraries/architecture/guide.html
 */
class QuoteRepository(private val quoteDao: QuoteDao) {

    // Room executes all queries on a separate thread.
    // Observed LiveData will notify the observer when the data has changed.
    val allQuotes: LiveData<List<Quote>> = quoteDao.getAllQuotes()

    // You must call this on a non-UI thread or your app will crash. So we're making this a
    // suspend function so the caller methods know this.
    // Like this, Room ensures that you're not doing any long running operations on the main
    // thread, blocking the UI.
    @WorkerThread
    suspend fun insert(quote: Quote) {
        quoteDao.insert(quote)
    }

    @WorkerThread
    suspend fun insertFetchedQuotes(fetchedQuotes: List<Quote>) {
        for (quote in fetched) {
            quoteDao.insertAll(quote)
        }
    }

    //
    private var fetchedQuotes: MutableLiveData<List<Quote>> = MutableLiveData()
    //val q: LiveData<List<Quote>> = fetchedQuotes
    var fetched: List<Quote> = emptyList()

    private val baseURL = "https://thesimpsonsquoteapi.glitch.me/"

    fun downloadData() : List<Quote> {
        val retrofit = Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .baseUrl(baseURL).build()

        val postsApi = retrofit.create(INetworkAPI::class.java)

        val response = postsApi.getQuotes(40)

        response.observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(IoScheduler())
                .subscribe { result ->
                    //fetchedQuotes.value = result
                    fetched = result
        }
        return fetched
    }

}
