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
    private var list: MutableList<User> = mutableListOf()

    override fun takeView(view: FindContract.View) {
        this.view = view
    }

    override fun dropView() {
        this.view = null
    }

    override fun getFindFriends(currentId: String) {
        view?.showProgress()
        val reference = FirebaseDatabase.getInstance().getReference(USER_REFERENCE)
        reference.addListenerForSingleValueEvent(object: ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {
                view?.onDataFindFailure(p0.message)
                view?.hideProgress()
            }

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                list.clear()
                dataSnapshot.children.forEach { it ->
                    it.getValue(User::class.java).let {
                        if (it!!.uid != currentId) {
                            list.add(it)
                        }
                    }
                }
                view?.onDataFindResponse(list)
                view?.hideProgress()

            }

        })
    }

}