package com.example.guru26

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class LikeFragment : Fragment() {

    private var firestore: FirebaseFirestore? = null
    private var favoriteContentList: ArrayList<ContentDTO> = arrayListOf()
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: FavoriteAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_like2, container, false)

        firestore = FirebaseFirestore.getInstance()
        recyclerView = view.findViewById(R.id.rv_poster3)
        recyclerView.layoutManager = LinearLayoutManager(activity)
        adapter = FavoriteAdapter(favoriteContentList)
        recyclerView.adapter = adapter

        // Fetch favorite content from Firestore
        fetchFavoriteContent()

        return view
    }

    private fun fetchFavoriteContent() {
        // Get the current user's UID
        val currentUserUid = FirebaseAuth.getInstance().currentUser?.uid

        // Make sure the user is logged in before fetching favorite content
        if (currentUserUid == null) {
            return
        }

        // Firestore query to fetch the favorite content
        firestore?.collection("images")
            ?.whereEqualTo("favorites.$currentUserUid", true)
            ?.addSnapshotListener { querySnapshot, error ->
                if (error != null) {
                    // Handle error
                    return@addSnapshotListener
                }

                favoriteContentList.clear()
                for (snapshot in querySnapshot!!) {
                    val contentDTO = snapshot.toObject(ContentDTO::class.java)
                    favoriteContentList.add(contentDTO)
                }
                // Update the RecyclerView
                adapter.notifyDataSetChanged()
            }
    }

    // RecyclerView Adapter
    inner class FavoriteAdapter(private val favoriteList: List<ContentDTO>) : RecyclerView.Adapter<FavoriteAdapter.CustomViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item, parent, false)
            return CustomViewHolder(view)
        }

        override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {
            val contentDTO = favoriteList[position]

            // 데이터 설정
            holder.exhNameTextView.text = contentDTO.exhName
            holder.exhPlaceTextView.text = contentDTO.exhPlace
            holder.exhStartDayTextView.text = contentDTO.exhStartDay
            holder.exhEndDayTextView.text = contentDTO.exhEndDay

            // 이미지 설정
            Glide.with(holder.itemView.context).load(contentDTO.imageUrl).into(holder.posterImageView)

            holder.itemView.setOnClickListener {
                // Launch DetailActivity when an item is clicked
                val intent = Intent(activity, Detail::class.java).apply {
                    putExtra("exhName", contentDTO.exhName)
                    putExtra("exhPlace", contentDTO.exhPlace)
                    putExtra("exhStartDay", contentDTO.exhStartDay)
                    putExtra("exhEndDay", contentDTO.exhEndDay)
                    putExtra("exhTime", contentDTO.exhTime)
                    putExtra("explain", contentDTO.explain)
                    putExtra("imageUrl", contentDTO.imageUrl)
                    putExtra("exhLink", contentDTO.exhLink)
                    putExtra("favoriteCount", contentDTO.favoriteCount)
                }
                activity?.startActivity(intent)
            }
        }

        override fun getItemCount(): Int {
            return favoriteList.size
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
