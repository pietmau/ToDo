package com.pppp.todo.main

import android.os.Bundle
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.pppp.database.FirebaseListsRepository
import com.pppp.entities.ToDoList
import com.pppp.todo.GenericViewModelWithOneOffEvents
import com.pppp.todo.drawer.Drawer
import com.pppp.todo.main.view.MainScreen
import com.pppp.todo.main.viewmodel.MainViewEvent
import com.pppp.todo.main.viewmodel.MainViewModel
import com.pppp.todo.main.viewmodel.MainViewState
import com.pppp.todo.main.viewmodel.OneOffEvent
import com.pppp.todo.ui.theme.ToDoTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @ExperimentalComposeUiApi
    @ExperimentalMaterialApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
        setContent {
            ToDoTheme {
                Surface(color = MaterialTheme.colors.background) {
                    MainScreen(
                        listId = "m7nagiQ0KWgCg2Cj61Ho",
                        drawerContent = { Drawer.Content() }
                    )
                }
            }
        }
    }

    companion object {
        const val PENDING_INTENT_REQUEST_CODE = 2
    }
}

@Composable
fun Greeting(name: String) {
    Text(text = "Hello $name!")
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    ToDoTheme {
        Greeting("Android")
    }
}
