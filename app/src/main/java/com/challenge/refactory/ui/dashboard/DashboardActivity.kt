package com.challenge.refactory.ui.dashboard

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import com.challenge.refactory.App
import com.challenge.refactory.R
import com.challenge.refactory.databinding.ActivityDashboardBinding
import com.challenge.refactory.ui.dashboard.adapter.TaskAdapter
import com.challenge.refactory.ui.form.FormDialogFragment
import com.challenge.refactory.ui.profile.ProfileActivity

class DashboardActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDashboardBinding

    private val viewModel:TaskViewModel by viewModels() {
        TaskViewModelFactory(
            (application as App).database.itemDao()
        )
    }

    companion object{
        const val EXTRAS_USER_EMAIL = "USER_EMAIL"

        @JvmStatic
        fun startActivity(context: Context, email: String?){
            val intent = Intent(context, DashboardActivity::class.java)
            intent.putExtra(EXTRAS_USER_EMAIL, email)
            context.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDashboardBinding.inflate(layoutInflater)
        supportActionBar?.hide()
        setContentView(binding.root)

        val email = intent?.getStringExtra(EXTRAS_USER_EMAIL)
        binding.tvEmail.text = email
        binding.ivProfile.setOnClickListener { navigateToProfile() }
        binding.fabAddTask.setOnClickListener { openDialogFragment() }

        val adapter = TaskAdapter()
        binding.rvTask.adapter = adapter

        viewModel.allTasks.observe(this){ items->
            items.let { adapter.submitList(it) }
        }
    }

    private fun navigateToProfile(){
        val intent = Intent(this, ProfileActivity::class.java)
        startActivity(intent)
    }

    private fun openDialogFragment() {
        FormDialogFragment("Add Task"){
            Toast.makeText(this, "Saved", Toast.LENGTH_SHORT).show()
        }.show(supportFragmentManager,"custom_dialog")
    }
}