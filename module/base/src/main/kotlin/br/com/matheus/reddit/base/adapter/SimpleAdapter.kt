package br.com.matheus.reddit.base.adapter

import android.content.Context
import android.view.View
import br.com.matheus.reddit.base.BaseRecyclerAdapter
import br.com.matheus.reddit.base.ViewBinder

class SimpleAdapter<MODEL, VIEW>(creator: (context: Context) -> VIEW) :
        BaseRecyclerAdapter<MODEL>({ context, _ -> creator.invoke(context) })
        where VIEW : View, VIEW : ViewBinder<MODEL>