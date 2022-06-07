package com.example.simplenotepad

import android.graphics.Color
import com.airbnb.epoxy.EpoxyController
import com.example.simplenotepad.databinding.ModelColorWheelBinding
import com.example.simplenotepad.model.ViewBindingKotlinModel

enum class Colors(val color: String) {
    PastelBlue("#DAEAF1"),
    PastelPink("#FFE6E6"),
    PastelMint("#B7E5DD"),
    PastelViolet("#9A86A4"),
    PastelCream("#FEFBE7"),
    PastelBrown("#DAB88B"),
    PastelRed("#BB6464")
}

class ColorWheelEpoxyController: EpoxyController() {

    override fun buildModels() {
        for(color in Colors.values()){
            ColorsModel(color).id(color.name).addTo(this)
        }
    }

    data class ColorsModel(
        val colorState: Colors
    ) : ViewBindingKotlinModel<ModelColorWheelBinding>(R.layout.model_color_wheel){
        override fun ModelColorWheelBinding.bind() {
            colorWheel.setBackgroundColor(Color.parseColor(colorState.color))
        }
    }
}

