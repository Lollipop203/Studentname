package vn.edu.hust.studentman

import android.app.ActionBar
import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar

class MainActivity : AppCompatActivity() {
  private val students = mutableListOf(
    StudentModel("Hoàng Anh Tú", "SV001"),
    StudentModel("Lê Ngọc Ánh", "SV002"),
    StudentModel("Phạm Minh Hiếu", "SV003"),
    StudentModel("Nguyễn Thùy Trang", "SV004"),
    StudentModel("Trần Đức Thịnh", "SV005"),
    StudentModel("Vũ Khánh Linh", "SV006"),
    StudentModel("Bùi Nhật Huy", "SV007"),
    StudentModel("Đào Thanh Tâm", "SV008"),
    StudentModel("Lý Quốc Hưng", "SV009"),
    StudentModel("Nguyễn Hoài Nam", "SV010"),
    StudentModel("Phạm Hương Giang", "SV011"),
    StudentModel("Hoàng Văn Lợi", "SV012"),
    StudentModel("Trần Thanh Vân", "SV013"),
    StudentModel("Lê Thị Thanh", "SV014"),
    StudentModel("Nguyễn Quang Dũng", "SV015"),
    StudentModel("Đinh Thị Thu", "SV016"),
    StudentModel("Phạm Quốc Khánh", "SV017"),
    StudentModel("Vũ Anh Đào", "SV018"),
    StudentModel("Bùi Minh Nhật", "SV019"),
    StudentModel("Lý Thị Ngọc", "SV020"),
    )

  private lateinit var studentAdapter: StudentAdapter

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)

    studentAdapter = StudentAdapter(students, ::editStudent, ::removeStudent)
    findViewById<RecyclerView>(R.id.recycler_view_students).apply {
      adapter = studentAdapter
      layoutManager = LinearLayoutManager(this@MainActivity)
    }

    // Add new student
    findViewById<Button>(R.id.btn_add_new).setOnClickListener {
      val dialogView = LayoutInflater.from(this@MainActivity)
        .inflate(R.layout.layout_alert_dialog, null)

      val editHoten = dialogView.findViewById<EditText>(R.id.edit_hoten)
      val editMssv = dialogView.findViewById<EditText>(R.id.edit_mssv)

      AlertDialog.Builder(this)
        .setTitle("Enter student information")
        .setView(dialogView)
        .setPositiveButton("OK") { _, _ ->
          val hoten = editHoten.text.toString()
          val mssv = editMssv.text.toString()
          if (hoten.isNotEmpty() && mssv.isNotEmpty()) {
            addStudent(StudentModel(hoten, mssv))
            Log.v("TAG", "$hoten: $mssv")
          }
        }
        .setNegativeButton("Cancel", null)
        .create()
        .show()
    }
  }

  //Edit
  private fun editStudent(position: Int) {
    val currentStudent = students[position]

    val dialog = Dialog(this@MainActivity)
    dialog.setContentView(R.layout.layout_dialog)

    val editHoten = dialog.findViewById<EditText>(R.id.edit_hoten)
    val editMssv = dialog.findViewById<EditText>(R.id.edit_mssv)
    editHoten.setText(currentStudent.studentName)
    editMssv.setText(currentStudent.studentId)

    dialog.findViewById<Button>(R.id.button_ok).setOnClickListener {
      val hoten = editHoten.text.toString()
      val mssv = editMssv.text.toString()

      if (hoten.isNotEmpty() && mssv.isNotEmpty()) {
        students[position] = StudentModel(hoten, mssv)
        studentAdapter.notifyItemChanged(position)
      }
      Log.v("TAG", "$hoten: $mssv")
      dialog.dismiss()
    }

    dialog.findViewById<Button>(R.id.button_cancel).setOnClickListener {
      dialog.dismiss()
    }

    dialog.window?.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT)
    dialog.show()
  }

  //Delete
  private fun removeStudent(position: Int) {
    val currentStudent = students[position]

    AlertDialog.Builder(this)
      .setTitle("Do you really want to delete student ${currentStudent.studentName}?")
      .setPositiveButton("Yes") { _, _ ->
        students.removeAt(position)
        studentAdapter.notifyItemRemoved(position)

        val view = findViewById<View>(R.id.recycler_view_students)
        Snackbar.make(view, "Deleted ${currentStudent.studentName}", Snackbar.LENGTH_LONG)
          .setAction("Undo") {
               students.add(position, currentStudent)
            studentAdapter.notifyItemInserted(position)
          }
          .show()
      }
      .setNegativeButton("No", null)
      .create()
      .show()
  }

  private fun addStudent(student: StudentModel) {
    students.add(student)
    studentAdapter.notifyItemInserted(students.size - 1)
  }
}