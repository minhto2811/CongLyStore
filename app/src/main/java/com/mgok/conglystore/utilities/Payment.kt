package com.mgok.conglystore.utilities

import android.app.Activity
import android.content.Context
import org.json.JSONException
import org.json.JSONObject
import vn.momo.momo_partner.AppMoMoLib

fun openMomoPay(context: Context, billId: String, price: Long, isUpdatePaymentStatus: Boolean = false) {
    AppMoMoLib.getInstance().setAction(AppMoMoLib.ACTION.PAYMENT)
    AppMoMoLib.getInstance().setActionType(AppMoMoLib.ACTION_TYPE.GET_TOKEN)
    val eventValue: MutableMap<String, Any> = HashMap()
    val merchantName = "merchantName"
    val merchantCode = "123456"
    eventValue["merchantname"] = merchantName
    eventValue["merchantcode"] = merchantCode
    eventValue["amount"] = price//Kiểu integer
    eventValue["orderId"] = billId
    eventValue["orderLabel"] = "Mã đơn hàng"
    eventValue["merchantnamelabel"] = "Dịch vụ"
    eventValue["fee"] = 0
    eventValue["description"] = "Thanh toán hóa đơn"
    eventValue["requestId"] = merchantCode + "merchant_billId_" + System.currentTimeMillis()
    eventValue["partnerCode"] = merchantCode
    val objExtraData = JSONObject()
    try {
        objExtraData.put("isUpdatePaymentStatus", isUpdatePaymentStatus)
    } catch (e: JSONException) {
        e.printStackTrace()
    }
    eventValue["extraData"] = objExtraData.toString()
    eventValue["extra"] = ""
    AppMoMoLib.getInstance().requestMoMoCallBack(context as Activity, eventValue)
}