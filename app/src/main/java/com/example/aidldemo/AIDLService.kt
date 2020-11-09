package com.example.aidldemo

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log

class AIDLService : Service() {

    private val tag = this.javaClass.simpleName

    private val bookList = ArrayList<Book>()

    override fun onCreate() {
        super.onCreate()
        Log.d(tag, "onCreate")

        val book = Book()
        book.name = "围城"
        book.price = 20.5f
        bookList.add(book)
    }

    private val bookManager = object : BookManager.Stub() {
        override fun getBooks(): MutableList<Book> {
            Log.d(tag, "getBooks()")
            return bookList
        }

        override fun getFirstBook(): Book {
            Log.d(tag, "getFirstBook()")
            return bookList[0]
        }

        override fun getBookCount(): Int {
            Log.d(tag, "getBookCount()")
            return bookList.size
        }

        override fun setBookPrice(book: Book?, price: Float) {
            Log.d(tag, "setBookPrice()")
            if (book != null) {
                bookList.forEach {
                    if (it.name == book.name) {
                        Log.d(tag,"找到相同的书籍$it")
                        it.price = price
                    }
                }
            }
        }

        override fun setBookName(book: Book?, name: String?) {
            Log.d(tag, "setBookName()")
            if (book != null) {
                bookList.forEach {
                    if (it.price == book.price) {
                        Log.d(tag,"找到相同的书籍$it")
                        it.name = name
                    }
                }
            }
        }

        override fun addBookIn(book: Book?) {
            Log.d(tag, "addBookIn()")
            book?.also {
                Log.d(tag,"$it")
                it.price = 11f
                bookList.add(it)
            }
        }

        override fun addBookOut(book: Book?) {
            Log.d(tag, "addBookOut()")
            book?.also {
                Log.d(tag,"$it")
                it.price = 22f
                bookList.add(it)
            }
        }

        override fun addBookInout(book: Book?) {
            Log.d(tag, "addBookInout()")
            book?.also {
                Log.d(tag,"$it")
                it.price = 33f
                bookList.add(it)
            }
        }
    }

    override fun onBind(intent: Intent): IBinder {
        Log.d(tag, "onBind")
        return bookManager
    }

    override fun onUnbind(intent: Intent?): Boolean {
        Log.d(tag, "onUnbind")
        return super.onUnbind(intent)
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(tag, "onDestroy")
    }
}