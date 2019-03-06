@file:Suppress("DEPRECATION")

package com.example.week3

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.support.v7.widget.RecyclerView
import android.text.format.DateUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.bumptech.glide.request.RequestOptions.bitmapTransform
import com.example.week3.modal.*
import jp.wasabeef.glide.transformations.CropCircleTransformation

@Suppress("DEPRECATION", "CAST_NEVER_SUCCEEDS")
class AdapterRecyclerView(val context: Context, private val arrData: ArrayList<Data>): RecyclerView.Adapter<AdapterRecyclerView.ViewHolder>() {
    enum class Type{
        profile_media,
        share,
        cover_photo
    }

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_recycler_view , p0 , false))
    }

    override fun getItemCount(): Int {
        return arrData.size
    }

    override fun onBindViewHolder(p0: ViewHolder, p1: Int) {
        val data = arrData[p1]

        p0.txtDate.text = DateUtils.getRelativeTimeSpanString(data.createdTime!! * 1000L , System.currentTimeMillis(),DateUtils.MINUTE_IN_MILLIS).toString()

        if(data.attachments?.data?.get(0)?.url != null){
            p0.txtLink.text = data.attachments.data[0].url
            p0.txtLink.visibility = View.VISIBLE
        }
        else {
            p0.txtLink.visibility = View.GONE
        }

        val typeText = when(data.attachments?.data?.get(0)?.type){
            Type.share.name -> " shared"
            Type.cover_photo.name -> " changed the cover photo"
            Type.profile_media.name -> " changed the avatar"
            else -> ""
        }
        p0.txtType.text = typeText

        p0.txt.text = data.from?.name

        if(data.message == null)
            p0.txtStatus.visibility = View.GONE
        else {
            p0.txtStatus.visibility = View.VISIBLE
            if(!data.message.any { it == '#' })
                p0.txtStatus.text = data.message
            else{
                p0.txtStatus.text = data.message
                p0.txtStatus.setTextColor(Color.BLACK)
            }

        }
        GlideApp.with(context).load(data.attachments?.data?.get(0)?.media?.image?.src)
            .placeholder(R.drawable.image_placeholder)
            .into(p0.img)
        GlideApp.with(context).load(data.from?.picture?.data?.url)
            .placeholder(R.drawable.user)
            .apply(bitmapTransform(CropCircleTransformation()))
            .into(p0.imgAvatar)

        p0.txtLike.setOnClickListener {
            p0.txtLike.setCompoundDrawablesWithIntrinsicBounds(R.drawable.fb_liked, 0, 0, 0)
            p0.txtLike.setTextColor(Color.parseColor("#3578e5"))
        }

        p0.linear.setOnClickListener {
            val i = Intent(context , PostsDetails::class.java)
            i.putExtra("Data" , data)
            context.startActivity(i)
        }
    }

    class ViewHolder(item : View) : RecyclerView.ViewHolder(item){
        var txt : TextView = item.findViewById(R.id.txt)
        var txtDate : TextView = item.findViewById(R.id.txtDate)
        var txtLike : TextView = item.findViewById(R.id.txtLike)
        var txtLink : TextView = item.findViewById(R.id.txtLink)
        var txtType : TextView = item.findViewById(R.id.txtType)
//        var txtComment : TextView = item.findViewById(R.id.txtComment)
//        var txtShare : TextView = item.findViewById(R.id.txtShare)
        var txtStatus : TextView = item.findViewById(R.id.txtStatus)
        var img : ImageView = item.findViewById(R.id.img)
        var imgAvatar : ImageView = item.findViewById(R.id.imgAvatar)
        var linear : LinearLayout = item.findViewById(R.id.linear)
    }
}