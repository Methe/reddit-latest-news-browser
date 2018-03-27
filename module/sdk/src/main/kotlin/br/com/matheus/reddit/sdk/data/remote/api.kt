package br.com.matheus.reddit.sdk.data.remote

import br.com.matheus.reddit.sdk.BuildConfig
import br.com.matheus.reddit.sdk.data.remote.adapter.DateTypeAdapter
import br.com.matheus.reddit.sdk.data.remote.factory.LiveDataCallAdapterFactory
import br.com.matheus.reddit.sdk.data.remote.interceptor.ResponseInterceptor
import br.com.matheus.reddit.sdk.liveData.ResponseLiveData
import br.com.matheus.reddit.sdk.model.DateType
import br.com.matheus.reddit.sdk.model.Page
import br.com.matheus.reddit.sdk.model.Result
import br.com.matheus.reddit.sdk.model.domain.CommentVO
import br.com.matheus.reddit.sdk.model.domain.PostVO
import com.google.gson.FieldNamingPolicy
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import okhttp3.logging.HttpLoggingInterceptor.Level
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import timber.log.Timber

internal interface Api {

    //region Post

    @GET("r/Android/new.json")
    fun listProjects(@Query("after") afterPage: String,
                     @Query("limit") limit: Int = 10): ResponseLiveData<Result<Page<Result<PostVO>>>>

    //endregion


    //region Comment

    @GET("r/Android/comments/{postId}.json")
    fun listCommentFromPost(@Path("postId") postId: String,
                            @Query("after") afterPage: String,
                            @Query("limit") limit: Int = 10): ResponseLiveData<List<Result<Page<Result<CommentVO>>>>>

    //endregion

}

internal val gson = GsonBuilder()
        .registerTypeAdapter(DateType::class.java, DateTypeAdapter())
        .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
        .create()

internal val apiInstance: Api = Retrofit.Builder()
        .addConverterFactory(buildGson())
        .client(buildClient())
        .addCallAdapterFactory(LiveDataCallAdapterFactory())
        .baseUrl(BuildConfig.BASE_URL).build().create(Api::class.java)

private fun buildClient(): OkHttpClient {
    val logging = HttpLoggingInterceptor { Timber.i(it) }.setLevel(Level.BODY)
    val response = ResponseInterceptor()

    return OkHttpClient.Builder()
            .addInterceptor(logging)
            .addInterceptor(response)
            .build()
}

private fun buildGson() = GsonConverterFactory.create(gson)