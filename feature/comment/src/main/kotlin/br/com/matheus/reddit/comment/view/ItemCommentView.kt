package br.com.matheus.reddit.comment.view

import android.content.Context
import android.support.constraint.ConstraintLayout
import android.util.AttributeSet
import android.util.TypedValue
import android.view.View
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.TextView
import br.com.matheus.reddit.base.ViewBinder
import br.com.matheus.reddit.base.delegate.viewProvider
import br.com.matheus.reddit.comment.R
import br.com.matheus.reddit.sdk.model.domain.CommentVO


class ItemCommentView : ConstraintLayout, ViewBinder<CommentVO> {

    // Views
    private val author: TextView by viewProvider(R.id.comment_author)
    private val body: TextView by viewProvider(R.id.comment_body)
    private val ups: TextView by viewProvider(R.id.comment_ups)
    private val downs: TextView by viewProvider(R.id.comment_downs)

    constructor(context: Context) : this(context, null)

    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    init {
        View.inflate(context, R.layout.view_item_comment, this)
        layoutParams = LayoutParams(MATCH_PARENT, WRAP_CONTENT)
        val padding = resources.getDimensionPixelOffset(R.dimen.default_padding)
        setPadding(padding, padding, padding, padding)

        val outValue = TypedValue()
        context.theme.resolveAttribute(android.R.attr.selectableItemBackground, outValue, true)
        setBackgroundResource(outValue.resourceId)

        id = R.id.item_comment
    }

    override fun bind(model: CommentVO) {
        author.text = model.author
        body.text = model.body
        ups.text = "${model.ups}"
        downs.text = "${model.downs}"
    }

}