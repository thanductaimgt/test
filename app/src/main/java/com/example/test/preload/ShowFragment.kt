package com.example.test.preload

import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import com.example.test.R
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.offline.DownloaderConstructorHelper
import com.google.android.exoplayer2.offline.StreamKey
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.source.hls.HlsMediaSource
import com.google.android.exoplayer2.source.hls.offline.HlsDownloader
import kotlinx.android.synthetic.main.live_show_fragment.*
import kotlinx.coroutines.*

class ShowFragment : Fragment() {
    private val preloadActivity: PreloadActivity
        get() = activity as PreloadActivity

    private val player: SimpleExoPlayer
        get() = preloadActivity.player

    private val uri by lazy { Uri.parse(preloadActivity.showDetails[position].streamUrl) }

    private val cacheStreamKeys = arrayListOf(
        StreamKey(0, 1),
        StreamKey(1, 1),
        StreamKey(2, 1),
        StreamKey(3, 1),
        StreamKey(4, 1)
    )

    private val mediaSource: MediaSource by lazy {
        val dataSourceFactory = preloadActivity.cacheDataSourceFactory
        HlsMediaSource.Factory(dataSourceFactory)
            .setStreamKeys(cacheStreamKeys)
            .setAllowChunklessPreparation(true)
            .createMediaSource(uri)
    }

    private var position = -2

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        position = arguments?.getInt(KEY_POSITION) ?: -2
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.live_show_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        preloadActivity.pagerLastItem.observe(viewLifecycleOwner, Observer {
            if (it == position) {
                cancelPreCache()
                Log.d(TAG, "Cancel preload at position: $position")
                player.stop(true)
                val renderViewX = renderView
                if (renderViewX is SurfaceView) {
                    player.setVideoSurfaceView(renderViewX)
                } else if (renderViewX is TextureView) {
                    player.setVideoTextureView(renderViewX)
                }
                player.prepare(mediaSource)
            }
        })

        viewLifecycleOwner.lifecycleScope.launch(Dispatchers.IO) {
            preCacheVideo()
        }

        dummyButton.addOnLayoutChangeListener { v, left, top, right, bottom, oldLeft, oldTop, oldRight, oldBottom ->
            Log.d(
                TAG,
                "dummyButton.onLayoutChange: $v, $left, $top, $right, $bottom, $oldLeft, $oldTop, $oldRight, $oldBottom"
            )
        }
        dummyButton.setOnClickListener {
            Log.d(TAG, "dummyButton.onClick")
        }

        showContainer.addOnLayoutChangeListener { v, left, top, right, bottom, oldLeft, oldTop, oldRight, oldBottom ->
            Log.d(
                TAG,
                "showContainer.onLayoutChange: $v, $left, $top, $right, $bottom, $oldLeft, $oldTop, $oldRight, $oldBottom"
            )
        }
    }

    private val downloadConstructorHelper by lazy {
        DownloaderConstructorHelper(
            preloadActivity.cache,
            preloadActivity.upstreamDataSourceFactory,
            preloadActivity.cacheDataSourceFactory,
            null,
            null
        )
    }

    private val downloader by lazy {
        HlsDownloader(uri, cacheStreamKeys, downloadConstructorHelper)
    }

    private fun cancelPreCache() {
//        cancelFlag.compareAndSet(false, true)
        downloader.cancel()
    }

    private suspend fun preCacheVideo() = withContext(Dispatchers.IO) {
        runCatching {
            // do nothing if already cache enough
            if (preloadActivity.cache.isCached(uri.toString(), 0, PRE_CACHE_SIZE)) {
                Log.d(TAG, "video has been cached, return")
                return@runCatching
            }

            downloader.download { contentLength, bytesDownloaded, percentDownloaded ->
                if (bytesDownloaded >= PRE_CACHE_SIZE) downloader.cancel()
                Log.d(
                    TAG,
                    "contentLength: $contentLength, bytesDownloaded: $bytesDownloaded, percentDownloaded: $percentDownloaded"
                )
            }
        }.onFailure {
            if (it is InterruptedException) return@onFailure

            Log.d(TAG, "Cache fail for position: $position with exception: $it}")
            it.printStackTrace()
        }.onSuccess {
            Log.d(TAG, "Cache success for position: $position")
        }
        Unit
    }

    companion object {
        const val KEY_POSITION = "KEY_POSITION"
        private const val PRE_CACHE_SIZE = 5 * 1024 * 1024L
        const val TAG = "ShowFragment"
    }
}
