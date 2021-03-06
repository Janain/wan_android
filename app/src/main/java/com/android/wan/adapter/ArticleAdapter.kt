package com.android.wan.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.android.wan.R
import com.android.wan.callback.OnArticleClickListener
import com.android.wan.net.response.entity.Datas
import com.android.wan.utils.HtmlUtil
import de.hdodenhof.circleimageview.CircleImageView

/**
 * 文章列表适配器
 * @author by Jsion on 2018/1/15.
 */
class ArticleAdapter(mContext: Context) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    var mContext: Context? = mContext
    var articleClickListener: OnArticleClickListener<Datas>? = null
    var articleList: List<Datas> = ArrayList<Datas>()

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): RecyclerView.ViewHolder {
        return ArticleViewHoler(LayoutInflater.from(mContext).inflate(R.layout.item_home_article, parent, false))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder?, position: Int) {
        val currentData: Datas = articleList.get(position)
        currentData.postion = position

        when (holder) {
            is ArticleViewHoler -> {
                holder.authName.text = currentData.author
                holder.ariticleTitle.text = HtmlUtil.htmlRemoveTag(currentData.title)
                holder.chapterName.text = currentData.chapterName
                holder.uploadTime.text = currentData.niceDate

                holder.favorite.setOnClickListener {
                    articleClickListener?.onArticleLikeClick(currentData)
                }

                holder.authName.setOnClickListener {
                    articleClickListener?.onArticleAuthClick(currentData)
                }

                holder.chapterName.setOnClickListener {
                    articleClickListener?.onArticleTypeClick(currentData)
                }

                if (currentData.collect) {
                    holder.favorite.setImageResource(R.mipmap.icon_like)
                } else {
                    holder.favorite.setImageResource(R.mipmap.icon_dislike)
                }
            }
        }

        holder?.itemView?.setOnClickListener {
            articleClickListener?.onRecyItemClick(position, currentData)
        }
    }

    override fun getItemCount(): Int {
        return articleList.size
    }


    fun setArticleData(refresh: Boolean, articleData: List<Datas>) {
        if (refresh) {
            if (articleData.size > 0) {
                (articleList as ArrayList<Datas>).clear()
                (articleList as ArrayList<Datas>).addAll(articleData)
            }
        } else {
            (articleList as ArrayList<Datas>).addAll(articleData)
        }
        notifyDataSetChanged()
    }

    fun clearArticleData() {
        (articleList as ArrayList<Datas>).clear()
    }

    class ArticleViewHoler(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var circleImageIcon: CircleImageView = itemView.findViewById(R.id.circleImageIcon)
        var authName: TextView = itemView.findViewById(R.id.authName)
        var ariticleTitle: TextView = itemView.findViewById(R.id.ariticleTitle)
        var chapterName: TextView = itemView.findViewById(R.id.chapterName)
        var uploadTime: TextView = itemView.findViewById(R.id.uploadTime)
        var favorite: ImageView = itemView.findViewById(R.id.favorite)
    }

    fun likeItemByPosition(position: Int) {
        val currentLikeData = articleList.get(position)
        currentLikeData.collect = true
        notifyDataSetChanged()
    }

    fun unLikeItemByPosition(position: Int) {
        val currentLikeData = articleList.get(position)
        currentLikeData.collect = false
        notifyDataSetChanged()
    }

    fun unLikeItemByPositionDelete(position: Int) {
        val currentLikeData = articleList.get(position)
        (articleList as ArrayList).remove(currentLikeData)
        notifyDataSetChanged()
    }

    fun makeAllItemLike(){
        for (item: Datas in articleList) {
            item.collect = true
        }
    }
}