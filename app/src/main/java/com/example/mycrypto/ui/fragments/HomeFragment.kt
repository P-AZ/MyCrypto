package com.example.mycrypto.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.DividerItemDecoration.VERTICAL
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mycrypto.R
import com.example.mycrypto.models.Token
import com.example.mycrypto.ui.adapters.TokenAdapter
import com.example.mycrypto.utils.States
import com.example.mycrypto.viewmodels.TokenViewModel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_home.*

@AndroidEntryPoint
class HomeFragment : Fragment(R.layout.fragment_home) {

    private val tokenViewModel: TokenViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initObservers()
        initUi()
    }

    private fun initUi() {
        initAdapters()
    }

    private fun initAdapters() {
        initTokenAdapter()
    }

    private fun initObservers() {
        tokenViewModel.tokensRegularListEvent.observe(viewLifecycleOwner) {
            (fragmentHome_token_vertical_rv.adapter as TokenAdapter).submitList(it)
        }

        tokenViewModel.state.observe(viewLifecycleOwner) { state ->
            Log.d(TAG, "state = $state")
            when (state) {
                States.Idle -> fragmentHomeProgressBar.visibility = View.GONE
                States.Loading -> fragmentHomeProgressBar.visibility = View.VISIBLE
                States.Deleted -> {
                    val snackBar = Snackbar.make(requireView(), resources.getString(R.string.snackbar_token_deleted), Snackbar.LENGTH_LONG)
                    snackBar.setAction(resources.getString(R.string.snackbar_undo_token_click)) {
                        tokenViewModel.onFavoriteUndoClick()
                    }
                    snackBar.show()
                }
                else -> fragmentHomeProgressBar.visibility = View.GONE
            }
        }
    }

    private fun initTokenAdapter() {
        fragmentHome_token_vertical_rv.apply {
            layoutManager = LinearLayoutManager(context)
            addItemDecoration(DividerItemDecoration(context, VERTICAL))

            val onSymbolClick: (Token) -> Unit = { token ->
                tokenViewModel.onTokenClick(token)
                Log.d("onclickinfragment", token.toString())
            }
            val onFavoriteIconClick: (Token) -> Unit = { token ->
                tokenViewModel.onFavoriteIconClick(token)
            }
            val onOpenBtnClick: (Token) -> Unit = { token ->
//                tokenViewModel.onOpenBtnClick(token)
            }
            adapter = TokenAdapter(onSymbolClick, onFavoriteIconClick, onOpenBtnClick)
        }
    }

    companion object {
        const val TAG = "HomeFragmentTAG"
    }

}