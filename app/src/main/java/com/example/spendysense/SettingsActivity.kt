package com.example.spendysense

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.spendysense.AddTransactionActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStreamWriter
import java.io.BufferedWriter
import androidx.core.content.FileProvider

class SettingsActivity : AppCompatActivity() {

    private lateinit var sharedPreferences: SharedPreferences
    private val CHANNEL_ID = "download_channel"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        sharedPreferences = getSharedPreferences("user_prefs", MODE_PRIVATE)

        val usernameTextView = findViewById<TextView>(R.id.usernameTextView)
        val emailTextView = findViewById<TextView>(R.id.emailTextView)
        val currentPasswordEditText = findViewById<EditText>(R.id.currentPasswordEditText)
        val newPasswordEditText = findViewById<EditText>(R.id.newPasswordEditText)
        val confirmNewPasswordEditText = findViewById<EditText>(R.id.confirmNewPasswordEditText)
        val changePasswordButton = findViewById<Button>(R.id.changePasswordButton)
        val logoutButton = findViewById<Button>(R.id.logoutButton)
        val backupButton = findViewById<Button>(R.id.backupButton)
        val restoreButton = findViewById<Button>(R.id.restoreButton)
        val resetButton = findViewById<Button>(R.id.resetButton)

        // Fetch and display the user's username and email
        val username = sharedPreferences.getString("username", "Unknown")
        val email = sharedPreferences.getString("email", "Unknown")
        usernameTextView.text = username
        emailTextView.text = email

        // Handle password change
        changePasswordButton.setOnClickListener {
            val currentPassword = currentPasswordEditText.text.toString()
            val newPassword = newPasswordEditText.text.toString()
            val confirmNewPassword = confirmNewPasswordEditText.text.toString()

            val storedPassword = sharedPreferences.getString("password", null)

            if (storedPassword == currentPassword) {
                if (newPassword == confirmNewPassword) {
                    changePassword(newPassword)
                } else {
                    Toast.makeText(this, "New passwords do not match.", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Current password is incorrect.", Toast.LENGTH_SHORT).show()
            }
        }

        // Handle logout
        logoutButton.setOnClickListener {
            clearSession()
            Toast.makeText(this, "Logged out successfully.", Toast.LENGTH_SHORT).show()
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }

        backupButton.setOnClickListener {
            backupTransactions()
        }

        restoreButton.setOnClickListener {
            restoreTransactions()
        }

        resetButton.setOnClickListener {
            resetApp()
        }

        // Bottom Navigation Bar
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        bottomNavigationView.selectedItemId = R.id.navigation_settings

        bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_home -> {
                    startActivity(Intent(this, MainActivity::class.java))
                    true
                }
                R.id.navigation_add_transaction -> {
                    startActivity(Intent(this, AddTransactionActivity::class.java))
                    true
                }
                R.id.navigation_view_transactions -> {
                    startActivity(Intent(this, ViewTransactionsActivity::class.java))
                    true
                }
                R.id.navigation_notifications -> {
                    startActivity(Intent(this, NotificationsActivity::class.java))
                    true
                }
                R.id.navigation_settings -> true
                else -> false
            }
        }
    }

    private fun changePassword(newPassword: String) {
        val editor = sharedPreferences.edit()
        editor.putString("password", newPassword)
        editor.apply()

        Toast.makeText(this, "Password changed successfully.", Toast.LENGTH_SHORT).show()
    }

    private fun clearSession() {
        val editor = sharedPreferences.edit()
        editor.remove("isLoggedIn")
        editor.remove("loggedInUsername")
        editor.remove("loggedInUserEmail")
        editor.apply()
    }

    private fun backupTransactions() {
        // Keep the original backup functionality
        val transactions = sharedPreferences.getStringSet("transactions", emptySet())
        val editor = sharedPreferences.edit()
        editor.putStringSet("transactions_backup", transactions)
        editor.apply()

        // Export transactions to a text file
        if (transactions.isNullOrEmpty()) {
            Toast.makeText(this, "No transactions to export", Toast.LENGTH_SHORT).show()
            return
        }

        try {
            // Create a file in the app's external files directory
            val fileName = "SpendySense_Transactions_${getCurrentDateTimeStamp()}.txt"
            val file = File(getExternalFilesDir(null), fileName)

            // Write transactions to the file
            FileOutputStream(file).use { fos ->
                OutputStreamWriter(fos).use { osw ->
                    BufferedWriter(osw).use { writer ->
                        // Write header
                        writer.write("SpendySense Transactions Export\n")
                        writer.write("Generated: ${getCurrentDateTimeStamp()}\n\n")
                        writer.write("TYPE | CATEGORY | DESCRIPTION | AMOUNT | DATE\n")
                        writer.write("------------------------------------------------\n\n")

                        // Write each transaction
                        transactions.forEach { transaction ->
                            writer.write("$transaction\n")
                        }
                    }
                }
            }

            // Show success message with file path
            Toast.makeText(this, "Transactions exported to:\n${file.absolutePath}", Toast.LENGTH_LONG).show()

            // Share the file
            shareExportedFile(file)

        } catch (e: Exception) {
            Toast.makeText(this, "Export failed: ${e.message}", Toast.LENGTH_SHORT).show()
            e.printStackTrace()
        }
    }

    private fun restoreTransactions() {
        val transactions = sharedPreferences.getStringSet("transactions_backup", emptySet())
        val editor = sharedPreferences.edit()
        editor.putStringSet("transactions", transactions)
        editor.apply()

        Toast.makeText(this, "Transactions restored successfully.", Toast.LENGTH_SHORT).show()
    }

    private fun resetApp() {
        val editor = sharedPreferences.edit()
        editor.remove("transactions")
        editor.remove("transactions_backup")
        editor.remove("budget_${getCurrentMonth()}")
        editor.apply()

        Toast.makeText(this, "App data reset successfully.", Toast.LENGTH_SHORT).show()
    }

    private fun getCurrentMonth(): String {
        val dateFormat = SimpleDateFormat("MMMM yyyy", Locale.getDefault())
        return dateFormat.format(Date())
    }

    // Add this method to get a timestamp for the filename
    private fun getCurrentDateTimeStamp(): String {
        val sdf = SimpleDateFormat("yyyy-MM-dd_HH-mm-ss", Locale.getDefault())
        return sdf.format(Date())
    }

    // Add this method to share the exported file
    private fun shareExportedFile(file: File) {
        val uri = FileProvider.getUriForFile(
            this,
            "${packageName}.fileprovider",
            file
        )

        val shareIntent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_STREAM, uri)
            type = "text/plain"
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }

        startActivity(Intent.createChooser(shareIntent, "Share Transactions Export"))
    }
}
