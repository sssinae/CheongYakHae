package com.example.cheongyakhae.fragment

import android.os.Bundle
import android.text.method.LinkMovementMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.cheongyakhae.databinding.FragmentDetailBinding

class DetailFragment : Fragment() {

    private var _binding: FragmentDetailBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailBinding.inflate(inflater, container, false)

        // 뒤로 가기 버튼 동작 설정
        handleBackPressed()

        // 데이터 바인딩
        bindAnnouncementData()

        return binding.root
    }

    private fun handleBackPressed() {
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            findNavController().popBackStack()
        }
    }

    private fun bindAnnouncementData() {
        val args = requireArguments()
        binding.announcementTitle.text = args.getString("announcement_title", "제목 없음")
        binding.houseType.text = args.getString("house_type", "유형 없음")
        binding.houseDetailType.text = args.getString("house_detail_type", "세부 유형 없음")
        binding.announcementDate.text = args.getString("announcement_date", "날짜 없음")
        binding.supplyHouseholdCount.text = args.getString("supply_household_count", "정보 없음")
        binding.contactNumber.text = args.getString("contact_number", "연락처 없음")
        binding.announcementUrl.text = args.getString("announcement_url", "URL 없음")

        // URL을 클릭 가능하게 설정
        binding.announcementUrl.movementMethod = LinkMovementMethod.getInstance()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
