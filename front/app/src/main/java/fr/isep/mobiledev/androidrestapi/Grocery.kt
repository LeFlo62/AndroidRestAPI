package fr.isep.mobiledev.androidrestapi

class Grocery(val name : String, val quantity : Int) {
    var id : Long = 0

    constructor(id : Long, name : String, quantity : Int) : this(name, quantity){
        this.id = id
    }
}