package net.hafiznaufalr.kiwari_androidtest.ui.room

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.activity_chat_room.*
import net.hafiznaufalr.kiwari_androidtest.R
import net.hafiznaufalr.kiwari_androidtest.data.Message
import net.hafiznaufalr.kiwari_androidtest.data.User
import net.hafiznaufalr.kiwari_androidtest.util.Preferences
import net.hafiznaufalr.kiwari_androidtest.util.showToast

class ChatRoomActivity : AppCompatActivity(), RoomContract.View {
    private lateinit var presenter: RoomContract.Presenter
    private lateinit var adapter: ChatRoomAdapter
    private var listMessage: ArrayList<Message> = arrayListOf()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat_room)
        setPresenter()
        prepareRv()
        postMessage()
        getMessage()
        getDataOpponent()
        doBack()
    }

    private fun doBack() {
        ic_back.setOnClickListener {
            onBackPressed()
        }
    }

    private fun getDataOpponent() {
        val opponentId = intent.getStringExtra("OUID")!!
        presenter.getDataOpponent(opponentId)
    }

    private fun prepareRv() {
        val currentId = Preferences.getUser(this)!!.uid!!
        adapter = ChatRoomAdapter(this, listMessage, currentId)
        rv_chat_log.adapter = adapter
    }

    private fun getMessage() {
        val currentId = Preferences.getUser(this)!!.uid!!
        val opponentId = intent.getStringExtra("OUID")!!
        presenter.getDataMessage(currentId, opponentId)
    }

    private fun setPresenter() {
        presenter = RoomPresenter()
        presenter.takeView(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.dropView()
    }

    private fun postMessage() {
        btn_send_message.setOnClickListener {
            if (et_message.text.toString().isEmpty()){
                showToast(this, getString(R.string.validate_text))
            }else{
                doPostMessage()
            }
        }
    }

    private fun doPostMessage() {
        val currentId = Preferences.getUser(this)?.uid!!
        val opponentId = intent.getStringExtra("OUID")!!
        val text = et_message.text.toString()
        val timeStamp = System.currentTimeMillis() / 1000
        presenter.postDataMessage(text, currentId, opponentId, timeStamp)
    }


    override fun showProgress() {
        swipe_room.isRefreshing = true
    }

    override fun hideProgress() {
        swipe_room.isRefreshing = false
    }

    override fun disableProgress() {
        swipe_room.isEnabled = false
    }

    override fun onDataMessageResponse(data: List<Message>) {
        listMessage.clear()
        listMessage.addAll(data)
        adapter.notifyDataSetChanged()
        rv_chat_log.smoothScrollToPosition(adapter.itemCount - 1)
    }

    override fun onDataMessageFailure(message: String) {
        showToast(this, message)
    }

    override fun onPostMessageResponse(message: String) {
        Log.d("TAG", message)
        et_message.text.clear()
        rv_chat_log.smoothScrollToPosition(adapter.itemCount - 1)
    }

    override fun onPostMessageFailure(message: String) {
        showToast(this, message)
    }

    override fun onDataOpponentResponse(data: User) {
        tv_name.text = data.name
        Glide.with(this).load(data.avatar).into(ci_avatar)
    }

    override fun onDataOpponentFailure(message: String) {
        showToast(this, message)
    }

}
