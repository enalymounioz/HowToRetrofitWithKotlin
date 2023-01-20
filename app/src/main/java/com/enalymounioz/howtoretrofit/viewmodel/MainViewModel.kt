package com.enalymounioz.howtoretrofit.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.enalymounioz.howtoretrofit.model.*
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainViewModel(application: Application) : AndroidViewModel(application) {

    var job: Job? = null
    val exceptionHandler = CoroutineExceptionHandler { coroutineContext, throwable ->
        GlobalScope.launch(Dispatchers.Main) {
            onError("Exception: ${throwable.localizedMessage}")
        }
    }

    val compositeDisposable = CompositeDisposable()

    val apiResponse = MutableLiveData<List<Item>>()
    val loading = MutableLiveData<Boolean>()
    val error = MutableLiveData<String?>()

    fun fetchData() {
        loading.value = true
        val call = ApiCallService.call(getApplication())

        call.enqueue(object : Callback<ApiCallResponse> {

            override fun onResponse(
                call: Call<ApiCallResponse>,
                response: Response<ApiCallResponse>
            ) {
                val body = response.body()
                apiResponse.value = body?.flatten()
                error.value = null
                loading.value = false
            }

            override fun onFailure(call: Call<ApiCallResponse>, t: Throwable) {
                if (call.isCanceled) {
                    onError("The call was canceled")
                } else {
                    onError(t.localizedMessage)
                }

            }
        })
        //   call.cancel()
    }

    fun fetchDataRx() {
        loading.value = true

        compositeDisposable.add(
            ApiCallService.callRx().callGetRx()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<ApiCallResponse>() {
                    override fun onSuccess(response: ApiCallResponse) {
                        apiResponse.value = response.flatten()
                        error.value = null
                        loading.value = false
                    }

                    override fun onError(e: Throwable) {
                        onError(e.localizedMessage)
                    }
                })
        )

    }

    fun fetchDataSync() {
        loading.value = true
        job = CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
            val response = ApiCallService.call(getApplication()).execute()
            withContext(Dispatchers.Main) {
                if (response.isSuccessful) {
                    val body = response.body()
                    apiResponse.value = body?.flatten()
                    error.value = null
                    loading.value = false
                } else {
                    onError("Error: ${response.message()}")
                }
            }
        }


    }

    private fun onError(message: String) {
        error.value = message
        loading.value = false
    }

    override fun onCleared() {
        super.onCleared()
        job?.cancel()
    }


}