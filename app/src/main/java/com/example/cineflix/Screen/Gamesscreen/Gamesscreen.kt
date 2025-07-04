package com.example.cineflix.Screen.Gamesscreen

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.cineflix.R

sealed class BottomNavItem(
    val route: String,
    @DrawableRes val icon: Int,
    val label: String
) {
    object Home      : BottomNavItem("home",      R.drawable.netlogo,        "Home")
    object Games     : BottomNavItem("games",     R.drawable.netlogo,       "Games")
    object NewHot    : BottomNavItem("new_hot",   R.drawable.netlogo,     "New & Hot")
    object MyNetflix : BottomNavItem("my_netflix",R.drawable.netlogo,     "My Netflix")

    companion object { val items = listOf(Home, Games, NewHot, MyNetflix) }
}

data class Game(
    val id: Int,
    val title: String,
    @DrawableRes val cover: Int
)

val demoGames = listOf(
    Game(1, "Squid Game: Unleashed", R.drawable.netlogo),
    Game(2, "GTA: San Andreas",      R.drawable.netlogo),
    Game(3, "Farming Simulator 23",  R.drawable.netlogo),
    Game(4, "Stranger Things IV",    R.drawable.netlogo)
)

@Composable
fun GamesScreen(
    currentRoute: String = BottomNavItem.Games.route,
    onNavigate: (String) -> Unit = {}
) {
    Scaffold(
        topBar = { GamesTopAppBar() },
        bottomBar = {
            NetflixBottomBar(
                currentRoute = currentRoute,
                onNavigate   = onNavigate
            )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            item { HeroBanner() }
            item { SectionHeader("Mobile Games") }
            item {
                Spacer(Modifier.height(4.dp))
                GamesRow(demoGames)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GamesTopAppBar() = CenterAlignedTopAppBar(
    title = {
        Text("Games", style = MaterialTheme.typography.headlineSmall)
    },
    actions = {
        IconButton(onClick = { /* TODO – search */ }) {
            Icon(Icons.Default.Search, contentDescription = "Search")
        }
    }
)

@Composable
fun HeroBanner() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .aspectRatio(1.4f),    // tweak to your liking
        shape = RoundedCornerShape(12.dp)
    ) {
        /* BACKGROUND IMAGE */
        Image(
            painterResource(id = R.drawable.netflixlogo),
            contentDescription = "Hero",
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        /* GRADIENT SCRIM */
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        0f to Color.Transparent,
                        0.5f to Color(0xB3000000), // semi‑transparent black
                        1f to Color(0xFF000000)
                    )
                )
        )

        /* FOREGROUND COPY */
        Column(
            modifier = Modifier.width(140.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "N  G A M E S",
                style = MaterialTheme.typography.labelSmall,
                letterSpacing = 2.sp
            )
            Text(
                text = "Unlimited access to\nexclusive games",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.ExtraBold,
                lineHeight = 32.sp
            )
            Text(
                text = "No ads. No extra fees. No in‑app\npurchases. Included with your\nmembership.",
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}

/* ---------- 6. Section header ---------- */
@Composable
fun SectionHeader(title: String) = Text(
    text = title,
    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
)

@Composable
fun GamesRow(games: List<Game>) = LazyRow(
    contentPadding = PaddingValues(horizontal = 16.dp)
) {
    items(games) { game ->
        GameCard(game)
        Spacer(Modifier.width(12.dp))
    }
}

@Composable
fun GameCard(game: Game) = Column(
    horizontalAlignment = Alignment.CenterHorizontally,
    modifier = Modifier.width(140.dp)
) {
    Card(shape = RoundedCornerShape(12.dp)) {
        Box {
            Image(
                painterResource(id = game.cover),
                contentDescription = game.title,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .aspectRatio(1f)          // square cover
            )

            /* “N” badge, top‑left */
            Image(
                painterResource(id = R.drawable.netlogo),
                contentDescription = "N Badge",
                modifier = Modifier
                    .size(24.dp)
                    .align(Alignment.TopStart)
                    .padding(4.dp)
            )

        }
    }
    Spacer(Modifier.height(6.dp))
    Text(
        text = game.title,
        style = MaterialTheme.typography.bodyMedium,
        maxLines = 2,
        overflow = TextOverflow.Ellipsis
    )
}

@Composable
fun NetflixBottomBar(
    currentRoute: String,
    onNavigate: (String) -> Unit
) = NavigationBar(
    tonalElevation = 0.dp,
) {
    BottomNavItem.items.forEach { item ->
        NavigationBarItem(
            selected = currentRoute == item.route,
            onClick  = { onNavigate(item.route) },
            icon = {
                Icon(
                    painterResource(id = item.icon),
                    contentDescription = item.label,
                    modifier = Modifier.size(24.dp)
                )
            },
            label = { Text(item.label) }
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF000000)
@Composable
fun GamesScreenPreview() = MaterialTheme {
    GamesScreen()
}
