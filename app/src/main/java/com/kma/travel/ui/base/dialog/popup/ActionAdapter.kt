package com.kma.travel.ui.base.dialog.popup

import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import com.kma.travel.databinding.ItemPopupBinding


class ActionAdapter(
    private val actions: MutableList<ActionModel>,
    private val onActionClickListener: OnActionClickListener?
) : BaseAdapter() {
    override fun getCount() = actions.size
    override fun getItem(position: Int): ActionModel {
        return actions[position]
    }

    override fun getItemId(position: Int): Long = position.toLong()
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View? {
        val actionHolder: ActionHolder
        if (convertView == null) {
            val binding =
                ItemPopupBinding.inflate(LayoutInflater.from(parent?.context), parent, false)
            actionHolder = ActionHolder(binding)
            actionHolder.binding.root.tag = actionHolder
        } else {
            actionHolder = convertView.tag as ActionHolder
        }
        actionHolder.bind(position)
        return actionHolder.binding.root
    }

    inner class ActionHolder(val binding: ItemPopupBinding) {
        fun bind(position: Int) {
            binding.tvPopmenu.text = actions[position].title
            binding.clPopmenu.setOnClickListener {

//                binding.clPopmenu.setBackgroundColor(R.color.teal_200)
                onActionClickListener?.onItemActionClick(position)
            }
        }
    }

    interface OnActionClickListener {
        fun onItemActionClick(position: Int)
    }

}