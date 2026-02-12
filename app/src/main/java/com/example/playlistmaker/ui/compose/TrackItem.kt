package com.example.playlistmaker.ui.compose

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.playlistmaker.R
import com.example.playlistmaker.ui.search.models.SongUi

@Composable
fun TrackItem(
    song: SongUi,
    onTrackClick: (SongUi) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onTrackClick(song) }
            .padding(
                vertical = dimensionResource(R.dimen.card_margin_vert)

            ),
        verticalAlignment = Alignment.CenterVertically
    ) {
        AsyncImage(
            model = song.artworkUrl100,
            placeholder = painterResource(R.drawable.placeholder_45),
            contentDescription = null,
            modifier = Modifier
                .size(dimensionResource(R.dimen.album_size))
                .clip(RoundedCornerShape(dimensionResource(R.dimen.round_corners_cover)))
        )
        Spacer(Modifier.width(8.dp))

        Column(Modifier.weight(1f)) {
            Text(
                song.trackName,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                fontWeight = FontWeight.W400,
                fontSize = 16.sp,
                color = colorResource(R.color.text_b_s_pl)

            )
            Row(
            ) {
                Text(
                    song.artistName,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    fontWeight = FontWeight.Medium,
                    fontSize = 11.sp,
                    color = colorResource(R.color.outline_playlist),
                    modifier = Modifier.weight(1f, fill = false)
                )
                Icon(
                    painterResource(R.drawable.ic_dot_13),
                    null,
                    tint = colorResource(R.color.outline_playlist)
                )
                Text(
                    song.trackTime,
                    fontWeight = FontWeight.Medium,
                    fontSize = 11.sp,
                    color = colorResource(R.color.outline_playlist)
                )
            }
        }
        Spacer(Modifier.width(16.dp))
        Icon(
            painterResource(R.drawable.ic_user_agreement_24),
            contentDescription = null,
            tint = colorResource(R.color.outline_playlist)
        )

    }
}