package com.example.guru26

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class Detail : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        val backButton = findViewById<ImageButton>(R.id.backButton)
        backButton.setOnClickListener {
            onBackPressed() // 시스템 제공 함수를 호출하여 뒤로가기 동작 실행
        }

        // 인텐트에서 documentId를 가져옴
        val documentId = intent.getStringExtra("documentId")

        // Firestore에서 해당 documentId의 데이터 가져오기
        if (documentId != null) {
            fetchContentFromFirestore(documentId)
        }
    }

    private fun fetchContentFromFirestore(documentId: String) {
        val firestore = FirebaseFirestore.getInstance()
        firestore.collection("images").document(documentId).get()
            .addOnSuccessListener { documentSnapshot ->
                if (documentSnapshot.exists()) {
                    // Firestore에서 해당 documentId의 데이터를 가져옴
                    val contentDTO = documentSnapshot.toObject(ContentDTO::class.java)

                    // 데이터를 View에 설정
                    findViewById<TextView>(R.id.mainName).text = contentDTO?.exhName
                    findViewById<TextView>(R.id.subName).text = contentDTO?.exhName
                    findViewById<TextView>(R.id.mainPlace).text = contentDTO?.exhPlace
                    findViewById<TextView>(R.id.startDay).text = contentDTO?.exhStartDay
                    findViewById<TextView>(R.id.endDay).text = contentDTO?.exhEndDay
                    findViewById<TextView>(R.id.mainTime).text = contentDTO?.exhTime
                    findViewById<TextView>(R.id.mainExplain).text = contentDTO?.explain
                    findViewById<TextView>(R.id.like_count).text = contentDTO?.favoriteCount.toString()

                    // 이미지 설정
                    Glide.with(this).load(contentDTO?.imageUrl)
                        .into(findViewById(R.id.detail_imageview))

                    // mainLink 버튼 클릭 시 링크로 이동
                    findViewById<Button>(R.id.mainLink).setOnClickListener {
                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(contentDTO?.exhLink))
                        startActivity(intent)
                    }

                    // 좋아요 버튼 설정
                    val likeButton = findViewById<ImageButton>(R.id.like_button)
                    likeButton.setOnClickListener {
                        handleLikeButtonClick(documentId, contentDTO)
                    }

                    // Update the like button UI
                    updateLikeButtonUI(contentDTO)
                } else {
                    // 해당 documentId의 데이터가 없는 경우 처리
                }
            }
            .addOnFailureListener {
                // 데이터 가져오기 실패
                // 에러 처리를 해주시면 됩니다.
            }
    }
    private fun handleLikeButtonClick(documentId: String, contentDTO: ContentDTO?) {
        val currentUser = FirebaseAuth.getInstance().currentUser
        if (currentUser == null) {
            // 로그인이 되어 있지 않으면 로그인 화면으로 이동하도록 처리
            // 예: startActivity(Intent(this, LoginActivity::class.java))
            return
        }

        val firestore = FirebaseFirestore.getInstance()

        // 현재 사용자의 UID를 가져옴
        val currentUserId = currentUser.uid

        // 이미 좋아요를 누른 경우 (favorites 리스트에 사용자 UID가 있는 경우)
        if (contentDTO?.favorites?.contains(currentUserId) == true) {
            // 이미 좋아요를 누른 상태이므로 취소하도록 처리
            contentDTO!!.favoriteCount -= 1
            contentDTO!!.favorites.remove(currentUserId)
        } else {
            // 좋아요를 누른 상태이므로 좋아요 추가하도록 처리
            contentDTO!!.favoriteCount += 1
            contentDTO!!.favorites[currentUserId] = true
        }

        // Firebase Firestore에 데이터 업데이트
        firestore.collection("images").document(documentId)
            .update("favoriteCount", contentDTO.favoriteCount, "favorites", contentDTO.favorites)
            .addOnSuccessListener {

                updateLikeButtonUI(contentDTO)
            }
            .addOnFailureListener {

            }
    }

    private fun updateLikeButtonUI(contentDTO: ContentDTO?) {
        // contentDTO의 데이터를 바탕으로 좋아요 버튼 UI 업데이트
        if (contentDTO != null) {
            val likeButton = findViewById<ImageButton>(R.id.like_button)
            val likeCountTextView = findViewById<TextView>(R.id.like_count)

            if (contentDTO.favorites.containsKey(FirebaseAuth.getInstance().currentUser?.uid)) {
                // 현재 사용자가 좋아요를 누른 경우
                likeButton.setImageResource(R.drawable.group_780) // 좋아요 이미지로 변경
            } else {
                // 현재 사용자가 좋아요를 누르지 않은 경우
                likeButton.setImageResource(R.drawable._icon__heart_outline_) // 좋아요하지 않은 이미지로 변경
            }

            // 좋아요 개수 설정
            likeCountTextView.text = contentDTO.favoriteCount.toString()
        }
    }
}
