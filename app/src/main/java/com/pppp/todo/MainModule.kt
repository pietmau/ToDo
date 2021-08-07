package com.pppp.todo

import android.content.Context
import androidx.work.WorkManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.pppp.database.FirebaseLastVisitedListRepository
import com.pppp.database.FirebaseListsRepository
import com.pppp.database.FirebaseToDosRepository
import com.pppp.entities.ToDo
import com.pppp.entities.ToDoList
import com.pppp.entities.User
import com.pppp.todo.drawer.DrawerMapper
import com.pppp.todo.drawer.ViewState
import com.pppp.todo.main.viewmodel.MainViewState
import com.pppp.todo.main.mapper.Mapper
import com.pppp.todo.notification.WorkManagerNotificationScheduler
import com.pppp.usecases.ListsRepository
import com.pppp.usecases.todos.EditTodoUseCase
import com.pppp.usecases.ToDosRepository
import com.pppp.usecases.lists.GetListsUseCase
import com.pppp.usecases.main.LastVisitedListRepository
import com.pppp.usecases.main.LastVisitedUseCase
import com.pppp.usecases.notification.NotificationScheduler
import com.pppp.usecases.todos.GetToDoUseCase
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Qualifier

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
        fun provideFirebaseListsRepository(): ListsRepository =
            FirebaseListsRepository(Firebase.firestore)

        @Provides
        fun toDoListUseCase(toDosRepository: ToDosRepository): GetToDoUseCase =
            GetToDoUseCase(toDosRepository)

        @Provides
        fun listsUseCase(listsRepository: ListsRepository): GetListsUseCase =
            GetListsUseCase(listsRepository)

        @Provides
        fun editToDoUseCase(
            toDosRepository: ToDosRepository,
            notificationScheduler: NotificationScheduler
        ): EditTodoUseCase = EditTodoUseCase(toDosRepository, notificationScheduler)

        @Provides
        fun workManager(@ApplicationContext context: Context): WorkManager =
            WorkManager.getInstance(context)

        @Provides
        @UserId
        fun provideUserId(): String = requireNotNull(FirebaseAuth.getInstance().currentUser?.uid)

        @Provides
        fun provideDrawerMapper(): @JvmSuppressWildcards (List<ToDoList>) -> ViewState =
            DrawerMapper()

        @Provides
        fun provideUser(): User = User(Firebase.auth.currentUser?.uid!!)

        @Provides
        fun provideLastVisitedListRepository(): LastVisitedListRepository =
            FirebaseLastVisitedListRepository(Firebase.firestore)

        @Provides
        fun provideLastVisitedUseCase(repo: LastVisitedListRepository) = LastVisitedUseCase(repo)
    }
}

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class UserId