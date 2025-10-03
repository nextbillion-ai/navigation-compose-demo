package ai.nextbillion.compose.example.kotlin.ui.widget


import ai.nextbillion.compose.example.R
import ai.nextbillion.compose.example.kotlin.Constants
import ai.nextbillion.compose.example.kotlin.NavigationActivity
import ai.nextbillion.compose.example.kotlin.navigation.DemoScreen
import ai.nextbillion.compose.example.kotlin.viewModel.FullExperienceViewModel
import ai.nextbillion.compose.example.kotlin.viewModel.HomePagePanelState
import ai.nextbillion.navigation.core.navigation.NavigationConstants.ROUNDING_INCREMENT_TEN
import ai.nextbillion.navigation.core.utils.DistanceFormatter
import ai.nextbillion.navigation.core.utils.time.TimeFormatter
import android.content.Context
import android.content.Intent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material.ModalBottomSheetLayout
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material.ModalBottomSheetValue
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material.TabRow
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material.TabRowDefaults
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material.Text
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Tab
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import kotlinx.coroutines.launch
import java.io.Serializable

@OptIn(ExperimentalMaterialApi::class)
@Composable
internal fun RouteResultScreen(
    viewModel: FullExperienceViewModel,
    navController: NavController
) {
    val context = LocalContext.current

    val currentState by viewModel.uiState.collectAsState()
    var selectedTabIndex by remember { mutableIntStateOf(0) }

    val route = currentState.routes.first()
    val timeRemaining = TimeFormatter.formatTimeRemaining(context, route.duration())
    val formatDistance = DistanceFormatter(
        context, route.routeOptions()?.language() ?: "en",
        route.routeOptions()?.unit().toString(), ROUNDING_INCREMENT_TEN
    ).formatDistance(route.distance())

    val coroutineScope = rememberCoroutineScope()
    val bottomSheetState =
        rememberModalBottomSheetState(initialValue = ModalBottomSheetValue.Hidden)

    Box {
        Column {
            AnimatedVisibility(
                visible = currentState.pageState == HomePagePanelState.singleDes,
                enter = slideInVertically(),
                exit = slideOutVertically()
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(color = Color.White)
                        .padding(top = 12.dp, start = 15.dp, end = 18.dp)
                ) {
                    Column {
                        Row {
                            Box(modifier = Modifier
                                .height(40.dp)
                                .clickable(onClick = {
                                    viewModel.updatePageState(HomePagePanelState.empty)
                                }), contentAlignment = Alignment.CenterStart
                            ) {
                                Image(
                                    painter = painterResource(id = R.drawable.ic_arrow),
                                    contentDescription = "back"
                                )
                            }
                            Column {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Box(
                                        Modifier
                                            .fillMaxWidth()
                                            .padding(start = 10.dp, end = 10.dp)
                                            .height(40.dp)
                                            .weight(1f)
                                            .border(
                                                width = 1.dp,
                                                color = ai.nextbillion.compose.example.kotlin.AppColor.Background.Secondary,
                                                shape = CircleShape.copy(all = CornerSize(10.dp))
                                            )
                                            .clickable(onClick = {
//                                                viewModel.pushToSearchPage(0, activityLauncher)
                                            },
                                                indication = null,
                                                interactionSource = remember {
                                                    MutableInteractionSource()
                                                }),
                                        contentAlignment = Alignment.CenterStart,
                                    ) {
                                        Text(
                                            text = viewModel.currentWaypoints.first().getName(),
                                            modifier = Modifier.padding(start = 10.dp),
                                            maxLines = 1,
                                            overflow = TextOverflow.Ellipsis
                                        )
                                    }
                                    Image(
                                        painter = painterResource(id = R.drawable.ic_more_icon),
                                        contentDescription = "back",
                                        modifier = Modifier.clickable(onClick = {
                                            coroutineScope.launch {
                                                bottomSheetState.show()
                                            }
                                        },
                                            indication = null,
                                            interactionSource = remember {
                                                MutableInteractionSource()
                                            }
                                        )
                                    )
                                }
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier.padding(top = 12.dp)
                                ) {
                                    Box(
                                        Modifier
                                            .fillMaxWidth()
                                            .padding(start = 10.dp, end = 10.dp)
                                            .height(40.dp)
                                            .weight(1f)
                                            .border(
                                                width = 1.dp,
                                                color = ai.nextbillion.compose.example.kotlin.AppColor.Background.Secondary,
                                                shape = CircleShape.copy(all = CornerSize(10.dp))
                                            )
                                            .clickable(onClick = {
//                                                viewModel.pushToSearchPage(1, activityLauncher)
                                            },
                                                indication = null,
                                                interactionSource = remember {
                                                    MutableInteractionSource()
                                                }),
                                        contentAlignment = Alignment.CenterStart
                                    ) {
                                        Text(
                                            text = viewModel.currentWaypoints.last().getName(),
                                            modifier = Modifier.padding(start = 10.dp),
                                            maxLines = 1,
                                            overflow = TextOverflow.Ellipsis
                                        )
                                    }
                                    Image(
                                        painter = painterResource(id = R.mipmap.switch_icon),
                                        contentDescription = "switch",
                                        Modifier.clickable(onClick = {
                                            viewModel.switchODPair(context)
                                        },
                                            indication = null,
                                            interactionSource = remember {
                                                MutableInteractionSource()
                                            })
                                    )
                                }

                            }

                        }
                        Spacer(modifier = Modifier.height(10.dp))
                        TabRow(selectedTabIndex = selectedTabIndex,
                            backgroundColor = Color.White,
                            modifier = Modifier.height(45.dp),
                            indicator = { tabPositions ->
                                TabRowDefaults.Indicator(
                                    Modifier.tabIndicatorOffset(tabPositions[selectedTabIndex]),
                                    color = ai.nextbillion.compose.example.kotlin.AppColor.Background.Secondary
                                )
                            }) {
                            viewModel.tabs.forEachIndexed { index, trafficModel ->
                                Tab(
                                    modifier = Modifier.weight(0.16f),
                                    selected = selectedTabIndex == index,
                                    selectedContentColor = ai.nextbillion.compose.example.kotlin.AppColor.Primary.Main,
                                    unselectedContentColor = ai.nextbillion.compose.example.kotlin.AppColor.Primary.Dark,
                                    onClick = {
                                        selectedTabIndex = index
                                        viewModel.updateTravelMode(
                                            context = context,
                                            trafficModel.getTraffic()
                                        )
                                    }
                                ) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Image(
                                            painter = painterResource(id = trafficModel.getIcon()),
                                            contentDescription = "Setting",
                                            colorFilter = ColorFilter.tint(color = if (selectedTabIndex == index) ai.nextbillion.compose.example.kotlin.AppColor.Primary.Main else ai.nextbillion.compose.example.kotlin.AppColor.Primary.Dark),
                                            modifier = Modifier.size(28.dp)
                                        )
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Text(
                                            text = trafficModel.getName(),
                                            style = TextStyle(fontWeight = FontWeight.Bold)
                                        )
                                    }
                                }
                            }

                            Tab(
                                selected = selectedTabIndex == 1,
                                modifier = Modifier.weight(2f),
                                onClick = { selectedTabIndex = 1 },
                                enabled = false
                            ) {
                            }
                        }
                    }
                }

            }
            Spacer(modifier = Modifier.weight(1f))
            AnimatedVisibility(visible = currentState.pageState == HomePagePanelState.singleDes,
                enter = slideInVertically(initialOffsetY = {
                    it
                }),
                exit = slideOutVertically(targetOffsetY = {
                    it
                })
            ) {
                ElevatedCard(
                    elevation = CardDefaults.cardElevation(defaultElevation = 15.dp),
                    shape = RoundedCornerShape(topStart = 10.dp, topEnd = 10.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(
                            start = 16.dp,
                            top = 16.dp,
                            end = 16.dp,
                            bottom = 20.dp
                        )
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "$timeRemaining",
                                style = TextStyle(fontSize = 24.sp, fontWeight = FontWeight.Bold)
                            )
                            Text(
                                text = " ($formatDistance)",
                                style = TextStyle(fontSize = 16.sp, color = ai.nextbillion.compose.example.kotlin.AppColor.Text.Secondary)
                            )
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = viewModel.currentWaypoints.last().getName(),
                            style = TextStyle(color = ai.nextbillion.compose.example.kotlin.AppColor.Text.Secondary, fontSize = 16.sp)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Button(
                            onClick = {
//
                                val sharedPreferences =
                                    context.getSharedPreferences(Constants.KEY_SHARE_PREFERENCE, Context.MODE_PRIVATE)
                                val simulation =
                                    sharedPreferences.getBoolean(Constants.NavigationSettings.SIMULATION, false)
                                val intent = Intent(context, NavigationActivity::class.java)
                                intent.putExtra("routes", currentState.routes as Serializable?)
                                intent.putExtra("simulation", simulation)

                                context.startActivity(intent)
                            }, shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.padding(2.dp),
                            colors = ButtonDefaults.buttonColors()
                                .copy(containerColor = ai.nextbillion.compose.example.kotlin.AppColor.Primary.Main)
                        ) {
                            Image(
                                painter = painterResource(id = R.mipmap.start_nav_icon),
                                contentDescription = "Setting"
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "START",
                                style = TextStyle(
                                    fontSize = 16.sp,
                                    color = Color.White,
                                    fontWeight = FontWeight.Bold
                                )
                            )
                        }
                    }
                }

            }

        }
    }

    ModalBottomSheetLayout(
        sheetState = bottomSheetState,
        sheetContent = {
            Column(
                modifier = Modifier.padding(
                    start = 16.dp,
                    top = 25.dp,
                    end = 16.dp,
                    bottom = 16.dp
                ),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_route_option),
                        contentDescription = "back",
                        modifier = Modifier.clickable(onClick = {
                            navController.navigate(
                                DemoScreen.ComposeRouteOptions.route
                            )
                        },
                            indication = null,
                            interactionSource = remember {
                                MutableInteractionSource()
                            }
                        )
                    )
                    Text(text = "Route Options", style = TextStyle(fontSize = 16.sp))
                }
                Spacer(modifier = Modifier.height(10.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_cancel),
                        contentDescription = "back",
                        modifier = Modifier.clickable(onClick = {
                            coroutineScope.launch {
                                bottomSheetState.hide()
                            }
                        },
                            indication = null,
                            interactionSource = remember {
                                MutableInteractionSource()
                            }
                        )
                    )
                    Text(text = "Cancel", style = TextStyle(fontSize = 16.sp))

                }
            }
        },
        sheetBackgroundColor = Color.White
    ) {
    }
}
