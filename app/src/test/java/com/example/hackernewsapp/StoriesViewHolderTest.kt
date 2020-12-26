package com.example.hackernewsapp

import android.view.View
import android.widget.TextView
import com.example.hackernewsapp.data.model.Story
import com.example.hackernewsapp.ui.flows.StoriesAdapter
import com.example.hackernewsapp.ui.flows.StoryViewHolder
import kotlinx.android.synthetic.main.story_item_layout.view.*
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import org.mockito.Mockito.verify
import org.mockito.MockitoAnnotations

class StoriesViewHolderTest {

    @Mock
    lateinit var clickListener: StoriesAdapter.OnStoryClicked

    @Mock
    lateinit var view: View

    @Mock
    lateinit var story: Story

    private lateinit var viewHolder: StoryViewHolder

    private val title = "storyTitle"
    private val author = "someAuthor"
    private val createdAt = "213231"
    private val storyUrl = "url"

    @Mock
    lateinit var tvTitle: TextView

    @Mock
    lateinit var tvSubtitle: TextView

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        viewHolder = Mockito.spy(StoryViewHolder(view, clickListener))

        `when`(story.storyTitle).thenReturn(title)
        `when`(story.author).thenReturn(author)
        `when`(story.createdAt).thenReturn(createdAt)
        `when`(story.storyUrl).thenReturn(storyUrl)

        `when`(view.tvTitle).thenReturn(tvTitle)
        `when`(view.tvSubtitle).thenReturn(tvSubtitle)
    }

    @Test
    fun `when viewholder is binded, params are set`() {
        viewHolder.bind(story)

        verify(viewHolder).setTitle(title)
        verify(viewHolder).setSubtitle(story.author, story.createdAt)
        verify(viewHolder).setClickListener(clickListener, story.storyUrl)
    }

    @Test
    fun `title is set with the correct parameter`() {
        viewHolder.setTitle(title)

        verify(tvTitle).text = title
    }

    @Test
    fun `subtitle is set with the correct parameter`() {
        viewHolder.setSubtitle(author, createdAt)
        val expectedSubtitle = "$author - $createdAt"

        verify(tvSubtitle).text = expectedSubtitle
    }
}