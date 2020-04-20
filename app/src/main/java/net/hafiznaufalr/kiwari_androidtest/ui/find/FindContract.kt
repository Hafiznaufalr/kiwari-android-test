package net.hafiznaufalr.kiwari_androidtest.ui.find

import net.hafiznaufalr.kiwari_androidtest.data.User
import net.hafiznaufalr.kiwari_androidtest.ui.base.BasePresenter
import net.hafiznaufalr.kiwari_androidtest.ui.base.BaseView

interface FindContract {
    interface View: BaseView<Presenter>{
        fun onDataFindResponse(data: List<User>)
        fun onDataFindFailure(message: String)
    }

    interface Presenter: BasePresenter<View>{
        fun getFindFriends()
    }
}