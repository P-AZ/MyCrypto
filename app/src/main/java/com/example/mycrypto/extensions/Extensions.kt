package com.example.mycrypto.extensions

import android.widget.TextView
import androidx.core.content.ContextCompat
import com.example.mycrypto.R
import com.example.mycrypto.models.Token
import com.example.mycrypto.models.TokenAttributes
import com.example.mycrypto.utils.Const

//This object will contain extension functions
object Extensions {

    /** return true if the index is within the list limits */
    fun Int.isIndexListValid(listSize: Int): Boolean = this in 0 until listSize

    /** return true if the float is greather or equal to 0 */
    fun Float.isPositive() = this >= 0

    /** change textView color according to priceUp or priceDown */
    fun TextView.changeTextColor(isPositive: Boolean) {
        val textColor = if(isPositive) R.color.price_change_up else R.color.price_change_down
        setTextColor(ContextCompat.getColor(context, textColor))
    }

    /** update the list with the updated values of the token attribute sent */
    fun List<Token?>.updateList(token: Token, tokenAttribute: TokenAttributes): List<Token?> {
        val index = this.indexOfFirst { it?.id == token.id }
        if(index.isIndexListValid(this.size)) {
            this[index]?.let { currentToken ->
                when(tokenAttribute) {
                    TokenAttributes.IS_FAVORITE -> currentToken.isFavorite = !currentToken.isFavorite
                    TokenAttributes.IS_EXPANDED -> currentToken.isExpended = !currentToken.isExpended
                }
                //creating copy of list due to Diffutill not notice the different
                val tempTokensList = mutableListOf<Token>()
                this.forEach { newToken ->
                    newToken?.let {
                        tempTokensList.add(it.copy())
                    }
                }
                return tempTokensList
            }
        }
        return this
    }
}