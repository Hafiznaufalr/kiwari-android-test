package net.hafiznaufalr.kiwari_androidtest.ui.login

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.*
import com.google.gson.Gson
import dmax.dialog.SpotsDialog
import kotlinx.android.synthetic.main.activity_login.*
import net.hafiznaufalr.kiwari_androidtest.R
import net.hafiznaufalr.kiwari_androidtest.data.User
import net.hafiznaufalr.kiwari_androidtest.ui.main.MainActivity
import net.hafiznaufalr.kiwari_androidtest.util.showToast
import net.hafiznaufalr.kiwari_androidtest.util.Constant
import net.hafiznaufalr.kiwari_androidtest.util.Constant.USER_REFERENCE
import net.hafiznaufalr.kiwari_androidtest.util.Preferences

class LoginActivity : AppCompatActivity(), LoginContract.View {
    private lateinit var presenter: LoginPresenter
    private lateinit var dialog: SpotsDialog
    private lateinit var db: DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        dialog = SpotsDialog.Builder().setContext(this).build() as SpotsDialog
        setPresenter()
        setValidator()
    }

    override fun onStart() {
        super.onStart()
        if (Preferences.getStatus(this)) {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }

    private fun setValidator() {
        btn_login.setOnClickListener {
            if (et_email.text.toString().isEmpty()){
                showToast(this, getString(R.string.validator_email))
            }else if(et_password.text.toString().isEmpty()){
                showToast(this, getString(R.string.validator_password))
            }else if(!Patterns.EMAIL_ADDRESS.matcher(et_email.text.toString()).matches()){
                showToast(this, getString(R.string.matcher))
            }else{
                doLogin()
            }
        }
    }

    private fun doLogin() {
        val email = et_email.text.toString()
        val password = et_password.text.toString()
        presenter.postLogin(email, password)
    }

    private fun setPresenter() {
        presenter = LoginPresenter()
        presenter.takeView(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.dropView()
    }

    override fun onLoginResponse(uid: String) {
        db = FirebaseDatabase.getInstance().getReference(USER_REFERENCE)
        db.child(uid).addListenerForSingleValueEvent(object: ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {
                showToast(this@LoginActivity, p0.message)
            }

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val user = dataSnapshot.getValue(User::class.java)
                if (user == null){
                    showToast(this@LoginActivity, getString(R.string.user_not_found))
                }else{
                    if (user.uid == uid){
                        hideProgress()
                        showToast(this@LoginActivity, getString(R.string.success_login))
                        doPreference(user)
                        val intent = Intent(this@LoginActivity, MainActivity::class.java)
                        startActivity(intent)
                        finish()
                    }
                }
            }

        } )
    }

    private fun doPreference(user: User) {
        Preferences.putUser(this, Gson().toJson(user))
        Preferences.putStatus(this, true)
    }

    override fun onLoginFailure(message: String) {
        showToast(this, message)
    }
    override fun showProgress() {
        dialog.show()
    }

    override fun hideProgress() {
        dialog.dismiss()
    }

}
