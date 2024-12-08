package com.example.cheongyakhae.adpater

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.cheongyakhae.R
import com.example.cheongyakhae.model.Announcement

class AnnouncementAdapter(
    private val announcements: List<Announcement>,
    private val onClick: (Announcement) -> Unit
) : RecyclerView.Adapter<AnnouncementAdapter.AnnouncementViewHolder>() {

    inner class AnnouncementViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val title: TextView = itemView.findViewById(R.id.announcement_title)
        val date: TextView = itemView.findViewById(R.id.announcement_date)
        val houseType: TextView = itemView.findViewById(R.id.house_type)

        fun bind(announcement: Announcement) {
            title.text = announcement.announcement_title
            date.text = "날짜: ${announcement.announcement_date}"
            houseType.text = "유형: ${announcement.house_type}"

            itemView.setOnClickListener { onClick(announcement) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AnnouncementViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_announcement, parent, false)
        return AnnouncementViewHolder(view)
    }

    override fun onBindViewHolder(holder: AnnouncementViewHolder, position: Int) {
        holder.bind(announcements[position])
    }

    override fun getItemCount(): Int = announcements.size
}
