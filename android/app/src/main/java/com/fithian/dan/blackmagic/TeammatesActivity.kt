package com.fithian.dan.blackmagic

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import android.util.Log
import android.view.WindowManager
import com.beust.klaxon.Klaxon
import java.io.*

class TeammatesActivity : AppCompatActivity() {
    var teammates: Map<String, Teammate> = mapOf()
    val filename: String = "teammates"

    fun addRow(table: TableLayout, teammate: Teammate, select: Boolean) {
        var inflater = LayoutInflater.from(this)
        var row = inflater.inflate(R.layout.teammate_row, table, false)
        var name = row.findViewById<EditText>(R.id.teammateName)
        var email = row.findViewById<TextView>(R.id.teammateEmail)
        var include = row.findViewById<ToggleButton>(R.id.includeEmailButton)
        var delete = row.findViewById<Button>(R.id.deleteTeammateButton)
        name.setText(teammate.name)
        name.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {}
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                teammate.name = s.toString()
            }
        })
        if (select) {
            name.requestFocus()
        }
        email.setText(teammate.email)
        email.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {}
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                teammate.email = s.toString()
            }
        })
        include.isChecked = teammate.include
        include.setOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener { _, isChecked ->
            teammate.include = isChecked
        })
        delete.setOnClickListener(View.OnClickListener {
            this.teammates = this.teammates.minus(teammate.uuid)
            table.removeView(row)
        })
        table.addView(row)
    }

    override fun onPause() {
        super.onPause()
        this.openFileOutput(this.filename, Context.MODE_PRIVATE).use {
            var stream = OutputStreamWriter(it)
            stream.write(Klaxon().toJsonString(this.teammates.values))
            stream.close()
        }
        Log.d("TeammatesActivity", "Saved teammates")
        Log.d("TeammatesActivity", Klaxon().toJsonString(this.teammates))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.teammates)
        var table = findViewById<TableLayout>(R.id.teammatesTable)
        if (File(filesDir, this.filename).exists()) {
            Log.d("TeammatesActivity", "File exists")
            var teammatesList = ArrayList(Klaxon().parseArray<Teammate>(File(filesDir, this.filename)))
            this.teammates = teammatesList.map({ x -> Pair(x.uuid, x) }).toMap()
            Log.d("TeammatesActivity", Klaxon().toJsonString(this.teammates))
        } else {
            Log.d("TeammatesActivity", "File doesn't exist")
        }
        for ((email, teammate) in this.teammates) {
            this.teammates = this.teammates.plus(Pair(email, teammate))
            addRow(table, teammate, false)
        }
        findViewById<Button>(R.id.addNewTeammateButton).setOnClickListener(View.OnClickListener {
            var teammate = Teammate("", "", false)
            this.teammates = this.teammates.plus(Pair(teammate.uuid, teammate))
            addRow(table, teammate, true)
        })
        findViewById<Button>(R.id.emailTeammatesButton).setOnClickListener(View.OnClickListener {
            var emailIntent = Intent(Intent.ACTION_SENDTO)
            emailIntent.setData(Uri.parse("mailto:"))
            emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Black Magic") // TODO opponent and time
            emailIntent.putExtra(Intent.EXTRA_TEXT, "In: Fithian")
            emailIntent.putExtra(Intent.EXTRA_EMAIL, this.teammates.values.map({ x -> x.email }).toTypedArray())
            startActivity(Intent.createChooser(emailIntent, "Send email"))
        })
    }
}
