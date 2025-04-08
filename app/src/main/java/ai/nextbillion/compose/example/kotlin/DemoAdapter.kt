package ai.nextbillion.compose.example.kotlin

import ai.nextbillion.compose.example.R
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class DemoAdapter(private val items: List<DemoItem>) :
    RecyclerView.Adapter<DemoAdapter.DemoViewHolder>() {

    class DemoViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvTitle: TextView = view.findViewById(R.id.tvTitle)
        val tvDescription: TextView = view.findViewById(R.id.tvDescription)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DemoViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_demo, parent, false)
        return DemoViewHolder(
            view
        )
    }

    override fun onBindViewHolder(holder: DemoViewHolder, position: Int) {
        val item = items[position]
        holder.tvTitle.text = item.title
        holder.tvDescription.text = item.description
        holder.itemView.setOnClickListener { item.action() }
    }

    override fun getItemCount() = items.size
} 