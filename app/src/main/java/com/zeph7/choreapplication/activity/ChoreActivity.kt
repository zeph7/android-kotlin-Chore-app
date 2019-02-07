package com.zeph7.choreapplication.activity

import android.app.ProgressDialog
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.text.TextUtils
import android.widget.Toast
import com.zeph7.choreapplication.data.Chore
import com.zeph7.choreapplication.model.ChoresDatabaseHandler
import com.zeph7.choreapplication.R
import kotlinx.android.synthetic.main.activity_chore.*

class ChoreActivity : AppCompatActivity() {

    var dbHandler: ChoresDatabaseHandler? = null // database handler
    var progressDialog: ProgressDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chore)

        progressDialog = ProgressDialog(this)
        dbHandler = ChoresDatabaseHandler(this)

        var choreId = intent.getStringExtra("chore_id")
        var choreList = dbHandler!!.readChores()

        var chore: Chore = choreList[choreId.toInt()]

        enterChoreEditId.setText(chore.choreName)
        assignedToEditId.setText(chore.assignedTo)
        assignedByEditId.setText(chore.assignedBy)
        val id = chore.id!!

        saveChoreEdit.setOnClickListener {

            if (!TextUtils.isEmpty(enterChoreEditId.text.toString().trim()) &&
                !TextUtils.isEmpty(assignedToEditId.text.toString().trim()) &&
                !TextUtils.isEmpty(assignedByEditId.text.toString().trim())) {

                progressDialog!!.setMessage("Updating...")
                progressDialog!!.show()

                //save to database
                var chore = Chore()
                chore.choreName = enterChoreEditId.text.toString().trim()
                chore.assignedTo = assignedToEditId.text.toString().trim()
                chore.assignedBy = assignedByEditId.text.toString().trim()

                updateDB(chore, id)

                Handler().postDelayed({
                    progressDialog!!.cancel()
                    startActivity(Intent(this, MainActivity::class.java))
                }, 1000)

            } else {
                Toast.makeText(this, "Required fields are empty !!", Toast.LENGTH_LONG).show()
            }
        }

        deleteChoreEdit.setOnClickListener {
            deleteFromDB(id)

            progressDialog!!.setMessage("Deleting...")
            progressDialog!!.show()

            Handler().postDelayed({
                progressDialog!!.cancel()
                startActivity(Intent(this, MainActivity::class.java))
            }, 1000)
        }

        goBackEdit.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
        }
    }

    fun updateDB(chore: Chore, id: Int) {
        dbHandler!!.deleteChore(id)
        dbHandler!!.createChore(chore)
    }

    fun deleteFromDB(id: Int) {
        dbHandler!!.deleteChore(id)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        startActivity(Intent(this, MainActivity::class.java))
    }
}
