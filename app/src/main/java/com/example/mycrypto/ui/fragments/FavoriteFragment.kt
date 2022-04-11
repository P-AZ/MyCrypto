package com.example.mycrypto.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mycrypto.R
import com.example.mycrypto.models.Token
import com.example.mycrypto.ui.adapters.TokenAdapter
import com.example.mycrypto.utils.States
import com.example.mycrypto.viewmodels.TokenViewModel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_favorite.*

@AndroidEntryPoint
class FavoriteFragment : Fragment(R.layout.fragment_favorite) {

    private val tokenViewModel: TokenViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUi()
        initObservers()
    }

    private fun initUi() {
        initAdapters()
    }

    private fun initAdapters() {
        initTokenAdapter()
    }
    private fun initTokenAdapter() {
        fragmentFavorite_token_vertical_rv.apply {
            layoutManager = LinearLayoutManager(context)
            //Adding separation lines between items
            addItemDecoration(
                DividerItemDecoration(context, DividerItemDecoration.VERTICAL)
            )

            //Lambda function for token clicks
            val onTokenClick: (Token) -> Unit = {
                tokenViewModel.onTokenClick(it)
            }

            //Lambda function for favorites icon clicks
            val onFavoriteClick: (Token) -> Unit = {
                tokenViewModel.onFavoriteIconClick(it)
            }

            //Lambda function for open button clicks
            val onOpenBtnClick: (Token) -> Unit = { token ->
                tokenViewModel.onOpenBtnClick(token)
            }

            //Initializing the apapter
            adapter = TokenAdapter(onTokenClick, onFavoriteClick, onOpenBtnClick)
        }
    }

    private fun initObservers() {

        tokenViewModel.tokensFavoriteListEvent.observe(viewLifecycleOwner) { tokenFavoritesList ->
            (fragmentFavorite_token_vertical_rv.adapter as TokenAdapter).submitList(tokenFavoritesList)
        }

        tokenViewModel.state.observe(viewLifecycleOwner) { state ->
            when (state) {
                States.Idle -> fragmentFavoritesProgressBar.visibility = View.GONE
                States.Loading -> fragmentFavoritesProgressBar.visibility = View.VISIBLE
                States.Deleted -> {

                    val snackBar = Snackbar.make(requireView(), resources.getString(R.string.snackbar_token_deleted), Snackbar.LENGTH_LONG)
                    snackBar.setAction(resources.getString(R.string.snackbar_undo_token_click)) {
                        tokenViewModel.onFavoriteUndoClick()
                    }
                    snackBar.show()
                }
                else -> fragmentFavoritesProgressBar.visibility = View.GONE
            }
        }
    }
}