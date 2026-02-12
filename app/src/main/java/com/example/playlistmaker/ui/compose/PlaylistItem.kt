package com.example.playlistmaker.ui.compose

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.playlistmaker.R
import com.example.playlistmaker.domain.playlist.models.Playlist

@Composable
fun PlaylistItem(
    playlist: Playlist,
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .padding(dimensionResource(R.dimen.card_margin_vert))
            .clickable { onClick() }
    ) {
        AsyncImage(
            model = playlist.coverPath,
            placeholder = painterResource(R.drawable.placeholder_45),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1f)
                .size(dimensionResource(R.dimen.player_cover_size))

                .clip(RoundedCornerShape(dimensionResource(R.dimen.corner_radius_player_cover)))
        )

        Spacer(modifier = Modifier.height(dimensionResource(R.dimen.card_margin_vert)))

        Text(
            text = playlist.name,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            fontWeight = FontWeight.Medium,
            fontSize = dimensionResource(R.dimen.text_regular).value.sp
        )
        Text(
            text = "${playlist.songCount} треков",
            fontSize = dimensionResource(R.dimen.text_card_2).value.sp,
            color = colorResource(R.color.outline_playlist)
        )

    }
}