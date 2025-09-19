package com.example.testapp.ui

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.testapp.ui.pages.detail.DetailPageComposable
import com.example.testapp.ui.pages.list.ListPageComposable
import com.example.testapp.ui.pages.login.LoginPageComposable

@Composable
fun ComposeDemoApp() {
    val navController: NavHostController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = "login_page"
    ) {
        composable(route = "login_page") {
            LoginPageComposable(navHostController = navController)
        }

        composable(route = "list_page") {
            ListPageComposable(navHostController = navController)
        }

        composable(
            route = "detail_page/{item_id}",
            arguments = listOf(navArgument("item_id") { type = NavType.IntType })
        ) {
            DetailPageComposable(navHostController = navController)
        }
    }
}