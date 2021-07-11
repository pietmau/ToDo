package com.pppp.todo

import android.content.Context
import androidx.work.WorkManager
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.pppp.database.FirebaseToDosRepository
import com.pppp.entities.ToDo
import com.pppp.todo.main.viewmodel.MainViewState
import com.pppp.todo.main.mapper.Mapper
import com.pppp.todo.notification.WorkManagerNotificationScheduler
import com.pppp.usecases.todos.EditTodoUseCase
import com.pppp.usecases.ToDosRepository
import com.pppp.usecases.notification.NotificationScheduler
import com.pppp.usecases.todos.GetToDoUseCase
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.qualifiers.ApplicationContext

@Module
@InstallIn(ViewModelComponent::class)
abstract class MainModule {

    @Binds
    abstract fun bindsNotificationScheduler(workManagerNotificationScheduler: WorkManagerNotificationScheduler): NotificationScheduler

    companion object {
        @Provides
        fun bindsMapper(): @JvmSuppressWildcards (List<ToDo>) -> MainViewState = Mapper()

        @Provides
        fun provideRepository(): ToDosRepository = FirebaseToDosRepository(Firebase.firestore)

        @Provides
        fun toDoListUseCase(toDosRepository: ToDosRepository): GetToDoUseCase = GetToDoUseCase(toDosRepository)

        @Provides
        fun editToDoUseCase(
            toDosRepository: ToDosRepository,
            notificationScheduler: NotificationScheduler
        ): EditTodoUseCase = EditTodoUseCase(toDosRepository, notificationScheduler)

        @Provides
        fun workManager(@ApplicationContext context: Context): WorkManager =
            WorkManager.getInstance(context)
    }
}