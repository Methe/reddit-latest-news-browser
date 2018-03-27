package br.com.matheus.reddit.sdk.model.domain

import android.os.Parcel
import br.com.matheus.reddit.sdk.extension.KParcelable
import br.com.matheus.reddit.sdk.extension.parcelableCreator
import com.google.gson.annotations.Expose

data class PostVO(
        @Expose val id: String,
        @Expose val title: String,
        @Expose val numComments: Int,
        @Expose val ups: Int,
        @Expose val downs: Int,
        @Expose val author: String,
        @Expose val selftext: String,
        @Expose val thumbnail: String?
) : KParcelable {

    companion object {
        @JvmField
        val CREATOR = parcelableCreator(::PostVO)
    }

    private constructor(parcel: Parcel) : this(
            id = parcel.readString(),
            title = parcel.readString(),
            numComments = parcel.readInt(),
            ups = parcel.readInt(),
            downs = parcel.readInt(),
            author = parcel.readString(),
            selftext = parcel.readString(),
            thumbnail = parcel.readString()
    )

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeString(id)
        writeString(title)
        writeInt(numComments)
        writeInt(ups)
        writeInt(downs)
        writeString(author)
        writeString(selftext)
        writeString(thumbnail)
    }
}