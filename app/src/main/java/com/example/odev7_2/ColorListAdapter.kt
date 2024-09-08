package com.example.odev7_2

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView

class ColorListAdapter(
    private val context: Context,
    private val colors: List<String>,
    private val colorValues: List<Int>
) : ArrayAdapter<String>(context, 0, colors) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = convertView ?: LayoutInflater.from(context).inflate(R.layout.list_item_color, parent, false)


        val colorView = view.findViewById<View>(R.id.color_view)
        val colorName = view.findViewById<TextView>(R.id.color_name)


        colorName.text = colors[position]
        colorView.setBackgroundColor(colorValues[position])

        return view
    }
}
