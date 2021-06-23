package com.pppp.todo

import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.pppp.database.FirebaseRepository
import com.pppp.usecases.Repository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
abstract class MainModule {

    companion object {
        @Provides
        fun provideRepository(): Repository = FirebaseRepository(Firebase.firestore)

    }
}