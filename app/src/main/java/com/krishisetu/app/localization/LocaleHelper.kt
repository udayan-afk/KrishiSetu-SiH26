package com.krishisetu.app.localization

import android.content.Context
import android.content.res.Configuration
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import com.krishisetu.app.models.AppLanguage
import java.util.Locale

object LocaleHelper {
    fun updateLocale(context: Context, language: AppLanguage): Context {
        val locale = Locale(language.code)
        Locale.setDefault(locale)
        val config = Configuration(context.resources.configuration)
        config.setLocale(locale)
        return context.createConfigurationContext(config)
    }
}

@Composable
fun LocalizedApp(
    language: AppLanguage,
    content: @Composable () -> Unit
) {
    val currentContext = LocalContext.current
    val localizedContext = remember(language, currentContext) {
        LocaleHelper.updateLocale(currentContext, language)
    }
    CompositionLocalProvider(LocalContext provides localizedContext) {
        content()
    }
}
