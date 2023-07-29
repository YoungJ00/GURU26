package com.example.guru26

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
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

        // 인텐트에서 데이터를 받아옴
        val exhName = intent.getStringExtra("exhName") ?: ""
        val exhPlace = intent.getStringExtra("exhPlace") ?: ""
        val exhStartDay = intent.getStringExtra("exhStartDay") ?: ""
        val exhEndDay = intent.getStringExtra("exhEndDay") ?: ""
        val exhTime = intent.getStringExtra("exhTime") ?: ""
        val explain = intent.getStringExtra("explain") ?: ""
        val imageUrl = intent.getStringExtra("imageUrl") ?: ""
        val exhLink = intent.getStringExtra("exhLink") ?: ""
        val favoriteCount = intent.getIntExtra("favoriteCount", 0)

        // 데이터를 View에 설정
        findViewById<TextView>(R.id.mainName).text = exhName
        findViewById<TextView>(R.id.subName).text = exhName
        findViewById<TextView>(R.id.mainPlace).text = exhPlace
        findViewById<TextView>(R.id.startDay).text = exhStartDay
        findViewById<TextView>(R.id.endDay).text = exhEndDay
        findViewById<TextView>(R.id.mainTime).text = exhTime
        findViewById<TextView>(R.id.mainExplain).text = explain
        findViewById<TextView>(R.id.like_count).text = favoriteCount.toString()

        // 이미지 설정
        Glide.with(this).load(imageUrl).into(findViewById(R.id.detail_imageview))

        // mainLink 버튼 클릭 시 링크로 이동
        val mainLinkButton = findViewById<Button>(R.id.mainLink).setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(exhLink))
            startActivity(intent)
        }

        // 좋아요 버튼 설정
        val likeButton = findViewById<ImageButton>(R.id.like_button)
        likeButton.setOnClickListener {
            handleLikeButtonClick(exhName, favoriteCount)
        }

        // 초기 좋아요 버튼 UI 설정
        updateLikeButtonUI(exhName, favoriteCount)

        val backButton = findViewById<ImageButton>(R.id.backButton)
        backButton.setOnClickListener {
            onBackPressed()
        }

        // ImageButton으로 정의되어 있다면 ImageButton 타입으로 타입 변경
        val shareButton = findViewById<ImageButton>(R.id.shareButton)

        shareButton.setOnClickListener {
            // 공유 기능 코드
            val shareIntent = Intent(Intent.ACTION_SEND)
            shareIntent.type = "text/plain"
            shareIntent.putExtra(Intent.EXTRA_SUBJECT, "게시물 공유")
            shareIntent.putExtra(
                Intent.EXTRA_TEXT,
                "전시회 이름: $exhName\n장소: $exhPlace\n 개장시간:$exhTime\n 일시: $exhStartDay ~ $exhEndDay\n 사이트:$exhLink"

            )
            startActivity(Intent.createChooser(shareIntent, "게시물 공유하기"))
        }

    }


    private fun handleLikeButtonClick(exhName: String, currentFavoriteCount: Int) {
        val currentUser = FirebaseAuth.getInstance().currentUser
        if (currentUser == null) {
            // 로그인이 되어 있지 않으면 로그인 화면으로 이동하도록 처리
            // 예: startActivity(Intent(this, LoginActivity::class.java))
            return
        }

        val firestore = FirebaseFirestore.getInstance()

        // 현재 사용자의 UID를 가져옴
        val currentUserId = currentUser.uid

        // Firestore에서 해당 exhName의 데이터 가져오기
        firestore.collection("images")
            .whereEqualTo("exhName", exhName)
            .get()
            .addOnSuccessListener { querySnapshot ->
                if (!querySnapshot.isEmpty) {
                    val documentSnapshot = querySnapshot.documents[0]
                    val contentDTO = documentSnapshot.toObject(ContentDTO::class.java)
                    if (contentDTO != null) {
                        // 이미 좋아요를 누른 경우 (favorites 리스트에 사용자 UID가 있는 경우)
                        if (contentDTO.favorites.containsKey(currentUserId)) {
                            // 이미 좋아요를 누른 상태이므로 취소하도록 처리
                            contentDTO.favoriteCount -= 1
                            contentDTO.favorites.remove(currentUserId)
                        } else {
                            // 좋아요를 누른 상태이므로 좋아요 추가하도록 처리
                            contentDTO.favoriteCount += 1
                            contentDTO.favorites[currentUserId] = true
                        }

                        // Firebase Firestore에 데이터 업데이트
                        firestore.collection("images").document(documentSnapshot.id)
                            .update("favoriteCount", contentDTO.favoriteCount, "favorites", contentDTO.favorites)
                            .addOnSuccessListener {
                                // 데이터 업데이트 성공 시, 좋아요 버튼 UI 업데이트
                                updateLikeButtonUI(exhName, contentDTO.favoriteCount)
                            }
                            .addOnFailureListener {
                                // 데이터 업데이트 실패 처리
                            }
                    }
                } else {
                    // exhName과 일치하는 데이터가 없는 경우 처리
                }
            }
            .addOnFailureListener {
                // 데이터 가져오기 실패
                // 에러 처리를 해주시면 됩니다.
            }
    }


    private fun updateLikeButtonUI(exhName: String, favoriteCount: Int) {
        // 좋아요 버튼 UI 업데이트
        val likeButton = findViewById<ImageButton>(R.id.like_button)
        val likeCountTextView = findViewById<TextView>(R.id.like_count)

        // 좋아요 여부를 Firestore에서 다시 확인하여 UI 설정
        val currentUser = FirebaseAuth.getInstance().currentUser
        if (currentUser != null) {
            val currentUserId = currentUser.uid
            val firestore = FirebaseFirestore.getInstance()
            firestore.collection("images")
                .whereEqualTo("exhName", exhName)
                .get()
                .addOnSuccessListener { querySnapshot ->
                    if (!querySnapshot.isEmpty) {
                        val documentSnapshot = querySnapshot.documents[0]
                        val contentDTO = documentSnapshot.toObject(ContentDTO::class.java)
                        if (contentDTO != null) {
                            if (contentDTO.favorites.containsKey(currentUserId)) {
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
                .addOnFailureListener {
                    // 데이터 가져오기 실패
                    // 에러 처리를 해주시면 됩니다.
                }
        } else {
            // 로그인이 되어 있지 않은 경우
            likeButton.setImageResource(R.drawable._icon__heart_outline_) // 좋아요하지 않은 이미지로 변경
            likeCountTextView.text = favoriteCount.toString()
        }
    }
}