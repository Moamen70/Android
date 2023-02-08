package com.androiddevs.mvvmnewsapp.ui.Repository

import android.content.Context
import com.androiddevs.mvvmnewsapp.ui.api.RetrofitInstance
import com.androiddevs.mvvmnewsapp.ui.database.ArticleDatabase
import com.androiddevs.mvvmnewsapp.ui.models.Article

class NewsRepository(
    val db: ArticleDatabase.Companion,
    val context: Context
    ) {
    suspend fun getBreackingNews(countryCode : String , pageNumber : Int) =
        RetrofitInstance.api.getBreakingNews(countryCode,pageNumber)

    suspend fun searchNews(searchQuery : String , pageNumber : Int) =
        RetrofitInstance.api.serchForNews(searchQuery,pageNumber)

    suspend fun upsert(article: Article) =
        db.getInstance(context).getArticleDao.upsert(article)

    suspend fun deleteArticle(article: Article) =
        db.getInstance(context).getArticleDao.deleteArticle(article)

    fun getSavedNews() = db.getInstance(context).getArticleDao.getAllArticles()

}
