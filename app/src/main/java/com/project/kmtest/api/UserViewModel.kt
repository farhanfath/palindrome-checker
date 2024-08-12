package com.project.kmtest.api

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.project.kmtest.api.response.DataItem
import com.project.kmtest.api.response.UserResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UserViewModel : ViewModel() {

    private val repo = UserRepository()

    private val _users = MutableLiveData<List<DataItem>>()
    val users: LiveData<List<DataItem>> get() = _users

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> get() = _loading

    private val _emptyState = MutableLiveData<Boolean>()
    val emptyState: LiveData<Boolean> get() = _emptyState

    private val _networkError = MutableLiveData<Boolean>()
    val networkError: LiveData<Boolean> get() = _networkError

    private var currentPage = 1
    private val perPage = 10
    private var isLastPage = false
    var isRefreshing = false

    init {
        loadUsers()
    }

    private fun loadUsers() {
        if (isLastPage) return

        _loading.value = true
        _networkError.value = false
        repo.getUsers(currentPage, perPage).enqueue(object : Callback<UserResponse> {
            override fun onResponse(call: Call<UserResponse>, response: Response<UserResponse>) {
                _loading.value = false
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    responseBody?.let {
                        if (it.data.isEmpty()) {
                            isLastPage = true
                            _emptyState.value = currentPage == 1
                        } else {
                            val currentUsers = _users.value ?: emptyList()
                            _users.value = if (isRefreshing) it.data else currentUsers + it.data
                            currentPage++
                        }
                    }
                } else {
                    _networkError.value = true
                }
                isRefreshing = false
            }

            override fun onFailure(call: Call<UserResponse>, t: Throwable) {
                _loading.value = false
                _networkError.value = true
                isRefreshing = false
            }
        })
    }

    fun refresh() {
        currentPage = 1
        isLastPage = false
        isRefreshing = true
        _users.value = emptyList()
        loadUsers()
    }

    fun loadMore() {
        if (!isLastPage && _loading.value != true) {
            loadUsers()
        }
    }
}