package net.hafiznaufalr.kiwari_androidtest.ui.login

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import net.hafiznaufalr.kiwari_androidtest.data.User
import net.hafiznaufalr.kiwari_androidtest.ui.base.BasePresenter
import net.hafiznaufalr.kiwari_androidtest.util.Constant.USER_REFERENCE

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
                doPref(it.result!!.user!!.uid)
            }
            .addOnFailureListener {
                view?.onLoginFailure(it.message!!)
                view?.hideProgress()
            }
    }

    private fun doPref(uid: String) {
        val reference = FirebaseDatabase.getInstance().getReference(USER_REFERENCE)
        reference.child(uid).addListenerForSingleValueEvent(object: ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                view?.onLoginFailure(p0.message)
            }

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val user = dataSnapshot.getValue(User::class.java)
                    if (user!!.uid == uid){
                        view?.hideProgress()
                        view?.onLoginResponse(user)
                    }
                }

        } )
    }
}