package com.example.g2

class MyCipher {
    companion object {
        fun encrypt(string: String, cipherOf: Int = 1): String {
            var output = "";
            string.indices.map { string[it] + cipherOf }.forEach { output += it.toString() }
            return output
        }

        fun decrypt(string: String,cipherOf: Int = 1): String {
            var output = "";
            string.indices.map { string[it] - cipherOf }.forEach { output += it.toString() }
            return output
        }
    }
}

fun main() {
    println(MyCipher.encrypt("JosteveAdekanbiIsABoyAndHeIsGood.49",3000))
    println(MyCipher.decrypt(MyCipher.encrypt("JosteveAdekanbiIsABoyAndHeIsGood.49",3000),3000))
}