package com.example.cheongyakhae.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.cheongyakhae.R
import com.example.cheongyakhae.Announcement

class AnnouncementAdapter(
    private val announcements: List<Announcement>,
    private val onClick: (Announcement) -> Unit
) : RecyclerView.Adapter<AnnouncementAdapter.AnnouncementViewHolder>() {

    inner class AnnouncementViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val title: TextView = itemView.findViewById(R.id.announcement_title)
        val houseType: TextView = itemView.findViewById(R.id.house_type)
        val houseDetailType: TextView = itemView.findViewById(R.id.house_detail_type)
        val date: TextView = itemView.findViewById(R.id.announcement_date)
        val supplyCount: TextView = itemView.findViewById(R.id.supply_household_count)
        val contactNumber: TextView = itemView.findViewById(R.id.contact_number)

        fun bind(announcement: Announcement) {
            title.text = announcement.announcement_title
            houseType.text = announcement.house_type
            houseDetailType.text = announcement.house_detail_type
            date.text = "Date: ${announcement.announcement_date}"
            supplyCount.text = "Supply: ${announcement.supply_household_count ?: 0}"
            contactNumber.text = "Contact: ${announcement.contact_number ?: "N/A"}"

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
