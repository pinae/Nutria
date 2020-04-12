package de.ct.nutria

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import de.ct.nutria.dummy.DummyContent
import kotlinx.android.synthetic.main.fragment_logged_fooditem_list.*
import java.time.OffsetDateTime
import java.util.ArrayList

/**
 * A fragment representing a list of Items.
 * Activities containing this fragment MUST implement the
 * [LoggedFooditemFragment.OnLoggedFoodListInteractionListener] interface.
 */
class LoggedFooditemFragment : Fragment(), LoggedFoodRepositoryListener {
    private val repository = LoggedFoodRepository(this)
    private val parcelableFoodList = "de.ct.nutria.LoggedFooditemFragment.foodArray"
    private var loggedFoodListInteractionListener: OnLoggedFoodListInteractionListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (savedInstanceState != null) {
            // Read arguments from Bundle
            val savedFoodArray: ArrayList<FoodItem>? = savedInstanceState.getParcelableArrayList(
                    parcelableFoodList)
            if (savedFoodArray != null) repository.foodArray = savedFoodArray
        }
        Log.i("onCreate", "starting to load logged food.")
        repository.loadLoggedFoodForDay(OffsetDateTime.now())
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_logged_fooditem_list, container, false)

        // Set the adapter
        if (view is RecyclerView) {
            with(view) {
                layoutManager = LinearLayoutManager(context)
                adapter = LoggedFoodItemRecyclerViewAdapter(repository.foodArray, loggedFoodListInteractionListener, resources)
            }
        }
        return view
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnLoggedFoodListInteractionListener) {
            loggedFoodListInteractionListener = context
        } else {
            throw RuntimeException(context.toString() +
                    " must implement OnListFragmentInteractionListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        loggedFoodListInteractionListener = null
    }

    override fun onSaveInstanceState(savedInstanceState: Bundle) {
        super.onSaveInstanceState(savedInstanceState)
        savedInstanceState.putParcelableArrayList(
                parcelableFoodList, repository.foodArray)
    }

    override fun onFoodUpdate() {
        Log.i("food Update", repository.foodArray.toString())
        foodItemList.adapter?.notifyDataSetChanged()
        Log.i("cnt", foodItemList.adapter?.itemCount.toString())
    }

    fun populateFoodArrayInRepository(food: FoodItem) {
        if (repository.foodArray.isEmpty()) repository.foodArray.add(food)
    }

    interface OnLoggedFoodListInteractionListener {
        fun onFoodDetilsRequested(food: FoodItem)
    }

    companion object {
        @JvmStatic
        fun newInstance() = LoggedFooditemFragment()
    }
}
