package com.example.iotinfo.ui.notifications

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.iotinfo.R

class NotificationsFragment : Fragment() {

    private lateinit var notificationsViewModel: NotificationsViewModel

    private lateinit var saveButton: Button
    private lateinit var nameInput: EditText
    private lateinit var urlInput: EditText

    private lateinit var root: View

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        notificationsViewModel =
            ViewModelProvider(this).get(NotificationsViewModel::class.java)
        root = inflater.inflate(R.layout.fragment_settings, container, false)
        val urlText: EditText = root.findViewById(R.id.urlText)
        val nameText: EditText = root.findViewById(R.id.nameText)
        notificationsViewModel.text.observe(viewLifecycleOwner, Observer {
//            textView.text = it
        })
        val sharedPref = this.activity?.getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE)
        if (sharedPref != null) {
            nameText.setText(sharedPref.getString(getString(R.string.saved_user_name),"")!!)
            urlText.setText(sharedPref.getString(getString(R.string.saved_url),"")!!)
        }

        return root
    }
}