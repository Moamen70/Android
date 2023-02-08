package com.androiddevs.mvvmnewsapp.ui.adabters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.androiddevs.mvvmnewsapp.R
import com.androiddevs.mvvmnewsapp.ui.models.Article
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.item_article_preview.view.*

class NewsAdapter : ListAdapter<Article,NewsAdapter.ArticleViewHolder>(ArticleDiffCallBack()) {
    class ArticleViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {

    }

    class ArticleDiffCallBack : DiffUtil.ItemCallback<Article>() {

        override fun areItemsTheSame(oldItem: Article, newItem: Article): Boolean {
            return oldItem.url == newItem.url
        }


        override fun areContentsTheSame(oldItem: Article, newItem: Article): Boolean {
            return oldItem == newItem
        }

    }

//    private val differCallback = object : DiffUtil.ItemCallback<Article>() {
//        override fun areItemsTheSame(oldItem: Article, newItem: Article): Boolean {
//            return oldItem.url == newItem.url
//        }
//
//        override fun areContentsTheSame(oldItem: Article, newItem: Article): Boolean {
//            return oldItem == newItem
//        }
//    }




    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticleViewHolder {
        return ArticleViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_article_preview,
                parent,
                false
            )
        )
    }

    private var onItemClickListener: ((Article) -> Unit)? = null

    override fun onBindViewHolder(holder: ArticleViewHolder, position: Int) {
        val article = getItem(position)
        holder.itemView.apply {
            Glide.with(this).load(article.urlToImage).into(ivArticleImage)
            tvSource.text = article.source?.name
            tvTitle.text = article.title
            tvDescription.text = article.description
            tvPublishedAt.text = article.publishedAt
            setOnClickListener {
                onItemClickListener?.let { it(article) }
            }
        }
    }
//override fun onBindViewHolder(holder: ArticleViewHolder, position: Int) {
//    val article = differ.currentList[position]
//    holder.itemView.apply {
//        Glide.with(this).load(article.urlToImage).into(ivArticleImage)
//        tvSource.text = article.source.name
//        tvTitle.text = article.title
//        tvDescription.text = article.description
//        tvPublishedAt.text = article.publishedAt
//        setOnClickListener {
//            onItemClickListener?.let { it(article) }
//        }
//    }
//}



    fun setOnItemClickListener(listener: (Article) -> Unit) {
        onItemClickListener = listener
    }


//    override fun getItemCount(): Int {
//        return differ.currentList.size
//    }

}