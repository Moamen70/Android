package com.androiddevs.mvvmnewsapp.ui.fragments

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.androiddevs.mvvmnewsapp.R
//import com.androiddevs.mvvmnewsapp.ui.adabters.NewsAdabter
import com.androiddevs.mvvmnewsapp.ui.adabters.NewsAdapter
import com.androiddevs.mvvmnewsapp.ui.ui.NewsViewModel
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_saved_news.*
import kotlinx.android.synthetic.main.fragment_search_news.*

class SavedNewsFragment : Fragment(R.layout.fragment_saved_news) {
    lateinit var newsAdabter: NewsAdapter
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        viewModel = (activity as NewsActivity).viewModel
        val viewModel by activityViewModels<NewsViewModel>()
        setupRecyclerView()


        newsAdabter.setOnItemClickListener { article ->
            val bundle = Bundle().apply {
                putSerializable("article",article)
            }
            findNavController().navigate(R.id.action_savedNewsFragment_to_articleFragment,bundle)
        }

        val itemToutchHelperCallBack = object : ItemTouchHelper.SimpleCallback(
            ItemTouchHelper.UP or ItemTouchHelper.DOWN ,
            ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
        ){

            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return true
            }


            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                Toast.makeText(context,"swiped",Toast.LENGTH_SHORT).show()
            }

//            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
//                val position = viewHolder.adapterPosition
//                val differ = AsyncListDiffer(newsAdabter,NewsAdapter.ArticleDiffCallBack())
//                val article =
//                viewModel.deleteArticle(article)
//                Snackbar.make(view,"Article Deleted Successfully",Snackbar.LENGTH_SHORT).apply {
//                    setAction("undo"){
//                        viewModel.saveArticle(article)
//                    }
//                    show()
//                }
//            }
        }

        ItemTouchHelper(itemToutchHelperCallBack).apply {
            attachToRecyclerView(rvSavedNews)
        }

        viewModel.getSaveNews().observe(viewLifecycleOwner, Observer { articles ->
            newsAdabter.submitList(articles)
        })

    }
    private fun setupRecyclerView(){
        newsAdabter = NewsAdapter()
        rvSavedNews.apply {
            adapter = newsAdabter
            layoutManager = LinearLayoutManager(activity)
        }
    }

}
