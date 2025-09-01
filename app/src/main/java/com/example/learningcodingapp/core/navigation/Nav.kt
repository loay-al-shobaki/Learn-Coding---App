package com.example.learningcodingapp.core.navigation

import SignUpScreen
import WelcomeScreen
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.learningcodingapp.ui.HomeScreen.HomeScreen
import com.example.learningcodingapp.ui.LoginSignUpScreen.LoginScreen
import com.example.learningcodingapp.ui.auth.ResetPasswordScreen
import com.google.firebase.auth.FirebaseAuth

object Routes {
    const val WELCOME = "welcome"
    const val SIGN_IN = "sign_in"
    const val SIGN_UP = "sign_up"
    const val FORGOT = "forgot"
    const val HOME = "home"
}

@Composable
fun AppNavHost(nav: NavHostController) {

    // حدّد البداية حسب حالة تسجيل الدخول
    val startDest = remember {
        if (FirebaseAuth.getInstance().currentUser != null)
            Routes.HOME
        else
            Routes.WELCOME
    }

    NavHost(navController = nav, startDestination = Routes.WELCOME) {
        composable(Routes.WELCOME) {
            WelcomeScreen(
                onClickSignUp = { nav.navigate(Routes.SIGN_UP) },
                onClickSignIn = { nav.navigate(Routes.SIGN_IN) },
                onAlreadySignedIn = {
                    nav.navigate(Routes.HOME) {
                        popUpTo(Routes.WELCOME) { inclusive = true }
                        launchSingleTop = true
                    }
                }
            )
        }
        // شاشة التسجيل
        composable(Routes.SIGN_UP) {
            SignUpScreen(
                onBack = { nav.popBackStack() },
                onSignedUp = {
                    nav.navigate(Routes.HOME) {
                        popUpTo(Routes.WELCOME) { inclusive = true } // امسح الويلكم من الستاك
                        launchSingleTop = true
                    }
                }
            )
        }
        // شاشة استعادة كلمة المرور
        composable(Routes.FORGOT) {
            ResetPasswordScreen(
                onBack = { nav.popBackStack() },
                onSignUp = { nav.navigate(Routes.SIGN_UP) }
            )
        }
        // شاشة الدخول
        composable(Routes.SIGN_IN) {
            LoginScreen(
                onBack = { nav.popBackStack() },
                onGotoSignUp = { nav.navigate(Routes.SIGN_UP) },   // ← زر sign up يشتغل الآن
                onGotoForgot = { nav.navigate(Routes.FORGOT) },

                onLoggedIn = {                                     // ← بعد نجاح تسجيل الدخول
                    nav.navigate(Routes.HOME) {
                        popUpTo(Routes.WELCOME) { inclusive = true } // نظّف الستاك
                        launchSingleTop = true
                    }
                }
            )
        }

        // الصفحة الرئيسية بعد الدخول
        composable(Routes.HOME) {
            HomeScreen()
        }
    }
}