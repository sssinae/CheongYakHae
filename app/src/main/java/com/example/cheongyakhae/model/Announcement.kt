package com.example.cheongyakhae.model

data class Announcement(
    val business_name: String? = "Unknown",
    val announcement_title: String? = "No Title",
    val house_type: String? = "No Type",
    val house_detail_type: String? = "No Detail Type",
    val address: String? = "No Address",
    val zip_code: String? = null,  // String?로 변경 (우편번호는 문자형으로 처리)
    val contact_number: String? = null,
    val supply_household_count: Int? = null,  // 공급 세대수는 Int로 처리
    val announcement_date: String? = "No Date",
    val contract_date_start: String? = "No Start Date",
    val contract_date_end: String? = "No End Date",
    val move_in_date: String? = null,
    val announcement_url: String? = "No URL",
    val source: String? = "Unknown"
)
