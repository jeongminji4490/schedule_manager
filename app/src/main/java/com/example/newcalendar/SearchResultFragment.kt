package com.example.newcalendar

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.newcalendar.databinding.ResultsDialogBinding
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject

class SearchResultFragment : DialogFragment() {

    private val binding by viewBinding(ResultsDialogBinding::bind)
    private val viewModel : ViewModel by inject()
    private lateinit var list : List<ScheduleDataModel>
    private lateinit var adapter : TestAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.results_dialog, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.vm = viewModel
        binding.lifecycleOwner = this

        //binding.searchEdit.addTextChangedListener(this)
        val searchTextListener : SearchView.OnQueryTextListener =
            object : SearchView.OnQueryTextListener {
                //검색버튼 클릭 시 호출
                override fun onQueryTextSubmit(s: String?): Boolean {
                    return false
                }
                override fun onQueryTextChange(s: String?): Boolean {
                    adapter.filter.filter(s)
                    if (s != null) {
                        Log.e(TAG, s)
                    }
                    return false
                }
            }
        binding.searchEdit.setOnQueryTextListener(searchTextListener)

        viewModel.result.observe(viewLifecycleOwner, Observer {
            it.let {
                list = it
                adapter = TestAdapter(list)
            }
        })
    }

    override fun onStart() {
        super.onStart()
    }

    companion object{
        const val TAG = "SearchResultFragment"
    }
}