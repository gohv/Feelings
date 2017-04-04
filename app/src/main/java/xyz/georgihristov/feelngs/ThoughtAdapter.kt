package xyz.georgihristov.feelngs

import android.graphics.Typeface
import android.support.v7.widget.RecyclerView
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import org.jetbrains.anko.*


data class Thought(val condition: String,val thought: String)

class ThoughtAdapter : RecyclerView.Adapter<ThoughtAdapter.ViewHolder>() {

    val thoughts = mutableListOf<Thought>()

    override fun getItemCount() = thoughts.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.update(thoughts[position])
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val view = with(parent.context) {
            verticalLayout {
                lparams{
                    margin = dip(8)
                }
                textView {
                    typeface = Typeface.DEFAULT_BOLD
                    id = 1
                }
                textView {

                    id = 2
                }
            }
        }

        return ViewHolder(view)
    }


    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view){
        val textView1 = view.find<TextView>(1)
        val textView2 = view.find<TextView>(2)

        fun update(thought: Thought) {
            textView1.text = thought.condition
            textView2.text = thought.thought
        }
    }


}