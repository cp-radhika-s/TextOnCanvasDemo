package com.app.textcanvas

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.layout.layout
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.ExperimentalTextApi
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.app.textcanvas.ui.theme.TextCanvasTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TextCanvasTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    ExampleTextString()
                }
            }
        }
    }
}

@OptIn(ExperimentalTextApi::class)
@Composable
fun ExampleTextString() {

    val textMeasure = rememberTextMeasurer()

    Canvas(modifier = Modifier
        .fillMaxWidth()
        .height(100.dp), onDraw = {
        drawRect(color = Color.Black)

        drawText(
            textMeasurer = textMeasure, text = "Text on Canvas!",
            style = TextStyle(
                fontSize = 35.sp,
                brush = Brush.linearGradient(
                    colors = RainbowColors
                )
            ),
            topLeft = Offset(20.dp.toPx(), 20.dp.toPx())
        )
    })
}


@OptIn(ExperimentalTextApi::class)
@Composable
fun ExampleTextOverFlow() {

    val textMeasure = rememberTextMeasurer()

    Canvas(modifier = Modifier
        .fillMaxWidth()
        .height(100.dp), onDraw = {
        drawRect(color = Color.Black)

        drawText(
            textMeasurer = textMeasure,
            text = "Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book.",
            style = TextStyle(
                fontSize = 20.sp,
                color = Color.White
            ),
            topLeft = Offset(10.dp.toPx(), 10.dp.toPx()),
            overflow = TextOverflow.Ellipsis,
            maxLines = 3
        )
    })
}

@OptIn(ExperimentalTextApi::class)
@Composable
fun ExampleTextAnnotatedString() {

    val textMeasure = rememberTextMeasurer()

    val text = buildAnnotatedString {
        withStyle(
            style = SpanStyle(
                color = Color.White,
                fontSize = 22.sp,
                fontStyle = FontStyle.Italic
            )
        ) {
            append("Hello,")
        }
        withStyle(
            style = SpanStyle(
                brush = Brush.horizontalGradient(colors = RainbowColors),
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold
            )
        ) {
            append("\nText on Canvas️")
        }
    }
    Canvas(modifier = Modifier
        .fillMaxWidth()
        .height(100.dp), onDraw = {
        drawRect(color = Color.Black)

        drawText(
            textMeasurer = textMeasure,
            text = text,
            topLeft = Offset(10.dp.toPx(), 10.dp.toPx()),
            overflow = TextOverflow.Ellipsis
        )
    })
}

private val RainbowColors = listOf(
    Color(0xff9c4f96),
    Color(0xffff6355),
    Color(0xfffba949),
    Color(0xfffae442),
    Color(0xff8bd448),
    Color(0xff2aa8f2)
)

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    TextCanvasTheme {
        ExampleTextOverFlow()
    }
}

@Composable
fun NativeDrawText() {

    val paint = Paint().asFrameworkPaint().apply {
        // paint configuration
    }

    Canvas(modifier = Modifier
        .fillMaxWidth()
        .height(100.dp), onDraw = {
        drawRect(color = Color.Black)

        drawIntoCanvas {
            it.nativeCanvas.drawText("Text on Canvas!",  20f, 200f, paint)
        }
    })
}

@OptIn(ExperimentalTextApi::class)
@Composable
fun ExampleTextLayoutResult() {
    val textMeasure = rememberTextMeasurer()
    var textLayoutResult by remember { mutableStateOf<TextLayoutResult?>(null) }

    Canvas(
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp)
            .layout { measurable, constraints ->
                val placeable = measurable.measure(constraints)

                textLayoutResult = textMeasure.measure(
                    AnnotatedString("Text on Canvas!"),
                    style = TextStyle(
                        fontSize = 45.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                )
                layout(placeable.width, placeable.height) {
                    placeable.place(0, 0)
                }
            }
    ) {
        drawRect(color = Color.Black)

        textLayoutResult?.let {
            drawText(
                textLayoutResult = it,
                alpha = 1f,
                shadow = Shadow(color = Color.Red, offset = Offset(5f, 8f)),
                textDecoration = TextDecoration.Underline
            )
        }

    }
}