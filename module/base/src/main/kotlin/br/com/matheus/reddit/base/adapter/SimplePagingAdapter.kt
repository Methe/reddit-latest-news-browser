package br.com.matheus.reddit.base.adapter

import android.content.Context
import android.view.View
import br.com.matheus.reddit.base.BasePaginationAdapter
import br.com.matheus.reddit.base.ViewBinder

class SimplePagingAdapter<MODEL, VIEW>(creator: (context: Context) -> VIEW) :
        BasePaginationAdapter<MODEL, VIEW>({ context, _ -> creator.invoke(context) })
        where VIEW : View, VIEW : ViewBinder<MODEL>