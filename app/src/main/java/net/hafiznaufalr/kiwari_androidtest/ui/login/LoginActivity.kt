package net.hafiznaufalr.kiwari_androidtest.ui.login

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.DatabaseReference
import com.google.gson.Gson
import dmax.dialog.SpotsDialog
import kotlinx.android.synthetic.main.activity_login.*
import net.hafiznaufalr.kiwari_androidtest.R
import net.hafiznaufalr.kiwari_androidtest.data.User
import net.hafiznaufalr.kiwari_androidtest.ui.main.MainActivity
import net.hafiznaufalr.kiwari_androidtest.util.Preferences
import net.hafiznaufalr.kiwari_androidtest.util.showToast

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

    override fun onLoginResponse(data: User) {
        showToast(this@LoginActivity, getString(R.string.success_login))
        Preferences.putUser(this, Gson().toJson(data))
        Preferences.putStatus(this, true)
        val intent = Intent(this@LoginActivity, MainActivity::class.java)
        startActivity(intent)
        finish()
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
