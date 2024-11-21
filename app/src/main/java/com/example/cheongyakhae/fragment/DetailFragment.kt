package com.example.cheongyakhae.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.cheongyakhae.databinding.FragmentDetailBinding

class DetailFragment : Fragment() {

    private lateinit var binding: FragmentDetailBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDetailBinding.inflate(inflater, container, false)

        // 데이터 수신
        val announcementTitle = arguments?.getString("announcement_title")
        val houseType = arguments?.getString("house_type")
        val houseDetailType = arguments?.getString("house_detail_type")
        val announcementDate = arguments?.getString("announcement_date")
        val supplyHouseholdCount = arguments?.getInt("supply_household_count") ?: 0
        val contactNumber = arguments?.getString("contact_number")

        // UI 업데이트
        binding.announcementTitle.text = announcementTitle
        binding.houseType.text = houseType
        binding.houseDetailType.text = houseDetailType
        binding.announcementDate.text = "Date: $announcementDate"
        binding.supplyHouseholdCount.text = "Supply: $supplyHouseholdCount"
        binding.contactNumber.text = "Contact: $contactNumber"

        return binding.root
    }
}
