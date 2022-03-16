package com.example.quicksharereceiver.contentReceiver

import android.content.ClipData
import android.net.Uri
import android.view.View
import androidx.core.view.ContentInfoCompat
import androidx.core.view.OnReceiveContentListener

class CustomContentReceiver(
    private val contentReceived: (uri: Uri) -> Unit
) : OnReceiveContentListener {

    override fun onReceiveContent(
        view: View,
        contentInfo: ContentInfoCompat
    ): ContentInfoCompat? {
        val split = contentInfo.partition { item: ClipData.Item -> item.uri != null }
        split.first?.let { uriContent ->
            contentReceived(uriContent.clip.getItemAt(0).uri)
        }
        return split.second
    }
}
