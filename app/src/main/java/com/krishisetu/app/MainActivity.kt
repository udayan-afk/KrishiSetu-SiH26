package com.krishisetu.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.krishisetu.app.localization.LocaleHelper
import com.krishisetu.app.localization.LocalizedApp
import com.krishisetu.app.ui.navigation.AppNavHost
import com.krishisetu.app.ui.theme.KrishiSetuTheme
import com.krishisetu.app.viewmodel.KrishiSetuViewModel

class MainActivity : ComponentActivity() {
    private val viewModel: KrishiSetuViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (savedInstanceState == null) {
            val savedLang = LocaleHelper.getSavedLanguage(applicationContext)
            viewModel.setLanguage(savedLang)
        }
        setContent {
            val selectedLanguage by viewModel.selectedLanguage.collectAsState()
            
            LocalizedApp(
                language = selectedLanguage,
                onLanguageChange = { viewModel.setLanguage(it) }
            ) {
                KrishiSetuTheme {
                    Surface(
                        modifier = Modifier.fillMaxSize(),
                        color = MaterialTheme.colorScheme.background
                    ) {
                        AppNavHost(viewModel = viewModel)
                    }
                }
            }
        }
    }
}
