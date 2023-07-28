package com.example.guru26
import android.os.Bundle
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class Detail : AppCompatActivity() {

    private var firestore: FirebaseFirestore? = null
    private var likeCount: Int = 0
    private var isLiked: Boolean = false
    private var contentDTO: ContentDTO? = null
    private lateinit var likeCountTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        firestore = FirebaseFirestore.getInstance()
        contentDTO = ContentDTO()
        likeCountTextView = findViewById(R.id.like_count)

        // ...
/*
        // Add like button click event
        val likeButton = findViewById<ImageButton>(R.id.like_button)
        likeButton.setOnClickListener {
            if (isLiked) {
                // 이미 좋아요를 누른 경우 좋아요 취소 처리
                dislikeImage()
            } else {
                // 좋아요를 누른 경우 좋아요 추가 처리
                likeImage()
            }
        }

        // Get like information from Firestore
        val imageId = intent.getStringExtra("IMAGE_ID") ?: ""
        getLikeInfo(imageId)
    }

    private fun likeImage() {
        likeCount++
        val imageId = intent.getStringExtra("IMAGE_ID") ?: ""
        val likeRef = firestore?.collection("images")?.document(imageId)
        likeRef?.update("likeCount", likeCount)
        likeRef?.update("likes.${getCurrentUserId()}", true)
        updateLikeButton()
        getLikeInfo(imageId)
    }

    private fun dislikeImage() {
        likeCount--
        val imageId = intent.getStringExtra("IMAGE_ID") ?: ""
        val likeRef = firestore?.collection("images")?.document(imageId)
        likeRef?.update("likeCount", likeCount)
        likeRef?.update("likes.${getCurrentUserId()}", false)
        updateLikeButton()
        getLikeInfo(imageId)
    }

    private fun getLikeInfo(imageId: String) {
        // Firestore에서 해당 이미지의 좋아요 정보를 가져와서 화면에 반영
        val likeRef = firestore?.collection("images")?.document(imageId)
        likeRef?.get()?.addOnSuccessListener { documentSnapshot ->
            contentDTO = documentSnapshot.toObject(ContentDTO::class.java)
            likeCount = contentDTO?.likeCount ?: 0
            isLiked = contentDTO?.likes?.get(getCurrentUserId()) ?: false
            updateLikeButton()
            updateLikeCount()
        }
    }

    private fun getCurrentUserId(): String {
        val firebaseAuth = FirebaseAuth.getInstance()
        return firebaseAuth.currentUser?.uid ?: ""
    }

    private fun updateLikeButton() {
        // 좋아요 버튼의 이미지와 상태를 업데이트
        val likeButton = findViewById<ImageButton>(R.id.like_button)
        val drawableRes = if (isLiked) R.drawable.group_762 else R.drawable._icon__heart_outline_
        likeButton.setImageResource(drawableRes)
    }

    private fun updateLikeCount() {
        // 좋아요 수를 TextView에 업데이트
        likeCountTextView.text = likeCount.toString()
    }*/

}}