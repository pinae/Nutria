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
    private var repository: FoodItemRepository? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //if (activity != null) {
            //val foundFoodModel = ViewModelProviders.of(activity!!).get(FoundFoodViewModel::class.java)
            //foundFoodModel.getFood().observe(activity!!, Observer<List<FoodItem>> { foodItems ->
                // update UI
            //})
        //}
        repository = FoodItemRepository(this)
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
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                requestList(s)
            }

            override fun afterTextChanged(s: Editable) {

            }
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
        val test = FoodItem("Foo")
        test.type = 1
        test.authorName = "Maria Musterfrau"
        test.setCategory(1, "test")
        test.calories = 423.0f
        Log.d(test.name, test.caloriesString)
        Log.d("Manufacturer", test.describeManufacturer())
        repository!!.query(query.toString())
        //createDummyList()
        updateList()
    }

    private fun updateList() {
        val manSt = ManufacturerDescriptionStrings(
                recipeBy = getString(R.string.recipe_by),
                selfmade = getString(R.string.selfmade),
                harvested = getString(R.string.harvested))
        val displayFoodList = ArrayList<HashMap<String, String>>()
        if (repository != null) for (item in repository!!.foodArray) {
            item.manSt = manSt
            val entry = HashMap<String, String>()
            entry["foodName"] = item.name
            entry["manufacturer"] = item.describeManufacturer()
            entry["calories"] = item.caloriesString
            entry["reference_amount"] = item.referenceAmountString
            entry["iconID"] = getIcon(item.categoryId).toString() + ""
            displayFoodList.add(entry)
        }
        val fromArray = arrayOf("foodName", "manufacturer", "calories", "reference_amount", "iconID")
        val viewIndexes = intArrayOf(R.id.listFoodName, R.id.listManufacturer,
                R.id.listCalories, R.id.listReferenceAmount, R.id.listCategoryIcon)
        val adapter = SimpleAdapter(activity, displayFoodList,
                R.layout.fragment_fooditem, fromArray, viewIndexes)
        foodList.adapter = adapter
        foodList.visibility = if (displayFoodList.size > 0) View.VISIBLE else View.INVISIBLE
        foodListEmpty.visibility = if (displayFoodList.size <= 0) View.VISIBLE else View.INVISIBLE
    }

    override fun onFoodItemRepositoryUpdate() {
        updateList()
        // Update Spinner
    }

    private fun createDummyList() {
        repository!!.foodArray.clear()
        val i1 = FoodItem("Banane")
        i1.type = 0
        i1.setCategory(1, "Obst")
        i1.calories = 123f
        i1.authorName = "Max Mustermann"
        repository!!.foodArray.add(i1)
        val i2 = FoodItem("Apfel")
        i2.type = 0
        i2.setCategory(6, "Obst")
        i2.calories = 82f
        i2.authorName = "Max Mustermann"
        repository!!.foodArray.add(i2)
        val i3 = FoodItem("Ritter Sport - Honig-Salz-Mandel")
        i3.type = 0
        i3.manufacturer = "Ritter Sport"
        i3.setCategory(2, "Schokolade")
        i3.calories = 564f
        i3.authorName = "Andreas Adipositas"
        repository!!.foodArray.add(i3)
        val i4 = FoodItem("Salz")
        i4.type = 0
        i4.manufacturer = "Bad Reichenhaller"
        i4.setCategory(3, "Gewürz")
        i4.calories = 1f
        i4.authorName = "Andreas Adipositas"
        repository!!.foodArray.add(i4)
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
