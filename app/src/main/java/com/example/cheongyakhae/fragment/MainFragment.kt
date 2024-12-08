package com.example.cheongyakhae.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.cheongyakhae.KAKAO_MAP_KEY
import com.example.cheongyakhae.adpater.AnnouncementAdapter
import com.example.cheongyakhae.databinding.FragmentMainBinding
import com.example.cheongyakhae.model.Announcement
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.kakao.vectormap.KakaoMap
import com.kakao.vectormap.KakaoMapReadyCallback
import com.kakao.vectormap.KakaoMapSdk
import com.kakao.vectormap.MapLifeCycleCallback
import com.kakao.vectormap.MapView

class MainFragment : Fragment() {
    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!
    private lateinit var mapView: MapView
    private var kakaoMap: KakaoMap? = null

    private lateinit var adapter: AnnouncementAdapter
    private val announcements = mutableListOf<Announcement>()
    private val db = FirebaseFirestore.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // KakaoMap 초기화
        showMapView()

        // RecyclerView 초기화
        setupRecyclerView()

        // 최신 공고 가져오기
        fetchLatestAnnouncements()
    }

    private fun showMapView() {
        mapView = binding.mapView
        KakaoMapSdk.init(requireContext(), KAKAO_MAP_KEY)

        mapView.start(object : MapLifeCycleCallback() {
            override fun onMapDestroy() {
                Log.d("KakaoMap", "onMapDestroy")
            }

            override fun onMapError(p0: Exception?) {
                Log.e("KakaoMap", "onMapError")
            }
        }, object : KakaoMapReadyCallback() {
            override fun onMapReady(kakaomap: KakaoMap) {
                kakaoMap = kakaomap
            }
        })
    }

    private fun setupRecyclerView() {
        adapter = AnnouncementAdapter(announcements) { announcement ->
            Toast.makeText(requireContext(), "${announcement.announcement_title} 선택됨", Toast.LENGTH_SHORT).show()
        }
        binding.latestAnnouncementsRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.latestAnnouncementsRecyclerView.adapter = adapter
    }

    private fun fetchLatestAnnouncements() {
        db.collection("announcements")
            .orderBy("announcement_date", Query.Direction.DESCENDING)
            .limit(5)  // 최신 5개 공고만 가져오기
            .get()
            .addOnSuccessListener { documents ->
                announcements.clear()
                for (document in documents) {
                    val data = document.data

                    val supplyCount = when (val count = data["supply_household_count"]) {
                        is Long -> count.toInt()           // Firestore에서 숫자를 Long으로 반환할 경우
                        is String -> count.toIntOrNull()   // 문자열인 경우 Int로 변환 시도
                        else -> null                       // 다른 타입이거나 변환에 실패하면 null
                    }

                    val announcement = Announcement(
                        business_name = data["business_name"] as? String ?: "Unknown",
                        announcement_title = data["announcement_title"] as? String ?: "No Title",
                        house_type = data["house_type"] as? String ?: "No Type",
                        house_detail_type = data["house_detail_type"] as? String ?: "No Detail Type",
                        address = data["address"] as? String ?: "No Address",
                        zip_code = data["zip_code"] as? String,
                        contact_number = data["contact_number"] as? String,
                        supply_household_count = supplyCount,
                        announcement_date = data["announcement_date"] as? String ?: "No Date",
                        contract_date_start = data["contract_date_start"] as? String ?: "No Start Date",
                        contract_date_end = data["contract_date_end"] as? String ?: "No End Date",
                        move_in_date = data["move_in_date"] as? String,
                        announcement_url = data["announcement_url"] as? String ?: "No URL",
                        source = data["source"] as? String ?: "Unknown"
                    )

                    announcements.add(announcement)
                }
                adapter.notifyDataSetChanged()
            }
            .addOnFailureListener { exception ->
                Toast.makeText(requireContext(), "데이터를 불러오지 못했습니다.", Toast.LENGTH_SHORT).show()
            }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
