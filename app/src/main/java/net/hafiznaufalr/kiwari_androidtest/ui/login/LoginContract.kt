package net.hafiznaufalr.kiwari_androidtest.ui.login

import net.hafiznaufalr.kiwari_androidtest.data.User
import net.hafiznaufalr.kiwari_androidtest.ui.base.BasePresenter
import net.hafiznaufalr.kiwari_androidtest.ui.base.BaseView

interface LoginContract {
    interface View: BaseView<Presenter>{
        fun onLoginResponse(data: User)
        fun onLoginFailure(message: String)
    }

    interface Presenter: BasePresenter<View>{
        fun postLogin(email: String, password: String)
    }
}