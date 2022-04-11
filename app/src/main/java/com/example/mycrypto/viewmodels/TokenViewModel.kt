package com.example.mycrypto.viewmodels

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mycrypto.extensions.Extensions.isIndexListValid
import com.example.mycrypto.extensions.Extensions.updateList
import com.example.mycrypto.models.Token
import com.example.mycrypto.models.TokenAttributes
import com.example.mycrypto.repos.TokenRepo
import com.example.mycrypto.utils.States
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

//Using dependency injection to inject TokenRepo into TokenViewModel constructor
@HiltViewModel
class TokenViewModel @Inject constructor (private val tokenRepo: TokenRepo) : ViewModel() {

    // Hold the states of the application
    val state = MutableLiveData<States>().apply {
        postValue(States.Idle)
    }
    // creating 2 list to separate responsibility -> one list to observe and one list for manipulation
    private var tokensRegularList = mutableListOf<Token?>()
    val tokensRegularListEvent = MutableLiveData<List<Token?>?>()
    private var tokensFavoriteList = mutableListOf<Token>()
    val tokensFavoriteListEvent = MutableLiveData<List<Token?>?>()

    lateinit var lastTokenInteract: Token

    // Moved the initialization due to BottomNav + Navgraph calling oncreatedview multiply time.
    init {
        initRepoData()
        initRoomData()
    }

    //initRepoData for future scalability incase of more then one
    private fun initRepoData() {
        getAllTokens()
    }

    //initRoomData for future scalability incase of more then one
    private fun initRoomData() {
        getAllFavorites()
    }

    // getAllTokens update tokensFavoriteList with top 100 cryptocurrency
    private fun getAllTokens() {
        viewModelScope.launch(Dispatchers.IO ) {
            state.postValue(States.Loading)
            tokensRegularList = tokenRepo.getAllTokens()?.toMutableList() ?: mutableListOf()

            //Assign isFavorite = true if token is in favoriteList
            tokensRegularList.forEach { regularToken ->
                regularToken?.let { regularTokenNotNull ->
                    tokensFavoriteList.find { it.id == regularTokenNotNull.id }?.let {
                        regularToken.isFavorite = true
                        return@forEach
                    }
                }
            }
            tokensRegularListEvent.postValue(tokensRegularList)
//            Log.d(TAG, tokenList.toString())
            state.postValue(States.Idle)
        }
    }

    //getAllFavorites update favoriteList with all favorites token in db
     private fun getAllFavorites() {
        viewModelScope.launch(Dispatchers.IO) {
            state.postValue(States.Loading)
            tokenRepo.getAllFavorites().collect { favoritesList ->
                tokensFavoriteList = favoritesList.toMutableList()
                tokensFavoriteListEvent.postValue(tokensFavoriteList)
                state.postValue(States.Idle)
            }
        }
    }

    //addTokenFavorites add token to db
    private fun addTokenFavorites(token:Token) {
        viewModelScope.launch(Dispatchers.IO) {
            state.postValue(States.Loading)
            tokenRepo.insertTokenFavorites(token)
            state.postValue(States.Idle)
        }
    }

    //updateTokenFavorites update token in db
    private fun updateTokenFavorites(token: Token) {
        viewModelScope.launch(Dispatchers.IO) {
            state.postValue(States.Loading)
            tokenRepo.updateTokenFavorites(token)
            state.postValue(States.Idle)
        }
    }

    //deleteTokenFavorites delete token from db
    private fun deleteTokenFavorites(token: Token) {
        viewModelScope.launch(Dispatchers.IO) {
            state.postValue(States.Loading)
            tokenRepo.deleteTokenFavorites(token)
            state.postValue(States.Deleted)
            state.postValue(States.Idle)
        }
    }

    //onTokenClick responsible to update tokensRegularList or db according to which fragment it been press from
    fun onTokenClick(token: Token) {
        tokensRegularListEvent.postValue(tokensRegularList.updateList(token, TokenAttributes.IS_EXPANDED))
        val tempToken = token.copy()
        tempToken.isExpended = !tempToken.isExpended
        updateTokenFavorites(tempToken)
    }

    //onFavoriteIconClick responsible to update tokensRegularList or db according to which fragment it been press from
    fun onFavoriteIconClick(token: Token) {
        val tempToken = token.copy()
        //Updating regular list
        tokensRegularListEvent.postValue(tokensRegularList.updateList(token, TokenAttributes.IS_FAVORITE))

        //updating favorite list
        val tokenFavoritesListIndex = tokensFavoriteList.indexOfFirst { it.id == tempToken.id }
        if(tokenFavoritesListIndex.isIndexListValid(tokensFavoriteList.size)) {
            deleteTokenFavorites(tempToken)
        } else {
            tempToken.isExpended = false
            tempToken.isFavorite = true
            addTokenFavorites(tempToken)
        }
        lastTokenInteract = tempToken.copy()
    }

    //onFavoriteUndoClick will call onFavoriteIconClick with lastTokenInteract to undo changes
    fun onFavoriteUndoClick() {
        try {
            onFavoriteIconClick(lastTokenInteract)
        } catch (e: Exception) {
            Log.e(TAG, e.toString())
        }
    }

    //This function will open dialog of single token
    fun onOpenBtnClick(token: Token) { }

    companion object {
        const val TAG = "TokenViewModelTAG"
    }
}