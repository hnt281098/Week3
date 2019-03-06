package com.example.week3

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

class AdapterRecyclerViewCmt(val context : Context, private val arrCmt: ArrayList<String>): RecyclerView.Adapter<AdapterRecyclerViewCmt.ViewHolder>() {
    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_recycler_view_cmt , p0 , false))
    }

    override fun getItemCount(): Int {
        return arrCmt.size
    }

    override fun onBindViewHolder(p0: ViewHolder, p1: Int) {
        p0.txtCmt.text = arrCmt[p1]
    }

    class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        var txtCmt : TextView = itemView.findViewById(R.id.txtCmt)
    }
}