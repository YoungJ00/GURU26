import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.example.guru26.LikeFragment
import com.example.guru26.WorteFragment

class TabPagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> WorteFragment() // 첫 번째 탭에 대한 프래그먼트
            1 ->LikeFragment() // 두 번째 탭에 대한 프래그먼트
            else -> throw IllegalArgumentException("Invalid tab position")
        }
    }

    override fun getCount(): Int {
        return 3 // 탭 개수
    }
}