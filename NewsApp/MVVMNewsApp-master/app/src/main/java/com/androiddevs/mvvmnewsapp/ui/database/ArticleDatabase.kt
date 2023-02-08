package com.androiddevs.mvvmnewsapp.ui.database

import android.content.Context
import androidx.room.*
import com.androiddevs.mvvmnewsapp.ui.models.Article

@Database(entities = [Article::class], version = 1, exportSchema = false)

@TypeConverters(Converters::class)
abstract class ArticleDatabase : RoomDatabase() {

    abstract val getArticleDao: ArticleDao
    companion object {
        @Volatile
        private var INSTANCE: ArticleDatabase? = null
        fun getInstance(context: Context): ArticleDatabase {
            synchronized(this) {
                var instance = INSTANCE
                if (instance == null){
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        ArticleDatabase::class.java,
                        "article_db.db"
                    )
                        .fallbackToDestructiveMigration()
                        .build()
                    INSTANCE = instance
                }
                return instance
            }
        }

    }
}