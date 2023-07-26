package com.example.guru26

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.text.FieldPosition

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [DetailFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class DetailFragment : Fragment() {

    var firestore : FirebaseFirestore? =null
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_detail, container, false)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment DetailFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            DetailFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    viewholder.detailviewitem_favorite_imageview.setOnClickListner{
        favoriteEvent(p1)
    }

    if(contentDTOs!![p1].favorites.containsKey(uid)){
        viewholder.detailviewitem_favorite_imageview.setImageResource(R.drawable._icon__heart_outline_)
    }
    else{
        viewholder.detailviewitem_favorite_imageview.setImageResource(R.drawable._icon__heart_outline_)    }

    fun favoriteEvent(position: Int)
    {
        var tsDoc = firestore?.collection("images")?.document(contentUidList[position])
        firestore?. runTransaction{ transaction ->

            var uid = FirebaseAuth.getInstance().currentUser?.uid
            var contentDTO = transaction.get(tsDoc!!).toObject(ContentDTO::class.java)

            if(cotentDTO!!.favorites.containsKey(uid)){
                //when the button is clicked
                contentDTO?.favoriteCount = contentDTO?.favoriteCount -1
                contentDTO?.favorites.remove(uid)
            }
            else{
                // when the button is not clicked
                contentDTO?.favoriteCount = contentDTO?.favoritecount + 1
                contentDTO?.favorites.[uid!!] = true
            }
            transaction.set(tsDoc,contentDTO)
        }
    }

}