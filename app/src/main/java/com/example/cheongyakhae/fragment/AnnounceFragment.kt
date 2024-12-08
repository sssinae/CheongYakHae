package com.example.cheongyakhae.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.cheongyakhae.R
import com.example.cheongyakhae.databinding.FragmentAnnounceBinding
import com.example.cheongyakhae.adpater.AnnouncementAdapter
import com.example.cheongyakhae.model.Announcement
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.QueryDocumentSnapshot
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Locale

class AnnounceFragment : Fragment() {
    private var _binding: FragmentAnnounceBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: AnnouncementAdapter
    private val announcements = mutableListOf<Announcement>()
    private val db = FirebaseFirestore.getInstance()

    // 선택된 필터를 저장하는 Set
    private val selectedFilters = mutableSetOf<String>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAnnounceBinding.inflate(inflater, container, false)

        // RecyclerView 설정
        setupRecyclerView()

        // Firestore 데이터 가져오기
        fetchAnnouncements()

        // 뒤로 가기 콜백 설정
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                findNavController().popBackStack()
            }
        })

        // 필터 텍스트뷰 클릭 이벤트 설정
        setupFilterListeners()

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null // 메모리 누수 방지
    }

    private fun setupRecyclerView() {
        adapter = AnnouncementAdapter(announcements) { announcement ->
            navigateToDetailFragment(announcement)
        }
        binding.announceRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.announceRecyclerView.adapter = adapter
    }

    private fun navigateToDetailFragment(announcement: Announcement) {
        val bundle = Bundle().apply {
            putString("announcement_title", announcement.announcement_title)
            putString("house_type", announcement.house_type)
            putString("house_detail_type", announcement.house_detail_type)
            putString("announcement_date", formatDate(announcement.announcement_date))
            putString("supply_household_count", formatNumber(announcement.supply_household_count))
            putString("contact_number", formatPhoneNumber(announcement.contact_number))
            putString("announcement_url", announcement.announcement_url)
        }
        findNavController().navigate(R.id.action_announceFragment_to_detailFragment, bundle)
    }

    private fun setupFilterListeners() {
        val filters = mapOf(
            binding.regionSeoul to "서울",
            binding.regionBusan to "부산",
            binding.regionDaegu to "대구",
            binding.houseHappy to "행복주택",
            binding.houseYouth to "청년매입주택",
            binding.houseRedevelopment to "재개발임대주택",
            binding.qualificationYouth to "청년",
            binding.qualificationSingle to "신혼부부",
            binding.qualificationTwoChildren to "다자녀"
        )

        filters.forEach { (view, filter) ->
            view.setOnClickListener {
                if (selectedFilters.contains(filter)) {
                    selectedFilters.remove(filter)
                    view.setBackgroundResource(R.drawable.textview_selector)
                } else {
                    selectedFilters.add(filter)
                    view.setBackgroundResource(R.drawable.textview_selector_selected)
                }
                updateSelectedFiltersDisplay()
            }
        }

        binding.applyFilterButton.setOnClickListener {
            fetchFilteredAnnouncements()
        }
    }

    private fun updateSelectedFiltersDisplay() {
        binding.selectedFilters.text = "선택된 필터: ${selectedFilters.joinToString(" #") { it }}"
    }

    private fun fetchAnnouncements() {
        binding.progressBar.visibility = View.VISIBLE

        db.collection("announcements")
            .orderBy("announcement_date", Query.Direction.DESCENDING)
            .get()
            .addOnSuccessListener { documents ->
                announcements.clear()
                for (document in documents) {
                    val announcement = parseDocumentToAnnouncement(document)
                    announcements.add(announcement)
                }
                adapter.notifyDataSetChanged()
                binding.progressBar.visibility = View.GONE
            }
            .addOnFailureListener {
                Toast.makeText(requireContext(), "데이터를 불러오는데 실패했습니다.", Toast.LENGTH_SHORT).show()
                binding.progressBar.visibility = View.GONE
            }
    }

    private fun fetchFilteredAnnouncements() {
        binding.progressBar.visibility = View.VISIBLE

        var query: Query = db.collection("announcements").orderBy("announcement_date", Query.Direction.DESCENDING)
        selectedFilters.forEach { filter ->
            query = query.whereArrayContains("filters", filter)
        }

        query.get()
            .addOnSuccessListener { documents ->
                announcements.clear()
                for (document in documents) {
                    val announcement = parseDocumentToAnnouncement(document)
                    announcements.add(announcement)
                }
                adapter.notifyDataSetChanged()
                binding.progressBar.visibility = View.GONE
            }
            .addOnFailureListener {
                Toast.makeText(requireContext(), "필터링된 데이터를 가져오는데 실패했습니다.", Toast.LENGTH_SHORT).show()
                binding.progressBar.visibility = View.GONE
            }
    }

    private fun parseDocumentToAnnouncement(document: QueryDocumentSnapshot): Announcement {
        val supplyHouseholdCount = document.get("supply_household_count")?.toString()?.toIntOrNull() ?: 0
        return Announcement(
            announcement_title = document.getString("announcement_title") ?: "제목 없음",
            house_type = document.getString("house_type") ?: "유형 없음",
            house_detail_type = document.getString("house_detail_type") ?: "세부 유형 없음",
            announcement_date = document.getString("announcement_date") ?: "날짜 없음",
            supply_household_count = supplyHouseholdCount,
            contact_number = document.getString("contact_number") ?: "연락처 없음",
            announcement_url = document.getString("announcement_url") ?: "URL 없음"
        )
    }

    // 날짜 포맷 함수
    private fun formatDate(date: String?): String {
        return try {
            val inputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.KOREA)
            val outputFormat = SimpleDateFormat("yyyy년 MM월 dd일", Locale.KOREA)
            inputFormat.parse(date ?: "")?.let { outputFormat.format(it) } ?: "날짜 없음"
        } catch (e: Exception) {
            "날짜 없음"
        }
    }

    // 숫자 포맷 함수
    private fun formatNumber(number: Int?): String {
        return number?.let { "${NumberFormat.getInstance(Locale.KOREA).format(it)} 세대" } ?: "정보 없음"
    }

    // 전화번호 포맷 함수
    private fun formatPhoneNumber(phone: String?): String {
        return phone?.replace("(\\d{3})(\\d{4})(\\d{4})".toRegex(), "$1-$2-$3") ?: "연락처 없음"
    }
}
