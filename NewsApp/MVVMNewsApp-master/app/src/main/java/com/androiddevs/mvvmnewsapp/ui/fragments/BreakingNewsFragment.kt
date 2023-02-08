package com.androiddevs.mvvmnewsapp.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.androiddevs.mvvmnewsapp.R
import com.androiddevs.mvvmnewsapp.ui.NewsActivity
//import com.androiddevs.mvvmnewsapp.ui.adabters.NewsAdabter
import com.androiddevs.mvvmnewsapp.ui.adabters.NewsAdapter
import com.androiddevs.mvvmnewsapp.ui.ui.NewsViewModel
import com.androiddevs.mvvmnewsapp.ui.util.Resource
import kotlinx.android.synthetic.main.fragment_breaking_news.*
import kotlinx.android.synthetic.main.fragment_breaking_news.paginationProgressBar
import kotlinx.android.synthetic.main.fragment_search_news.*

class BreakingNewsFragment : Fragment(R.layout.fragment_breaking_news) {

//    lateinit var viewModel: NewsViewModel
    lateinit var newsAdabter: NewsAdapter
    val TAG = "BreakingNewsFragment"

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        viewModel = (activity as NewsActivity).viewModel
        val viewModel by activityViewModels<NewsViewModel>()
        setupRecyclerView()

        newsAdabter.setOnItemClickListener { article ->
            val bundle = Bundle().apply {
                putSerializable("article",article)
            }
            requireView().findNavController().navigate(BreakingNewsFragmentDirections.actionBreakingNewsFragmentToArticleFragment(article))
        }

        viewModel.breakingNews.observe(viewLifecycleOwner , Observer { response ->
            when(response){
                is Resource.Succsess ->{
                    hideProgressBar()
                    response.data?.let { newsResponse ->
                        newsAdabter.submitList(newsResponse.articles)
                    }
                }
                is Resource.Erorr ->{
                    response.message?.let { message ->
                        Toast.makeText(context,"Connection Erorr",Toast.LENGTH_SHORT).show()
                    }
                }
                is Resource.Loading ->{
                    showProgressBar()
                }
            }
        })
    }

    private fun hideProgressBar() {
        paginationProgressBar.visibility = View.INVISIBLE
    }

    private fun showProgressBar() {
        paginationProgressBar.visibility = View.VISIBLE
    }

    private fun setupRecyclerView(){
        newsAdabter = NewsAdapter()
        rvBreakingNews.apply {
            adapter = newsAdabter
            layoutManager = LinearLayoutManager(activity)
        }
    }
}