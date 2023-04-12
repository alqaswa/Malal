package com.example.malal.presentation

import android.content.Intent
import android.opengl.Visibility
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.malal.R
import com.example.malal.databinding.ActivityMainBinding
import com.example.malal.presentation.fragment.main.CartFragment
import com.example.malal.presentation.fragment.main.CheckoutFragment
import com.example.malal.util.ConnectionLiveData
import com.example.malal.util.MyTag
import com.example.malal.util.extention.hide
import com.example.malal.util.extention.hideBottomNav
import com.example.malal.util.extention.show
import com.example.malal.util.extention.showBottomNav
import com.example.malal.viewmodel.ShopViewModel
import com.google.android.material.snackbar.Snackbar
import com.razorpay.Checkout
import com.razorpay.PaymentResultListener
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class MainActivity:AppCompatActivity(),NavController.OnDestinationChangedListener,PaymentResultListener
{
    private lateinit var binding:ActivityMainBinding
    private val connectionLiveData by lazy { ConnectionLiveData(this) }
    private var firstCheckInternetConnection = true

    override fun onCreate(savedInstanceState:Bundle?)
    {
        super.onCreate(savedInstanceState)
        binding=DataBindingUtil.setContentView(this,R.layout.activity_main)

        observeNetworkConnection()
        initBottomNavigationView()
    }

    private fun observeNetworkConnection() {
        connectionLiveData.observe(this) {isInternetAvailable ->
            if(isInternetAvailable && !firstCheckInternetConnection)
            {
                showViews()
                Snackbar.make(
                        binding.parentLayout,
                        getString(R.string.backOnline),
                        Snackbar.LENGTH_SHORT
                             ).setBackgroundTint(getColor(R.color.success)).show()
            }
            else if(!isInternetAvailable)
            {
                hideViews()
                Snackbar.make(
                        binding.parentLayout,
                        getString(R.string.connectionLost),
                        Snackbar.LENGTH_SHORT
                             ).setBackgroundTint(getColor(R.color.error)).show()
            }
            firstCheckInternetConnection=false
        }
    }

    private fun showViews()
    {
        binding.apply {
            fragmentContainer.show()
            bottomNavigationView.show()
            networkLayout.hide()
        }
    }

    private fun hideViews()
    {
        binding.apply {
            fragmentContainer.hide()
            networkLayout.show()
        }
    }

    private fun initBottomNavigationView() {
        val navHostFragment =supportFragmentManager.findFragmentById(R.id.fragmentContainer) as NavHostFragment
        val navController = navHostFragment.findNavController()
        navController.addOnDestinationChangedListener(this)
        binding.bottomNavigationView.setupWithNavController(navController)
    }

    override fun onDestinationChanged(controller: NavController, destination: NavDestination, arguments: Bundle?) {
        when (destination.id) {
            R.id.homeFragment, R.id.cartFragment, R.id.favouriteFragment,
            R.id.exploreFragment, R.id.accountFragment -> showBottomNav()
            else -> hideBottomNav()
        }
    }

    override fun onPause()
    {
        super.onPause()
        firstCheckInternetConnection=true
    }

    override fun onPaymentSuccess(p0:String?)
    {
        Toast.makeText(this, resources.getText(R.string.orderPlaced), Toast.LENGTH_SHORT).show()
    }

    override fun onPaymentError(p0:Int, p1:String?)
    {
        Toast.makeText(this, resources.getText(R.string.errorMessage), Toast.LENGTH_SHORT).show()
    }
}