package net.hafiznaufalr.kiwari_androidtest.ui.room

import net.hafiznaufalr.kiwari_androidtest.data.Message
import net.hafiznaufalr.kiwari_androidtest.data.User
import net.hafiznaufalr.kiwari_androidtest.ui.base.BasePresenter
import net.hafiznaufalr.kiwari_androidtest.ui.base.BaseView
import java.sql.Timestamp

interface RoomContract {
    interface View: BaseView<Presenter>{
        fun disableProgress()

        fun onDataMessageResponse(data: List<Message>)
        fun onDataMessageFailure(message: String)

        fun onPostMessageResponse(message: String)
        fun onPostMessageFailure(message: String)

        fun onDataOpponentResponse(data: User)
        fun onDataOpponentFailure(message: String)
    }

    interface Presenter: BasePresenter<View>{
        fun getDataMessage(currentId: String, opponentId: String)
        fun postDataMessage(text: String, currentId: String, opponentId: String, timestamp: Long)
        fun getDataOpponent(opponentId: String)
    }
}