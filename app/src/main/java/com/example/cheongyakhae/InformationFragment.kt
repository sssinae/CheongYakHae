package com.example.cheongyakhae

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import com.example.cheongyakhae.databinding.FragmentInformationBinding
import com.google.android.material.tabs.TabLayout

class InformationFragment : Fragment() {
    private var _binding: FragmentInformationBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // View Binding 설정
        _binding = FragmentInformationBinding.inflate(inflater, container, false)

        // 각 섹션을 나타내는 뷰 참조
        val section1 = binding.section1
        val section2 = binding.section2
        val section3 = binding.section3

        // TabLayout 탭 클릭 리스너
        binding.tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                // 선택된 탭에 따라 ScrollView의 위치를 이동
                when (tab.position) {
                    0 -> scrollToSection(section1)
                    1 -> scrollToSection(section2)
                    2 -> scrollToSection(section3)
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {}
            override fun onTabReselected(tab: TabLayout.Tab) {}
        })

        // TabLayout에 탭 추가
        binding.tabLayout.addTab(binding.tabLayout.newTab().setText("당첨되면?").setContentDescription("당첨되면? 탭"))
        binding.tabLayout.addTab(binding.tabLayout.newTab().setText("신청 조건").setContentDescription("신청 조건 탭"))
        binding.tabLayout.addTab(binding.tabLayout.newTab().setText("주택 유형").setContentDescription("주택 유형 탭"))

        return binding.root
    }

    // 특정 섹션으로 스크롤하는 함수
    private fun scrollToSection(section: View) {
        binding.scrollView.post {
            binding.scrollView.smoothScrollTo(0, section.top)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
