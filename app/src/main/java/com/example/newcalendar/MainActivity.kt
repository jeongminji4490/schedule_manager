package com.example.newcalendar

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.PopupMenu
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import com.example.newcalendar.databinding.ActivityMainBinding
import com.kizitonwose.calendarview.model.CalendarDay
import com.kizitonwose.calendarview.model.CalendarMonth
import com.kizitonwose.calendarview.model.DayOwner
import com.kizitonwose.calendarview.ui.DayBinder
import com.kizitonwose.calendarview.ui.MonthHeaderFooterBinder
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import java.time.LocalDate
import java.time.YearMonth
import java.time.temporal.WeekFields
import java.util.*
import kotlin.collections.ArrayList


class MainActivity : AppCompatActivity() {

    private lateinit var binding : ActivityMainBinding
    private lateinit var navController : NavController
    private val coroutineScope by lazy { CoroutineScope(Dispatchers.IO) }
    private val context by lazy { this }
    //private val viewModel: ViewModel by inject()


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.fragmentContainer) as NavHostFragment
        navController = navHostFragment.navController

//        viewModel.getAllAlarms().observe(this, androidx.lifecycle.Observer {
//            for (i in it.indices){
//                val code = it[i].alarm_code
//                val content = it[i].content
//                Log.e("MainActivity", code.toString())
//                Log.e("MainActivity", content)
//            }
//        })


        setupSmoothBottomMenu()
    }

    private fun setupSmoothBottomMenu() { // smooth bottomBar & navController 연결
        val popupMenu = PopupMenu(this, null)
        popupMenu.inflate(R.menu.menu)
        val menu = popupMenu.menu
        binding.bottomBar.setupWithNavController(menu, navController)
    }

//    override fun onClick(v: View?) {
//        when(v?.id){
//            R.id.addScheduleBtn -> {
//                val dialog = AddDialogFragment()
//                dialog.show(supportFragmentManager, "AddScheduleDialog")
//            }
//            R.id.openScheduleBtn -> {
//                val dialog = ShowListFragment()
//                dialog.show(supportFragmentManager, "ShowListFragment")
//            }
//        }
//    }

    override fun onStart() {
        super.onStart()
    }
}