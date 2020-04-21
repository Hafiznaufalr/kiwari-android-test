package net.hafiznaufalr.kiwari_androidtest.ui.find

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.item_people.view.*
import net.hafiznaufalr.kiwari_androidtest.R
import net.hafiznaufalr.kiwari_androidtest.data.User
import net.hafiznaufalr.kiwari_androidtest.ui.room.ChatRoomActivity

class FindAdapter(
    private val context: Context,
    private val listPerson: List<User>,
    private val onClickListener: (User) -> Unit
) : RecyclerView.Adapter<FindAdapter.MyHolder>() {

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): MyHolder {
        return MyHolder(LayoutInflater.from(p0.context).inflate(R.layout.item_people, p0, false))
    }

    override fun getItemCount(): Int = listPerson.size

    override fun onBindViewHolder(holder: MyHolder, position: Int) {
        val data = listPerson[position]
        val view = holder.itemView

        view.tv_name.text = data.name
        Glide.with(context).load(data.avatar).into(view.ci_avatar)


        view.setOnClickListener {
            onClickListener(data)
        }
    }

    class MyHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

}