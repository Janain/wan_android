package com.android.wan.moudle

import RetrofitHelper
import android.util.Log
import com.android.wan.callback.MvpCallback
import com.android.wan.net.response.KnowledgeHierarchyResponse
import rx.Subscriber
import rx.Subscription
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers

/**
 * @author by 有人@我 on 18/1/16.
 */
class KnowledgeTreeMoudle {

    fun getKnowledgeTreeData(mvpCallback: MvpCallback<KnowledgeHierarchyResponse>) {
        var subscription: Subscription? = null
        subscription?.unsubscribe()
        subscription = RetrofitHelper
                .retrofitService
                .getKnowledgeHierarchyList()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe {
                    mvpCallback.showLoading()
                }
                .subscribe(object : Subscriber<KnowledgeHierarchyResponse>() {
                    override fun onError(e: Throwable?) {
                        mvpCallback.onError()
                    }

                    override fun onCompleted() {
                        subscription?.unsubscribe()
                        mvpCallback.dissMissLoading()
                    }

                    override fun onNext(t: KnowledgeHierarchyResponse?) {
                        mvpCallback.onSuccess(t!!)
                    }
                })
    }

}