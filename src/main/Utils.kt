package main

import java.math.BigInteger
import java.security.MessageDigest
import kotlin.io.path.Path
import kotlin.io.path.readLines
import kotlin.math.max
import kotlin.math.min

/**
 * Reads lines from the given input txt file.
 */
fun readInput(name: String) = Path("src/main/$name.txt").readLines()

/**
 * Converts string to md5 hash.
 */
fun String.md5() = BigInteger(1, MessageDigest.getInstance("MD5").digest(toByteArray()))
    .toString(16)
    .padStart(32, '0')

/**
 * The cleaner shorthand for printing output.
 */
fun Any?.println() = println(this)
fun gcd(k: Long, m: Long): Long {
    var x = max(k, m)
    var y = min(k, m)
    while (y != 0L) x = y.also { y = x % y }
    return x
}
fun lcm(x: Long, y: Long): Long {
    return (x * y) / gcd(x, y)
}
