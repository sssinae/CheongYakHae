package com.example.cheongyakhae.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.cheongyakhae.databinding.FragmentUpdateBinding
import com.example.cheongyakhae.model.Post
import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat
import java.util.*

class UpdateFragment : Fragment() {
    private lateinit var binding: FragmentUpdateBinding
    private val db = FirebaseFirestore.getInstance()
    private lateinit var postId: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentUpdateBinding.inflate(inflater, container, false)

        // 전달된 postId로 Firebase에서 기존 글 불러오기
        postId = arguments?.getString("postId") ?: ""
        fetchPost()

        binding.btnUpdate.setOnClickListener {
            val updatedTitle = binding.etTitle.text.toString()
            val updatedContent = binding.etContent.text.toString()
            val updatedLastModified = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault()).format(Date())

            val updatedPost = Post(postId, updatedTitle, updatedContent, "작성자", updatedLastModified)
            db.collection("user").document(postId).set(updatedPost)
                .addOnSuccessListener {
                    parentFragmentManager.popBackStack()
                }
        }

        binding.btnDelete.setOnClickListener {
            db.collection("user").document(postId).delete()
                .addOnSuccessListener {
                    parentFragmentManager.popBackStack()
                }
        }

        return binding.root
    }

    private fun fetchPost() {
        db.collection("user").document(postId)
            .get()
            .addOnSuccessListener { document ->
                val post = document.toObject(Post::class.java)
                binding.etTitle.setText(post?.title)
                binding.etContent.setText(post?.content)
            }
    }
}
