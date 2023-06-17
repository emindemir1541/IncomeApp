package com.emindev.expensetodolist.main.ui.page

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.emindev.expensetodolist.BuildConfig
import com.emindev.expensetodolist.R
import com.emindev.expensetodolist.main.ui.component.LocalColor

@Composable
fun UpdatePage(forceUpdate: Boolean = true, navController: NavController,updateShowed:MutableState<Boolean>) {

    val context = LocalContext.current
    val launcher = rememberLauncherForActivityResult(contract = ActivityResultContracts.StartActivityForResult()) {

    }


    Box(modifier = Modifier.background(LocalColor.updateScreenBackColor)) {
        Column(modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                if (!forceUpdate)
                    IconButton(onClick = { navController.popBackStack();updateShowed.value = true}, modifier = Modifier
                        .size(24.dp))
                    {
                        Icon(imageVector = Icons.Default.Close, contentDescription = "close icon", tint = LocalColor.updateScreeForegroundColor)
                    }
            }

            Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
                Column(modifier = Modifier, verticalArrangement = Arrangement.spacedBy(70.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(painter = painterResource(id = R.drawable.update_icon), contentDescription = "updateLesson icon", tint = LocalColor.updateScreeForegroundColor)
                    Text(text = "Time to Update!", color = LocalColor.updateScreeForegroundColor, fontSize = 20.sp)
                    Button(onClick = {
                          val intent = Intent(Intent.ACTION_VIEW).apply {
                              data = Uri.parse("market://details?id=${BuildConfig.APPLICATION_ID}")
                          }
                        try {
                            launcher.launch(intent)
                        } catch (e: ActivityNotFoundException) {
                            // Handle exception if play store is not installed on device
                            Toast.makeText(context, "Play Store not found", Toast.LENGTH_SHORT).show()
                        }
                    }, colors = ButtonDefaults.buttonColors(containerColor = LocalColor.updateButtonBackColor, contentColor = LocalColor.updateButtonTextColor)) {
                        Text(text = "Update Now")
                    }
                }

            }
        }
        
        BackHandler() {
            
        }
    }
}