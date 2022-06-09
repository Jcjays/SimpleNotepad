package com.example.simplenotepad

import android.graphics.Color
import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyController
import com.example.simplenotepad.databinding.ModelColorWheelBinding
import com.example.simplenotepad.model.ViewBindingKotlinModel

enum class Colors(val color: String) {
    DefaultColor("#FAFAFA"),
    PastelBlue("#DAEAF1"),
    PastelPink("#FFE6E6"),
    PastelMint("#B7E5DD"),
    PastelViolet("#D9D7F1"),
    PastelCream("#FEF5ED"),
    PastelBrown("#E7E0C9"),
    PastelRed("#F7D1BA")
}

class ColorWheelEpoxyController(
    val colorAttribute: (String) -> Unit
): EpoxyController() {

    override fun buildModels() {
        for(color in Colors.values()){
            ColorsModel(color, colorAttribute).id(color.name).addTo(this)
        }
    }

    data class ColorsModel(
        val colorState: Colors,
        val colorAttribute: (String) -> Unit
    ) : ViewBindingKotlinModel<ModelColorWheelBinding>(R.layout.model_color_wheel){
        override fun ModelColorWheelBinding.bind() {

            colorWheel.setBackgroundColor(Color.parseColor(colorState.color))
            colorWheel.setOnClickListener {
                colorAttribute(colorState.color)
            }
        }
    }
}

