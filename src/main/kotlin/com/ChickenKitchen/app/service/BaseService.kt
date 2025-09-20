package com.ChickenKitchen.app.service

interface BaseService<R, D, C, U, ID> {
    fun getAll(): List<R>?
    fun getById(id: ID): D?
    fun create(req: C): D
    fun update(id: ID, req: U): D
}

// R: Name of the response when get all
// D: Detail of the response when get by id
// C: Create request
// U: Update request
// ID: Type of the ID (Long, String, UUID, ...)