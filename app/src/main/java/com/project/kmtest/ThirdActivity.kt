package com.project.kmtest

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.project.kmtest.adapter.UserAdapter
import com.project.kmtest.api.UserViewModel
import com.project.kmtest.databinding.ActivityThirdBinding

class ThirdActivity : AppCompatActivity() {

    private lateinit var binding: ActivityThirdBinding
    private lateinit var adapter: UserAdapter

    private val viewModel: UserViewModel by lazy {
        ViewModelProvider(this)[UserViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityThirdBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.title = "Third Screen"

        setupRecyclerView()
        observeViewModel()

        binding.swipeRefreshLayout.setOnRefreshListener {
            viewModel.refresh()
        }
    }

    private fun setupRecyclerView() {
        adapter = UserAdapter(
            onItemClick = { user ->
                val resultIntent = Intent().apply {
                    val fullName = getString(R.string.userFullName, user.firstName, user.lastName)
                    putExtra("USER_NAME", fullName)
                }
                setResult(RESULT_OK, resultIntent)
                finish()
            },
            onLoadMore = {
                viewModel.loadMore()
            }
        )

        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = adapter
    }

    private fun observeViewModel() {
        viewModel.users.observe(this) { users ->
            binding.swipeRefreshLayout.isRefreshing = false
            adapter.addUsers(users)
            handleEmptyState(users.isEmpty() && !viewModel.loading.value!!)
        }

        viewModel.loading.observe(this) { isLoading ->
            binding.swipeRefreshLayout.isRefreshing = isLoading
        }

        viewModel.emptyState.observe(this) { isEmpty ->
            handleEmptyState(isEmpty)
        }

        viewModel.networkError.observe(this) { hasError ->
            if (hasError) {
                Toast.makeText(this, getString(R.string.networkErrorMessage), Toast.LENGTH_SHORT).show()
                binding.swipeRefreshLayout.isRefreshing = false
                handleEmptyState(true)
            }
        }
    }

    private fun handleEmptyState(isEmpty: Boolean) {
        if (isEmpty) {
            binding.recyclerView.visibility = View.GONE
            binding.emptyStateImage.root.visibility = View.VISIBLE
        } else {
            binding.recyclerView.visibility = View.VISIBLE
            binding.emptyStateImage.root.visibility = View.GONE
        }
    }
}