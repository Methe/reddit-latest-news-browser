package br.com.matheus.reddit.comment.list

import android.os.Bundle
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.transition.Fade
import android.transition.Transition
import android.transition.TransitionManager
import android.view.View
import android.view.ViewGroup
import br.com.matheus.reddit.base.BaseFragment
import br.com.matheus.reddit.base.adapter.SimplePagingAdapter
import br.com.matheus.reddit.base.animation.SimpleTransitionListener
import br.com.matheus.reddit.base.delegate.extraProvider
import br.com.matheus.reddit.base.delegate.viewModelProvider
import br.com.matheus.reddit.base.delegate.viewProvider
import br.com.matheus.reddit.base.extension.toast
import br.com.matheus.reddit.base.statemachine.ViewStateMachine
import br.com.matheus.reddit.base.view.WarningView
import br.com.matheus.reddit.comment.R
import br.com.matheus.reddit.comment.view.ItemCommentView
import br.com.matheus.reddit.sdk.model.domain.CommentVO


class CommentListFragment : BaseFragment(R.layout.fragment_comment_list) {

    private val viewModel by viewModelProvider(CommentListViewModel::class)

    private val stateMachine = ViewStateMachine()
    private val adapter = SimplePagingAdapter(::ItemCommentView)
            .withListener(this::onCommentClick)

    // Extras
    private val postId: String? by extraProvider(POST_ID)

    // Views
    private val root: ViewGroup by viewProvider(R.id.comment_list_root)
    private val emptyView: WarningView by viewProvider(R.id.empty_view)
    private val loadingView: View by viewProvider(R.id.progress_view)
    private val errorView: WarningView by viewProvider(R.id.error_view)
    private val recyclerView: RecyclerView by viewProvider(R.id.recycler_view)

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
        recyclerView.adapter = adapter

        errorView.setOnClickListener {
            viewModel.getCommentPageLiveData(postId!!).invalidate()
            startObserveData()
        }

        setupStateMachine(savedInstanceState?.getBundle(STATE_MACHINE))
        startObserveData()
    }

    private fun setupStateMachine(bundle: Bundle?) = stateMachine.setup(initialState = LOADING_STATE, restoreState = bundle) {
        add(SUCCESS_STATE) {
            visibles(recyclerView)
            gones(loadingView, emptyView, errorView)
        }
        add(LOADING_STATE) {
            visibles(loadingView)
            gones(recyclerView, emptyView, errorView)
        }
        add(EMPTY_STATE) {
            visibles(emptyView)
            gones(loadingView, recyclerView, errorView)
        }
        add(ERROR_STATE) {
            visibles(errorView)
            gones(loadingView, emptyView, recyclerView)
        }

        onChangeState {
            val transition = Fade().apply {
                duration = 600L
                addListener(object : SimpleTransitionListener() {
                    override fun onTransitionEnd(transition: Transition) =
                            recyclerView.invalidateItemDecorations()
                })
            }
            TransitionManager.beginDelayedTransition(root, transition)
        }
    }

    private fun startObserveData() = with(viewModel.getCommentPageLiveData(postId!!)) {
        observeSingleLoading(this@CommentListFragment) {
            if (it) stateMachine.changeState(LOADING_STATE)
        }

        observeSingleError(this@CommentListFragment) {
            stateMachine.changeState(ERROR_STATE)
        }

        observeSingleData(this@CommentListFragment) {
            stateMachine.changeState(if (it.isNotEmpty()) SUCCESS_STATE else EMPTY_STATE)
            adapter.addPage(it)
        }
    }

    @Suppress("UNUSED_PARAMETER")
    private fun onCommentClick(comment: CommentVO) {
        context?.toast(R.string.comment_list_coming_soon)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putBundle(STATE_MACHINE, stateMachine.saveInstanceState())
    }

    companion object {

        private const val POST_ID = "POST_ID"

        private const val SUCCESS_STATE = 0
        private const val LOADING_STATE = 1
        private const val EMPTY_STATE = 2
        private const val ERROR_STATE = 3
        private const val STATE_MACHINE = "STATE_MACHINE"

        fun instance(postId: String): CommentListFragment {
            val bundle = Bundle()
            bundle.putString(POST_ID, postId)
            return CommentListFragment().apply { arguments = bundle }
        }
    }
}