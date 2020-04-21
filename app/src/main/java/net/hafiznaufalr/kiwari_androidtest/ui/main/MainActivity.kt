package net.hafiznaufalr.kiwari_androidtest.ui.main

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.activity_main.*
import net.hafiznaufalr.kiwari_androidtest.R
import net.hafiznaufalr.kiwari_androidtest.data.Message
import net.hafiznaufalr.kiwari_androidtest.data.User
import net.hafiznaufalr.kiwari_androidtest.ui.find.FindFriendsActivity
import net.hafiznaufalr.kiwari_androidtest.ui.login.LoginActivity
import net.hafiznaufalr.kiwari_androidtest.ui.room.ChatRoomActivity
import net.hafiznaufalr.kiwari_androidtest.util.Preferences
import net.hafiznaufalr.kiwari_androidtest.util.showToast


class MainActivity : AppCompatActivity(), MainContract.View {
    private lateinit var presenter: MainContract.Presenter
    private lateinit var adapter: MainAdapter
    private lateinit var user: User
    private lateinit var name: TextView
    private lateinit var email: TextView
    private lateinit var btnLogout: Button
    private lateinit var avatar: CircleImageView
    private var listLatest: MutableList<Message> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setPresenter()
        getData()
        prepareRv()
        fetchIcon()
        actionClick()
        refresh()
        user = Preferences.getUser(this)!!
    }

    private fun refresh() {
        swipe_main.setOnRefreshListener {
            getData()
        }
    }

    private fun prepareRv() {
        val currentId = Preferences.getUser(this)?.uid!!
        adapter = MainAdapter(this,currentId, listLatest){
            val intent = Intent(this, ChatRoomActivity::class.java)
            intent.putExtra("OUID", it)
            startActivity(intent)
        }
        rv_latest.adapter = adapter
    }

    private fun getData() {
        val currentId = Preferences.getUser(this)?.uid!!
        presenter.getDataLatestMessage(currentId)
    }

    private fun setPresenter() {
        presenter = MainPresenter()
        presenter.takeView(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.dropView()
    }

    private fun fetchIcon() {
        val avatar = Preferences.getUser(this)?.avatar!!
        Glide.with(this).load(avatar).placeholder(R.drawable.ic_person_24dp).into(ic_to_profile)
    }

    private fun actionClick() {
        fab_new.setOnClickListener {
            val intent = Intent(this, FindFriendsActivity::class.java)
            startActivity(intent)
        }

        ic_to_profile.setOnClickListener {
            showDialog()
        }
    }

    private fun showDialog() {
        val alertDialog = AlertDialog.Builder(this)
        val inflater: LayoutInflater = this.layoutInflater
        val profileLayout: View = inflater.inflate(R.layout.dialog_profile, null)
        name = profileLayout.findViewById(R.id.tv_name)
        email = profileLayout.findViewById(R.id.tv_email)
        btnLogout = profileLayout.findViewById(R.id.btn_logout)
        avatar = profileLayout.findViewById(R.id.ci_avatar)
        alertDialog.setView(profileLayout)
        name.text = user.name
        email.text = user.email
        Glide.with(this).load(user.avatar).into(avatar)
        btnLogout.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            Preferences.clearUser(this)
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
            showToast(this, getString(R.string.success_logout))
        }
        alertDialog.show()
    }

    override fun onDataLatestResponse(data: List<Message>) {
        if (data.isEmpty()){
            placeholder.visibility = View.VISIBLE
        }else {
            placeholder.visibility = View.GONE
            listLatest.clear()
            listLatest.addAll(data)
            adapter.notifyDataSetChanged()
        }
    }

    override fun onDataLatestFailure(message: String) {
        showToast(this, message)
    }

    override fun showProgress() {
        swipe_main.isRefreshing = true
    }

    override fun hideProgress() {
        swipe_main.isRefreshing = false
    }
}
