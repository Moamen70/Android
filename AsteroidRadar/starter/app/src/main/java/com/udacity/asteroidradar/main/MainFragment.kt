package com.udacity.asteroidradar.main

import android.os.Bundle
import android.view.*
import androidx.annotation.RequiresPermission
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.udacity.asteroidradar.R
import com.udacity.asteroidradar.database.AsteroidDao
import com.udacity.asteroidradar.databinding.FragmentMainBinding
import com.udacity.asteroidradar.repository.FilterList
import com.udacity.asteroidradar.repository.LoadingOrCompleteStatus
import com.udacity.asteroidradar.repository.Repository

class MainFragment : Fragment() {
    private lateinit var viewModel: MainViewModel


//    val viewModel by activityViewModels<MainViewModel>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val binding = FragmentMainBinding.inflate(inflater)
        binding.lifecycleOwner = this
        val viewModelFactory = MainViewModelFactory(requireActivity().application)
        viewModel = ViewModelProvider(this, viewModelFactory).get(MainViewModel::class.java)

        binding.viewModel = viewModel

        val adaptor =
            MainAdabter{ asteroid ->
                viewModel.onSelectAsteroid(asteroid)
            }

        binding.asteroidRecycler.adapter = adaptor

        viewModel.selectedAsteroid.observe(viewLifecycleOwner, Observer { selectedAsteroid ->
            if (selectedAsteroid != null) {
                requireView().findNavController()
                    .navigate(MainFragmentDirections.actionShowDetail(selectedAsteroid))
                viewModel.onClearSelected()
            }
        })

        viewModel.loadingStatus.observe(viewLifecycleOwner, Observer { status ->
            when (status) {
                LoadingOrCompleteStatus.LOADING -> {
                    binding.statusLoadingWheel.visibility = View.VISIBLE
                }
                else -> {
                    binding.statusLoadingWheel.visibility = View.GONE
                }
            }
        })

        setHasOptionsMenu(true)


        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main_overflow_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val selectedFilter: FilterList = when (item.itemId) {
            R.id.show_all_menuItem -> {
                FilterList.ALL
            }
            R.id.show_today_menuItem -> {
                FilterList.TODAY
            }
            else -> {
                FilterList.Week
            }
        }
        viewModel.selectFilter(selectedFilter)
        return true
    }
}
