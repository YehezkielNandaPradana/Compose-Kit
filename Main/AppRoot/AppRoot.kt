package io.github.ComposeKit.AppRoot

import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import io.github.ComposeKit.Helper.Notify.AppNotifierHost
import io.github.ComposeKit.Helper.PopUp.DialogHelper
import io.github.ComposeKit.Helper.PopUp.DialogHost
import io.github.ComposeKit.Helper.SuccessMessage.SimpleDialog
import io.github.ComposeKit.Helper.SuccessMessage.ToastHelper
import io.github.ComposeKit.Navigate.NavApp

@Composable
fun AppRoot() {
    val context = LocalContext.current
    DialogHost()

    ToastHelper.show = { msg ->
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
    }

    SimpleDialog.show = { content ->
        DialogHelper.showCustom {
            content()
        }
    }

    SimpleDialog.close = {
        DialogHelper.close()
    }
    AppNotifierHost()
    val navController = rememberNavController()
    val homeViewModel: HomeViewModel = viewModel()

    NavApp(

    )
}