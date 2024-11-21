package com.example.cheongyakhae.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.cheongyakhae.R

class LoginFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_login, container, false)

        val emailEditText = view.findViewById<EditText>(R.id.emailEditText)
        val passwordEditText = view.findViewById<EditText>(R.id.passwordEditText)
        val loginButton = view.findViewById<Button>(R.id.loginButton)

        // 로그인 버튼 클릭 리스너
        loginButton.setOnClickListener {
            val email = emailEditText.text.toString().trim()
            val password = passwordEditText.text.toString().trim()

            if (email.isNotEmpty() && password.isNotEmpty()) {
                // 로그인 로직 예시 (추가 필요)
                Toast.makeText(requireContext(), "로그인 시도: $email", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(requireContext(), "이메일과 비밀번호를 입력해주세요.", Toast.LENGTH_SHORT).show()
            }
        }

        return view
    }
}
