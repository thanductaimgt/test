package com.example.test.preload

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.MutableLiveData
import androidx.viewpager2.widget.ViewPager2
import com.example.test.R
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.database.ExoDatabaseProvider
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.upstream.FileDataSource
import com.google.android.exoplayer2.upstream.cache.*
import kotlinx.android.synthetic.main.activity_preload.*
import java.io.File
import java.util.*


class PreloadActivity : AppCompatActivity() {
    val cache by lazy {
        return@lazy cacheInstance ?: {
            val exoCacheDir = File("${cacheDir.absolutePath}/live/exo")
            val evictor = LeastRecentlyUsedCacheEvictor(CACHE_SIZE)
            SimpleCache(exoCacheDir, evictor, ExoDatabaseProvider(this))
        }.invoke().also {
            cacheInstance = it
        }
    }

    val upstreamDataSourceFactory by lazy { DefaultDataSourceFactory(this, "Android") }

    val cacheDataSourceFactory by lazy {
        CacheDataSourceFactory(
            cache,
            upstreamDataSourceFactory,
            FileDataSource.Factory(),
            CacheDataSinkFactory(cache, CacheDataSink.DEFAULT_FRAGMENT_SIZE),
            CacheDataSource.FLAG_IGNORE_CACHE_ON_ERROR,
            object : CacheDataSource.EventListener {
                override fun onCachedBytesRead(cacheSizeBytes: Long, cachedBytesRead: Long) {
                    Log.d(
                        ShowFragment.TAG,
                        "onCachedBytesRead. cacheSizeBytes:$cacheSizeBytes, cachedBytesRead: $cachedBytesRead"
                    )
                }

                override fun onCacheIgnored(reason: Int) {
                    Log.d(ShowFragment.TAG, "onCacheIgnored. reason:$reason")
                }
            }
        )
    }

    val trackSelector by lazy {
        DefaultTrackSelector(this).apply {
            parameters = buildUponParameters()
                .setAllowVideoNonSeamlessAdaptiveness(false)
                .build()
        }
    }

    val player by lazy {
        SimpleExoPlayer.Builder(this)
            .setTrackSelector(trackSelector)
            .build().apply {
                repeatMode = Player.REPEAT_MODE_OFF
                playWhenReady = true
            }
    }

    var pagerLastItem = MutableLiveData(0)

    var i = -1
    val showDetails = listOf(
        ShowDetail(
            id = i++,
            streamUrl = "https://edge.tikicdn.com/data/hls/902297/1/3/1478/manifest.m3u8"
        ),
        ShowDetail(
            id = i++,
            streamUrl = "https://edge.tikicdn.com/data/hls/901262/1/3/1478/manifest.m3u8"
        ),
        ShowDetail(
            id = i++,
            streamUrl = "https://edge.tikicdn.com/data/hls/901261/1/3/1478/manifest.m3u8"
        )
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_preload)

        viewPager.apply {
            adapter = PagerAdapter(this@PreloadActivity).apply {
                showDetails = this@PreloadActivity.showDetails
            }
            orientation = ViewPager2.ORIENTATION_VERTICAL
            offscreenPageLimit = 1
            registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                override fun onPageScrollStateChanged(state: Int) {
                    if (state == ViewPager2.SCROLL_STATE_IDLE) {
                        if (currentItem != pagerLastItem.value) {
                            pagerLastItem.value = currentItem
                        }
                        player.playWhenReady = true
                    } else {
                        player.playWhenReady = false
                    }
                }
            })
        }

        val a: LinkedList<LinkedList<Int>> = LinkedList()
        a
    }

    override fun onDestroy() {
        super.onDestroy()
        player.release()
//        cache.release()
    }

    companion object {
        private const val CACHE_SIZE = 100 * 1024 * 1024L
        private var cacheInstance: Cache? = null
    }
}
