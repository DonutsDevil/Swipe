package com.swapnil.myapplication.di

import android.content.Context
import com.swapnil.myapplication.di.module.LocalServiceModule
import com.swapnil.myapplication.di.module.NetworkServiceModule
import com.swapnil.myapplication.di.module.RetrofitModule
import com.swapnil.myapplication.views.AddProductFragment
import com.swapnil.myapplication.views.ProductListingFragment
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Component(modules = [RetrofitModule::class, NetworkServiceModule::class, LocalServiceModule::class])
@Singleton
interface ApplicationComponent {
    fun inject(addProductFragment: AddProductFragment)
    fun inject(productListingFragment: ProductListingFragment)

    @Component.Factory
    interface Factory {
        fun create(@BindsInstance context: Context): ApplicationComponent
    }
}