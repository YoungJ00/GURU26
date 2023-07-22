package com.example.guru26

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class PosterAdapter(val posterList: ArrayList<Poster>): RecyclerView.Adapter<PosterAdapter.CustomViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PosterAdapter.CustomViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item, parent, false)
        return CustomViewHolder(view)
    }

    override fun onBindViewHolder(holder: PosterAdapter.CustomViewHolder, position: Int) {
        holder.brochure.setImageResource(posterList.get(position).brochure)
        holder.name.text = posterList.get(position).name
        holder.date.text=posterList.get(position).date.toString()
        holder.place.text=posterList.get(position).place
    }

    override fun getItemCount(): Int {
        return posterList.size
    }

    class CustomViewHolder (itemView: View) : RecyclerView.ViewHolder(itemView){
        val brochure = itemView.findViewById<ImageView>(R.id.iv_poster) // 포스터
        val name = itemView.findViewById<TextView>(R.id.tvname) // 이름
        val date = itemView.findViewById<TextView>(R.id.tv_date) // 일시
        val place = itemView.findViewById<TextView>(R.id.tv_place) // 장소
    }
}