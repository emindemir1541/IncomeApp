package com.example.gelirgideruygulamas.helperlibrary.common.helper

object TestHelper {
    fun failIfNotEqual(data:Any,condition:Any){
        assert(data==condition){data}
        println("Data: $data")
        println("Condition: $condition")
    }

    fun failIfEqual(data:Any,condition:Any){
        assert(data!=condition){data}
        println("Data: $data")
        println("Condition: $condition")
    }
}