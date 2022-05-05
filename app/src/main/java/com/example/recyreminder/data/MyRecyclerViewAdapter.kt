package com.example.recyreminder.data

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.recyreminder.R

internal class MyRecyclerViewAdapter(
    private val mNames: List<String>,
    private val mRowLayout: Int
) : RecyclerView.Adapter<MyRecyclerViewAdapter.ViewHolder>() {

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ViewHolder {
        val v = LayoutInflater.from(viewGroup.context).inflate(mRowLayout, viewGroup, false)
        return ViewHolder(v)
    }

    // Binding: The process of preparing a child view to display data corresponding to a position within the adapter.
    override fun onBindViewHolder(viewHolder: ViewHolder, i: Int) {
        viewHolder.mName.text = mNames[i]
    }

    override fun getItemCount(): Int {
        return mNames.size
    }

    class ViewHolder internal constructor(itemView: View) : RecyclerView.ViewHolder(itemView),
        View.OnClickListener {
        internal val mName: TextView = itemView.findViewById(R.id.notification)

        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(view: View) {
            // Heres where anything you would want to happen upon clicking a
            //notification would go
        }
    }
}