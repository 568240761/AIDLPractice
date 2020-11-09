package com.example.aidldemo

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import kotlinx.android.synthetic.main.activity_main.*
import kotlin.random.Random

class MainActivity : AppCompatActivity() {

    private val tag = this.javaClass.simpleName

    private val bookNameArray = arrayOf("西游记", "解忧杂货店", "贫穷的本质", "围城")

    private lateinit var bookManager: BookManager

    private lateinit var bookList: List<Book>

    private fun isCreateBookManager() = this::bookManager.isInitialized

    private fun isCreateBookList() = this::bookList.isInitialized

    private val inBook by lazy {
        val book = Book()
        book.price = 24.6f
        book.name = "设计模式"
        book
    }

    private val outBook by lazy {
        val book = Book()
        book.price = 26.3f
        book.name = "博弈论"
        book
    }

    private val inoutBook by lazy {
        val book = Book()
        book.price = 20.8f
        book.name = "自卑与超越"
        book
    }

    private val serviceConnection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            Log.d(tag, "连接成功")
            bookManager = BookManager.Stub.asInterface(service)

            if (isCreateBookManager()) {
                bookList = bookManager.books
                Log.d(tag, "获得书单")
            }
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            Log.d(tag, "连接断开")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val intent = Intent(this, AIDLService::class.java)
        bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE)

        addOnClickListener()
    }

    private fun addOnClickListener() {
        //getBooks
        button.setOnClickListener {
            Log.d(tag, "点击了“书单”按钮")
            if (isCreateBookManager()) {
                bookList = bookManager.books
                Log.d(tag, "获得书单")
            }
        }

        //getFirstBook
        button2.setOnClickListener {
            Log.d(tag, "点击了“第一本书”按钮")
            if (isCreateBookManager()) {
                val book = bookManager.firstBook
                Log.d(tag, book.toString())
            }
        }

        //getBookCount
        button3.setOnClickListener {
            Log.d(tag, "点击了“书本数量”按钮")
            if (isCreateBookManager()) {
                val count = bookManager.bookCount
                Log.d(tag, count.toString())
            }
        }

        //setBookPrice
        button4.setOnClickListener {
            Log.d(tag, "点击了“设置价格”按钮")
            if (isCreateBookManager() && isCreateBookList()) {
                val price = Random.nextFloat() * 100f
                Log.d(tag, "生成随机价格：$price")
                bookManager.setBookPrice(bookList[0], price)
            }
        }

        //setBookName
        button5.setOnClickListener {
            Log.d(tag, "点击了“设置书名”按钮")
            if (isCreateBookManager() && isCreateBookList()) {
                val index = Random.nextInt(4)
                val name = bookNameArray[index]
                Log.d(tag, "随机书名：$name")
                bookManager.setBookName(bookList[0], name)
            }
        }

        //addBookIn
        button6.setOnClickListener {
            Log.d(tag, "点击了“添加书(in)”按钮")
            if (isCreateBookManager()) {
                bookManager.addBookIn(inBook)
            }
        }

        //addBookOut
        button7.setOnClickListener {
            Log.d(tag, "点击了“添加书(out)”按钮")
            if (isCreateBookManager()) {
                bookManager.addBookOut(outBook)
            }
        }

        //addBookInout
        button8.setOnClickListener {
            Log.d(tag, "点击了“添加书(inout)”按钮")
            if (isCreateBookManager()) {
                bookManager.addBookInout(inoutBook)
            }
        }

        button9.setOnClickListener {
            Log.d(tag, "点击了“打印书单内容”按钮")

            if (isCreateBookList()) {
                bookList.forEach {
                    Log.d(tag, it.toString())
                }
            }

            Log.d(tag, "inBook=$inBook")
            Log.d(tag, "outBook=$outBook")
            Log.d(tag, "inoutBook=$inoutBook")
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        unbindService(serviceConnection)
    }
}