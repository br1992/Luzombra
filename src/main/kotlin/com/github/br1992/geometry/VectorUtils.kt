package com.github.br1992.geometry

import java.awt.Color
import org.jetbrains.kotlinx.multik.api.d1array
import org.jetbrains.kotlinx.multik.api.mk
import org.jetbrains.kotlinx.multik.ndarray.data.D1Array
import org.jetbrains.kotlinx.multik.ndarray.data.get
import org.jetbrains.kotlinx.multik.ndarray.operations.div
import org.jetbrains.kotlinx.multik.ndarray.operations.map
import org.jetbrains.kotlinx.multik.ndarray.operations.plus
import org.jetbrains.kotlinx.multik.ndarray.operations.sum
import org.jetbrains.kotlinx.multik.ndarray.operations.times
import kotlin.math.sqrt

fun vec3(x: Double, y: Double, z: Double): Vec3 {
    val vector = listOf(x, y, z)
    return mk.d1array(3) { index ->
        vector[index]
    }
}

fun unit3(vec: Vec3): Vec3 {
    return vec.div(vec.length())
}

fun rayColor(ray: Ray3): RGB {
    val unitDirection = unit3(ray.direction)
    val t = 0.5*(unitDirection.y() + 1.0)
    return rgb(1.0, 1.0, 1.0) * (1.0-t) + rgb(0.5, 0.7, 1.0) * t
}

fun pos3(x: Double, y: Double, z: Double): Pos3 = vec3(x, y, z)
fun rgb(r: Double, g: Double, b: Double): RGB = vec3(r, g, b)

fun RGB.toAWT(): Color {
    return Color(this.x().toFloat(), this.y().toFloat(), this.z().toFloat(), 1F)
}

fun Vec3.length(): Double {
    return sqrt(this.map { it * it }.sum())
}

fun Vec3.x(): Double {
    return this[0]
}

fun Vec3.y(): Double {
    return this[1]
}

fun Vec3.z(): Double {
    return this[2]
}

typealias Vec3 = D1Array<Double>
typealias Pos3 = Vec3
typealias RGB = Vec3