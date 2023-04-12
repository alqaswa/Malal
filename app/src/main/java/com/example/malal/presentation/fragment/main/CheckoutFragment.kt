package com.example.malal.presentation.fragment.main

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.malal.R
import com.example.malal.databinding.FragmentCheckoutBinding
import com.example.malal.model.CheckoutModel
import com.example.malal.model.OrderModel
import com.example.malal.model.UserInfoModel
import com.example.malal.util.DISPLAY_DIALOG
import com.example.malal.util.LOADING_ANNOTATION
import com.example.malal.util.ROZERPAY_KEY_ID
import com.example.malal.util.Resource
import com.example.malal.util.extention.closeFragment
import com.example.malal.util.extention.showToast
import com.example.malal.viewmodel.AuthenticationViewModel
import com.example.malal.viewmodel.CheckoutViewModel
import com.example.malal.viewmodel.UserInfoViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.razorpay.Checkout
import com.razorpay.PaymentResultListener
import com.stripe.android.payments.paymentlauncher.PaymentResult
import com.stripe.android.view.CardMultilineWidget
import dagger.hilt.android.AndroidEntryPoint
import org.json.JSONException
import org.json.JSONObject
import javax.inject.Inject
import javax.inject.Named

@AndroidEntryPoint
class CheckoutFragment:BottomSheetDialogFragment()
{
    private val userInfoViewModel by activityViewModels<UserInfoViewModel>()
    private val checkoutViewModel by activityViewModels<CheckoutViewModel>()

    private lateinit var binding:FragmentCheckoutBinding

    private val args by navArgs<CheckoutFragmentArgs>()
    private val totalCost by lazy { args.totalCost }
    private val cartProductsList by lazy { args.productList }

    private var userAddress = ""

    private lateinit var userInfoModel:UserInfoModel


    @Inject
    @Named(DISPLAY_DIALOG)
    lateinit var displayAlert:AlertDialog

    @Inject
    @Named(LOADING_ANNOTATION)
    lateinit var loadingDialog:Dialog
    private lateinit var checkout:Checkout

    override fun onCreate(savedInstanceState:Bundle?)
    {
        super.onCreate(savedInstanceState)
        checkout=Checkout()
        checkout.setKeyID(ROZERPAY_KEY_ID)
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View
    {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_checkout, container, false)
        return binding.run {
            fragment = this@CheckoutFragment
            binding.root
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeListener()
    }

    private fun observeListener() {
        userInfoViewModel.userInformationLiveData.observe(viewLifecycleOwner) {userInfo ->
            when(userInfo)
            {
                is Resource.Success ->
                {

                    userInfoModel=userInfo.data!!
                    userAddress=userInfoModel.userAddress
                    val checkoutModel=CheckoutModel(userAddress, totalCost)
                    binding.checkoutModel=checkoutModel
                }
                is Resource.Error ->
                {
                    showToast(userInfo.message.toString())
                }
                else ->
                {

                }
            }
        }

    }

    private fun pushUserOrders()
    {
        checkoutViewModel.pushUserOrder(cartProductsList, userAddress, totalCost)
    }


    fun placeOrder()
    {
        pushUserOrders()
        userInfoModel.userAddress=binding.userLocationForOrder.text.toString().trim()
        val obj = JSONObject()
        try
        {
            obj.put("name", userInfoModel.userName)
            obj.put("description", "Test payment")
            obj.put("theme.color", "")
            obj.put("currency", "INR")
            obj.put("amount", totalCost*100)
            obj.put("prefill.contact", "8800245060")
            obj.put("prefill.email", userInfoModel.userEmail)

            checkout.open(requireActivity(), obj)
        }
        catch(e:JSONException)
        {
            e.printStackTrace()
        }
        closeDialog()

    }


    fun closeDialog()
    {
        closeFragment()
    }
}
