package br.com.matheus.reddit.list

import android.os.Bundle
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import android.transition.Fade
import android.transition.Slide
import android.transition.Transition
import android.transition.TransitionManager
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import br.com.matheus.reddit.R
import br.com.matheus.reddit.base.BaseActivity
import br.com.matheus.reddit.base.adapter.SimplePagingAdapter
import br.com.matheus.reddit.base.animation.SimpleTransitionListener
import br.com.matheus.reddit.base.delegate.viewModelProvider
import br.com.matheus.reddit.base.delegate.viewProvider
import br.com.matheus.reddit.base.extension.addStatusBarPadding
import br.com.matheus.reddit.base.extension.createOptionsWithElements
import br.com.matheus.reddit.base.statemachine.ViewStateMachine
import br.com.matheus.reddit.base.view.WarningView
import br.com.matheus.reddit.detail.PostActivity
import br.com.matheus.reddit.sdk.model.domain.PostVO
import br.com.matheus.reddit.view.ItemPostView

private const val SUCCESS_STATE = 0
private const val LOADING_STATE = 1
private const val EMPTY_STATE = 2
private const val ERROR_STATE = 3
private const val STATE_MACHINE = "STATE_MACHINE"

class PostListActivity : BaseActivity() {

    private val viewModel by viewModelProvider(PostListViewModel::class)

    private val stateMachine = ViewStateMachine()
    private val adapter = SimplePagingAdapter(::ItemPostView)
            .withLoadMore(this::onLoadMore)
            .withListener(this::onItemCLick)

    // Views
    private val root: ViewGroup by viewProvider(R.id.post_list_root)
    private val toolbar: Toolbar by viewProvider(R.id.toolbar)
    private val emptyView: WarningView by viewProvider(R.id.empty_view)
    private val loadingView: View by viewProvider(R.id.progress_view)
    private val errorView: WarningView by viewProvider(R.id.error_view)
    private val recyclerView: RecyclerView by viewProvider(R.id.recycler_view)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_post_list)
        toolbar.addStatusBarPadding()
        setSupportActionBar(toolbar)

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))
        recyclerView.adapter = adapter
        errorView.setOnClickListener {
            viewModel.postListLiveData.invalidate()
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
            val transition = if (it == SUCCESS_STATE) Slide(Gravity.BOTTOM) else Fade()
            transition.duration = 600L
            transition.addListener(object : SimpleTransitionListener() {
                override fun onTransitionEnd(transition: Transition) =
                        recyclerView.invalidateItemDecorations()
            })
            TransitionManager.beginDelayedTransition(root, transition)
        }
    }

    private fun startObserveData() = with(viewModel.postListLiveData) {
        observeSingleLoading(this@PostListActivity) {
            if (it) stateMachine.changeState(LOADING_STATE)
        }

        observeSingleError(this@PostListActivity) {
            stateMachine.changeState(ERROR_STATE)
        }

        observeSingleData(this@PostListActivity) {
            stateMachine.changeState(if (it.isNotEmpty()) SUCCESS_STATE else EMPTY_STATE)
            adapter.addPage(it)
        }
    }

    private fun onItemCLick(post: PostVO, view: ItemPostView) {
        val options = createOptionsWithElements(view)
        startActivity(PostActivity.intent(this, post), options.toBundle())
    }

    private fun onLoadMore(nextPage: String, isRetry: Boolean) {
        with(viewModel.getNextPostPageLiveData(nextPage)) {
            if (isRetry) invalidate()
            observeSingleData(this@PostListActivity, adapter::addPage)
            observeSingleError(this@PostListActivity) { adapter.failPage() }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putBundle(STATE_MACHINE, stateMachine.saveInstanceState())
    }

}