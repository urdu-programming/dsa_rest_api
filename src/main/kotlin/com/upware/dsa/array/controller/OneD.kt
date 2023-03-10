package com.upware.dsa.array.controller

import org.springframework.web.bind.annotation.*
import java.io.File
import java.io.Serializable

@RestController
@CrossOrigin
@RequestMapping("/1d")
class OneD {
    var size: Int = 5;
    val file = File("D:1dArray.txt")
    var array: Array<String?> = arrayOfNulls(size)

    @GetMapping("/getAll")
    fun getAll(): Array<String?> {
        getData()
        return array
    }

    @GetMapping("/{index}")
    fun getById(@PathVariable("index") index: Int): String {
        getData()
        return if (index <= size) array[index - 1].toString() else "Value not exists"
    }

    @GetMapping("/search/{value}")
    fun search(@PathVariable("value") value: String): String {
        getData()
        return if (array.contains(value)) "${array.indexOf(value) + 1}: $value" else "Not exists"
    }

    @PostMapping("/{value}")
    fun insert(@PathVariable("value") value: String): Serializable {
        getData()
        try {
            array[array.indexOf(null)] = value
            file.appendText(value + "\n")
        } catch (e: Exception) {
            if (array.contains("null")) {
                update(array.indexOf("null") + 1, value)
                return getAll()
            }
            return "Array is full!"
        }

        return "$value is inserted"
    }

    @PutMapping("/{id}/{value}")
    fun update(@PathVariable("id") id: Int, @PathVariable("value") value: String): Array<String?> {
        getData()
        array[id - 1] = value
        insertToFile()
        return array
    }

    @DeleteMapping("/{id}")
    fun delete(@PathVariable("id") id: Int): Array<String?> {
        getData()
        array[id - 1] = null
        for (i in id - 1..4) {
            if (i + 1 <= 4) {
                var temp = array[i]
                array[i] = array[i + 1]
                array[i + 1] = temp
            }
        }
        insertToFile()
        return array
    }

    private fun getData() {
        var i: Int = 0
        file.forEachLine {
            if (i > 4) return@forEachLine
            array[i] = it
            i++
        }
    }
    private fun insertToFile() {
        file.bufferedWriter().flush()
        array.forEach {
            file.appendText(it + "\n")
        }
    }

}