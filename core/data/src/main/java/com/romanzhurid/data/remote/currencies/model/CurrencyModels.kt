package com.romanzhurid.data.remote.currencies.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class BankCurrencyRemote(
    @SerialName("Cur_Abbreviation")
    val abbreviation: String,

    @SerialName("Cur_ID")
    val id: Int,

    @SerialName("Cur_Name")
    val curName: String,

    @SerialName("Cur_OfficialRate")
    val officialRate: Double,

    @SerialName("Cur_Scale")
    val scale: Int,

    @SerialName("Date")
    val date: String
)

@Serializable
data class CurrencyRemote(
    @SerialName("Cur_Abbreviation")
    val abbreviation: String,

    @SerialName("Cur_ID")
    val curId: Int,

    @SerialName("Cur_Name")
    val curName: String,

    @SerialName("Cur_OfficialRate")
    val officialRate: Double,

    @SerialName("Cur_Scale")
    val curScale: Int,

    @SerialName("Date")
    val date: String
)
