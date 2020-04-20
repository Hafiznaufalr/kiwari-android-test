package net.hafiznaufalr.kiwari_androidtest.ui.login

import com.google.firebase.auth.FirebaseAuth
import net.hafiznaufalr.kiwari_androidtest.ui.base.BasePresenter

class LoginPresenter: BasePresenter<LoginContract.View>, LoginContract.Presenter {
    var view: LoginContract.View? = null
    private val auth = FirebaseAuth.getInstance()

    override fun takeView(view: LoginContract.View) {
        this.view = view
    }

    override fun dropView() {
        this.view = null
    }

    override fun postLogin(email: String, password: String) {
        view?.showProgress()
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener {
                if (!it.isSuccessful)return@addOnCompleteListener
                view?.onLoginResponse(it.result!!.user!!.uid)
            }
            .addOnFailureListener {
                view?.onLoginFailure(it.message!!)
                view?.hideProgress()
            }
    }
}