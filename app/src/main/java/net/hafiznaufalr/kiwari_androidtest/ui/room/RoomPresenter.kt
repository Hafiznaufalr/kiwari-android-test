package net.hafiznaufalr.kiwari_androidtest.ui.room

import android.util.Log
import com.google.firebase.database.*
import net.hafiznaufalr.kiwari_androidtest.data.Message
import net.hafiznaufalr.kiwari_androidtest.data.User
import net.hafiznaufalr.kiwari_androidtest.ui.base.BasePresenter
import net.hafiznaufalr.kiwari_androidtest.util.Constant
import net.hafiznaufalr.kiwari_androidtest.util.Constant.LATEST_MSG_REFERENCE
import net.hafiznaufalr.kiwari_androidtest.util.Constant.MESSAGE_REFERENCE
import net.hafiznaufalr.kiwari_androidtest.util.showToast

class RoomPresenter: BasePresenter<RoomContract.View>, RoomContract.Presenter {
    var view: RoomContract.View? = null
    private var list: MutableList<Message> = mutableListOf()

    override fun takeView(view: RoomContract.View) {
        this.view = view
    }

    override fun dropView() {
        this.view = null
    }
    override fun getDataMessage(currentId: String, opponentId: String) {
        view?.showProgress()
        val reference = FirebaseDatabase.getInstance().getReference("$MESSAGE_REFERENCE/$currentId/$opponentId")
        reference.addListenerForSingleValueEvent(object: ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {
                view?.onDataMessageFailure(p0.message)
                view?.hideProgress()
            }

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                Log.d("TEST", dataSnapshot.toString())
                if (!dataSnapshot.hasChildren()){
                    view?.hideProgress()
                    view?.disableProgress()
                }
            }

        })

        reference.addChildEventListener(object: ChildEventListener{
            override fun onCancelled(p0: DatabaseError) {
            }

            override fun onChildMoved(p0: DataSnapshot, p1: String?) {
            }

            override fun onChildChanged(p0: DataSnapshot, p1: String?) {
            }

            override fun onChildAdded(dataSnapshot: DataSnapshot, p1: String?) {
                dataSnapshot.getValue(Message::class.java).let {
                    list.add(it!!)
                }
                view?.onDataMessageResponse(list)
                view?.hideProgress()
                view?.disableProgress()
            }

            override fun onChildRemoved(p0: DataSnapshot) {
            }

        })
    }

    override fun postDataMessage(
        text: String,
        currentId: String,
        opponentId: String,
        timestamp: Long
    ) {
        val reference = FirebaseDatabase.getInstance().getReference("$MESSAGE_REFERENCE/$currentId/$opponentId").push()
        val opponentReference = FirebaseDatabase.getInstance().getReference("$MESSAGE_REFERENCE/$opponentId/$currentId").push()

        val message = Message(reference.key, text, currentId, opponentId, timestamp)
        reference.setValue(message)
            .addOnSuccessListener {
                view?.onPostMessageResponse("Saved our chat message: ${reference.key}")
            }
            .addOnFailureListener {
                view?.onPostMessageFailure(it.message!!)
            }
        opponentReference.setValue(message)
        val latestMessageRef = FirebaseDatabase.getInstance().getReference("$LATEST_MSG_REFERENCE/$currentId/$opponentId")
        latestMessageRef.setValue(message)

        val latestMessageToRef = FirebaseDatabase.getInstance().getReference("$LATEST_MSG_REFERENCE/$opponentId/$currentId")
        latestMessageToRef.setValue(message)
    }

    override fun getDataOpponent(opponentId: String) {
        val reference = FirebaseDatabase.getInstance().getReference(Constant.USER_REFERENCE)
        reference.child(opponentId).addListenerForSingleValueEvent(object: ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                view?.onDataOpponentFailure(p0.message)
            }

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val opponent = dataSnapshot.getValue(User::class.java)
                if (opponent!!.uid == opponentId){
                    view?.hideProgress()
                    view?.onDataOpponentResponse(opponent)
                }
            }

        } )
    }
}