package com.example.hackernewsapp.ui.flows

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.hackernewsapp.R
import com.example.hackernewsapp.core.WEBVIEW_URL_EXTRA
import com.example.hackernewsapp.core.utils.SwipeToDeleteCallback
import com.example.hackernewsapp.core.webview.WebviewActivity
import com.example.hackernewsapp.data.model.Story
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview

@FlowPreview
@ExperimentalCoroutinesApi
@AndroidEntryPoint
class StoriesListActivity : AppCompatActivity(), StoriesAdapter.OnStoryClicked,
    StoriesAdapter.OnStorySwiped {

    private val viewModel: StoriesListViewModel by viewModels()

    private lateinit var storiesAdapter: StoriesAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setupUI()
        setupObservers()
    }

    private fun setupUI() {
        storiesAdapter = StoriesAdapter(this, this, this)
        val decoration = DividerItemDecoration(this, DividerItemDecoration.VERTICAL)
        val linearLayoutManager = LinearLayoutManager(this)

        rvStoriesList.apply {
            adapter = storiesAdapter
            layoutManager = linearLayoutManager
            setHasFixedSize(true)
            addItemDecoration(decoration)
        }
        val itemTouchHelper = ItemTouchHelper(SwipeToDeleteCallback(storiesAdapter))
        itemTouchHelper.attachToRecyclerView(rvStoriesList)

        swipeRefreshStories.setOnRefreshListener {
            viewModel.swipedToRefresh()
        }
    }

    private fun setupObservers() {
        viewModel.stories.observe(this, {
            storiesAdapter.addStories(it)
            swipeRefreshStories.isRefreshing = false
        })

        viewModel.clickedStoryEvent.observe(this, {
            intentToWebView(it)
        })

        viewModel.progressBarStatus.observe(this, {
            progressBarStoriesList.isVisible = it
        })
    }

    private fun intentToWebView(url: String) {
        val intent = Intent(this, WebviewActivity::class.java)
        intent.putExtra(WEBVIEW_URL_EXTRA, url)
        startActivity(intent)
    }

    override fun storyClicked(url: String) {
        viewModel.clickedStory(url)
    }

    override fun onStorySwiped(story: Story) {
        viewModel.swipedStory(story)
    }
}