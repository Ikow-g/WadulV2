package com.example.wadulv2

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.navigation.fragment.findNavController

class LoginFrag : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val btnLogin = view.findViewById<Button>(R.id.login_button)
        val btReg = view.findViewById<TextView>(R.id.register)

        btnLogin.setOnClickListener {
            findNavController().navigate(R.id.action_loginFrag_to_dashboardFrag)
        }

        btReg.setOnClickListener{
            findNavController().navigate(R.id.action_loginFrag_to_registerFrag)
        }
    }
}