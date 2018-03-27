package br.com.matheus.reddit.detail

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.Toolbar
import android.transition.Transition
import android.widget.TextView
import br.com.matheus.reddit.R
import br.com.matheus.reddit.base.BaseActivity
import br.com.matheus.reddit.base.animation.SimpleTransitionListener
import br.com.matheus.reddit.base.delegate.extraProvider
import br.com.matheus.reddit.base.delegate.viewProvider
import br.com.matheus.reddit.base.extension.addStatusBarPadding
import br.com.matheus.reddit.base.extension.enableBack
import br.com.matheus.reddit.base.extension.fragmentTransaction
import br.com.matheus.reddit.comment.list.CommentListFragment
import br.com.matheus.reddit.sdk.model.domain.PostVO
import br.com.matheus.reddit.view.ItemPostView

class PostActivity : BaseActivity() {

    // Extras
    private val post: PostVO? by extraProvider(EXTRA_POST)

    // Views
    private val toolbar: Toolbar by viewProvider(R.id.toolbar)
    private val postInfo: ItemPostView by viewProvider(R.id.post_info)
    private val postContent: TextView by viewProvider(R.id.post_content)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_post)
        toolbar.addStatusBarPadding()
        setSupportActionBar(toolbar)
        supportActionBar.enableBack()

        post?.let {
            supportActionBar?.title = it.title
            postInfo.bind(it)
            postContent.text = it.selftext
        }

        window.enterTransition.addListener(object : SimpleTransitionListener() {
            override fun onTransitionEnd(transition: Transition) {
                fragmentTransaction { add(R.id.comment_content, CommentListFragment.instance(post!!.id), "") }
            }
        })
    }

    override fun onHomeClick() = finishAfterTransition()

    companion object {
        private const val EXTRA_POST = "EXTRA_POST"
        fun intent(context: Context, post: PostVO): Intent {
            return Intent(context, PostActivity::class.java).putExtra(EXTRA_POST, post)
        }
    }

}
