package com.pppp.todo

import android.content.Context
import androidx.work.WorkManager
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.pppp.database.FirebaseRepository
import com.pppp.entities.ToDo
import com.pppp.todo.main.TodoMainViewModel
import com.pppp.todo.main.mapper.Mapper
import com.pppp.todo.notification.WorkManagerNotificationScheduler
import com.pppp.usecases.EditToDoUseCase
import com.pppp.usecases.Repository
import com.pppp.usecases.addtodo.AddToDoUseCase
import com.pppp.usecases.notification.NotificationScheduler
import com.pppp.usecases.notification.NotificationUseCase
import com.pppp.usecases.todolist.ToDoListUseCase
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
        fun bindsMapper(): @JvmSuppressWildcards (List<ToDo>) -> TodoMainViewModel = Mapper()

        @Provides
        fun provideRepository(): Repository = FirebaseRepository(Firebase.firestore)

        @Provides
        fun toDoListUseCase(repository: Repository): ToDoListUseCase = ToDoListUseCase(repository)

        @Provides
        fun addToDoUseCase(repository: Repository): AddToDoUseCase = AddToDoUseCase(repository)

        @Provides
        fun editToDoUseCase(repository: Repository): EditToDoUseCase = EditToDoUseCase(repository)

        @Provides
        fun notificationUseCase(notificationScheduler: NotificationScheduler): NotificationUseCase =
            NotificationUseCase(notificationScheduler)

        @Provides
        fun workManager(@ApplicationContext context: Context): WorkManager =
            WorkManager.getInstance(context)
    }
}