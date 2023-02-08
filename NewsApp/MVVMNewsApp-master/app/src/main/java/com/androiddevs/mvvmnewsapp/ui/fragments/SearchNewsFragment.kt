package com.androiddevs.mvvmnewsapp.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.androiddevs.mvvmnewsapp.R
//import com.androiddevs.mvvmnewsapp.ui.adabters.NewsAdabter
import com.androiddevs.mvvmnewsapp.ui.adabters.NewsAdapter
import com.androiddevs.mvvmnewsapp.ui.ui.NewsViewModel
import com.androiddevs.mvvmnewsapp.ui.util.Resource
import kotlinx.android.synthetic.main.fragment_breaking_news.*
import kotlinx.android.synthetic.main.fragment_search_news.*
import kotlinx.android.synthetic.main.fragment_search_news.paginationProgressBar
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class SearchNewsFragment : Fragment(R.layout.fragment_search_news) {
    lateinit var newsAdabter: NewsAdapter
    val TAG = "SearchNewsFragment"
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        viewModel = (activity as NewsActivity).viewModel
        val viewModel by activityViewModels<NewsViewModel>()
        setupRecyclerView()

        newsAdabter.setOnItemClickListener { article ->
            val bundle = Bundle().apply {
                putSerializable("article",article)
            }
            findNavController().navigate(R.id.action_searchNewsFragment_to_articleFragment,bundle)
        }


        var job : Job? = null
        etSearch.addTextChangedListener {
            job?.cancel()
            job = MainScope().launch {
                delay(500)
                it.let {
                    if (it.toString().isNotEmpty()){
                        viewModel.getSearchNews(it.toString())
                    }
                }
            }
        }

        viewModel.searchNews.observe(viewLifecycleOwner , Observer { response ->
            when(response){
                is Resource.Succsess ->{
                    hideProgressBar()
                    response.data?.let { newsResponse ->
                        newsAdabter.submitList(newsResponse.articles)
                    }
                }
                is Resource.Erorr ->{
                    response.message?.let { message ->
                        Log.e(TAG , "An erorr occured: $message" )
                    }
                }
                is Resource.Loading ->{
                    showProgressBar()
                }
            }
        })

    }

    private fun showProgressBar() {
        paginationProgressBar.visibility = View.INVISIBLE
    }

    private fun hideProgressBar() {
        paginationProgressBar.visibility = View.VISIBLE
    }

    private fun setupRecyclerView(){
        newsAdabter = NewsAdapter()
        rvSearchNews.apply {
            adapter = newsAdabter
            layoutManager = LinearLayoutManager(activity)
        }
    }
}