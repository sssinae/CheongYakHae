package com.example.cheongyakhae.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.cheongyakhae.databinding.ActivityPostDetailBinding

// 커뮤니티 > 카드로 돼 있는 각 게시글을 누르면 나오는 화면에 대한 fragment임.
class PostDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPostDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPostDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Intent에서 데이터 가져오기
        val postTitle = intent.getStringExtra("POST_TITLE") ?: "제목 없음"
        val postContent = intent.getStringExtra("POST_CONTENT") ?: "내용 없음"

        // TextView에 데이터 설정
        binding.postTitle.text = postTitle
        binding.postContent.text = postContent
    }
}
