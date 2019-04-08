package foodSelector

import android.app.Fragment
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
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

import de.ct.nutria.R
import de.ct.nutria.foodSelector.NutriaRequestCallback
import kotlinx.android.synthetic.main.fragment_fooditem_list.*

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
class FoodItemFragment : Fragment(), AdapterView.OnItemClickListener, NutriaRequestCallback<String> {
    private val PARCELABLE_FOOD_LIST = "foodSelector.FoodItemFragment.foodArray"
    private var listSelectListener: OnListSelect? = null
    private var foodArray: ArrayList<FoodItem>? = null
    //private var nutriaNetworkFragment: NutriaHeadlessNetworkFragment? = null
    private var nutriaRequestOngoing = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        foodArray = ArrayList()
        if (arguments != null && savedInstanceState != null) {
            // Read arguments from Bundle
            foodArray = savedInstanceState!!.getParcelableArrayList(PARCELABLE_FOOD_LIST)
        }
        //nutriaNetworkFragment = NutriaHeadlessNetworkFragment.getInstance(
        //        activity.fragmentManager)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_fooditem_list, container, false)
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
        savedInstanceState.putParcelableArrayList(PARCELABLE_FOOD_LIST, foodArray)
    }

    override fun onItemClick(adapterView: AdapterView<*>, view: View, i: Int, l: Long) {
        Log.d("Item clicked with no", Integer.toString(i))
        if (this.listSelectListener != null && foodArray != null)
            this.listSelectListener!!.onListSelect(foodArray!![i])
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
        Log.d("Manufacturer", test.describeManufacturer("a ", "b", "c"))
        var i = 0
        if (foodArray != null) {
            while (i < foodArray!!.size) {
                if (!foodArray!![i].name.contains(query)) {
                    foodArray!!.removeAt(i)
                    i--
                }
                i++
            }
        }
        requestQueryFromNutriaDb(query)
        updateList()
    }

    private fun updateList() {
        val displayFoodList = ArrayList<HashMap<String, String>>()
        if (foodArray != null) for (item in foodArray!!) {
            val entry = HashMap<String, String>()
            entry["foodName"] = item.name
            entry["manufacturer"] = item.describeManufacturer(
                    getString(R.string.recipe_by),
                    getString(R.string.selfmade),
                    getString(R.string.harvested))
            entry["calories"] = item.caloriesString
            entry["iconID"] = getIcon(item.categoryId).toString() + ""
            displayFoodList.add(entry)
        }
        val fromArray = arrayOf("foodName", "manufacturer", "calories", "iconID")
        val viewIndexes = intArrayOf(R.id.listFoodName, R.id.listManufacturer, R.id.listCalories, R.id.listCategoryIcon)
        val adapter = SimpleAdapter(activity, displayFoodList,
                R.layout.fragment_fooditem, fromArray, viewIndexes)
        foodList.adapter = adapter
        foodList.visibility = if (displayFoodList.size > 0) View.VISIBLE else View.INVISIBLE
        foodListEmpty.visibility = if (displayFoodList.size <= 0) View.VISIBLE else View.INVISIBLE
    }

    private fun getIcon(categoryId: Int): Int {
        if (categoryId == 1) return R.drawable.ic_category_1
        if (categoryId == 2) return R.drawable.ic_category_2
        if (categoryId == 3) return R.drawable.ic_category_3
        return if (categoryId == 6) R.drawable.ic_category_6 else R.drawable.ic_tupperdose
    }

    private fun requestQueryFromNutriaDb(query: CharSequence) {
        /*if (!this.nutriaRequestOngoing && this.nutriaNetworkFragment != null) {
            try {
                this.nutriaNetworkFragment.doQuery(query);
                this.nutriaRequestOngoing = true;
            } catch (JSONException e) {
                Log.e("JSON error in payload", e.getMessage());
            }
        }*/ //TODO: Decide what to do if a request is already running.
        createDummyList()
    }

    override fun updateFromRequest(result: String) {

    }

    override fun getActiveNetworkInfo(): NetworkInfo {
        val connectivityManager = activity.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        return connectivityManager.activeNetworkInfo
    }

    override fun onProgressUpdate(progressCode: Int) {
        when (progressCode) {
            // You can add UI behavior for progress updates here.
            NutriaRequestCallback.Progress.ERROR -> {
            }
            NutriaRequestCallback.Progress.CONNECT_SUCCESS -> {
            }
            NutriaRequestCallback.Progress.GET_INPUT_STREAM_SUCCESS -> {
            }
            NutriaRequestCallback.Progress.PROCESS_INPUT_STREAM_IN_PROGRESS -> {
            }
            NutriaRequestCallback.Progress.PROCESS_INPUT_STREAM_SUCCESS -> {
            }
        }
    }

    override fun finishRequest() {
        this.nutriaRequestOngoing = false
        /*if (this.nutriaNetworkFragment != null) {
            this.nutriaNetworkFragment!!.cancelRequest()
        }*/
    }

    private fun createDummyList() {
        if (foodArray == null) foodArray = ArrayList()
        foodArray!!.clear()
        val i1 = FoodItem("Banane")
        i1.type = 0
        i1.setCategory(1, "Obst")
        i1.calories = 123f
        i1.authorName = "Max Mustermann"
        foodArray!!.add(i1)
        val i2 = FoodItem("Apfel")
        i2.type = 0
        i2.setCategory(6, "Obst")
        i2.calories = 82f
        i2.authorName = "Max Mustermann"
        foodArray!!.add(i2)
        val i3 = FoodItem("Ritter Sport - Honig-Salz-Mandel")
        i3.type = 0
        i3.manufacturer = "Ritter Sport"
        i3.setCategory(2, "Schokolade")
        i3.calories = 564f
        i3.authorName = "Andreas Adipositas"
        foodArray!!.add(i3)
        val i4 = FoodItem("Salz")
        i4.type = 0
        i4.manufacturer = "Bad Reichenhaller"
        i4.setCategory(3, "Gewürz")
        i4.calories = 1f
        i4.authorName = "Andreas Adipositas"
        foodArray!!.add(i4)
    }

    companion object {

        fun newInstance(): FoodItemFragment {
            val fragment = FoodItemFragment()
            val args = Bundle()
            fragment.arguments = args
            return fragment
        }
    }
}
