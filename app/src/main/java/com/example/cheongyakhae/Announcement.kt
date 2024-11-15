package com.example.cheongyakhae.model

data class Announcement(
    val announcement_title: String = "No Title",  // 공고 제목
    val house_type: String = "No Type",          // 주택 유형
    val house_detail_type: String = "No Detail Type", // 세부 유형
    val announcement_date: String = "No Date",   // 공고 게시일
    val supply_household_count: Int = 0          // 공급 세대수
)
