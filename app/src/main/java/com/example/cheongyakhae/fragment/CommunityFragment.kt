package com.example.cheongyakhae.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.cheongyakhae.R
import com.example.cheongyakhae.adpater.PostAdapter
import com.example.cheongyakhae.databinding.FragmentCommunityBinding
import com.example.cheongyakhae.model.Post
import com.google.firebase.firestore.FirebaseFirestore

class CommunityFragment : Fragment() {
    private lateinit var binding: FragmentCommunityBinding
    private lateinit var adapter: PostAdapter
    private val posts = mutableListOf<Post>()
    private val db = FirebaseFirestore.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCommunityBinding.inflate(inflater, container, false)

        // RecyclerView 설정
        adapter = PostAdapter(posts)
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView.adapter = adapter

        // Floating 버튼 클릭 -> WriteFragment로 이동
        binding.fab.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.nav_host_fragment, WriteFragment())
                .addToBackStack(null)
                .commit()
        }

        fetchPosts()
        return binding.root
    }

    private fun fetchPosts() {
        db.collection("user")
            .get()
            .addOnSuccessListener { documents ->
                posts.clear()
                for (document in documents) {
                    val post = document.toObject(Post::class.java)
                    posts.add(post)
                }
                adapter.notifyDataSetChanged()
            }
    }
}
