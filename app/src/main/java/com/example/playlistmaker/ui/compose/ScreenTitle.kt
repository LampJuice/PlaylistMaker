package com.example.playlistmaker.ui.compose

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.playlistmaker.R

@Composable
fun ScreenTitle(title: String)  {
    Text(
        text = title,
        fontSize = dimensionResource(R.dimen.text_main).value.sp,
        fontWeight = FontWeight.Medium,
        color = colorResource(R.color.text_menu),
        modifier = Modifier.padding(
            top = dimensionResource(R.dimen.secondary_padding),
            bottom = dimensionResource(R.dimen.main_padding)
        )
    )
}