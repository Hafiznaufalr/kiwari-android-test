package net.hafiznaufalr.kiwari_androidtest.ui.find

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_find_friends.*
import net.hafiznaufalr.kiwari_androidtest.R
import net.hafiznaufalr.kiwari_androidtest.data.User
import net.hafiznaufalr.kiwari_androidtest.util.Preferences
import net.hafiznaufalr.kiwari_androidtest.util.showToast

class FindFriendsActivity : AppCompatActivity(), FindContract.View {
    private var listPerson: ArrayList<User> = arrayListOf()
    private lateinit var adapter: FindAdapter
    private lateinit var presenter: FindContract.Presenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_find_friends)
        setPresenter()
        setupRv()
        getData()
        refresh()
    }

    private fun refresh() {
        swipe_find.setOnRefreshListener {
            getData()
        }
    }

    private fun getData() {
        presenter.getFindFriends()
    }

    private fun setupRv() {
        val currentId = Preferences.getUser(this)?.uid!!
        adapter = FindAdapter(this, listPerson, currentId)
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
