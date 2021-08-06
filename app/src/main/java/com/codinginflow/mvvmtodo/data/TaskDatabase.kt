package com.codinginflow.mvvmtodo.data

import android.app.Application
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.codinginflow.mvvmtodo.di.ApplicationScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Provider

@Database(entities = [Task::class], version = 1)
abstract class TaskDatabase: RoomDatabase() {

    abstract fun taskDao(): TaskDao

    // here we have to tell Dagger how it can create an instance of class Callback, so it can later inject/pass it to our Db.
    class Callback @Inject constructor(
        private val database: Provider<TaskDatabase>,
        @ApplicationScope private val  applicationScope: CoroutineScope
    ): RoomDatabase.Callback(){
        //Called when the database is created for the first time. This is called after all the tables are created.
        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)

            val dao =database.get().taskDao() // here Dagger will instantiate the db when this onCreate() method is executed

            applicationScope.launch {
                dao.insert(Task("wash the dishes"))
                dao.insert(Task("make stuff"))
                dao.insert(Task("TAKE A BATH", important = true))
                dao.insert(Task("HELLO WORLD", completed = true))
                dao.insert(Task("STUDY DAY"))
            }

        }
    }
}