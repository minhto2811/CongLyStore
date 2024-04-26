package com.mgok.conglystore.presentation.bill.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mgok.conglystore.data.remote.error_payment.ErrorPayment
import com.mgok.conglystore.data.remote.refund.Refund
import com.mgok.conglystore.usecases.bill.GetBillByIdUseCase
import com.mgok.conglystore.usecases.bill.UpdatePaymentStatusUseCase
import com.mgok.conglystore.usecases.bill.UpdateStatusBillUseCase
import com.mgok.conglystore.usecases.coffee.UpdateSoldProductUseCase
import com.mgok.conglystore.usecases.error_payment.CreateErrorPaymentUseCase
import com.mgok.conglystore.usecases.error_payment.UpdatePaymentErrorStatusUseCase
import com.mgok.conglystore.usecases.refund.CreateRefundUseCase
import com.mgok.conglystore.usecases.refund.UpdateRefundStatusUseCase
import com.mgok.conglystore.usecases.user.GetInfoUserUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BillViewModel @Inject constructor(
    private val getBillByIdUseCase: GetBillByIdUseCase,
    private val updatePaymentStatusUseCase: UpdatePaymentStatusUseCase,
    private val createErrorPaymentUseCase: CreateErrorPaymentUseCase,
    private val getInfoUserUseCase: GetInfoUserUseCase,
    private val updatePaymentErrorStatusUseCase: UpdatePaymentErrorStatusUseCase,
    private val updateStatusBillUseCase: UpdateStatusBillUseCase,
    private val createRefundUseCase: CreateRefundUseCase,
    private val updateRefundStatusUseCase: UpdateRefundStatusUseCase,
    private val updateSoldProductUseCase: UpdateSoldProductUseCase
) : ViewModel() {
    private val _stateUI = MutableStateFlow(BillStateUI())
    val stateUI get() = _stateUI.asStateFlow()


    fun updateStatusBill(billId: String, status: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                _stateUI.update { it.copy(loading = true) }
                updateStatusBillUseCase.updateStatus(billId, status)
                getBillById(billId)
                if (status == 1) {
                    _stateUI.value.bill?.let { bill ->
                        bill.list.forEach { itemBill ->
                            updateSoldProductUseCase.invoke(itemBill.coffeeId, itemBill.quantity)
                        }
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
                _stateUI.update { it.copy(loading = false, message = e.message) }
            }
        }
    }

    fun updateRefundStatus(refundId: String, status: Int, callBack: () -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                _stateUI.update { it.copy(loading = true) }
                updateRefundStatusUseCase.updateStatus(refundId, status)
                _stateUI.update { it.copy(loading = false) }
                launch(Dispatchers.Main) { callBack() }
            } catch (e: Exception) {
                e.printStackTrace()
                _stateUI.update { it.copy(loading = false, message = e.message) }
            }
        }
    }


    fun cancelBill(userId: String, billId: String, status: Int = 2, percent: Float) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                _stateUI.update { it.copy(loading = true) }
                updateStatusBillUseCase.updateStatus(billId, status)
                createRefundUseCase.createRefund(
                    Refund(
                        userId = userId,
                        billId = billId,
                        percent = percent
                    )
                )
                val newBill = _stateUI.value.bill?.copy(status = status)
                _stateUI.update {
                    it.copy(
                        loading = false,
                        message = "Hủy đơn hàng thành công",
                        bill = newBill
                    )
                }
            } catch (e: Exception) {
                e.printStackTrace()
                _stateUI.update { it.copy(loading = false, message = e.message) }
            }
        }
    }

    fun checkRoleUser() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val role = getInfoUserUseCase()?.role ?: 1
                _stateUI.update { it.copy(role = role) }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }


    fun confirmPayment(epId: String, billId: String, statusEp: Int, statusPm: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                _stateUI.update { it.copy(loading = true) }
                updatePaymentErrorStatusUseCase.updateStatus(epId, statusEp)
                updatePaymentStatusUseCase.updateStatus(billId, statusPm)
                val newBill = _stateUI.value.bill?.copy(paymentStatus = statusPm)
                _stateUI.update {
                    it.copy(
                        loading = false,
                        message = "Cập nhật thành công",
                        bill = newBill
                    )
                }
            } catch (e: Exception) {
                e.printStackTrace()
                _stateUI.update { it.copy(loading = false, message = "Không tìm thấy yêu cầu từ người dùng") }
            }
        }
    }

    fun createErrorPayment(billId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                _stateUI.update { it.copy(loading = true) }
                val errorPayment = ErrorPayment(billId = billId)
                createErrorPaymentUseCase.create(errorPayment)
                _stateUI.update { it.copy(loading = false, message = "Đã gửi yêu cầu") }
            } catch (e: Exception) {
                e.printStackTrace()
                _stateUI.update { it.copy(loading = false, message = e.message) }
            }
        }
    }

    fun getBillById(billId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                _stateUI.update { it.copy(loading = true) }
                val bill = getBillByIdUseCase.getBillById(billId)
                _stateUI.update { it.copy(loading = false, bill = bill) }
            } catch (e: Exception) {
                e.printStackTrace()
                _stateUI.update { it.copy(loading = false, message = e.message) }
            }
        }
    }

    fun updatePaymentStatus(billId: String, status: Int = 0) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                _stateUI.update { it.copy(loading = true) }
                updatePaymentStatusUseCase.updateStatus(billId, status)
                val bill = getBillByIdUseCase.getBillById(billId)
                _stateUI.update { it.copy(loading = false, bill = bill) }
            } catch (e: Exception) {
                e.printStackTrace()
                _stateUI.update { it.copy(loading = false, message = e.message) }
            }
        }
    }
}