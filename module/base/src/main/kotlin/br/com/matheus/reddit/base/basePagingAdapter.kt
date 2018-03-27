package br.com.matheus.reddit.base

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.View
import br.com.matheus.reddit.base.view.ItemProgressView
import br.com.matheus.reddit.sdk.model.Page

private const val PROGRESS_TYPE = 1

open class BasePaginationAdapter<MODEL, VIEW>(creator: (context: Context, type: Int) -> VIEW) :
        BaseRecyclerAdapter<MODEL>({ context, type ->
            if (type == PROGRESS_TYPE) ItemProgressView(context)
            else creator.invoke(context, type)
        })
        where VIEW : View, VIEW : ViewBinder<MODEL> {

    private var onLoadMore: ((nextPage: String, isRetry: Boolean) -> Unit)? = null

    private var hasLoadingItem: Boolean = false
        get() = onLoadMore != null && field

    private var hasError: Boolean = false
    private var nextPage: String? = ""

    override fun getItemViewType(position: Int) =
            if (position == items.size) PROGRESS_TYPE
            else super.getItemViewType(position)

    override fun getItemCount() =
            super.getItemCount() + if (hasLoadingItem) 1 else 0

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        if (getItemViewType(position) == PROGRESS_TYPE)
            bindHolder(holder, hasError, ::onErrorClick)
        else
            super.onBindViewHolder(holder, position)
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        recyclerView.clearOnChildAttachStateChangeListeners()
        recyclerView.addOnChildAttachStateChangeListener(childAttachListener())
    }

    override fun onDetachedFromRecyclerView(recyclerView: RecyclerView) {
        super.onDetachedFromRecyclerView(recyclerView)
        recyclerView.clearOnChildAttachStateChangeListeners()
    }

    fun addPage(page: Page<MODEL>) {
        hasError = false
        if (hasLoadingItem) removeLoadingItem()

        addAll(page.children)
        nextPage = page.after

        if (!nextPage.isNullOrEmpty()) insertLoadingItem()
    }

    fun failPage() {
        if (!hasLoadingItem) return
        hasError = true

        notifyItemChanged(itemCount - 1)
    }

    @Suppress("UNUSED_PARAMETER")
    private fun onErrorClick(hasError: Boolean) {
        this.hasError = false
        notifyItemChanged(itemCount - 1)
        nextPage?.let { onLoadMore?.invoke(it, true) }
    }

    private fun insertLoadingItem() {
        notifyItemInserted(itemCount)
        hasLoadingItem = true
    }

    private fun removeLoadingItem() {
        hasLoadingItem = false
        notifyItemRemoved(itemCount)
    }

    private fun childAttachListener(): RecyclerView.OnChildAttachStateChangeListener {
        return object : RecyclerView.OnChildAttachStateChangeListener {
            override fun onChildViewAttachedToWindow(view: View) {
                view.id.takeIf { it == R.id.item_progress }?.takeUnless { hasError }?.let {
                    nextPage?.let { onLoadMore?.invoke(it, false) }
                }
            }

            override fun onChildViewDetachedFromWindow(view: View) {}
        }
    }

    override fun withListener(onItemClick: (MODEL) -> Unit): BasePaginationAdapter<MODEL, VIEW> {
        super.withListener(onItemClick)
        return this
    }

    override fun <V : ViewBinder<MODEL>> withListener(onItemClick: (MODEL, V) -> Unit): BasePaginationAdapter<MODEL, VIEW> {
        super.withListener(onItemClick)
        return this
    }

    fun withLoadMore(onLoadMore: ((nextPage: String, isRetry: Boolean) -> Unit)): BasePaginationAdapter<MODEL, VIEW> {
        this.onLoadMore = onLoadMore
        return this
    }

}