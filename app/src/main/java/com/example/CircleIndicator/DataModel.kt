package com.example.CircleIndicator

class DataModel {
    var name: String?=null
    var price: String?=null
    var status: String?=null
    var quantity: String?=null
    var image:String?=null
    var category:String?=null

    constructor()

    constructor(name: String?, price: String?, status: String?, quantity: String?, image: String?, category: String?) {
        this.name = name
        this.price = price
        this.status = status
        this.quantity = quantity
        this.image = image
        this.category = category
    }

    fun toMap(): Map<String, Any> {
        val result = HashMap<String, Any>()
        result.put("name", name!!)
        result.put("price", price!!)
        result.put("status", status!!)
        result.put("quantity", quantity!!)
        result.put("image", image!!)
        result.put("category", category!!)
        return result
    }

}