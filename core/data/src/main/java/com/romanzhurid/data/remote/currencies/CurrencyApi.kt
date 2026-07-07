package com.romanzhurid.data.remote.currencies

import com.romanzhurid.data.remote.currencies.model.BankCurrencyRemote
import com.romanzhurid.data.remote.currencies.model.CurrencyRemote
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface CurrencyApi {

    @GET("/api/exrates/rates/{cur_id}")
    suspend fun getCurrency(
        @Path("cur_id") curId: Int
    ): BankCurrencyRemote

    @GET("/api/exrates/rates")
    suspend fun getAllCurrency(
        @Query("periodicity") periodicity: Int = 0
    ): List<CurrencyRemote>
}
