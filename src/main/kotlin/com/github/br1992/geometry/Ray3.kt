package com.github.br1992.geometry

import org.jetbrains.kotlinx.multik.ndarray.operations.plus
import org.jetbrains.kotlinx.multik.ndarray.operations.times

data class Ray3(val origin: Pos3, val direction: Vec3) {
    fun at(tDistance: Double): Pos3 {
        return origin + direction * tDistance
    }
}
