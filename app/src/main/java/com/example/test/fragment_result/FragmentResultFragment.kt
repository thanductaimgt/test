package com.example.test.fragment_result

import android.content.Intent
import android.os.Bundle
import android.os.Parcel
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.test.R
import kotlinx.android.synthetic.main.fragment_result_fragment.*

class FragmentResultFragment : Fragment() {
    private var callback: Callback? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        callback = arguments?.getParcelable(KEY_CALLBACK)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        context
        return inflater.inflate(R.layout.fragment_result_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        dismissButton.setOnClickListener {
            callback?.onDismiss(1, Intent())
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        callback = null
    }

    interface Callback : Parcelable {
        fun onDismiss(code: Int, intent: Intent?)

        override fun describeContents(): Int {
            return 0
        }

        override fun writeToParcel(dest: Parcel?, flags: Int) {

        }
    }

    companion object {
        const val KEY_CALLBACK = "callback"
    }
}
