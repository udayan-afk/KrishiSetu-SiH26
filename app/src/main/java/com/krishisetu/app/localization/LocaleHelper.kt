package com.krishisetu.app.localization

import android.content.Context
import android.content.res.Configuration
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import com.krishisetu.app.models.AppLanguage
import java.util.Locale

val LocalAppLanguage = compositionLocalOf { AppLanguage.ENGLISH }
val LocalOnLanguageChange = compositionLocalOf<((AppLanguage) -> Unit)?> { null }

object LocaleHelper {
    private const val PREFS_NAME = "krishisetu_language_prefs"
    private const val KEY_LANGUAGE_CODE = "selected_language_code"

    fun updateLocale(context: Context, language: AppLanguage): Context {
        val locale = Locale(language.code)
        Locale.setDefault(locale)
        val config = Configuration(context.resources.configuration)
        config.setLocale(locale)
        return context.createConfigurationContext(config)
    }

    fun saveLanguage(context: Context, language: AppLanguage) {
        context.applicationContext
            .getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
            .edit()
            .putString(KEY_LANGUAGE_CODE, language.code)
            .apply()
    }

    fun getSavedLanguage(context: Context): AppLanguage {
        val savedCode = context.applicationContext
            .getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
            .getString(KEY_LANGUAGE_CODE, AppLanguage.ENGLISH.code)
        return AppLanguage.entries.firstOrNull { it.code == savedCode } ?: AppLanguage.ENGLISH
    }
}

@Composable
fun LocalizedApp(
    language: AppLanguage,
    onLanguageChange: ((AppLanguage) -> Unit)? = null,
    content: @Composable () -> Unit
) {
    val currentContext = LocalContext.current
    LaunchedEffect(language) {
        LocaleHelper.saveLanguage(currentContext, language)
    }
    val localizedContext = remember(language, currentContext) {
        LocaleHelper.updateLocale(currentContext, language)
    }
    val localizedConfiguration = remember(localizedContext) {
        localizedContext.resources.configuration
    }
    CompositionLocalProvider(
        LocalContext provides localizedContext,
        LocalConfiguration provides localizedConfiguration,
        LocalAppLanguage provides language,
        LocalOnLanguageChange provides onLanguageChange
    ) {
        content()
    }
}
