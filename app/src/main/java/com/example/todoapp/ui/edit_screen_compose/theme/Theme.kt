package com.example.todoapp.ui.edit_screen_compose.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp

@Stable
class TodoColors(
    colorPrimary: Color,
    colorOnPrimary: Color,
    colorSecondary: Color,
    colorOnSecondary: Color,
    colorRed: Color,
    colorGreen: Color,
    colorGrey:Color,
    colorLightGrey:Color

    ){
    var colorPrimary by mutableStateOf(colorPrimary)
        private set
    var colorOnPrimary by mutableStateOf(colorOnPrimary)
        private set
    var colorSecondary by mutableStateOf(colorSecondary)
        private set
    var colorOnSecondary by mutableStateOf(colorOnSecondary)
        private set
    var colorRed by mutableStateOf(colorRed)
        private set
    var colorGreen by mutableStateOf(colorGreen)
        private set
    var colorGrey by mutableStateOf(colorGrey)
        private set
    var colorLightGrey by mutableStateOf(colorLightGrey)
        private set

    fun update(other: TodoColors){
        colorPrimary = other.colorPrimary
        colorOnPrimary = other.colorOnPrimary
        colorSecondary = other.colorSecondary
        colorOnSecondary = other.colorOnSecondary
        colorRed = other.colorRed
        colorGreen = other.colorGreen
        colorGrey = other.colorGrey
        colorLightGrey = other.colorLightGrey
    }

}

private val LightColorScheme = TodoColors(
    colorPrimary = dark_primary,
    colorOnPrimary = dark_on_primary,
    colorSecondary = dark_secondary,
    colorOnSecondary = dark_on_secondary,
    colorRed = red,
    colorGreen = green,
    colorGrey = grey,
    colorLightGrey = light_grey,
)

private val DarkColorScheme = TodoColors(
    colorPrimary = light_primary,
    colorOnPrimary = light_on_primary,
    colorSecondary = light_secondary,
    colorOnSecondary = light_on_secondary,
    colorRed = red,
    colorGreen = green,
    colorGrey = grey,
    colorLightGrey = light_grey,
)

internal val LocalColors = staticCompositionLocalOf<TodoColors> { error("No colors") }

@Composable
fun ProvideTodoColors(
    colors: TodoColors,
    content: @Composable () -> Unit
) {
    val colorPalette = remember { colors }
    colorPalette.update(colors)
    CompositionLocalProvider(LocalColors provides colorPalette, content = content)
}

object TodoTheme {
    val colors: TodoColors
        @Composable
        get() = LocalColors.current

}

@Composable
fun TodoTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    ProvideTodoColors(colors = colorScheme) {
        MaterialTheme(
            content = content
        )
    }
}

@Preview("Light Theme")
@Preview("Dark Theme", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun AppThemePreview() {
    TodoTheme {
        Surface {
            Column {
//                ItemPreview(
//                    background = TodoTheme.colors.colorPrimary,
//                    textColor = TodoTheme.colors.colorOnPrimary,
//                    textStyle = Typography.h1,
//                    text = "Primary",
//                )
//                ItemPreview(
//                    background = YandexTodoTheme.colors.backSecondary,
//                    textColor = YandexTodoTheme.colors.labelSecondary,
//                    textStyle = Typography.h2,
//                    text = "Secondary",
//                )
//                ItemPreview(
//                    background = YandexTodoTheme.colors.backSecondary,
//                    textColor = YandexTodoTheme.colors.labelPrimary,
//                    textStyle = Typography.body1,
//                    text = "Background",
//                )
//                ItemPreview(
//                    background = YandexTodoTheme.colors.colorRed,
//                    textColor = YandexTodoTheme.colors.labelPrimary,
//                    textStyle = Typography.body1,
//                    text = "Red",
//                )
//                ItemPreview(
//                    background = YandexTodoTheme.colors.colorGreen,
//                    textColor = YandexTodoTheme.colors.labelPrimary,
//                    textStyle = Typography.body1,
//                    text = "Green",
//                )
//                ItemPreview(
//                    background = YandexTodoTheme.colors.colorBlue,
//                    textColor = YandexTodoTheme.colors.labelPrimary,
//                    textStyle = Typography.body1,
//                    text = "Blue",
//                )
//                ItemPreview(
//                    background = YandexTodoTheme.colors.colorGray,
//                    textColor = YandexTodoTheme.colors.labelPrimary,
//                    textStyle = Typography.body1,
//                    text = "Separator",
//                )
//                ItemPreview(
//                    background = YandexTodoTheme.colors.colorGrayLight,
//                    textColor = YandexTodoTheme.colors.labelPrimary,
//                    textStyle = Typography.body1,
//                    text = "Gray",
//                )
//                ItemPreview(
//                    background = YandexTodoTheme.colors.colorGray,
//                    textColor = YandexTodoTheme.colors.labelPrimary,
//                    textStyle = Typography.body1,
//                    text = "Disabled",
//                )
//                ItemPreview(
//                    background = YandexTodoTheme.colors.labelTertiary,
//                    textColor = YandexTodoTheme.colors.labelPrimary,
//                    textStyle = Typography.body1,
//                    text = "Tertiary",
//                )
            }
        }
    }
}

@Composable
fun ItemPreview(
    background: Color,
    textColor: Color,
    textStyle: TextStyle,
    text: String,
) {
    Box(
        modifier = Modifier
            .background(background)
            .fillMaxWidth()
            .border(0.5.dp, Color.Black)
            .height(50.dp)
            .padding(vertical = 4.dp),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = text,
            color = textColor,
            style = textStyle,
        )
    }
}