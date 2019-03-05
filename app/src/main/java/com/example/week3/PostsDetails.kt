@file:Suppress("DEPRECATION")

package com.example.week3

import android.annotation.SuppressLint
import android.databinding.BindingAdapter
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.text.Editable
import android.text.InputFilter
import android.text.TextWatcher
import android.text.format.DateUtils
import android.util.Log
import android.widget.ImageView
import android.widget.Toast
import com.bumptech.glide.request.RequestOptions
import com.example.week3.databinding.PostDetailBinding
import com.example.week3.modal.Data
import jp.wasabeef.glide.transformations.CropCircleTransformation
import kotlinx.android.synthetic.main.post_detail.*

@SuppressLint("Registered")
class PostsDetails: AppCompatActivity() {
    object DataBindingAdapter{
        @BindingAdapter("imageUrl")
        @JvmStatic
        fun loadImage(view: ImageView, url: String?) {
            GlideApp.with(view.context).load(url)
                .placeholder(R.drawable.image_placeholder)
                .into(view)
        }

        @BindingAdapter("imageUrlAvata")
        @JvmStatic
        fun loadImageAvata(view: ImageView, url: String?) {
            GlideApp.with(view.context).load(url)
                .placeholder(R.drawable.user)
                .apply(RequestOptions.bitmapTransform(CropCircleTransformation()))
                .into(view)
        }
    }


    var maxLength = 140

    lateinit var adapter : AdapterRecyclerViewCmt
    lateinit var arr : ArrayList<String>

    var datum : Data? = null

    var bind : PostDetailBinding? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        title = "Posts"
        datum  = intent.extras.getParcelable("Data")
        Log.d("cmt" ,datum.toString())
        if(datum != null){
            bind = DataBindingUtil.setContentView(this , R.layout.post_detail)
            bind?.data = datum

            txtTimeAgo.text =
                DateUtils.getRelativeTimeSpanString(datum!!.createdTime!! * 1000L , System.currentTimeMillis(), DateUtils.MINUTE_IN_MILLIS).toString()
        }

        val arrayOfInputFilters = arrayOfNulls<InputFilter>(1)
        arrayOfInputFilters[0] = InputFilter.LengthFilter(maxLength)
        edtCmt.filters = arrayOfInputFilters

        edtCmt.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val lengthNow = maxLength - edtCmt.text.length
                txtLength.text = lengthNow.toString()
                btnSend.isEnabled = edtCmt.text.isNotEmpty()
            }

        })

        addArr()

        adapter = AdapterRecyclerViewCmt(applicationContext ,  arr)
        recycler_view_cmt?.layoutManager = LinearLayoutManager(applicationContext , LinearLayoutManager.VERTICAL , false)
        val divi = DividerItemDecoration(applicationContext , LinearLayoutManager.VERTICAL)
        recycler_view_cmt?.addItemDecoration(divi)
        recycler_view_cmt?.adapter = adapter

        img_detail.setOnClickListener {
            val tran = supportFragmentManager
            val dialog = DialogImageFull()

            val bundle = Bundle()
            bundle.putString("url" , datum?.attachments?.data!![0].media?.image?.src)
            dialog.arguments = bundle

            dialog.setStyle(DialogFragment.STYLE_NORMAL, R.style.dialog)

            dialog.show(tran , "dialog")
        }
    }

    private fun addArr() {
        arr = ArrayList()
        arr.add("Ahihi !!!")
        arr.add("Yolo !!!")
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }
}
