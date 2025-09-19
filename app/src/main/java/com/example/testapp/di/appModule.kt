package com.example.testapp.di

import com.example.testapp.ui.pages.detail.DetailPageViewModel
import com.example.testapp.ui.pages.list.ListPageViewModel
import com.example.testapp.ui.pages.login.LoginPageViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    viewModel { LoginPageViewModel() }
    viewModel { ListPageViewModel(get()) }
    viewModel { DetailPageViewModel(get(), get() ) }
    single { ItemsRepository() }
}