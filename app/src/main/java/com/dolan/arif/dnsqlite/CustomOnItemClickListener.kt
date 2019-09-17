package com.dolan.arif.dnsqlite

import android.view.View

/**
 * Created by Bencoleng on 17/09/2019.
 */
class CustomOnItemClickListener(
    private var position: Int,
    private var onItemClickCallBack: OnItemClickCallBack
) : View.OnClickListener {

    override fun onClick(view: View?) {
        onItemClickCallBack.onItemClicked(view, position)
    }

    interface OnItemClickCallBack {
        fun onItemClicked(view: View?, position: Int)
    }
}