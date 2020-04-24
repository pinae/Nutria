package de.ct.nutria

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_preferences.*

/**
 * Fragment with preferences
 */
class PreferencesFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_preferences, container, false)
    }

    override fun onStart() {
        super.onStart()
        deleteDatabaseButton.setOnClickListener {
            context?.deleteDatabase("cached-food-database")
            view?.let{
                Snackbar.make(it, getText(R.string.database_deletion_finished), Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()
            }
        }
        editTextQueryCacheSize.setText(prefs.sizeQueryCache.toString())
        editTextQueryCacheSize.addTextChangedListener {
            val cacheSizeText = it.toString()
            try {
                if (cacheSizeText.toInt() > 0) {
                    prefs.sizeQueryCache = cacheSizeText.toInt()
                }
            } catch (ex: NumberFormatException) {
                prefs.sizeQueryCache = 5000
            }

        }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment.
         *
         * @return A new instance of fragment PreferencesFragment.
         */
        @JvmStatic
        fun newInstance() =
                PreferencesFragment().apply {
                    arguments = Bundle()
                }
    }
}
