package com.landomen.composetooltips

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.MutatorMutex
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.DefaultTooltipCaretShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.PlainTooltip
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RichTooltip
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TooltipAnchorPosition
import androidx.compose.material3.TooltipBox
import androidx.compose.material3.TooltipDefaults
import androidx.compose.material3.TooltipScope
import androidx.compose.material3.TooltipState
import androidx.compose.material3.rememberTooltipState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.unit.dp
import com.landomen.composetooltips.ui.theme.ComposeTooltipsTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ComposeTooltipsTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    containerColor = Color.White
                ) { innerPadding ->
                    MainContent(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

@Composable
private fun MainContent(modifier: Modifier = Modifier) {
    LazyColumn(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
//        PlainTooltipLongPress()
//
//        Spacer(modifier = Modifier.height(32.dp))
//
//        PlainTooltipManual()
//
//        Spacer(modifier = Modifier.height(32.dp))
//
//        PlainTooltipWithCarrotManual()
//
//        Spacer(modifier = Modifier.height(32.dp))
//
//        RichTooltipClick()
//
//        Spacer(modifier = Modifier.height(32.dp))

        items(20) {
            Text("Filler content $it")
        }

        item {
            CustomRichTooltipClick()
        }

        items(20) {
            Text("Filler content $it + 21")
        }

//        Spacer(modifier = Modifier.height(32.dp))
//
//        ShowTwoTooltips()
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun PlainTooltipLongPress() {
    TooltipBox(
        positionProvider = TooltipDefaults.rememberPlainTooltipPositionProvider(),
        tooltip = { PlainTooltip { Text("This is a simple plain tooltip") } },
        state = rememberTooltipState()
    ) {
        Button(onClick = {}) {
            Text(text = "Show Plain Tooltip on Long Press")
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun PlainTooltipManual() {
    val tooltipState = rememberTooltipState()
    val scope = rememberCoroutineScope()
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        TooltipBox(
            positionProvider = TooltipDefaults.rememberPlainTooltipPositionProvider(),
            tooltip = { PlainTooltip { Text("This is a simple plain tooltip") } },
            state = tooltipState
        ) {
            Button(onClick = {
                scope.launch {
                    tooltipState.show()
                }
            }) {
                Text(text = "Show Plain Tooltip on Click")
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun PlainTooltipWithCarrotManual() {
    val tooltipState = rememberTooltipState()
    val scope = rememberCoroutineScope()
    TooltipBox(
        positionProvider = TooltipDefaults.rememberPlainTooltipPositionProvider(16.dp),
        tooltip = {
            PlainTooltip(
                contentColor = Color.Yellow,
                containerColor = Color.DarkGray,
                shadowElevation = 4.dp,
                tonalElevation = 12.dp,
                shape = RoundedCornerShape(16.dp)
            ) {

                Column(
                    modifier = Modifier
                        .padding(8.dp)
                        .clip(
                            RoundedCornerShape(16.dp)
                        )
                        .background(Color.Gray)
                        .padding(8.dp)
                ) {
                    Spacer(modifier = Modifier.height(4.dp))

                    Text("This is a simple customized plain tooltip")

                    Spacer(modifier = Modifier.height(4.dp))

                    Text("This is a second Text in the tooltip")
                }
            }
        },
        state = tooltipState
    ) {
        Button(onClick = {
            scope.launch {
                tooltipState.show()
            }
        }) {
            Text(text = "Show Custom Plain Tooltip on Click")
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RichTooltipClick() {
    val tooltipState = rememberTooltipState(isPersistent = true)
    val scope = rememberCoroutineScope()

    val action: @Composable () -> Unit = {
        TextButton(
            onClick = {
                scope.launch {
                    tooltipState.dismiss()
                }
            }
        ) {
            Text("Dismiss")
        }
    }

    TooltipBox(
        positionProvider = TooltipDefaults.rememberTooltipPositionProvider(
            positioning = TooltipAnchorPosition.Above,
            spacingBetweenTooltipAndAnchor = 12.dp,
        ),
        tooltip = {
            RichTooltip(
                title = { Text("Title of the tooltip") },
                action = action,
                caretShape = DefaultTooltipCaretShape(),
            ) {
                Text("This is the main content of the rich tooltip")
            }
        },
        state = tooltipState,
        onDismissRequest = {},
    ) {
        Button(onClick = {
            scope.launch {
                tooltipState.show()
            }
        }) {
            Text(text = "Show Rich Tooltip on Click")
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomRichTooltipClick() {
    var counter by remember { mutableIntStateOf(0) }
    val tooltipState = rememberTooltipState(initialIsVisible = false, isPersistent = true)

    LaunchedEffect(Unit) {
        tooltipState.show()
    }

    var tooltipPosition by remember {
        mutableStateOf(TooltipAnchorPosition.Above)
    }

    TooltipBox(
        positionProvider = TooltipDefaults.rememberTooltipPositionProvider(
            tooltipPosition,
            spacingBetweenTooltipAndAnchor = 8.dp
        ),
        tooltip = {
            CustomRichTooltipContent(
                tooltipState = tooltipState,
                counter = 0,
            )
        },
        state = tooltipState,
        onDismissRequest = {},
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Button(
                onClick = {
                    tooltipPosition = if (tooltipPosition == TooltipAnchorPosition.Above) {
                        TooltipAnchorPosition.Below
                    } else {
                        TooltipAnchorPosition.Above
                    }
                }
            ) {
                Text("Above / Below")
            }
        }
    }
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
private fun TooltipScope.CustomRichTooltipContent(
    tooltipState: TooltipState,
    counter: Int,
) {
    val scope = rememberCoroutineScope()

    RichTooltip(
        colors = TooltipDefaults.richTooltipColors(
            containerColor = Color.Black.copy(alpha = 0.9f),
            titleContentColor = Color.Green,
            contentColor = Color.White,
        ),
        shape = RectangleShape,
        title = {
            Row {
                Spacer(modifier = Modifier.width(4.dp))
                Text("Awesome!")
            }
        },
        action = {
            CustomRichtTooltipAction(scope, tooltipState)
        },
        caretShape = DefaultTooltipCaretShape(),
    ) {
        Text(
            "You've successfully opened a rich tooltip! 🎉\n" +
                    "You have clicked $counter times."
        )
    }
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
private fun CustomRichtTooltipAction(scope: CoroutineScope, tooltipState: TooltipState) {
    Row {
        TextButton(
            onClick = {
                scope.launch {
                    tooltipState.dismiss()
                }
            }
        ) {
            Text("Dismiss")
        }

        TextButton(
            onClick = {
                scope.launch {
                    tooltipState.dismiss()
                }
            }
        ) {
            Text("Next")
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ShowTwoTooltips() {
    val tooltipState1 = rememberTooltipState(isPersistent = true)
    val tooltipState2 = rememberTooltipState(
        isPersistent = true,
        mutatorMutex = MutatorMutex()
    )
    val scope = rememberCoroutineScope()

    Button(onClick = {
        scope.launch {
            tooltipState1.show()
        }
        scope.launch {
            tooltipState2.show()

        }
    }) {
        Text(text = "Show Two Tooltips on Click")
    }

    Spacer(Modifier.height(16.dp))

    Row(
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.Top,
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {

        TooltipBox(
            positionProvider = TooltipDefaults.rememberPlainTooltipPositionProvider(),
            tooltip = { PlainTooltip { Text("Select option 1") } },
            state = tooltipState1
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                RadioButton(selected = false, onClick = {
                    // ignore
                })

                Text("Option 1")
            }
        }

        Spacer(Modifier.width(32.dp))

        TooltipBox(
            positionProvider = TooltipDefaults.rememberPlainTooltipPositionProvider(),
            tooltip = { PlainTooltip { Text("Select option 2") } },
            state = tooltipState2
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                RadioButton(selected = false, onClick = {
                    // ignore
                })

                Text("Option 2")
            }
        }
    }
}