package com.example.guru26

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore
import java.util.*
import com.bumptech.glide.Glide

class HomeFragment : Fragment() {

    var firestore : FirebaseFirestore? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
    {
        // Inflate the layout for this fragment
        var view = LayoutInflater.from(activity).inflate(R.layout.fragment_home, container, false)
        firestore = FirebaseFirestore.getInstance()

        view.findViewById<RecyclerView>(R.id.rv_poster).adapter = PosterAdapter()
        view.findViewById<RecyclerView>(R.id.rv_poster).layoutManager = LinearLayoutManager(activity)

        return view
    }

    inner class PosterAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
        var contentDTOs : ArrayList<ContentDTO > = arrayListOf()
        private var contentUidList :ArrayList<String> = arrayListOf()

        init {
            firestore?.collection("images")?.orderBy("timestamp")?.addSnapshotListener { querySnapshot, FirebaseFirestoreException ->
                contentDTOs.clear()
                contentUidList.clear()
                for(snapshot in querySnapshot!!.documents){
                    var item = snapshot. toObject(ContentDTO::class.java)
                    contentDTOs.add(item!!)
                    contentUidList.add(snapshot.id)
                }
                notifyDataSetChanged()
            }
        }

        override fun onCreateViewHolder(p0: ViewGroup, p1: Int): RecyclerView.ViewHolder {
            val view = LayoutInflater.from(p0.context).inflate(R.layout.list_item, p0, false)
            return CustomViewHolder(view)
        }

        inner class CustomViewHolder (view : View) : RecyclerView.ViewHolder(view){}


        override fun getItemCount(): Int {
            return contentDTOs.size
        }

        override fun onBindViewHolder(p0: RecyclerView.ViewHolder, p1: Int) {

            var viewholder = (p0 as CustomViewHolder).itemView

            //UserID
            viewholder.findViewById<TextView>(R.id.tv_name).text= contentDTOs!![p1].exhName
            viewholder.findViewById<TextView>(R.id.tv_start_date).text= contentDTOs!![p1].exhStartDay
            viewholder.findViewById<TextView>(R.id.tv_end_date).text= contentDTOs!![p1].exhEndDay
            viewholder.findViewById<TextView>(R.id.tv_place).text= contentDTOs!![p1].exhPlace

            //Image
            Glide.with(p0.itemView.context).load(contentDTOs!![p1].imageUrl).into(viewholder.findViewById(R.id.iv_poster))

        }
    }
}
