package com.taghavi.rxjavarxandroidtest

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log

import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Observer

import io.reactivex.rxjava3.schedulers.Schedulers

class MainActivity : AppCompatActivity() {
    private val TAG = "MainActivity"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val taskObservable: Observable<Task> = Observable
            .fromIterable(DataSource.createTasksList())
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())

        taskObservable.subscribe(object : Observer<Task> {
            override fun onSubscribe(d: Disposable?) {
                Log.d(TAG, "onSubscribe: Called")
            }

            override fun onNext(task: Task) {
                Log.d(TAG, "onNext: " + task.description)
            }

            override fun onError(e: Throwable?) {
                Log.d(TAG, "onError: $e")
            }
            override fun onComplete() {
                Log.d(TAG, "onComplete: Called")
            }
        })
    }
}