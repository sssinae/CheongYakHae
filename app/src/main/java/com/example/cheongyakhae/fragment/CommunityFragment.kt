package com.example.cheongyakhae.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.cheongyakhae.R
import com.example.cheongyakhae.activity.PostDetailActivity
import com.example.cheongyakhae.adapter.PostAdapter
import com.example.cheongyakhae.databinding.FragmentCommunityBinding
import com.example.cheongyakhae.model.Post
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.ktx.toObjects

class CommunityFragment : Fragment() {

    private var _binding: FragmentCommunityBinding? = null
    private val binding get() = _binding!!

    private lateinit var firestore: FirebaseFirestore

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCommunityBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        firestore = FirebaseFirestore.getInstance()

        // RecyclerView 설정
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        fetchPosts()

        // FAB 클릭 리스너 설정
        binding.fab.setOnClickListener {
            val navController = findNavController()
            navController.navigate(R.id.action_communityFragment_to_writeFragment)
        }
    }

    private fun fetchPosts() {
        firestore.collection("community")
            .orderBy("timestamp", Query.Direction.DESCENDING) // 최근 글을 위로 정렬
            .get()
            .addOnSuccessListener { result ->
                val posts = result.toObjects<Post>()
                binding.recyclerView.adapter = PostAdapter(posts) { post ->
                    val intent = Intent(requireContext(), PostDetailActivity::class.java).apply {
                        putExtra("POST_TITLE", post.title) // 제목 전달
                        putExtra("POST_CONTENT", post.content) // 내용 전달
                    }
                    startActivity(intent)
                }
            }
            .addOnFailureListener { exception ->
                // 실패 시 처리
                exception.printStackTrace()
            }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}