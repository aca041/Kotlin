package conditionals

fun main (args: Array<String>){
    val age: Int = 24
    if (age < 18){
        println("you cannot register")
    }else if (age <21){
        println("maybe you can register")
    }else if (age <27){
        println("maybe you can register")
    }else {
        println("you are good to go")
    }
println("end")
}