import android.content.ContentValues
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AlertDialog
import com.example.fitness_app.Data.SQLiteHelper
import com.example.fitness_app.adpters.MealModel
import com.example.fitness_app.R

class MealAdapter(
    private val context: Context,
    private val meals: MutableList<MealModel>,
    private val dbHelper: SQLiteHelper
) : BaseAdapter() {

    override fun getCount(): Int = meals.size

    override fun getItem(position: Int): Any = meals[position]

    override fun getItemId(position: Int): Long = position.toLong()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val meal = meals[position]
        val view = convertView ?: LayoutInflater.from(context).inflate(R.layout.meal_item, parent, false)

        val mealName = view.findViewById<TextView>(R.id.mealName)
        val mealDetails = view.findViewById<TextView>(R.id.mealDetails)
        val btnDelete = view.findViewById<Button>(R.id.btnDeleteMeal)
        val btnEdit = view.findViewById<Button>(R.id.btnEditMeal)

        mealName.text = meal.name
        mealDetails.text = "${meal.calories} كالوري | ${meal.protein}g بروتين | ${meal.fat}g دهون | ${meal.carbs}g كربوهيدرات"

        // ✅ حذف الوجبة
        btnDelete.setOnClickListener {
            AlertDialog.Builder(context)
                .setTitle("تأكيد الحذف")
                .setMessage("هل تريد حذف هذه الوجبة؟")
                .setPositiveButton("نعم") { _, _ ->
                    deleteMeal(meal, position)
                }
                .setNegativeButton("إلغاء", null)
                .show()
        }

        // ✅ تعديل الوجبة
        btnEdit.setOnClickListener {
            showEditDialog(meal, position)
        }

        return view
    }

    private fun deleteMeal(meal: MealModel, position: Int) {
        val db = dbHelper.writableDatabase
        val result = db.delete(
            "meals",
            "name = ? AND calories = ? AND date = ?",
            arrayOf(meal.name, meal.calories.toString(), meal.date)
        )
        db.close()

        if (result > 0) {
            meals.removeAt(position)
            notifyDataSetChanged()
            Toast.makeText(context, "✅ تم الحذف", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(context, "❌ فشل في الحذف", Toast.LENGTH_SHORT).show()
        }
    }

    private fun showEditDialog(meal: MealModel, position: Int) {
        val dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_edit_meal, null)
        val dialog = AlertDialog.Builder(context).create()
        dialog.setView(dialogView)

        val etName = dialogView.findViewById<EditText>(R.id.etEditName)
        val etCalories = dialogView.findViewById<EditText>(R.id.etEditCalories)
        val etProtein = dialogView.findViewById<EditText>(R.id.etEditProtein)
        val etFat = dialogView.findViewById<EditText>(R.id.etEditFat)
        val etCarbs = dialogView.findViewById<EditText>(R.id.etEditCarbs)

        val btnSave = dialogView.findViewById<Button>(R.id.btnSaveEdit)
        val btnCancel = dialogView.findViewById<Button>(R.id.btnCancelEdit)

        // تعبئة البيانات الحالية
        etName.setText(meal.name)
        etCalories.setText(meal.calories.toString())
        etProtein.setText(meal.protein.toString())
        etFat.setText(meal.fat.toString())
        etCarbs.setText(meal.carbs.toString())

        btnCancel.setOnClickListener {
            dialog.dismiss()
        }

        btnSave.setOnClickListener {
            btnSave.text = "⏳ جارٍ الحفظ..."
            btnSave.isEnabled = false
            btnCancel.isEnabled = false

            val newName = etName.text.toString().trim()
            val newCalories = etCalories.text.toString().toIntOrNull() ?: 0
            val newProtein = etProtein.text.toString().toDoubleOrNull() ?: 0.0
            val newFat = etFat.text.toString().toDoubleOrNull() ?: 0.0
            val newCarbs = etCarbs.text.toString().toDoubleOrNull() ?: 0.0

            val db = dbHelper.writableDatabase
            val values = ContentValues().apply {
                put("name", newName)
                put("calories", newCalories)
                put("protein", newProtein)
                put("fat", newFat)
                put("carbs", newCarbs)
            }

            val result = db.update(
                "meals", values,
                "name = ? AND date = ?",
                arrayOf(meal.name, meal.date)
            )
            db.close()

            if (result > 0) {
                meals[position] = meal.copy(
                    name = newName,
                    calories = newCalories,
                    protein = newProtein,
                    fat = newFat,
                    carbs = newCarbs
                )
                notifyDataSetChanged()
                Toast.makeText(context, "✅ تم تحديث الوجبة", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(context, "❌ فشل في التحديث", Toast.LENGTH_SHORT).show()
            }

            dialog.dismiss()
        }


        dialog.show()
    }
}
