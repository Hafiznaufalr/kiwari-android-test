package net.hafiznaufalr.kiwari_androidtest.ui.room

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_current.view.*
import kotlinx.android.synthetic.main.item_opponent.view.*
import net.hafiznaufalr.kiwari_androidtest.R
import net.hafiznaufalr.kiwari_androidtest.data.Message
import net.hafiznaufalr.kiwari_androidtest.util.DateUtils.getFormattedTimeChatLog

class ChatRoomAdapter(
    private val context: Context,
    private val listMessage: List<Message>,
    private val currentUser: String
) : RecyclerView.Adapter<ChatRoomAdapter.MyHolder>() {
    private lateinit var user: String

    companion object {
        const val ITEM_VIEW_TYPE_CURRENT = 0
        const val ITEM_VIEW_TYPE_OPPONENT = 1
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            ITEM_VIEW_TYPE_CURRENT -> MyHolder(
                layoutInflater.inflate(R.layout.item_current, parent, false)
            )
            else -> MyHolder(
                layoutInflater.inflate(R.layout.item_opponent, parent, false)
            )
        }
    }

    override fun getItemCount(): Int = listMessage.size

    override fun onBindViewHolder(holder: MyHolder, position: Int) {
        val data = listMessage[position]
        val view = holder.itemView

           when (holder.itemViewType) {
            ITEM_VIEW_TYPE_CURRENT -> {
                view.tv_current_text.text = data.text
                view.tv_current_time.text = getFormattedTimeChatLog(data.timestamp!!)
            }
            ITEM_VIEW_TYPE_OPPONENT -> {
                view.tv_opponent_text.text = data.text
                view.tv_opponent_time.text = getFormattedTimeChatLog(data.timestamp!!)
            }
        }

    }

    override fun getItemViewType(position: Int): Int {
        user = listMessage[position].currentId!!
        return if (user == currentUser) {
            ITEM_VIEW_TYPE_CURRENT
        } else {
            ITEM_VIEW_TYPE_OPPONENT
        }
    }


    class MyHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

}