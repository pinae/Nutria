package de.ct.nutria.foodSelector;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import de.ct.nutria.R;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * A fragment displaying a list of FoodItems.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListSelect}
 * interface.
 */
public class FoodItemFragment extends Fragment implements AdapterView.OnItemClickListener {
    private String PARCELABLE_FOOD_LIST = "de.ct.nutria.foodSelector.FoodItemFragment.foodList";
    private OnListSelect listSelectListener;
    private ArrayList<FoodItem> foodList;
    private ListView listView;
    private TextView emptyView;
    private EditText searchEntry;
    private ImageButton searchButton;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public FoodItemFragment() {
    }

    public static FoodItemFragment newInstance() {
        FoodItemFragment fragment = new FoodItemFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        foodList = new ArrayList<>();
        if (getArguments() != null) {
            // Read arguments from Bundle
            foodList = savedInstanceState.getParcelableArrayList(PARCELABLE_FOOD_LIST);
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_fooditem_list, container, false);
        listView = view.findViewById(R.id.foodList);
        listView.setOnItemClickListener(this);
        emptyView = view.findViewById(R.id.foodListEmpty);
        searchEntry = view.findViewById(R.id.searchEntry);
        searchEntry.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                requestList(s);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        searchButton = view.findViewById(R.id.searchButton);
        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnListSelect) {
            listSelectListener = (OnListSelect) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listSelectListener = null;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putParcelableArrayList(PARCELABLE_FOOD_LIST, foodList);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Log.d("Item clicked with no", Integer.toString(i));
        this.listSelectListener.onListSelect(foodList.get(i));
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     */
    public interface OnListSelect {
        void onListSelect(FoodItem item);
    }

    private void requestList(CharSequence query) {
        FoodItem test = new FoodItem("Foo");
        test.setType(1);
        test.setAuthorName("Maria Musterfrau");
        test.setCategory(1, "test");
        test.setCalories(423);
        Log.d(test.getName(), test.getCaloriesString());
        Log.d("Manufacturer", test.describeManufacturer("a ", "b", "c"));
        for(int i=0; i < foodList.size(); i++) {
            if(!foodList.get(i).getName().contains(query)) {
                foodList.remove(i);
                i--;
            }
        }
        requestQueryFromNutriaDb(query);
        updateList();
    }

    private void updateList() {
        ArrayList<HashMap<String,String>> displayFoodList=new ArrayList<>();
        for (FoodItem item : foodList) {
            HashMap<String, String> entry = new HashMap<>();
            entry.put("foodName", item.getName());
            entry.put("manufacturer", item.describeManufacturer(
                    getString(R.string.recipe_by),
                    getString(R.string.selfmade),
                    getString(R.string.harvested)));
            entry.put("calories", item.getCaloriesString());
            entry.put("iconID", getIcon(item.getCategoryId())+"");
            displayFoodList.add(entry);
        }
        String[] fromArray = {"foodName", "manufacturer",
                              "calories", "iconID"};
        int[] viewIndexes = {R.id.listFoodName, R.id.listManufacturer,
                             R.id.listCalories, R.id.listCategoryIcon};
        SimpleAdapter adapter = new SimpleAdapter(getActivity(), displayFoodList,
                R.layout.fragment_fooditem, fromArray, viewIndexes);
        listView.setAdapter(adapter);
        listView.setVisibility((displayFoodList.size() > 0) ? View.VISIBLE : View.INVISIBLE);
        emptyView.setVisibility((displayFoodList.size() <= 0) ? View.VISIBLE : View.INVISIBLE);
    }

    private int getIcon(int categoryId) {
        if (categoryId == 1) return R.drawable.ic_category_1;
        if (categoryId == 2) return R.drawable.ic_category_2;
        if (categoryId == 3) return R.drawable.ic_category_3;
        if (categoryId == 6) return R.drawable.ic_category_6;
        return R.drawable.ic_tupperdose;
    }

    private void requestQueryFromNutriaDb(CharSequence query) {
        createDummyList();
    }

    private void createDummyList() {
        foodList.clear();
        FoodItem i1 = new FoodItem("Banane");
        i1.setType(0);
        i1.setCategory(1, "Obst");
        i1.setCalories(123);
        i1.setAuthorName("Max Mustermann");
        foodList.add(i1);
        FoodItem i2 = new FoodItem("Apfel");
        i2.setType(0);
        i2.setCategory(6, "Obst");
        i2.setCalories(82);
        i2.setAuthorName("Max Mustermann");
        foodList.add(i2);
        FoodItem i3 = new FoodItem("Ritter Sport - Honig-Salz-Mandel");
        i3.setType(0);
        i3.setManufacturer("Ritter Sport");
        i3.setCategory(2, "Schokolade");
        i3.setCalories(564);
        i3.setAuthorName("Andreas Adipositas");
        foodList.add(i3);
        FoodItem i4 = new FoodItem("Salz");
        i4.setType(0);
        i4.setManufacturer("Bad Reichenhaller");
        i4.setCategory(3, "Gewürz");
        i4.setCalories(1);
        i4.setAuthorName("Andreas Adipositas");
        foodList.add(i4);
    }
}
