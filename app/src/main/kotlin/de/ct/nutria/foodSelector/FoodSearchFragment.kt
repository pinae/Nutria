package de.ct.nutria.foodSelector

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.EditText
import android.widget.ListView
import android.widget.SimpleAdapter
import androidx.fragment.app.Fragment
import de.ct.nutria.*

import kotlinx.android.synthetic.main.fragment_fooditem_query.*

import java.util.ArrayList
import java.util.HashMap

/**
 * A fragment displaying a list of FoodItems.
 *
 *
 * Activities containing this fragment MUST implement the [OnListSelect]
 * interface.
 */
/**
 * Mandatory empty constructor for the fragment manager to instantiate the
 * fragment (e.g. upon screen orientation changes).
 */
class FoodSearchFragment : Fragment(), AdapterView.OnItemClickListener, FoodItemRepositoryListener {
    private val parcelableFoodList = "de.ct.nutria.foodSelector.FoodSearchFragment.foodArray"
    private var listSelectListener: OnListSelect? = null
    private var repository: QueryFoodItemRepository? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //if (activity != null) {
            //val foundFoodModel = ViewModelProviders.of(activity!!).get(FoundFoodViewModel::class.java)
            //foundFoodModel.getFood().observe(activity!!, Observer<List<FoodItem>> { foodItems ->
                // update UI
            //})
        //}
        repository = QueryFoodItemRepository(this)
        repository?.let{ it.manSt = ManufacturerDescriptionStrings(
                recipeBy = getString(R.string.recipe_by),
                selfmade = getString(R.string.selfmade),
                harvested = getString(R.string.harvested)) }
        if (arguments != null && savedInstanceState != null) {
            // Read arguments from Bundle
            val savedFoodArray: ArrayList<FoodItem>? = savedInstanceState.getParcelableArrayList(
                    parcelableFoodList)
            if (savedFoodArray != null) repository!!.foodArray = savedFoodArray
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_fooditem_query, container, false)
        view.findViewById<ListView>(R.id.foodList)?.onItemClickListener = this
        view.findViewById<EditText>(R.id.searchEntry)?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                requestList(s)
            }

            override fun afterTextChanged(s: Editable) {}
        })
        return view
    }


    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnListSelect) {
            listSelectListener = context
        } else {
            throw RuntimeException("$context must implement OnListFragmentInteractionListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        listSelectListener = null
    }

    override fun onSaveInstanceState(savedInstanceState: Bundle) {
        super.onSaveInstanceState(savedInstanceState)
        if (repository != null) savedInstanceState.putParcelableArrayList(
                parcelableFoodList, repository!!.foodArray)
    }

    override fun onItemClick(adapterView: AdapterView<*>, view: View, i: Int, l: Long) {
        Log.d("Item clicked with no", i.toString())
        if (this.listSelectListener != null && repository != null)
            this.listSelectListener!!.onListSelect(repository!!.foodArray[i])
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     */
    interface OnListSelect {
        fun onListSelect(item: FoodItem)
    }

    private fun requestList(query: CharSequence) {
        repository?.let { it.query(query.toString()) }
        updateList()
    }

    private fun updateList() {
        val manSt = ManufacturerDescriptionStrings(
                recipeBy = getString(R.string.recipe_by),
                selfmade = getString(R.string.selfmade),
                harvested = getString(R.string.harvested))
        val displayFoodList = ArrayList<HashMap<String, String>>()
        repository?.let {
            for (item in it.foodArray) {
                item.manSt = manSt
                val entry = HashMap<String, String>()
                entry["foodName"] = item.name
                entry["manufacturer"] = item.describeManufacturer()
                entry["calories"] = item.caloriesString
                entry["reference_amount"] = item.referenceAmountString
                entry["iconID"] = "%d".format(getIcon(item.categoryId))
                displayFoodList.add(entry)
            }
        }
        val fromArray = arrayOf("foodName", "manufacturer", "calories", "reference_amount", "iconID")
        val viewIndexes = intArrayOf(R.id.listFoodName, R.id.listManufacturer,
                R.id.listCalories, R.id.listReferenceAmount, R.id.listCategoryIcon)
        val adapter = SimpleAdapter(activity, displayFoodList,
                R.layout.fragment_fooditem, fromArray, viewIndexes)
        foodList.adapter = adapter
        foodList.visibility = if (displayFoodList.size > 0) View.VISIBLE else View.INVISIBLE
        foodListEmpty.visibility = if (displayFoodList.size <= 0) View.VISIBLE else View.GONE
    }

    override fun onFoodItemRepositoryUpdate() {
        updateList()
        // Update Spinner
    }

    companion object {

        fun newInstance(): FoodSearchFragment {
            val fragment = FoodSearchFragment()
            val args = Bundle()
            fragment.arguments = args
            return fragment
        }
    }
}
