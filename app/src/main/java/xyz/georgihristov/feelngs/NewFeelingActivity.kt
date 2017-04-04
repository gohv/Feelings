package xyz.georgihristov.feelngs

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.ArrayAdapter
import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.android.extension.responseJson
import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.runBlocking
import org.jetbrains.anko.*
import org.json.JSONObject

data class Conditions(val code: Int,val condition: String)
class NewFeelingActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        verticalLayout {
            runBlocking(CommonPool) {
                val conditions = getFeelings()
                val stringConditions = conditions.map { it.condition }
               val spinner = spinner {
                    val adapter = ArrayAdapter<String>(context,
                            R.layout.support_simple_spinner_dropdown_item,
                            stringConditions.toTypedArray())
                    setAdapter(adapter)
                }
                val notesEditText = editText {
                    hint = "...notes go here..."
                }
                button {
                    text = "Post"
                    onClick {
                        val url = "https://treehouse-django-feelings.herokuapp.com/api/thoughts/"
                        val header = mapOf("Authorization" to "Bearer ${prefs.authToken}",
                                "Content-type" to "application/json")
                        val itemPossition = conditions[spinner.selectedItemPosition].code
                        val editTextNotes = notesEditText.text.toString()
                        val body = "{ \"condition\":$itemPossition, \"notes\": \"$editTextNotes\" }"
                        Fuel.post(url).header(header).body(body).responseJson { request, response, result ->

                        }
                    finish()
                    }
                }
            }
        }
    }
    suspend fun  getFeelings(): MutableList<Conditions>{
        val conditions = mutableListOf<Conditions>()
        val url = "https://treehouse-django-feelings.herokuapp.com/api/conditions.json/"
        val header = mapOf("Authorization" to "Bearer ${prefs.authToken}")
        val (request, response, result) = Fuel.get(url).header(header).responseJson()
        result.fold({
            val array = it.array()
            for (i in 0..array.length()-1){
                val value =  (array[i] as JSONObject)["value"].toString().toInt()
                val label =  (array[i] as JSONObject)["label"].toString()
                conditions.add(Conditions(value,label))
            }
        },{
            Log.d(result.toString(),"`")
        })
        return conditions
    }
}

