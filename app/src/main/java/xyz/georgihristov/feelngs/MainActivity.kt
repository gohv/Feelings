package xyz.georgihristov.feelngs

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.text.InputType
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.LinearLayout
import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.android.extension.responseJson
import org.jetbrains.anko.*
import org.jetbrains.anko.recyclerview.v7.recyclerView
import org.json.JSONObject

class MainActivity : AppCompatActivity() {

    var clientID = "3ioT2OA8W0lY02Rh37zG7bzsu8aW40jVJ5rrWDdS"
    var clientSecret = "sNZyXaNbhJjuZwhR38vAICGBrPBdWhjMbHZPXeyGd3IhWeskjHkNjjPtozgneldTiVWlpmqWSRewemVCEkSqhOP8TQudRVzHnZ6nIp69AeL4t6CB887qnqo9J3NftEuA"
    var verticalLayout : LinearLayout? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        verticalLayout = verticalLayout {
            if (prefs.authToken == "") {
                val usernameEditText = editText {
                    singleLine = true
                    hint = "username"
                }
                val passwordEditText = editText {
                    singleLine = true
                    inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
                    hint = "password"
                }
                button("submit") {
                    onClick {
                        val username = usernameEditText.text.toString()
                        val password = passwordEditText.text.toString()
                        login(username, password)
                    }
                }
            }else{
               getThoughts()
                button("refresh") {
                    onClick {
                        recreate()
                    }
                }
            }
        }
    }
    private fun getThoughts() {
        val url = "https://treehouse-django-feelings.herokuapp.com/api/thoughts.json"
        val header = mapOf("Authorization" to "Bearer ${prefs.authToken}")
        Fuel.get(url).header(header).responseJson { request, response, result ->
            result.fold({
                val thoughtAdapter = ThoughtAdapter()
                val thoughts = it.obj().getJSONArray("results")
                val numThoughts = thoughts.length()
                for (i in 0..numThoughts-1){
                    val json = thoughts[i] as JSONObject
                    val condition = json["condition_display"].toString()
                    val notes = json["notes"].toString()
                    thoughtAdapter.thoughts.add(Thought(condition,notes))
                }
                with(verticalLayout!!) {
                    recyclerView {
                        adapter = thoughtAdapter
                        layoutManager = LinearLayoutManager(context,LinearLayout.VERTICAL,false)

                    }
                }
            },{
                Log.d(result.toString(),"ERROR OCCURRED")
            })

        }
    }

    fun  login(username: String, password: String){
         val url = "https://treehouse-django-feelings.herokuapp.com/o/token/"
         val body = "client_id=$clientID&client_secret=$clientSecret&" +
                 "grant_type=password&username=$username&password=$password"

         Fuel.post(url).body(body).responseJson { request, response, result ->
             result.fold({
                 val token = it.obj()["access_token"]
                 prefs.authToken = token.toString()
                 toast(prefs.authToken)
             },{
                 Log.d(result.toString(),"ERROR OCCURRED")
             })
         }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val menuItem = menu.add("New Feeling")
        menuItem.setIcon(R.drawable.ic_add_box_black_24dp)
        menuItem.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        startActivity<NewFeelingActivity>()
        return super.onOptionsItemSelected(item)
    }
}
