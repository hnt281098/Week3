package com.example.week3

import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import kotlinx.android.synthetic.main.image_full.view.*

class DialogImageFull : DialogFragment() {


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return LayoutInflater.from(context).inflate(R.layout.image_full , container , false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val img : ImageView = view.imageViewFull


//        GlideApp.with(context!!).load(arguments?.getString("url")).into(img)
    }

    override fun onResume() {
        super.onResume()
        val window = dialog.window
        window!!.decorView.setOnTouchListener(
            SwipeDismissTouchListener(
                window.decorView,
                null,
                object : SwipeDismissTouchListener.DismissCallbacks {
                    override fun canDismiss(token: Any?): Boolean {
                        return true
                    }

                    override fun onDismiss(view: View, token: Any?) {
                        dismiss()
                    }
                })
        )
    }
}