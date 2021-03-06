package com.android.dsgame.activities

import android.app.Application
import com.android.dsgame.model.Board
import com.android.dsgame.model.Card
import com.android.dsgame.managers.GameManager
import com.chaquo.python.PyObject
import com.chaquo.python.Python
import com.chaquo.python.android.AndroidPlatform


class MyApplication : Application(){
    companion object{
        lateinit var PACKAGE_NAME: String
        lateinit var pyBoard: PyObject
        lateinit var board: Board
    }

    override fun onCreate(){
        super.onCreate()
        PACKAGE_NAME = packageName
        initPython()
        GameManager.initBoard()

        // SAVE ALL CARDS VALUE
        var map : MutableMap<Int, Card>
        for (item in pyBoard.getValue("all_cards").asMap()){
            map = mutableMapOf()
            for (item2 in item.value.asMap())
                map[item2.key.toInt()] = Card(item2.value)
            GameManager.ALL_CARDS[item.key.toString()] = map
        }

        // SAVE ALL EXTRA CARDS VALUE
        for (item in pyBoard.getValue("all_extra_cards").asMap())
            GameManager.ALL_EXTRA_CARDS[item.key.toString()] = Card(item.value)
    }

    private fun initPython(){
        if (!Python.isStarted()){
            Python.start(AndroidPlatform(this))
        }
    }
}