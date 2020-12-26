package com.example.hackernewsapp.ui.flows

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.VisibleForTesting
import androidx.recyclerview.widget.RecyclerView
import com.example.hackernewsapp.R
import com.example.hackernewsapp.data.model.Story
import kotlinx.android.synthetic.main.story_item_layout.view.*

class StoryViewHolder(
    itemView: View,
    private val clickListener: StoriesAdapter.OnStoryClicked,
) : RecyclerView.ViewHolder(itemView) {

    fun bind(item: Story) {
        setTitle(item.storyTitle)
        setSubtitle(item.author, item.createdAt)
        setClickListener(clickListener, item.storyUrl)
    }

    @VisibleForTesting
    fun setTitle(title: String) {
        itemView.tvTitle.text = title
    }

    @VisibleForTesting
    fun setSubtitle(author: String, createdAt: String) {
        val subtitle = "$author - $createdAt"
        itemView.tvSubtitle.text = subtitle
    }

    @VisibleForTesting
    fun setClickListener(clickListener: StoriesAdapter.OnStoryClicked, storyUrl: String) {
        itemView.setOnClickListener {
            clickListener.storyClicked(storyUrl)
        }
    }

    companion object {
        fun create(
            parent: ViewGroup,
            clickListener: StoriesAdapter.OnStoryClicked
        ): StoryViewHolder {
            val view =
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.story_item_layout, parent, false)
            return StoryViewHolder(view, clickListener)
        }
    }
}