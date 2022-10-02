package com.arksana.mistoly.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
            adapter = MyStoliRecyclerViewAdapter(ArrayList())
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        homeViewModel = ViewModelProvider(requireActivity())[HomeViewModel::class.java]
        homeViewModel.stories.observe(viewLifecycleOwner) { stories ->
            (binding.list.adapter as MyStoliRecyclerViewAdapter).submitList(stories)
            if (stories?.isNotEmpty() == true) {
                binding.message.visibility = View.GONE
                binding.message.text = ""
            } else {
                binding.message.text = getString(R.string.empty_list)
                binding.message.visibility = View.VISIBLE
            }
        }
        refresh()
    }

    fun refresh() {
        binding.loadingView.group.visibility = View.VISIBLE
        homeViewModel.getStories(callback = { isError, message ->
            binding.message.text = message.ifEmpty { getString(R.string.error_message) }
            binding.message.visibility = if (isError) View.VISIBLE else View.GONE
            binding.loadingView.group.visibility = View.GONE
        })
    }

}