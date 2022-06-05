package com.archer.ktcoroutineslearn

import android.content.Intent
import android.os.Bundle
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.alibaba.fastjson.JSON
import com.archer.ktcoroutineslearn.collection.CollectionOperator
import com.archer.ktcoroutineslearn.databinding.ActivityMainBinding
import com.archer.ktcoroutineslearn.lifecycle.LifecycleTestActivity
import com.archer.ktcoroutineslearn.util.GSLog
import kotlinx.coroutines.*
import retrofit2.await
import retrofit2.awaitResponse
import java.lang.RuntimeException
import kotlin.Exception

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navView: BottomNavigationView = binding.navView

        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(setOf(
            R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications))
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
//        ContextAndDispatchers.contexts()
//        ContextAndDispatchers.threadLocal()
//        SuspendMethods.main()
//        FlowKt.flowTest()
//        FlowKt.showFlowIsCold()
//        FlowKt.cancelFlow()
//        runBlocking {
//            FlowKt.flowMapOperator()
//        }
//        CollectionOperator.operatorDemo()
//        FlowKt.flowOnTest()
//        FlowKt.conflateFlowTest()
        FlowKt.collectLatestTest()
        startActivity(Intent(applicationContext, LifecycleTestActivity::class.java))
    }

}