package br.com.matheus.reddit.view

import android.content.Context
import android.content.res.TypedArray
import android.support.constraint.ConstraintLayout
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.ImageView
import android.widget.TextView
import br.com.matheus.reddit.R
import br.com.matheus.reddit.base.ViewBinder
import br.com.matheus.reddit.base.delegate.viewProvider
import br.com.matheus.reddit.base.extension.loadUrl
import br.com.matheus.reddit.base.extension.obtain
import br.com.matheus.reddit.base.extension.setCustomTextAppearance
import br.com.matheus.reddit.base.picasso.transformation.CircleTransform
import br.com.matheus.reddit.sdk.model.domain.PostVO

class ItemPostView : ConstraintLayout, ViewBinder<PostVO> {

    // Views
    private val icon: ImageView by viewProvider(R.id.post_icon)
    private val title: TextView by viewProvider(R.id.post_title)
    private val author: TextView by viewProvider(R.id.post_author)
    private val comments: TextView by viewProvider(R.id.post_comments)
    private val ups: TextView by viewProvider(R.id.post_ups)
    private val downs: TextView by viewProvider(R.id.post_downs)

    constructor(context: Context) : this(context, null)

    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, R.attr.itemPostViewStyle)

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        setup(context.obtainStyledAttributes(attrs, R.styleable.ItemPostView, defStyleAttr, 0))
    }

    init {
        View.inflate(context, R.layout.view_item_post, this)
        layoutParams = LayoutParams(MATCH_PARENT, WRAP_CONTENT)
    }

    private fun setup(typedArray: TypedArray) = typedArray.obtain {
        if (id == 0) id = R.id.item_post
        title.setCustomTextAppearance(getResourceId(R.styleable.ItemPostView_titleTextAppearance, 0))
        author.setCustomTextAppearance(getResourceId(R.styleable.ItemPostView_authorTextAppearance, 0))
    }

    override fun bind(model: PostVO) {
        transitionName = model.id

        icon.loadUrl(url = model.thumbnail, placeholder = R.drawable.ic_placeholder, transformation = CircleTransform())
        title.text = model.title
        author.text = model.author
        comments.text = "${model.numComments}"
        ups.text = "${model.ups}"
        downs.text = "${model.downs}"
    }

}