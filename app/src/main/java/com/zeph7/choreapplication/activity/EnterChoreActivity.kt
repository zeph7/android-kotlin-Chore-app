package com.zeph7.choreapplication.activity

import android.app.ProgressDialog
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.text.TextUtils
import android.widget.Toast
import com.zeph7.choreapplication.model.ChoresDatabaseHandler
import com.zeph7.choreapplication.data.Chore
import com.zeph7.choreapplication.R
import kotlinx.android.synthetic.main.activity_enter_chore.*

class EnterChoreActivity : AppCompatActivity() {
    var dbHandler: ChoresDatabaseHandler? = null
    var progressDialog: ProgressDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_enter_chore)

        progressDialog = ProgressDialog(this)
        dbHandler = ChoresDatabaseHandler(this)

        checkDB()

        var choreList = mutableListOf<Chore>()

        saveChore.setOnClickListener {

            if (!TextUtils.isEmpty(EnterChoreId.text.toString().trim()) &&
                !TextUtils.isEmpty(AssignedToId.text.toString().trim()) &&
                !TextUtils.isEmpty(AssignedById.text.toString().trim())) {

                progressDialog!!.setMessage("Saving...")
                progressDialog!!.show()

                //save to database
                var chore = Chore()
                chore.choreName = EnterChoreId.text.toString().trim()
                chore.assignedTo = AssignedToId.text.toString().trim()
                chore.assignedBy = AssignedById.text.toString().trim()

                saveToDB(chore)

                Handler().postDelayed({
                    progressDialog!!.cancel()
                    startActivity(Intent(this, MainActivity::class.java))
                }, 1000)

            } else {
                Toast.makeText(this, "Required fields are empty !!", Toast.LENGTH_LONG).show()
            }
        }
    }

    fun checkDB() {
        if (dbHandler!!.getChoresCount() > 0) {
            startActivity(Intent(this, MainActivity::class.java))
        }
    }

    fun saveToDB(chore: Chore) {
        dbHandler!!.createChore(chore)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        moveTaskToBack(true)
    }
}
