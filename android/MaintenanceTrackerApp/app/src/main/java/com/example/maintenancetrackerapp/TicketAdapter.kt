package com.example.maintenancetrackerapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.maintenancetrackerapp.model.MaintenanceTicket

class TicketAdapter(
    private var ticketList: List<MaintenanceTicket> = emptyList(),
    private val onResolveClick: (MaintenanceTicket) -> Unit
) : RecyclerView.Adapter<TicketAdapter.TicketViewHolder>() {

    class TicketViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvTicketId: TextView = itemView.findViewById(R.id.tvTicketId)
        val tvCategory: TextView = itemView.findViewById(R.id.tvCategory)
        val tvStatus: TextView = itemView.findViewById(R.id.tvStatus)
        val tvDescription: TextView = itemView.findViewById(R.id.tvDescription)
        val btnResolve: Button = itemView.findViewById(R.id.btnResolve)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TicketViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_ticket, parent, false)
        return TicketViewHolder(view)
    }

    override fun onBindViewHolder(holder: TicketViewHolder, position: Int) {
        val ticket = ticketList[position]

        holder.tvTicketId.text = "#${ticket.id}"
        holder.tvCategory.text = ticket.category
        holder.tvStatus.text = ticket.status

        if (!ticket.description.isNullOrBlank()) {
            holder.tvDescription.visibility = View.VISIBLE
            holder.tvDescription.text = ticket.description
        } else {
            holder.tvDescription.visibility = View.GONE
        }

        if (ticket.status.equals("RESOLVED", ignoreCase = true)) {
            holder.btnResolve.visibility = View.GONE
        } else {
            holder.btnResolve.visibility = View.VISIBLE
            holder.btnResolve.setOnClickListener {
                onResolveClick(ticket)
            }
        }
    }

    override fun getItemCount(): Int = ticketList.size

    fun updateTickets(newTickets: List<MaintenanceTicket>) {
        this.ticketList = newTickets
        notifyDataSetChanged()
    }
}