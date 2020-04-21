package net.hafiznaufalr.kiwari_androidtest.ui.main

import net.hafiznaufalr.kiwari_androidtest.data.Message
import net.hafiznaufalr.kiwari_androidtest.ui.base.BasePresenter
import net.hafiznaufalr.kiwari_androidtest.ui.base.BaseView

interface MainContract {
    interface View: BaseView<Presenter>{
        fun onDataLatestResponse(data: List<Message>)
        fun onDataLatestFailure(message: String)
    }

    interface Presenter: BasePresenter<View>{
        fun getDataLatestMessage(currentId: String)
    }
}