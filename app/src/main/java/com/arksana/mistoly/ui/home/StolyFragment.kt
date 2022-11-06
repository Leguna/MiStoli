package com.arksana.mistoly.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.arksana.mistoly.R
import com.arksana.mistoly.databinding.FragmentItemListBinding

class StolyFragment : Fragment() {

    private lateinit var binding: FragmentItemListBinding
    private lateinit var homeViewModel: HomeViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?,
    ): View {
        binding = FragmentItemListBinding.inflate(inflater, container, false)

        with(binding.list) {
            layoutManager = LinearLayoutManager(context)
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        homeViewModel = ViewModelProvider(requireActivity())[HomeViewModel::class.java]
        binding.loadingView.group.visibility = View.GONE
        binding.list.adapter = MyStoliRecyclerViewAdapter()
        val adapter = (binding.list.adapter as MyStoliRecyclerViewAdapter)

        homeViewModel.getAllStories().observe(viewLifecycleOwner) { stories ->
            binding.loadingView.group.visibility = View.VISIBLE

            try {
                adapter.submitData(lifecycle, stories)
            } catch (e: Exception) {
                Toast.makeText(context, getString(R.string.error_message), Toast.LENGTH_SHORT).show()
                binding.message.text = getString(R.string.error_message)
                binding.message.visibility = View.VISIBLE
            }

            binding.loadingView.group.visibility = View.GONE
        }
    }

    fun refresh() {
        homeViewModel.getAllStories()
    }

}