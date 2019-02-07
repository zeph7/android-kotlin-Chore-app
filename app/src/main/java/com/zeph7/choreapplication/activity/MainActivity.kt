package com.zeph7.choreapplication.activity

import android.app.AlertDialog
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.animation.AnimationUtils
import android.widget.ListView
import android.widget.Toast
import com.zeph7.choreapplication.data.Chore
import com.zeph7.choreapplication.model.ChoreListAdapter
import com.zeph7.choreapplication.model.ChoresDatabaseHandler
import com.zeph7.choreapplication.R
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.popup.view.*

class MainActivity : AppCompatActivity() {

    lateinit var listView: ListView
    var adapter: ChoreListAdapter? = null
    var dialogBuilder: AlertDialog.Builder? = null // class that creates popups
    var dialog: AlertDialog? = null
    var choreList: ArrayList<Chore>? = null // list of chores
    var choreListItems: ArrayList<Chore>? = null
    var dbHandler: ChoresDatabaseHandler? = null // database handler

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        dbHandler = ChoresDatabaseHandler(this)

        var fade = AnimationUtils.loadAnimation(applicationContext, R.anim.fade)

        choreList = ArrayList<Chore>()
        choreListItems = ArrayList<Chore>()
        adapter = ChoreListAdapter(
            this,
            R.layout.chore_list_item,
            choreListItems!!
        )

        // setup list = listview
        listView = findViewById<ListView>(R.id.listViewId)
        listView.adapter = adapter

        // load chores
        choreList = dbHandler!!.readChores()
        choreList!!.reverse()
        for (c in choreList!!.iterator()) {

            val chore = Chore()
            chore.choreName = "${c.choreName}"
            chore.assignedBy = "assigned by : ${c.assignedBy}"
            chore.assignedTo = "assigned to : ${c.assignedTo}"
            chore.id = c.id
            chore.showHumanDate(c.timeAssigned!!)

            choreListItems!!.add(chore)
        }

        adapter!!.notifyDataSetChanged()

        addChoreId.setOnClickListener {
            createPopDialog()
        }

        listView.setOnItemClickListener { parent, view, position, id ->
            view.startAnimation(fade)
            startActivity(Intent(this, ChoreActivity::class.java).putExtra("chore_id",
                (choreListItems!!.size - id - 1).toString()))
        }
    }

    fun createPopDialog () {
        var view = layoutInflater.inflate(R.layout.popup, null)
        var choreName = view.popEnterChoreId
        var assignedTo = view.popAssignedToId
        var assignedBy = view.popAssignedById
        var saveButton = view.popSaveChore

        dialogBuilder = AlertDialog.Builder(this).setView(view)
        dialog = dialogBuilder!!.create()
        dialog?.show()

        saveButton.setOnClickListener {
            if (!TextUtils.isEmpty(choreName.text.toString().trim()) && !TextUtils.isEmpty(assignedTo.text.
                    toString().trim()) && !TextUtils.isEmpty(assignedBy.text.toString().trim())) {
                var chore = Chore()

                chore.choreName = choreName.text.toString().trim()
                chore.assignedTo = assignedTo.text.toString().trim()
                chore.assignedBy = assignedBy.text.toString().trim()

                dbHandler!!.createChore(chore)

                dialog!!.dismiss()
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            } else {
                Toast.makeText(this, "Required fields are empty !!", Toast.LENGTH_LONG).show()
            }
        }
    }

    fun checkDB() {
        if (dbHandler!!.getChoresCount() <= 0) {
            startActivity(Intent(this, EnterChoreActivity::class.java))
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        moveTaskToBack(true)
    }
}
