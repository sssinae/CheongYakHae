package com.example.cheongyakhae

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.cheongyakhae.databinding.FragmentWriteBinding
import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat
import java.util.*

class WriteFragment : Fragment() {
    private lateinit var binding: FragmentWriteBinding
    private val db = FirebaseFirestore.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentWriteBinding.inflate(inflater, container, false)

        binding.btnSubmit.setOnClickListener {
            val title = binding.etTitle.text.toString()
            val content = binding.etContent.text.toString()
            val author = "작성자" // Firebase Auth에서 가져올 수 있음
            val lastModified = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault()).format(Date())

            val post = Post(UUID.randomUUID().toString(), title, content, author, lastModified)
            db.collection("user").document(post.id).set(post)
                .addOnSuccessListener {
                    parentFragmentManager.popBackStack()
                }
        }

        return binding.root
    }
}
