package net.hafiznaufalr.kiwari_androidtest.ui.main

import com.google.firebase.database.*
import net.hafiznaufalr.kiwari_androidtest.data.Message
import net.hafiznaufalr.kiwari_androidtest.ui.base.BasePresenter
import net.hafiznaufalr.kiwari_androidtest.util.Constant.LATEST_MSG_REFERENCE

class MainPresenter: BasePresenter<MainContract.View>, MainContract.Presenter {
    var view: MainContract.View? = null
    private var listLatest: MutableList<Message> = mutableListOf()

    override fun takeView(view: MainContract.View) {
        this.view = view
    }

    override fun dropView() {
        this.view = null
    }

    override fun getDataLatestMessage(currentId: String) {
        view?.showProgress()
        val reference = FirebaseDatabase.getInstance().getReference("$LATEST_MSG_REFERENCE/$currentId")
        reference.addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {
                view?.onDataLatestFailure(p0.message)
            }

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (!dataSnapshot.hasChildren()) {
                    view?.hideProgress()
                }
            }

        })

        reference.addChildEventListener(object : ChildEventListener{
            override fun onCancelled(p0: DatabaseError) {
            }

            override fun onChildMoved(p0: DataSnapshot, p1: String?) {
            }

            override fun onChildChanged(dataSnapshot: DataSnapshot, p1: String?) {
                listLatest.clear()
                dataSnapshot.getValue(Message::class.java).let {
                    listLatest.add(it!!)
                }
                view?.onDataLatestResponse(listLatest)
                view?.hideProgress()
            }

            override fun onChildAdded(dataSnapshot: DataSnapshot, p1: String?) {
                listLatest.clear()
                dataSnapshot.getValue(Message::class.java).let {
                    listLatest.add(it!!)
                }
                view?.onDataLatestResponse(listLatest)
                view?.hideProgress()
            }

            override fun onChildRemoved(p0: DataSnapshot) {
            }

        })
    }
}