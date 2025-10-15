package com.ChickenKitchen.app.service.transaction

import com.ChickenKitchen.app.model.dto.response.TransactionResponse


interface TransactionService {

    fun getAll() : List<TransactionResponse>?
    fun getById(id : Long) : TransactionResponse
}