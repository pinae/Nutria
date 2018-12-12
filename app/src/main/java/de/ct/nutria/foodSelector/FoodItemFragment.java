package de.ct.nutria.foodSelector;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import de.ct.nutria.R;

import java.util.List;

/**
 * A fragment displaying a list of FoodItems.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListSelect}
 * interface.
 */
public class FoodItemFragment extends Fragment implements AdapterView.OnItemClickListener {
    private OnListSelect listSelectListener;

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

        if (getArguments() != null) {
            // Read arguments from Bundle
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_fooditem_list, container, false);
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
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Log.d("Item clicked with no", Integer.toString(i));
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
}
