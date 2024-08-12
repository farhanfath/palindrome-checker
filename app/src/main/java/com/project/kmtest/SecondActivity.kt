package com.project.kmtest

import android.content.Intent
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.project.kmtest.api.UserViewModel
import com.project.kmtest.databinding.ActivitySecondBinding

class SecondActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySecondBinding

    private val viewModel: UserViewModel by lazy {
        ViewModelProvider(this)[UserViewModel::class.java]
    }

    private val startForResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == RESULT_OK) {
            val userName = result.data?.getStringExtra("USER_NAME")
            viewModel.users.observe(this) {
                binding.selectedUserText.text = userName
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySecondBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.title = "Second Screen"

        val userName = intent.getStringExtra("username")
        binding.userNameText.text = userName

        binding.chooseUserBtn.setOnClickListener {
            val intent = Intent(this, ThirdActivity::class.java)
            startForResult.launch(intent)
        }
    }
}