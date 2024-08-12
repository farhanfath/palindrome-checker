package com.project.kmtest

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import com.project.kmtest.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        binding.palindromeCheckBtn.setOnClickListener {
            palindromeHandler()
        }
        binding.nextBtn.setOnClickListener {
            openSecondScreenHandler()
        }
    }

    private fun openSecondScreenHandler() {
        val username = binding.nameEt.text.toString()
        if (username.isEmpty()) {
            binding.nameEtLayout.error = "Name cannot be empty"
        } else {
            val intent = Intent(this, SecondActivity::class.java)
            intent.putExtra("username", username)
            startActivity(intent)
        }
    }

    private fun palindromeHandler() {
        val palindromeText = binding.palindromeEt.text.toString()

        if (palindromeText.isEmpty()) {
            binding.palindromeEtLayout.error = "Palindrome text cannot be empty"
        } else {
            val isPalindrome = palindromeCheck(palindromeText)
            showResultDialog(isPalindrome)
        }
    }

    private fun palindromeCheck(sentence: String): Boolean {
        val noSpaceText = sentence.replace("\\s".toRegex(), "").lowercase()
        return noSpaceText == noSpaceText.reversed()
    }

    private fun showResultDialog(isPalindrome: Boolean) {
        val message = if (isPalindrome) "Is Palindrome" else "Not Palindrome"

        AlertDialog.Builder(this)
            .setTitle("Palindrome Status :")
            .setMessage(message)
            .setPositiveButton("OK") { dialog, _ -> dialog.dismiss() }
            .show()
    }
}