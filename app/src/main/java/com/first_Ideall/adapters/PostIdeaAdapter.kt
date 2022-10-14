package com.first_Ideall.adapters

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.firestore.FirebaseFirestore
import com.first_Ideall.R
import com.first_Ideall.model.Post
import kotlinx.android.synthetic.main.item_post_idea.view.*
import java.text.SimpleDateFormat
import com.google.firebase.storage.FirebaseStorage


class PostIdeaAdapter(
    private val fragment: Fragment,
    private val dataset: List<Post>,
): RecyclerView.Adapter<PostIdeaAdapter.PostViewHolder>() {

    //Firebase Conection
    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore
    private lateinit var databaseReference: DatabaseReference

    class PostViewHolder (val layout: View):
        RecyclerView.ViewHolder(layout){

        fun profileImage(image: String?, imageView: ImageView){

            // WORKING CODE?
            val storage = FirebaseStorage.getInstance()
            // Create a reference to a file from a Google Cloud Storage URI
            val gsReference = storage.getReferenceFromUrl("gs://ideall-b4b40.appspot.com/image/${image}")
            Log.d("Message", "LOLOL"+ gsReference.toString())
            Glide.with(this.layout)
                .load(gsReference)
                .error(R.drawable.addlogo)
                .into(imageView)
        }

    }

    override fun onCreateViewHolder (parent: ViewGroup, viewType: Int): PostViewHolder{
        val layout = LayoutInflater.from(parent.context).inflate(R.layout.item_post_idea,parent,false)
        return PostViewHolder(layout)
    }

    override fun getItemCount() = dataset.size


    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        //Firebase conection
        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()
        databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(auth.currentUser!!.uid)

        if(dataset.isNotEmpty()){
            val post = dataset[position]
            //Setting image
            holder.profileImage(post.userName, holder.layout.iv_postProfile)
            holder.profileImage(post.userName + post.postTitle, holder.layout.iv_postImage)

            holder.layout.tv_postTitle.text = post.postTitle
            holder.layout.tv_postDescription.text = post.postDescription
            holder.layout.tv_postUserName.text = post.userName
            val sdf = SimpleDateFormat ("EEE, MMM d, ''yy")
            holder.layout.tv_date.text = "Posted "+ sdf.format(post.date)

            //Like Button
            val likes = post.likes!!.toMutableList()
            var liked = false

            //If Likes exist show number else get it out
            if (likes.isNotEmpty()){
                liked = likes.contains(auth.uid)
                holder.layout.tv_likesCount.text = "${likes.size}"
                setColor(liked,holder.layout.b_like)
            }else{
                holder.layout.tv_likesCount.visibility = View.GONE
            }

            //Change to checked like and show number of likes
            holder.layout.b_like.setOnClickListener{
                liked = !liked
                setColor(liked,holder.layout.b_like)

                if(liked){
                    likes.add(auth.uid!!)
                }else{
                    likes.remove(auth.uid!!)
                }

                if (likes.isNotEmpty()){
                    holder.layout.tv_likesCount.visibility = View.VISIBLE
                }else{
                    holder.layout.tv_likesCount.visibility = View.GONE
                }
                // upadate the post
                val doc = db.collection("posts").document(post.uid!!)
                db.runTransaction() {
                    it.update(doc,"likes",likes)
                    null
                }
            }

            // Delete post
            if(post.userName == auth.currentUser?.displayName){

                holder.layout.b_delete.visibility = View.VISIBLE
                holder.layout.b_delete.setOnClickListener{

                    AlertDialog.Builder(fragment.activity).apply {
                        setTitle("Delete post")
                        setMessage("The post will be deleted forever")

                        setNegativeButton("No",null)

                        setPositiveButton("Yes"){ dialogInterface:DialogInterface, i: Int ->
                            db.collection("posts").document(post.uid!!).delete()
                                .addOnSuccessListener {
                                    Toast.makeText(fragment.context, "Deleted", Toast.LENGTH_SHORT).show()

                                }
                                .addOnFailureListener {
                                    Toast.makeText(fragment.context, "Fail to delete", Toast.LENGTH_SHORT).show()
                                }
                        }
                    }.show()
                }
            }

            //Share button
            holder.layout.b_share.setOnClickListener{
                val sendIntent =Intent().apply {
                    action = Intent.ACTION_SEND
                    putExtra(Intent.EXTRA_TEXT, post.postDescription)
                    type = "text/plain"
                }
                val shareIntent = Intent.createChooser(sendIntent, null)
                fragment.startActivity(shareIntent)
            }
        }else{

        }

    }

    //Change color of like button
    private fun setColor(liked:Boolean, likeButton: Button){
        if (liked){
            likeButton.setBackgroundResource(R.drawable.ic_ideas)
        }else{
            likeButton.setBackgroundResource(R.drawable.ic_ideas_border)
        }
    }
}