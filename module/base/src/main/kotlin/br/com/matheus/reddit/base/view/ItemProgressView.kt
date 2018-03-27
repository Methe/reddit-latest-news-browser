package br.com.matheus.reddit.base.view

import android.content.Context
import android.support.v4.content.ContextCompat.getDrawable
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.FrameLayout
import android.widget.ProgressBar
import br.com.matheus.reddit.base.R
import br.com.matheus.reddit.base.ViewBinder
import br.com.matheus.reddit.base.delegate.viewProvider
import br.com.matheus.reddit.base.extension.isAnimationEnabled

class ItemProgressView : FrameLayout, ViewBinder<Boolean> {

    private val progress: ProgressBar by viewProvider(R.id.progress)
    private val error: View by viewProvider(R.id.error)

    constructor(context: Context) : this(context, null)

    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : this(context, attrs, defStyleAttr, 0)

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes)

    init {
        id = R.id.item_progress
        layoutParams = LayoutParams(MATCH_PARENT, WRAP_CONTENT)
        View.inflate(context, R.layout.view_item_progress, this)

        if (!isAnimationEnabled) {
            progress.indeterminateDrawable = getDrawable(context, android.R.drawable.ic_menu_revert)
        }
    }

    override fun bind(model: Boolean) {
        progress.visibility = if (model) GONE else VISIBLE
        error.visibility = if (model) VISIBLE else GONE
    }

    override fun setOnClickListener(l: OnClickListener?) {
        error.setOnClickListener(l)
    }

}