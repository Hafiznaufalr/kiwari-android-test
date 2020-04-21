package net.hafiznaufalr.kiwari_androidtest.ui.find

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_find_friends.*
import net.hafiznaufalr.kiwari_androidtest.R
import net.hafiznaufalr.kiwari_androidtest.data.User
import net.hafiznaufalr.kiwari_androidtest.ui.room.ChatRoomActivity
import net.hafiznaufalr.kiwari_androidtest.util.Preferences
import net.hafiznaufalr.kiwari_androidtest.util.showToast

class FindFriendsActivity : AppCompatActivity(), FindContract.View {
    private var listPerson: MutableList<User> = mutableListOf()
    private lateinit var adapter: FindAdapter
    private lateinit var presenter: FindContract.Presenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_find_friends)
        setPresenter()
        setupRv()
        getData()
        refresh()
        doBack()
    }

    private fun doBack() {
        ic_back.setOnClickListener {
            onBackPressed()
        }
    }

    private fun refresh() {
        swipe_find.setOnRefreshListener {
            getData()
        }
    }

    private fun getData() {
        val currentId = Preferences.getUser(this)?.uid!!
        presenter.getFindFriends(currentId)
    }

    private fun setupRv() {
        adapter = FindAdapter(this, listPerson){
            val intent = Intent(this, ChatRoomActivity::class.java)
            intent.putExtra("OUID", it.uid)
            startActivity(intent)
            finish()
        }
        rv_friends.adapter = adapter
    }

    private fun setPresenter() {
        presenter = FindPresenter()
        presenter.takeView(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.dropView()
    }

    override fun showProgress() {
        swipe_find.isRefreshing = true
    }

    override fun hideProgress() {
        swipe_find.isRefreshing = false
    }

    override fun onDataFindResponse(data: List<User>) {
        listPerson.clear()
        listPerson.addAll(data)
        adapter.notifyDataSetChanged()
    }

    override fun onDataFindFailure(message: String) {
        showToast(this, message)
    }
}
