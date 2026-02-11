package com.example.playlistmaker.ui.mediateka.fragments.favorite

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.playlistmaker.R

@Composable
fun EmptyFavorites() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 110.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(R.drawable.ic_no_result),
            contentDescription = null,
            modifier = Modifier.size(dimensionResource(R.dimen.placeholder_size))
        )

        Spacer(modifier = Modifier.height(dimensionResource(R.dimen.main_padding)))

        Text(
            text = stringResource(R.string.media_empty),
            fontSize = dimensionResource(R.dimen.ph_text).value.sp,
            fontWeight = FontWeight.Medium,
            color = colorResource(R.color.text_menu)
        )
    }
}