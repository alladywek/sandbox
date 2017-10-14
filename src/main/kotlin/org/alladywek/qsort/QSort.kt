package org.alladywek.qsort

class QSort {

    fun sort(arr: MutableList<Int>, lo: Int = 0, hi: Int = arr.lastIndex) {
        if (lo < hi) {
            val p = partition(arr, lo, hi)
            sort(arr, lo, p - 1)
            sort(arr, p, hi)
        }
    }

    private fun partition(arr: MutableList<Int>, lo: Int, hi: Int): Int {
        val pivot = getPivot(arr, lo, hi)
        var i = lo
        var j = hi
        while (i <= j) {
            while (arr[i] < pivot) {
                i++
            }
            while (arr[j] > pivot) {
                j--
            }
            if (i <= j) {
                val temp = arr[i]
                arr[i] = arr[j]
                arr[j] = temp
                i++
                j--
            }
        }
        return i
    }

    private fun getPivot(arr: MutableList<Int>, lo: Int, hi: Int): Int {
        return arr[lo].div(2) + arr[hi].div(2)
    }
}