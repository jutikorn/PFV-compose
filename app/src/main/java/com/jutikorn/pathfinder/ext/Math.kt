package com.jutikorn.pathfinder.ext

fun gcd(x: Int, y: Int): Int {
    var n1 = x
    var n2 = y

    while (n1 != n2) {
        if (n1 > n2)
            n1 -= n2
        else
            n2 -= n1
    }
    return n1
}

/**
 * `probability` should be given as a percentage, such as
 * 10.0 (10.0%) or 25.5 (25.5%). As an example, if `probability`
 * is 60% (60.0), 100 calls to this function should return ~60
 * `true` values.
 * (Note that Math.random returns a value >= 0.0 and < 1.0.)
 */
fun getRandomBoolean(probability: Double): Boolean {
    val randomValue = Math.random() * 100 //0.0 to 99.9
    return randomValue <= probability
}