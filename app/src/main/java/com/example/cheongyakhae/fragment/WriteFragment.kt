package com.example.cheongyakhae.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.cheongyakhae.R
import com.example.cheongyakhae.databinding.FragmentWriteBinding
import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat
import java.util.*

class WriteFragment : Fragment() {

    private var _binding: FragmentWriteBinding? = null
    private val binding get() = _binding!!

    private lateinit var firestore: FirebaseFirestore

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentWriteBinding.inflate(inflater, container, false)
        firestore = FirebaseFirestore.getInstance() // Firestore 인스턴스 초기화
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 저장 버튼 클릭
        binding.saveButton.setOnClickListener {
            val title = binding.titleEditText.text.toString().trim()
            val content = binding.contentEditText.text.toString().trim()

            if (title.isEmpty() || content.isEmpty()) {
                Toast.makeText(requireContext(), "제목과 내용을 입력하세요.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Firestore에 저장할 데이터 생성
            val post = hashMapOf(
                "title" to title,
                "content" to content,
                "timestamp" to SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(Date())
            )

            // Firestore에 데이터 추가
            firestore.collection("community")
                .add(post)
                .addOnSuccessListener {
                    Toast.makeText(requireContext(), "게시글이 저장되었습니다.", Toast.LENGTH_SHORT).show()
                    // 뒤로 이동 (CommunityFragment로 돌아가기)
                    parentFragmentManager.popBackStack()
                }
                .addOnFailureListener { e ->
                    Toast.makeText(requireContext(), "게시글 저장에 실패했습니다. 다시 시도해 주세요.", Toast.LENGTH_SHORT).show()
                    e.printStackTrace()
                }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}