package com.first_Ideall.dialogs

import android.content.Context
import android.view.LayoutInflater
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.first_Ideall.R
import com.first_Ideall.adapters.ColorPaletteAdapter

class ColorPaletteDialog(context: Context) : MaterialAlertDialogBuilder(context) {
    private var recyclerView: RecyclerView
    var adapter: ColorPaletteAdapter

    init {
        val view = LayoutInflater.from(context).inflate(R.layout.color_palette_dialog, null)
        recyclerView = view.findViewById(R.id.colorPaletteRecyclerView)
        adapter = ColorPaletteAdapter(context.resources.getIntArray(R.array.palettes))
        recyclerView.adapter = adapter
        recyclerView.layoutManager = GridLayoutManager(context, 4)
        background = ContextCompat.getDrawable(context, R.drawable.color_picker_dialog_background)
        setView(view)
    }
}