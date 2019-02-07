package com.zeph7.choreapplication.model

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.zeph7.choreapplication.R
import com.zeph7.choreapplication.data.Chore

class ChoreListAdapter (var mCtx: Context, var resource: Int, var items: List<Chore>):
    ArrayAdapter<Chore>(mCtx, resource, items) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {

        val layoutInflater: LayoutInflater = LayoutInflater.from(mCtx)

        // from this view we will get image button and textview
        val view: View = layoutInflater.inflate(resource, null)

        val enterChore: TextView = view.findViewById(R.id.enterChore)
        val assignedTo: TextView = view.findViewById(R.id.assignedTo)
        val assignedBy: TextView = view.findViewById(R.id.assignedBy)
        val createdDate: TextView = view.findViewById(R.id.createdDate)

        val chore: Chore = items[position]

        enterChore.text = chore.choreName
        assignedTo.text = chore.assignedTo
        assignedBy.text = chore.assignedBy
        createdDate .text = chore.showHumanDate(System.currentTimeMillis())

        return view
    }

}