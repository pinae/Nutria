package de.ct.nutria

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener

import kotlinx.android.synthetic.main.activity_food_details.*
import kotlinx.android.synthetic.main.activity_food_details.toolbar
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter

class FoodDetailsActivity : AppCompatActivity(), LoggedFoodRepositoryListener {
    private val repository = LoggedFoodRepository(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_food_details)
        setSupportActionBar(toolbar)
        supportActionBar?.let{
            it.setDisplayHomeAsUpEnabled(true)
            it.setDisplayShowHomeEnabled(true)
        }
        toolbar.setNavigationOnClickListener { finish() }
        if (intent.hasExtra("foodItem"))
            intent.getParcelableExtra<FoodItem>("foodItem")?.let {
                repository.clear()
                repository.replaceOrAppend(it)
                repository.updateDetailsForAllFoods()
            }
        textInputAmount.addTextChangedListener {
            fab.visibility = if (it.isNullOrBlank()) View.GONE else View.VISIBLE
        }
        fab.setOnClickListener {
            view -> if (repository.foodArray.isNotEmpty()) {
                Log.i("save button", textInputAmount.text.toString().toFloat().toString())
                val selectedFood = repository.foodArray[0].copyToScaled(
                        textInputAmount.text.toString().toFloat())
                if (selectedFood.lastLogged == null)
                    selectedFood.lastLogged = OffsetDateTime.now()
                Log.i("before save or update", selectedFood.roomId.toString())
                if (selectedFood.roomId != null && selectedFood.roomId!! < 0)
                    selectedFood.roomId = null
                if (selectedFood.roomId == null) {
                    repository.saveToRoom(selectedFood)
                } else {
                    repository.updateInRoom(selectedFood)
                }
                val intent = Intent(this, MainActivity::class.java)
                intent.putExtra("foodItem", selectedFood)
                startActivity(intent)
            }
        }
    }

    fun amountButtonClicked(button: View) {
        when (button) {
            button5g -> textInputAmount.setText("5")
            button10g -> textInputAmount.setText("10")
            button25g -> textInputAmount.setText("25")
            button50g -> textInputAmount.setText("50")
            button75g -> textInputAmount.setText("75")
            button100g -> textInputAmount.setText("100")
            button200g -> textInputAmount.setText("200")
            button500g -> textInputAmount.setText("500")
        }
    }

    private fun updateTextView(value: Float?, textView: TextView, container: View, unit: String = "") {
        if (value?.isNaN() == false) {
            textView.text = getString(R.string.nutrition_display_float).format(value, unit)
            container.visibility = View.VISIBLE
        } else {
            container.visibility = View.GONE
        }
    }

    private fun updateTextView(value: String?, textView: TextView, container: View, unit: String = "") {
        if (!value.isNullOrBlank()) {
            textView.text = getString(R.string.nutrition_display_string).format(value, unit)
            container.visibility = View.VISIBLE
        } else {
            container.visibility = View.GONE
        }
    }

    private fun updateTextView(value: Long, textView: TextView, container: View, unit: String = "") {
        if (value > 0) {
            textView.text = getString(R.string.nutrition_display_ulong).format(value, unit)
            container.visibility = View.VISIBLE
        } else {
            container.visibility = View.GONE
        }
    }

    private fun updateTextView(value: OffsetDateTime?, textView: TextView, container: View,
                               unit: String = "") {
        if (value != null) {
            textView.text = getString(R.string.nutrition_display_string).format(
                    value.format(DateTimeFormatter.ofPattern(
                            getString(R.string.nutrition_display_date))),
                    unit)
            container.visibility = View.VISIBLE
        } else {
            container.visibility = View.GONE
        }
    }

    override fun onFoodUpdate() {
        if (repository.foodArray.isEmpty()) return
        val food = repository.foodArray[0]
        textViewName.text = food.name
        updateTextView(food.manufacturer, textViewManufacturer, textViewManufacturer)
        updateTextView(food.author, textViewAuthorName, authorBlock)
        updateTextView(food.ean, textViewEan, eanLine)
        updateTextView(food.date, textViewDate, textViewDate)
        updateTextView(food.reference_amount, textViewReferenceAmount, textViewReferenceAmount)
        updateTextView(food.calories, textViewCalories, caloriesLine,
                getString(R.string.unit_kcal))
        updateTextView(food.total_fat, textViewTotalFat, totalFatLine,
                getString(R.string.unit_g))
        updateTextView(food.saturated_fat, textViewSaturatedFat, saturatedFatLine,
                getString(R.string.unit_g))
        updateTextView(food.cholesterol, textViewCholesterol, cholesterolLine,
                getString(R.string.unit_mg))
        updateTextView(food.protein, textViewProtein, proteinLine,
                getString(R.string.unit_g))
        updateTextView(food.total_carbs, textViewTotalCarbs, totalCarbsLine,
                getString(R.string.unit_g))
        updateTextView(food.sugar, textViewSugar, sugarLine,
                getString(R.string.unit_g))
        updateTextView(food.dietary_fiber, textViewDietaryFiber, dietaryFiberLine,
                getString(R.string.unit_g))
        updateTextView(food.salt, textViewSalt, saltLine,
                getString(R.string.unit_g))
        updateTextView(food.sodium, textViewSodium, sodiumLine,
                getString(R.string.unit_mg))
        updateTextView(food.potassium, textViewPotassium, potassiumLine,
                getString(R.string.unit_mg))
        updateTextView(food.copper, textViewCopper, copperLine,
                getString(R.string.unit_mg))
        updateTextView(food.iron, textViewIron, ironLine,
                getString(R.string.unit_mg))
        updateTextView(food.magnesium, textViewMagnesium, magnesiumLine,
                getString(R.string.unit_mg))
        updateTextView(food.manganese, textViewManganese, manganeseLine,
                getString(R.string.unit_mg))
        updateTextView(food.zinc, textViewZinc, zincLine,
                getString(R.string.unit_mg))
        updateTextView(food.phosphorous, textViewPhosphorous, phosphorousLine,
                getString(R.string.unit_mg))
        updateTextView(food.sulphur, textViewSulphur, sulphurLine,
                getString(R.string.unit_mg))
        updateTextView(food.chloro, textViewChloro, chloroLine,
                getString(R.string.unit_mg))
        updateTextView(food.fluoric, textViewFluoric, fluoricLine,
                getString(R.string.unit_mg))
        updateTextView(food.vitamin_b1, textViewVitaminB1, vitaminB1Line,
                getString(R.string.unit_mg))
        updateTextView(food.vitamin_b12, textViewVitaminB12, vitaminB12Line,
                getString(R.string.unit_mg))
        updateTextView(food.vitamin_b6, textViewVitaminB6, vitaminB6Line,
                getString(R.string.unit_mg))
        updateTextView(food.vitamin_c, textViewVitaminC, vitaminCLine,
                getString(R.string.unit_mg))
        updateTextView(food.vitamin_d, textViewVitaminD, vitaminDLine,
                getString(R.string.unit_mg))
        updateTextView(food.vitamin_e, textViewVitaminE, vitaminELine,
                getString(R.string.unit_mg))
    }
}
