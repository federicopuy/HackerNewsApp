package com.example.hackernewsapp.ui.flows

import android.content.Context
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.hackernewsapp.data.model.Story

class StoriesAdapter(
    val context: Context,
    private val clickListener: OnStoryClicked,
    private val swipedListener: OnStorySwiped
) : RecyclerView.Adapter<StoryViewHolder>() {

    private var stories = mutableListOf<Story>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StoryViewHolder {
        return StoryViewHolder.create(parent, clickListener)
    }

    override fun onBindViewHolder(holder: StoryViewHolder, position: Int) {
        val item = stories[position]
        holder.bind(item)
    }

    override fun getItemCount(): Int = stories.size


    /**
     * Replaces displayed set of stories with new items and notifies the adapter.
     * @param items the new items to be displayed
     */
    fun addStories(items: List<Story>) {
        stories.apply {
            clear()
            addAll(items)
            notifyDataSetChanged()
        }
    }

    fun deleteItem(position: Int) {
        swipedListener.onStorySwiped(stories[position])
    }

    interface OnStoryClicked {
        fun storyClicked(url: String)
    }

    interface OnStorySwiped {
        fun onStorySwiped(story: Story)
    }

}
