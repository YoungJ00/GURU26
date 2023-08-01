package com.example.guru26
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration

class WorteFragment : Fragment() {

    private var firestore: FirebaseFirestore? = null
    private var contentDTOs: ArrayList<ContentDTO> = arrayListOf()
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: WroteAdapter
    private lateinit var snapshotListener: ListenerRegistration

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_worte2, container, false)

        firestore = FirebaseFirestore.getInstance()
        recyclerView = view.findViewById(R.id.rv_poster2)
        recyclerView.layoutManager = LinearLayoutManager(activity)
        adapter = WroteAdapter()
        recyclerView.adapter = adapter

        // Fetch data using addSnapshotListener
        fetchData()

        return view
    }

    private fun fetchData() {
        val currentUserUid = FirebaseAuth.getInstance().currentUser?.uid
        if (currentUserUid != null) {
            snapshotListener = firestore?.collection("images")?.whereEqualTo("uid", currentUserUid) // 이 부분을 추가하여 현재 사용자와 같은 사람이 작성한 데이터만 가져옵니다.
                ?.addSnapshotListener { querySnapshot, error ->
                    if (error != null) {
                        // Handle error
                        return@addSnapshotListener
                    }

                    contentDTOs.clear()
                    for (snapshot in querySnapshot!!) {
                        val item = snapshot.toObject(ContentDTO::class.java)
                        item?.let { contentDTOs.add(it) }
                    }
                    // Sort the list in reverse order (most recent items first)
                    contentDTOs.sortByDescending { it.timeStamp }
                    adapter.notifyDataSetChanged()
                } ?: return
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        // Remove the snapshot listener to avoid memory leaks
        snapshotListener.remove()
    }
    inner class WroteAdapter : RecyclerView.Adapter<WroteAdapter.CustomViewHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item, parent, false)
            return CustomViewHolder(view)
        }

        override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {
            val contentDTO = contentDTOs[position]

            // 데이터 설정
            holder.exhNameTextView.text = contentDTO.exhName
            holder.exhPlaceTextView.text = contentDTO.exhPlace
            holder.exhStartDayTextView.text = contentDTO.exhStartDay
            holder.exhEndDayTextView.text = contentDTO.exhEndDay

            // 이미지 설정
            Glide.with(holder.itemView.context).load(contentDTO.imageUrl).into(holder.posterImageView)


            holder.itemView.setOnClickListener {
                val contentDTO = contentDTOs[position]

                val intent = Intent(activity, Detail::class.java)
                intent.putExtra("exhName", contentDTO.exhName)
                intent.putExtra("exhPlace", contentDTO.exhPlace)
                intent.putExtra("exhStartDay", contentDTO.exhStartDay)
                intent.putExtra("exhEndDay", contentDTO.exhEndDay)
                intent.putExtra("imageUrl", contentDTO.imageUrl)
                intent.putExtra("explain", contentDTO.explain)
                intent.putExtra("exhTime", contentDTO.exhTime)
                intent.putExtra("exhLink", contentDTO.exhLink)
                intent.putExtra("favoriteCount", contentDTO.favoriteCount)

                activity?.startActivity(intent)
            }
        }

        override fun getItemCount(): Int {
            return contentDTOs.size
        }

        inner class CustomViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            val posterImageView: ImageView = view.findViewById(R.id.iv_poster)
            val exhNameTextView: TextView = view.findViewById(R.id.tv_name)
            val exhPlaceTextView: TextView = view.findViewById(R.id.tv_place)
            val exhStartDayTextView: TextView = view.findViewById(R.id.tv_start_date)
            val exhEndDayTextView: TextView = view.findViewById(R.id.tv_end_date)
        }
    }
}