package com.ergin.vkedu

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.ergin.vkedu.ui.theme.VKEduTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            VKEduTheme {
                MainScreen()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(modifier: Modifier = Modifier) {
    val context = LocalContext.current
    var inputText by rememberSaveable { mutableStateOf("") }
    var isError by rememberSaveable { mutableStateOf(false) }
    var errorMessage by rememberSaveable { mutableStateOf("") }

    val errorEmptyText = stringResource(id = R.string.error_empty_text)
    val errorInvalidPhone = stringResource(id = R.string.error_invalid_phone)
    val shareChooserTitle = stringResource(id = R.string.share_text)

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = { Text(text = stringResource(id = R.string.main_title)) }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(24.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            OutlinedTextField(
                value = inputText,
                onValueChange = {
                    inputText = it
                    if (isError) {
                        isError = false
                        errorMessage = ""
                    }
                },
                label = { Text(text = stringResource(id = R.string.enter_text_hint)) },
                isError = isError,
                supportingText = {
                    if (isError) {
                        Text(
                            text = errorMessage,
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {
                    val trimmed = inputText.trim()
                    if (trimmed.isEmpty()) {
                        isError = true
                        errorMessage = errorEmptyText
                        Toast.makeText(context, errorEmptyText, Toast.LENGTH_SHORT).show()
                    } else {
                        val intent = Intent(context, SecondActivity::class.java).apply {
                            putExtra(SecondActivity.EXTRA_TEXT, trimmed)
                        }
                        context.startActivity(intent)
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = stringResource(id = R.string.open_second_activity))
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    val trimmed = inputText.trim()
                    if (trimmed.isEmpty()) {
                        isError = true
                        errorMessage = errorEmptyText
                        Toast.makeText(context, errorEmptyText, Toast.LENGTH_SHORT).show()
                    } else {
                        val digitsCount = trimmed.count { it.isDigit() }
                        if (digitsCount < 3) {
                            isError = true
                            errorMessage = errorInvalidPhone
                            Toast.makeText(context, errorInvalidPhone, Toast.LENGTH_SHORT).show()
                        } else {
                            val intent = Intent(Intent.ACTION_DIAL).apply {
                                data = Uri.parse("tel:$trimmed")
                            }
                            context.startActivity(intent)
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = stringResource(id = R.string.call_friend))
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    val trimmed = inputText.trim()
                    if (trimmed.isEmpty()) {
                        isError = true
                        errorMessage = errorEmptyText
                        Toast.makeText(context, errorEmptyText, Toast.LENGTH_SHORT).show()
                    } else {
                        val intent = Intent(Intent.ACTION_SEND).apply {
                            type = "text/plain"
                            putExtra(Intent.EXTRA_TEXT, trimmed)
                        }
                        val chooser = Intent.createChooser(intent, shareChooserTitle)
                        context.startActivity(chooser)
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = stringResource(id = R.string.share_text))
            }
        }
    }
}
