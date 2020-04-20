package net.hafiznaufalr.kiwari_androidtest.ui.base

interface BasePresenter<T> {

    fun takeView(view: T)

    fun dropView()
}