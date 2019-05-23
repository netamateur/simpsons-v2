package com.example.android.roomwordssample

import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Query

interface INetworkAPI {

    @GET("quotes")
    fun getQuotes(
            @Query("count") count: Int
    ): Observable<List<Quote>>

}
