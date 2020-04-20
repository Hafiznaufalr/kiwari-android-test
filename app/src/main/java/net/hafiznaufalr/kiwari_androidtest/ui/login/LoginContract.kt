package net.hafiznaufalr.kiwari_androidtest.ui.login

import com.google.firebase.auth.FirebaseUser
import net.hafiznaufalr.kiwari_androidtest.data.User
import net.hafiznaufalr.kiwari_androidtest.ui.base.BasePresenter
import net.hafiznaufalr.kiwari_androidtest.ui.base.BaseView

interface LoginContract {
    interface View: BaseView<Presenter>{
        fun onLoginResponse(uid: String)
        fun onLoginFailure(message: String)
    }

    interface Presenter: BasePresenter<View>{
        fun postLogin(email: String, password: String)
    }
}