package com.example.guru26


import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.UploadTask
import java.text.SimpleDateFormat
import java.util.*
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView

class AddPhotoActivity : AppCompatActivity() {
    private val PICK_IMAGE_FROM_ALBUM = 0
    private var storage: FirebaseStorage? = null
    private var photoUri: Uri? = null
    private var auth: FirebaseAuth? = null
    private var firestore: FirebaseFirestore? = null
    private lateinit var button: Button
    private lateinit var imageView: ImageView
    private lateinit var editText01: EditText
    private lateinit var editText02: EditText
    private lateinit var editText03: EditText
    private lateinit var editText04: EditText
    private lateinit var editText05: EditText
    private lateinit var editText06: EditText
    private lateinit var editText07: EditText
    private var likeCount: Int = 0
    private var isLiked: Boolean = false


    private fun generateRandomId(): String {
        val random = Random()
        val sb = StringBuilder(16)
        repeat(16) {
            val num = random.nextInt(10)
            sb.append(num)
        }
        return sb.toString()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_photo)

        //Initiate
        storage = FirebaseStorage.getInstance()
        auth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()

        //Open the album
        val photoPickerIntent = Intent(Intent.ACTION_PICK)
        photoPickerIntent.type = "image/*"
        startActivityForResult(photoPickerIntent, PICK_IMAGE_FROM_ALBUM)

        //add image upload event
        button = findViewById(R.id.add_photo_btn)
        button.setOnClickListener {
            contentUpload()
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_FROM_ALBUM && resultCode == RESULT_OK) {
            //This is path to the selected image
            photoUri = data?.data
            imageView = findViewById(R.id.imageView5)
            imageView.setImageURI(photoUri)
        } else {
            //Exit the addPhotoActivity if you leave the album without selecting it
            finish()
        }
    }

    private fun contentUpload() {
        //Make filename
        val timestamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        val imageFileName = "IMAGE_"+timestamp+"_png"

        val storageRef = storage?.reference?.child("images")?.child(imageFileName)

        storageRef?.putFile(photoUri!!)?.continueWithTask { task: Task<UploadTask.TaskSnapshot> ->
            return@continueWithTask storageRef.downloadUrl
        }?.addOnSuccessListener { uri ->
            val contentDTO = ContentDTO()

            contentDTO.imageUrl = uri.toString()
            contentDTO.uid = auth?.currentUser?.uid
            contentDTO.usrId = auth?.currentUser?.email

            editText01 = findViewById(R.id.explain01)
            contentDTO.explain = editText01.text.toString()

            editText02 = findViewById(R.id.editTextText5)
            contentDTO.exhStartDay = editText02.text.toString()

            editText03 = findViewById(R.id.editTextText6)
            contentDTO.exhEndDay = editText03.text.toString()

            editText04 = findViewById(R.id.editTextText3)
            contentDTO.exhName = editText04.text.toString()

            editText05 = findViewById(R.id.editTextText4)
            contentDTO.exhPlace = editText05.text.toString()

            editText06 = findViewById(R.id.editTextText9)
            contentDTO.exhLink = editText06.text.toString()

            editText07 = findViewById(R.id.editTextText7)
            contentDTO.exhTime = editText07.text.toString()

            contentDTO.timeStamp = System.currentTimeMillis()

            val randomId = generateRandomId()
            contentDTO.documentId = randomId


            firestore?.collection("images")?.add(contentDTO)?.addOnSuccessListener {


                setResult(RESULT_OK)
                finish()
            }?.addOnFailureListener {
                // Handle failure
            }
        }?.addOnFailureListener {
            // Handle failure
        }
    }


}