package com.example.mycrypto.models

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey

//Using interface for scalability
interface IToken {
    val id: Long?
    val name: String?
    val symbol: String?
    val cmc_rank: Long?
    val circulating_supply: Float?
    val max_supply: Float?
    val quote: Quote?
}

//@Parcelize
@Entity
data class Token(
    @PrimaryKey(autoGenerate = true)
    override val id: Long?,
    override val name: String?,
    override val symbol: String?,
    override val cmc_rank: Long?,
    override val circulating_supply: Float?,
    override val max_supply: Float?,
    @Embedded override val quote: Quote?,
    var isExpended: Boolean = false,
    var isFavorite: Boolean = false
) : IToken
//, Parcelable

data class Quote(
    @Embedded val USD: Usd?
)

data class Usd(
    val price: Float?,
    val percent_change_24h: Float?,
    val market_cap: Float?,
    val fully_diluted_market_cap: Float?
)

//Using another data class that will hold the return value that the server returns
data class TokenResponse (
    val data: List<Token>
)