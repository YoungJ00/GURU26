package com.example.guru26

import TabPagerAdapter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import com.google.firebase.auth.FirebaseAuth



class SettingFragment : Fragment() {

    private lateinit var viewPager: ViewPager
    private lateinit var tabLayout: TabLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_setting, container, false)

        viewPager = view.findViewById(R.id.view_pager)
        tabLayout = view.findViewById(R.id.tab_layout)

        // ViewPager에 어댑터 연결
        val pagerAdapter = TabPagerAdapter(childFragmentManager)
        viewPager.adapter = pagerAdapter

        // TabLayout과 ViewPager 연동
        tabLayout.setupWithViewPager(viewPager)

        // 탭 이름 설정 (TabPagerAdapter에서 정의한 메소드 사용)
        tabLayout.getTabAt(0)?.text = "작성글"
        tabLayout.getTabAt(1)?.text = "좋아요"


        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 사용자 이메일 주소를 표시할 TextView를 찾습니다.
        val tvUserEmail: TextView = view.findViewById(R.id.tv_user_email)

        // FirebaseAuth 인스턴스를 가져옵니다.
        val auth = FirebaseAuth.getInstance()

        // 현재 로그인된 사용자가 있는지 확인합니다.
        val currentUser = auth.currentUser
        if (currentUser != null) {
            // 로그인된 사용자의 이메일 주소를 가져옵니다.
            val userEmail = currentUser.email

            // 이메일 주소에서 '@'를 기준으로 문자열을 분할합니다.
            val parts = userEmail?.split("@")
            val username = parts?.get(0) // '@' 앞의 텍스트를 가져옵니다.

            // 가져온 유저이름을 TextView에 설정합니다.
            tvUserEmail.text = username
        } else {
            // 로그인되지 않은 경우에는 TextView를 숨깁니다.
            tvUserEmail.visibility = View.GONE
        }
    }
}