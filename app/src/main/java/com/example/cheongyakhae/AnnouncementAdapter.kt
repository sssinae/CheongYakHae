package com.example.cheongyakhae

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.cheongyakhae.model.Announcement

class AnnouncementAdapter(private val announcements: List<Announcement>) :
    RecyclerView.Adapter<AnnouncementAdapter.AnnouncementViewHolder>() {

    inner class AnnouncementViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val title: TextView = itemView.findViewById(R.id.announcement_title)
        val houseType: TextView = itemView.findViewById(R.id.house_type)
        val houseDetailType: TextView = itemView.findViewById(R.id.house_detail_type)
        val date: TextView = itemView.findViewById(R.id.announcement_date)
        val supplyCount: TextView = itemView.findViewById(R.id.supply_household_count)
        val contactNumber: TextView = itemView.findViewById(R.id.contact_number)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AnnouncementViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_announcement, parent, false)
        return AnnouncementViewHolder(view)
    }

    override fun onBindViewHolder(holder: AnnouncementViewHolder, position: Int) {
        val announcement = announcements[position]
        holder.title.text = announcement.announcement_title
        holder.houseType.text = announcement.house_type
        holder.houseDetailType.text = announcement.house_detail_type
        holder.date.text = "Date: ${announcement.announcement_date}"
        holder.supplyCount.text = "Supply: ${announcement.supply_household_count ?: 0}"
        holder.contactNumber.text = "Contact: ${announcement.contact_number ?: "N/A"}"
    }

    override fun getItemCount(): Int = announcements.size
}
