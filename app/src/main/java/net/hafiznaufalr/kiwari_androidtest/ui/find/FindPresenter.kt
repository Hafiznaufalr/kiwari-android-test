package net.hafiznaufalr.kiwari_androidtest.ui.find

import android.util.Log
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import net.hafiznaufalr.kiwari_androidtest.data.User
import net.hafiznaufalr.kiwari_androidtest.ui.base.BasePresenter
import net.hafiznaufalr.kiwari_androidtest.util.Constant.USER_REFERENCE

class FindPresenter: BasePresenter<FindContract.View>, FindContract.Presenter{
    var view: FindContract.View? = null
    private var list: ArrayList<User> = arrayListOf()

    override fun takeView(view: FindContract.View) {
        this.view = view
    }

    override fun dropView() {
        this.view = null
    }

    override fun getFindFriends() {
        view?.showProgress()
        val db = FirebaseDatabase.getInstance().getReference(USER_REFERENCE)
        db.addListenerForSingleValueEvent(object: ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {
                view?.onDataFindFailure(p0.message)
                view?.hideProgress()
            }

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                list.clear()
                dataSnapshot.children.forEach {
                    Log.d("TWAG", it.toString())
                    val data = it.getValue(User::class.java)
                    list.add(data!!)
                    view?.onDataFindResponse(list)
                    view?.hideProgress()
                }
            }

        })
    }

}