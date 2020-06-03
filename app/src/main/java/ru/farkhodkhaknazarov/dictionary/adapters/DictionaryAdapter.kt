package ru.farkhodkhaknazarov.dictionary.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ru.farkhodkhaknazarov.dictionary.R
import ru.farkhodkhaknazarov.dictionary.data.room.DictionaryItem

class DictionaryAdapter(
    context: Context,
    contactList: List<DictionaryItem>,
    listener: DictionaryAdapterListener
) : RecyclerView.Adapter<DictionaryAdapter.MyViewHolder>(),
    Filterable {
    var context: Context
    var contactList: List<DictionaryItem>
    var contactListFiltered: List<DictionaryItem>
    var listener: DictionaryAdapterListener

    init {
        this.context = context
        this.listener = listener
        this.contactList = contactList
        this.contactListFiltered = contactList
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.list_row_item, parent, false)

        return MyViewHolder(itemView)
    }

    override fun getItemCount(): Int = contactListFiltered.size

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val dictionary: DictionaryItem = contactListFiltered.get(position)
        holder.textFrom.text = dictionary.textFrom
        holder.textTo.text = dictionary.textTo
    }

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var textFrom: TextView
        var textTo: TextView

        init {

            textFrom = itemView.findViewById(R.id.tvTextTo)
            textTo = itemView.findViewById(R.id.tvTexTo)

            itemView.setOnClickListener { v: View? ->
                onClickListener(v)
            }
        }

        private fun onClickListener(v: View?) {
            listener.onItemSelected(contactListFiltered.get(adapterPosition))
        }
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(charSequence: CharSequence): FilterResults {
                val charString = charSequence.toString()
                contactListFiltered = if (charString.isEmpty()) {
                    contactList
                } else {
                    val filteredList: MutableList<DictionaryItem> = ArrayList()
                    for (row in contactList) {

                        if (row.textFrom.toLowerCase()
                                .contains(charString.toLowerCase()) ||
                            row.textTo.toLowerCase()
                                .contains(charString.toLowerCase())
                        ) {
                            filteredList.add(row)
                        }
                    }
                    filteredList
                }
                val filterResults = FilterResults()
                filterResults.values = contactListFiltered
                return filterResults
            }

            override fun publishResults(
                charSequence: CharSequence,
                filterResults: FilterResults
            ) {
                contactListFiltered = filterResults.values as ArrayList<DictionaryItem>
                notifyDataSetChanged()
            }
        }
    }
}