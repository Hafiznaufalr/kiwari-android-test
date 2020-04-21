package net.hafiznaufalr.kiwari_androidtest.ui.main

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.item_message.view.*
import net.hafiznaufalr.kiwari_androidtest.R
import net.hafiznaufalr.kiwari_androidtest.data.Message
import net.hafiznaufalr.kiwari_androidtest.data.User
import net.hafiznaufalr.kiwari_androidtest.util.Constant.USER_REFERENCE

class MainAdapter(
    private val context: Context,
    private val currentId: String,
    private val data: List<Message>,
    private val onClickListener: (String) -> Unit
) : RecyclerView.Adapter<MainAdapter.MyHolder>() {

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): MyHolder {
        return MyHolder(LayoutInflater.from(p0.context).inflate(R.layout.item_message, p0, false))
    }

    override fun getItemCount(): Int = data.size

    override fun onBindViewHolder(holder: MyHolder, position: Int) {
        val latest = data[position]
        val view = holder.itemView

        view.tv_latest_message.text = latest.text

        val chatPartnerId = if (latest.currentId == currentId) {
            latest.opponentId!!
        } else {
            latest.currentId!!
        }

        val reference = FirebaseDatabase.getInstance().getReference("$USER_REFERENCE/$chatPartnerId")
        reference.addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {
            }

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val opponent = dataSnapshot.getValue(User::class.java)
                view.tv_name.text = opponent?.name
                Glide.with(context).load(opponent?.avatar).into(view.ci_avatar)
            }

        })


        view.setOnClickListener {
            onClickListener(chatPartnerId)
        }
    }

    class MyHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
}

