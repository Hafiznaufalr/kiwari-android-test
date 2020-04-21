package net.hafiznaufalr.kiwari_androidtest.ui.profile

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_profile.*
import net.hafiznaufalr.kiwari_androidtest.R
import net.hafiznaufalr.kiwari_androidtest.ui.login.LoginActivity
import net.hafiznaufalr.kiwari_androidtest.util.Preferences
import net.hafiznaufalr.kiwari_androidtest.util.showToast

class ProfileActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
        getData()
        actionClick()
    }

    private fun actionClick() {
        btn_logout.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            Preferences.clearUser(this)
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
            showToast(this, getString(R.string.success_logout))

        }

        ic_back.setOnClickListener {
            onBackPressed()
        }
    }

    private fun getData() {
        val user = Preferences.getUser(this)!!
        tv_name.text = user.name
        tv_email.text = user.email

        Glide.with(this).load(user.avatar).into(ci_avatar)
    }
}
