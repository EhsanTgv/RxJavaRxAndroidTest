package com.taghavi.rxjavarxandroidtest

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.annotation.RequiresApi

import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.annotations.NonNull
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Observer
import io.reactivex.rxjava3.disposables.CompositeDisposable

import io.reactivex.rxjava3.schedulers.Schedulers
import java.util.function.Predicate

class MainActivity : AppCompatActivity() {
    private val TAG = "MainActivity"

    private val disposable = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val taskObservable: Observable<Task> = Observable
            .fromIterable(DataSource.createTasksList())
            .subscribeOn(Schedulers.io())
            .filter(@RequiresApi(Build.VERSION_CODES.N)
            object : Predicate<Task>, @NonNull io.reactivex.rxjava3.functions.Predicate<Task> {
                override fun test(task: Task): Boolean {
                    return task.isComplete
                }
            })
            .observeOn(AndroidSchedulers.mainThread())

        taskObservable.subscribe(object : Observer<Task> {
            override fun onSubscribe(d: Disposable?) {
                Log.d(TAG, "onSubscribe: Called")
                disposable.add(d)
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

    override fun onDestroy() {
        super.onDestroy()
        disposable.clear()
    }
}