package com.example.jedi_windows.remoteyoutubeplay

import android.support.v7.widget.RecyclerView
import android.support.v7.widget.helper.ItemTouchHelper

/**
 * Created by Jedi-Windows on 16.01.2018..
 */
class SimpleItemTouchHelperCallback(val itemTouchAdapter:ItemTouchHelperAdapter) : ItemTouchHelper.Callback(){
    override fun getMovementFlags(recyclerView: RecyclerView?, viewHolder: RecyclerView.ViewHolder?): Int {
        val dragFlags:Int = ItemTouchHelper.UP or  ItemTouchHelper.DOWN
        var swipeFlags:Int = ItemTouchHelper.START or ItemTouchHelper.END
        return makeMovementFlags(0, swipeFlags)


    }

    override fun onMove(recyclerView: RecyclerView?, viewHolder: RecyclerView.ViewHolder?, target: RecyclerView.ViewHolder?): Boolean {
        itemTouchAdapter.onItemMove(viewHolder?.adapterPosition,target?.adapterPosition)
        return true
     }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder?, direction: Int) {
        itemTouchAdapter.onItemDismiss(viewHolder?.adapterPosition)
    }

    override fun isItemViewSwipeEnabled(): Boolean {
        return true
    }


}