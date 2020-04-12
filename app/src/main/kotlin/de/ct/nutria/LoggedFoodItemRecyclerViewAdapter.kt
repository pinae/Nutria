package de.ct.nutria

import android.content.res.Resources
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import de.ct.nutria.LoggedFooditemFragment.OnLoggedFoodListInteractionListener
import kotlinx.android.synthetic.main.fragment_logged_fooditem.view.*

class LoggedFoodItemRecyclerViewAdapter(
        private val displayedFood: List<FoodItem>,
        private val loggedFoodListener: OnLoggedFoodListInteractionListener?,
        private val resources: Resources)
    : RecyclerView.Adapter<LoggedFoodItemRecyclerViewAdapter.ViewHolder>() {

    private val onItemClickListener: View.OnClickListener

    init {
        onItemClickListener = View.OnClickListener { v ->
            val item = v.tag as FoodItem
            loggedFoodListener?.onFoodDetilsRequested(item)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.fragment_logged_fooditem, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = displayedFood[position]
        holder.nameView.text = item.name
        holder.manufacturerView.text = item.describeManufacturer()
        holder.caloriesView.text = item.calories.toString()
        holder.iconView.setImageDrawable(resources.getDrawable(getIcon(item.categoryId), null))

        with(holder.v) {
            tag = item
            setOnClickListener(onItemClickListener)
        }
    }

    override fun getItemCount(): Int = displayedFood.size

    inner class ViewHolder(val v: View) : RecyclerView.ViewHolder(v) {
        val nameView: TextView = v.loggedFoodName
        val caloriesView: TextView = v.loggedFoodCalories
        val manufacturerView: TextView = v.loggedFoodManufacturer
        val iconView: ImageView = v.loggedFoodCategoryIcon

        override fun toString(): String {
            return super.toString() + " '" + nameView.text + ": " +
                    caloriesView.text + "kcal – " + manufacturerView.text + "'"
        }
    }
}
