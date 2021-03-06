package com.example.iotinfo.ui

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.iotinfo.R
import org.json.JSONArray
import org.json.JSONObject

class PopAdapter(private val dataSet: JSONArray) :
    RecyclerView.Adapter<PopAdapter.ViewHolder>() {

    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder).
     */
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val name: TextView
        val count: TextView

        init {
            // Define click listener for the ViewHolder's View.
            name = view.findViewById(R.id.pop_name)
            count = view.findViewById(R.id.pop_count)
        }
    }

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        // Create a new view, which defines the UI of the list item
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.top_item, viewGroup, false)

        return ViewHolder(view)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {

        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        viewHolder.name.text = (dataSet[position] as JSONObject).get("name").toString()
        viewHolder.count.text = (dataSet[position] as JSONObject).get("count").toString()
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = dataSet.length()

}

class TripAdapter(private val dataSet: JSONArray, private val popSet: JSONArray) :
    RecyclerView.Adapter<TripAdapter.ViewHolder>() {

    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder).
     */
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val name: TextView
        val fire: ImageView

        init {
            // Define click listener for the ViewHolder's View.
            name = view.findViewById(R.id.trip_name)
            fire = view.findViewById(R.id.fire_icon)
        }
    }

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        // Create a new view, which defines the UI of the list item
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.trip_item, viewGroup, false)

        return ViewHolder(view)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {

        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        val name = (dataSet[position] as JSONObject).get("name").toString()
        if (name != "Place")
            viewHolder.name.text = name
        Log.d("TRIP", viewHolder.name.text.toString())
        for (i in 0 until popSet.length()){
            if((popSet.get(i) as JSONObject).get("name") == viewHolder.name.text) {
                viewHolder.fire.visibility = View.VISIBLE
                Log.d("TRIP", "same")
            }
        }
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = dataSet.length()

}