package br.com.matheus.reddit.sdk.model.domain

import android.os.Parcel
import br.com.matheus.reddit.sdk.extension.KParcelable
import br.com.matheus.reddit.sdk.extension.parcelableCreator
import com.google.gson.annotations.Expose

class CommentVO(
        @Expose val id: String,
        @Expose val body: String,
        @Expose val ups: Int,
        @Expose val downs: Int,
        @Expose val author: String
) : KParcelable {

    companion object {
        @JvmField
        val CREATOR = parcelableCreator(::CommentVO)
    }

    private constructor(parcel: Parcel) : this(
            id = parcel.readString(),
            body = parcel.readString(),
            ups = parcel.readInt(),
            downs = parcel.readInt(),
            author = parcel.readString()
    )

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeString(id)
        writeString(body)
        writeInt(ups)
        writeInt(downs)
        writeString(author)
    }
}