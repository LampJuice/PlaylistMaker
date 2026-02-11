package com.example.playlistmaker.ui.settings.fragment

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Switch
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.playlistmaker.R
import com.example.playlistmaker.domain.sharing.models.EmailData
import com.example.playlistmaker.ui.compose.ScreenTitle
import com.example.playlistmaker.ui.settings.view_model.SettingsEvent
import com.example.playlistmaker.ui.settings.view_model.SettingsViewModel

@Composable
fun SettingsScreen(
    viewModel: SettingsViewModel,
    onShare: (String) -> Unit,
    onSupport: (EmailData) -> Unit,
    onAgreement: (String) -> Unit
) {
    val isDark by viewModel.observeDarkTheme.observeAsState(false)

    val event by viewModel.observeEvents.observeAsState()

    LaunchedEffect(event) {
        when (val e = event) {
            is SettingsEvent.ContactSupport -> onSupport(e.data)
            is SettingsEvent.OpenAgreement -> onAgreement(e.link)
            is SettingsEvent.ShareApp -> onShare(e.link)
            null -> Unit
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(colorResource(id = R.color.background_menu))
            .padding(horizontal = dimensionResource(R.dimen.main_padding))

    ) {
        ScreenTitle(title = stringResource(R.string.settings))

        Row(
            modifier = Modifier
                .padding(bottom = 21.dp, top = 41.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(R.string.dark_theme),
                fontSize = dimensionResource(R.dimen.text_regular).value.sp,
                color = colorResource(R.color.text_b_s_pl),
                fontWeight = FontWeight.W400,
                modifier = Modifier.weight(1f)

            )
            Switch(
                checked = isDark,
                onCheckedChange = viewModel::onThemeSwitch
            )

        }

        SettingsItem(
            text = stringResource(R.string.share),
            icon = painterResource(R.drawable.ic_share_24),
            onClick = viewModel::onShareClick

        )

        SettingsItem(
            text = stringResource(R.string.support),
            icon = painterResource(R.drawable.ic_support_24),
            onClick = viewModel::onSupportClick

        )

        SettingsItem(
            text = stringResource(R.string.user_agreement),
            icon = painterResource(R.drawable.ic_user_agreement_24),
            onClick = { viewModel.onAgreementClick() }
        )
    }
}


@Composable
fun SettingsItem(
    text: String,
    icon: Painter,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(vertical = 21.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = text,
            fontSize = 16.sp,
            color = colorResource(R.color.text_b_s_pl),
            fontWeight = FontWeight.W400,
            modifier = Modifier.weight(1f)
        )
        Icon(
            painter = icon,
            contentDescription = null,
            tint = colorResource(R.color.ic_tint_2)
        )
    }
}