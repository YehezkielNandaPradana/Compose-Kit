package io.github.ComposeKit.ViewModel.ViewModel

import ComposeKit.Helper.Api.ApiClient
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.github.ComposeKit.Helper.Notify.AppNotifier
import io.github.ComposeKit.Helper.Notify.AppNotifier.error
import io.github.ComposeKit.Helper.PopUp.DialogHelper
import io.github.ComposeKit.Model.AddItemRequest
import io.github.ComposeKit.Model.Borrowings
import io.github.ComposeKit.Model.ReturnRequest
import io.github.ComposeKit.Model.ReturnResponse
import io.github.ComposeKit.ViewModel.ResourceState.ResourceState
import io.github.ComposeKit.ViewModel.ResourceState.execute
import io.github.ComposeKit.ViewModel.Search.FilterHelper
import io.github.ComposeKit.ViewModel.Search.SearchHelper
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class RiwayatViewModel : ViewModel() {

    private var allBorrowings: List<Borrowings> = emptyList()
    val borrowings = ResourceState<List<Borrowings>>()

    private val _selectedStatus = MutableStateFlow<String?>(null)
    val selectedStatus: StateFlow<String?> = _selectedStatus

    val returnItem = ResourceState<ReturnResponse>()

    init {
        loadData()
    }

    private fun applyFilter() {
        val filtered = FilterHelper.filter(
            items = allBorrowings,
            selected = selectedStatus.value,
            filterBy = { it.status }
        )
        borrowings.success(filtered)
    }

    fun onSelectedStatus(status: String?) {
        _selectedStatus.value = status
        applyFilter()
    }

    fun loadData() {
        viewModelScope.launch {
            borrowings.execute(
                request = {
                    ApiClient.service.getBorrowings()
                },
                onSuccess = {
                    allBorrowings = it
                    applyFilter()
                }
            )
        }
    }

    fun KembalikanBarang(itemId: Int) {
        viewModelScope.launch {
            returnItem.execute(
                request = {
                    ApiClient.service.returnItem(itemId)
                },
                onSuccess = {
                    AppNotifier.success("Berhasil Mengembalikan Item")
                    loadData()
                    DialogHelper.close()
                },
                onError = AppNotifier::error
            )
        }
    }
}