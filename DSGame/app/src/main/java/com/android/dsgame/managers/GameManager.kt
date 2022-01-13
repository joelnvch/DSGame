package com.android.dsgame.managers

import com.android.dsgame.activities.MyApplication
import com.chaquo.python.PyObject
import com.android.dsgame.activities.MyApplication.Companion.board
import com.android.dsgame.activities.MyApplication.Companion.pyBoard
import com.android.dsgame.model.Board
import com.android.dsgame.model.Card
import com.chaquo.python.Python

object GameManager {
    val ALL_CARDS = mutableMapOf<String, MutableMap<Int, Card>>()
    val COLORS = arrayOf("blue", "black", "yellow", "red", "green", "purple", "orange")
    val ALL_EXTRA_CARDS = mutableMapOf<String, Card>()
    val EXTRA_CARD_COLORS = arrayOf("blue", "white","black", "yellow", "red", "green", "purple", "orange")
    val COUNTRIES = listOf<String>("España", "Alemania", "Francia", "Italia", "Austria", "Grecia")

    fun initBoard() {
        val pythonFile = Python.getInstance().getModule("board")
        pyBoard = pythonFile.callAttr("init_board", "cardData")
        board = Board(pyBoard)
    }

    fun getBestCards(color: String): MutableList<Card> {
        val pyBestCards = pyBoard.callAttr("get_best_cards", color).asList()
        return transformPyCardList(pyBestCards)
    }

    fun getCardsByColor(color: String): MutableList<Card> {
        return ALL_CARDS.getValue(color).values.toMutableList()
    }

    fun setCard(color: String, cardId: Int) {
        // update py object
        pyBoard.callAttr("set_card", color, cardId)

        // update view object
        board.spots[color] = ALL_CARDS.getValue(color).getValue(cardId)
        board.score = pyBoard.getValue("score").toInt()
    }

    fun addExtraCard(color: String){
        pyBoard.callAttr("add_extra_card", color)

        board.extraCards[color] = ALL_EXTRA_CARDS.getValue(color)
        board.score = pyBoard.getValue("score").toInt()
    }

    fun removeExtraCard(color: String){
        pyBoard.callAttr("remove_extra_card", color)

        board.extraCards.remove(color)
        board.score = pyBoard.getValue("score").toInt()
    }

    fun updateBoard(board: Board){
        MyApplication.board = board
        pyBoard.callAttr("update_board", board)
    }

    fun createCompanyMessage(card: Card?): String{
        var message = ""
        if (card != null){
            message = "Creas una compañía basada en "
            when (card.name) {
                "IoT company" -> message += "IoT, Internet of Things. "
                "Telecom" -> message += "telecomunicaciones, proveyendo la infraestructura para conectar a personas y cosas unas con otras. "
                "Marketing" -> message += "proveer servicios de marketing. "
                "Energy" -> message += "la producción y venta de energía. "
                "Pharmaceutical" -> message += "farmacéutica, descubriendo, desarrollando y produciendo medicaciones. "
                "Finance" -> message += "finanzas, aceptando depósitos o realizando préstamos a clientes y empresas."
                "Farming company" -> message += "la ganadería, construyendo herramientas y habilidades según datos de animales y plantas. "
                "Government" -> message += "el gobierno, usando la ciencia de los datos para mejorar los servicios prestados a las personas."
                "Insurance" -> message += "seguros, compensando distintos siniestros acordados a cambio de pagos de primas. "
                "Transport" -> message += "transporte, usando la ciencia de datos para ayudar a las empresas de transporte a mejorar sus servicios. "
            }
            message += " La fundas en " + COUNTRIES.random() + "."
        }
        return message
    }

    // AUX
    private fun transformPyCardList(pyCards: List<PyObject>): MutableList<Card>{
        val cards = mutableListOf<Card>()

        for (pyCard in pyCards)
            cards.add(Card(pyCard))

        return cards
    }

}
