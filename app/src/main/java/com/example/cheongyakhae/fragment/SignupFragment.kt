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
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class SignupFragment : Fragment() {

    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_signup, container, false)

        // FirebaseAuth 초기화
        auth = FirebaseAuth.getInstance()

        val emailEditText = view.findViewById<EditText>(R.id.emailEditText)
        val passwordEditText = view.findViewById<EditText>(R.id.passwordEditText)
        val confirmPasswordEditText = view.findViewById<EditText>(R.id.confirmPasswordEditText)
        val signupButton = view.findViewById<Button>(R.id.signupButton)

        // 회원가입 버튼 클릭 리스너
        signupButton.setOnClickListener {
            val email = emailEditText.text.toString().trim()
            val password = passwordEditText.text.toString().trim()
            val confirmPassword = confirmPasswordEditText.text.toString().trim()

            if (email.isNotEmpty() && password.isNotEmpty() && confirmPassword.isNotEmpty()) {
                if (password == confirmPassword) {
                    if (password.length >= 6) {
                        // Firebase 회원가입 처리
                        registerUser(email, password)
                    } else {
                        Toast.makeText(
                            requireContext(),
                            "비밀번호는 최소 6자 이상이어야 합니다.",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                } else {
                    Toast.makeText(requireContext(), "비밀번호가 일치하지 않습니다.", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(requireContext(), "모든 필드를 입력해주세요.", Toast.LENGTH_SHORT).show()
            }
        }

        return view
    }

    private fun registerUser(email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // Firestore에 사용자 정보 저장
                    val userId = auth.currentUser?.uid
                    val db = FirebaseFirestore.getInstance()
                    val user = hashMapOf(
                        "email" to email
                    )
                    userId?.let {
                        db.collection("users").document(it)
                            .set(user)
                            .addOnSuccessListener {
                                Toast.makeText(requireContext(), "회원가입 성공!", Toast.LENGTH_SHORT).show()
                                Log.d("SignupFragment", "Firestore에 사용자 저장 성공")
                            }
                            .addOnFailureListener { e ->
                                Toast.makeText(requireContext(), "Firestore 저장 실패: ${e.message}", Toast.LENGTH_SHORT).show()
                            }
                    }
                } else {
                    Toast.makeText(requireContext(), "회원가입 실패: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun saveUserToFirestore(userId: String?, email: String) {
        if (userId == null) return

        val db = FirebaseFirestore.getInstance()
        val user = hashMapOf(
            "email" to email,
            "createdAt" to System.currentTimeMillis()
        )

        db.collection("users").document(userId)
            .set(user)
            .addOnSuccessListener {
                Log.d("SignupFragment", "유저 정보 Firestore에 저장 성공")
            }
            .addOnFailureListener { e ->
                Log.e("SignupFragment", "유저 정보 Firestore에 저장 실패", e)
            }
    }

}
