package com.example.monopoly.ui.screens

import android.content.res.Configuration
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.monopoly.R
import com.example.monopoly.ui.components.ScreensHeaderArea
import com.example.monopoly.ui.viewmodel.ConfigActivityViewModel


@Composable
fun OtherGamesScreen(
    modifier: Modifier,
    onNavigateBack: () -> Unit,
    viewModel: ConfigActivityViewModel = viewModel(),
) {
    OtherGamesContent(
        modifier = modifier,
        onNavigateBack = onNavigateBack
    )
}

@Composable
fun OtherGamesContent(
    modifier: Modifier,
    onNavigateBack: () -> Unit,
) {
    val isPortrait = LocalConfiguration.current.orientation == Configuration.ORIENTATION_PORTRAIT

    if (isPortrait) {
        DrawOtherGameScreenPortrait(
            onNavigateBack = onNavigateBack,
            modifier = Modifier
        )
    } else {
        DrawOtherGameScreenLandscape(
            onNavigateBack = onNavigateBack,
            modifier = Modifier
        )
    }
}

@Composable
fun DrawOtherGameScreenPortrait(
    onNavigateBack: () -> Unit,
    modifier: Modifier
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        //Header
        item {
            ScreensHeaderArea(
                onExit = onNavigateBack,
                modifier = Modifier,
                title = stringResource(id = R.string.OtherGames)
            )
            Spacer(modifier = Modifier.height(24.dp))
        }

        // Content
        item {
            // TODO
        }
    }
}

@Composable
fun DrawOtherGameScreenLandscape(
    onNavigateBack: () -> Unit,
    modifier: Modifier
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        //Header
        item {
            ScreensHeaderArea(
                onExit = onNavigateBack,
                modifier = Modifier,
                title = stringResource(id = R.string.OtherGames)
            )
            Spacer(modifier = Modifier.height(24.dp))
        }

        // Content
        item {
            // TODO
        }
    }
}

@Preview(showBackground = true, heightDp = 900, widthDp = 550)
@Composable
fun OtherGamesPreview() {
    OtherGamesContent(modifier = Modifier, onNavigateBack = {})
}
