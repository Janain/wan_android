package com.android.wan.fragment

import android.content.Context
import android.content.Intent
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.View
import android.widget.Toast
import com.android.wan.R
import com.android.wan.activity.BrowserActivity
import com.android.wan.adapter.ArticleAdapter
import com.android.wan.adapter.BannerAdapter
import com.android.wan.base.AbstractFragment
import com.android.wan.callback.OnArticleClickListener
import com.android.wan.callback.OnRecyItemClickListener
import com.android.wan.constant.Constant
import com.android.wan.customwidget.BannerRecyclerView
import com.android.wan.customwidget.CarLayout
import com.android.wan.net.response.BannerResponse
import com.android.wan.net.response.HomeListResponse
import com.android.wan.net.response.entity.Datas
import com.android.wan.presenter.HomeFragmentPresenter
import com.android.wan.view.HomeFragmentView
import com.jcodecraeer.xrecyclerview.ProgressStyle
import com.jcodecraeer.xrecyclerview.XRecyclerView


/**
 * @author by 有人@我 on 2018/1/12.
 */
class HomeFragment : AbstractFragment(), HomeFragmentView {
    var bannerRecycler: BannerRecyclerView? = null
    var articleRecycler: XRecyclerView? = null
    var bannerAdapter: BannerAdapter? = null
    var articleAdapter: ArticleAdapter? = null
    var pageIndex: Int = 0
    var refresh: Boolean = false

    override fun showLoading() {

    }

    override fun bindBannerData(bannerResponse: BannerResponse) {

        bannerAdapter!!.itemClickListener = object : OnRecyItemClickListener<BannerResponse.Data> {
            override fun onRecyItemClick(position: Int, t: BannerResponse.Data) {
                val intent = Intent(activityContext, BrowserActivity::class.java)
                intent.putExtra(Constant.BUNDLE_KEY_4_WEB_TITLE, t.title)
                intent.putExtra(Constant.BUNDLE_KEY_4_WEB_URL, t.url)
                startActivity(intent)
            }
        }
        bannerAdapter?.mContext = activityContext
        bannerAdapter?.bannerData = bannerResponse.data
        bannerAdapter?.notifyDataSetChanged()
        bannerRecycler?.bannerStart()

    }

    override fun bindHomeArticle(articleResponse: HomeListResponse) {

        when (articleResponse.errorCode) {
            0 -> {
                articleAdapter?.setArticleData(refresh, articleResponse.data.datas!!)
                articleAdapter?.articleClickListener = object : OnArticleClickListener<Datas> {
                    override fun onRecyItemClick(position: Int, t: Datas) {
                        val intent = Intent(activityContext, BrowserActivity::class.java)
                        intent.putExtra(Constant.BUNDLE_KEY_4_WEB_TITLE, t.title)
                        intent.putExtra(Constant.BUNDLE_KEY_4_WEB_URL, t.link)
                        startActivity(intent)
                    }

                    override fun onArticleTypeClick() {
                        Toast.makeText(activityContext, "类别", Toast.LENGTH_SHORT).show()
                    }

                    override fun onArticleAuthClick() {
                        Toast.makeText(activityContext, "作者", Toast.LENGTH_SHORT).show()
                    }

                    override fun onArticleLikeClick() {
                        Toast.makeText(activityContext, "收藏", Toast.LENGTH_SHORT).show()
                    }

                }
                articleAdapter?.notifyDataSetChanged()
                if (refresh) {
                    articleRecycler?.refreshComplete()
                } else {
                    articleRecycler?.loadMoreComplete()
                }
            }
        }
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
    }

    override fun hideLoading() {

    }

    override fun showToast(msg: String) {
        Toast.makeText(activityContext, msg, Toast.LENGTH_SHORT).show()
    }

    override fun showErr() {
        Toast.makeText(activityContext, "请求错误", Toast.LENGTH_SHORT).show()
    }

    override fun getContext(): Context {
        return activityContext!!
    }

    var homeFragmentPresenter: HomeFragmentPresenter? = null

    override fun setFragmentLayout(): Int {
        return R.layout.fragment_home
    }

    override fun initData() {
        homeFragmentPresenter = HomeFragmentPresenter()
        homeFragmentPresenter?.attachView(this)
        bannerAdapter = BannerAdapter(activityContext!!)
        articleAdapter = ArticleAdapter(activityContext!!)

    }

    override fun initView(rootView: View) {
        bannerRecycler = rootView.findViewById(R.id.bannerRecycler)
        bannerRecycler?.layoutManager = LinearLayoutManager(activityContext, LinearLayoutManager.HORIZONTAL, false)
        bannerRecycler?.adapter = bannerAdapter

        articleRecycler = rootView.findViewById(R.id.articleRecycler)
    }

    override fun initEvent() {
        homeFragmentPresenter?.getBannerData()
        homeFragmentPresenter?.getHomeListArticle(pageIndex)
        articleRecycler?.layoutManager = LinearLayoutManager(activityContext)

        articleRecycler?.setPullRefreshEnabled(true)
        articleRecycler?.setLoadingMoreEnabled(true)
        articleRecycler?.setRefreshProgressStyle(ProgressStyle.BallSpinFadeLoader)
        articleRecycler?.setLoadingMoreProgressStyle(ProgressStyle.LineScalePulseOutRapid)
        articleRecycler?.adapter = articleAdapter
        articleRecycler?.setLoadingListener(object : XRecyclerView.LoadingListener {
            override fun onLoadMore() {
                refresh = false
                homeFragmentPresenter?.getHomeListArticle(pageIndex)
            }

            override fun onRefresh() {
                refresh = true
                pageIndex = 0
                homeFragmentPresenter?.getHomeListArticle(pageIndex)
            }
        })

    }

}