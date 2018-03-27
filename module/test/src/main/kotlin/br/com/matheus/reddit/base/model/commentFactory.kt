package br.com.matheus.reddit.base.model

import br.com.matheus.reddit.sdk.model.Page
import br.com.matheus.reddit.sdk.model.domain.CommentVO

fun randomComment(): CommentVO {
    return CommentVO("12", "Body", 15, 35, "Author")
}

fun commentList(): List<CommentVO> {
    return arrayListOf(randomComment())
}

fun commentPage(): Page<CommentVO> {
    return pageWith(randomComment())
}