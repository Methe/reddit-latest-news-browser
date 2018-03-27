package br.com.matheus.reddit.base.model

import br.com.matheus.reddit.sdk.model.Page
import br.com.matheus.reddit.sdk.model.domain.PostVO

fun randomPost(): PostVO {
    return PostVO("12", "Title", 10, 30, 50, "ala", "Yohoho", "")
}

fun postList(): List<PostVO> {
    return arrayListOf(randomPost())
}

fun postPage(): Page<PostVO> {
    return pageWith(randomPost())
}

fun postPageWithNext(): Page<PostVO> {
    return pageWith(randomPost(), randomPost(), randomPost(), randomPost(), randomPost(), randomPost(), after = "after")
}