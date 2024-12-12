package com.example.cheongyakhae.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.cheongyakhae.R
import com.example.cheongyakhae.activity.MainActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class LoginFragment : Fragment() {

    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_login, container, false)

        // FirebaseAuth 초기화
        auth = FirebaseAuth.getInstance()

        val emailEditText = view.findViewById<EditText>(R.id.emailEditText)
        val passwordEditText = view.findViewById<EditText>(R.id.passwordEditText)
        val loginButton = view.findViewById<Button>(R.id.loginButton)

        loginButton.setOnClickListener {
            val email = emailEditText.text.toString().trim()
            val password = passwordEditText.text.toString().trim()

            if (email.isNotEmpty() && password.isNotEmpty()) {
                loginUser(email, password)
            } else {
                Toast.makeText(requireContext(), "이메일과 비밀번호를 입력해주세요.", Toast.LENGTH_SHORT).show()
            }
        }

        return view
    }

    private fun loginUser(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // 로그인 성공
                    Toast.makeText(requireContext(), "로그인 성공!", Toast.LENGTH_SHORT).show()
                    (requireActivity() as MainActivity).updateNavigationHeader() // UI 갱신
                } else {
                    // 로그인 실패
                    Toast.makeText(requireContext(), "로그인 실패: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                }
            }
    }


    // 필요 없을 것 같은데 ~?
//    private fun navigateToMainScreen() {
//        val mainFragment = MainFragment() // 메인 화면으로 이동 (예시)
//        requireActivity().supportFragmentManager.beginTransaction()
//            .replace(R.id.fragmentContainer, mainFragment)
//            .commit()
//    }
}


