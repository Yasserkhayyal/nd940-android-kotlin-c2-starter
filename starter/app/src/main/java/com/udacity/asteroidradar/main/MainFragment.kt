package com.udacity.asteroidradar.main

import android.os.Bundle
import android.view.*
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.udacity.asteroidradar.R
import com.udacity.asteroidradar.databinding.FragmentMainBinding

class MainFragment : Fragment() {

    private lateinit var asteroidsRecyclerAdapter: AsteroidsRecyclerAdapter
    private lateinit var binding: FragmentMainBinding
    private val viewModelProvider: ViewModelProvider.Factory by lazy {
        MainViewModel.Factory(requireActivity().application)
    }

    private val viewModel: MainViewModel by lazy {
        ViewModelProvider(this, viewModelProvider)[MainViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMainBinding.inflate(inflater)
        binding.lifecycleOwner = this

        binding.viewModel = viewModel

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        asteroidsRecyclerAdapter = AsteroidsRecyclerAdapter {
            findNavController().navigate(MainFragmentDirections.actionShowDetail(it))
        }
        binding.asteroidRecycler.adapter = asteroidsRecyclerAdapter
        viewModel.asteroids.observe(viewLifecycleOwner) {
            asteroidsRecyclerAdapter.data = it
        }

        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                // Add menu items here
                menuInflater.inflate(R.menu.main_overflow_menu, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                viewModel.showFilteredResults(
                    FilterType.values().first { it.menuItemId == menuItem.itemId })
                return true
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }

}
