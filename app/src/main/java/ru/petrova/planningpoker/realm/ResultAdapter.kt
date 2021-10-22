package ru.petrova.planningpoker.realm

import android.view.*
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import io.realm.OrderedRealmCollection
import io.realm.RealmRecyclerViewAdapter
import ru.petrova.planningpoker.R
import ru.petrova.planningpoker.realm.dto.Estimate

internal class ResultAdapter(data: OrderedRealmCollection<Estimate>):
    RealmRecyclerViewAdapter<Estimate, ResultAdapter.TaskViewHolder?>(data, true) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val itemView: View = LayoutInflater.from(parent.context).inflate(R.layout.result_view, parent, false)
        return TaskViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        val obj: Estimate? = getItem(position)
        holder.estimate.text = obj?.estimate
        holder.userName.text = obj?.userName
    }

    internal inner class TaskViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var estimate: TextView = view.findViewById(R.id.resultEstimate)
        var userName: TextView = view.findViewById(R.id.resultUserName)
    }
}
