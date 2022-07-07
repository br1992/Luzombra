package com.github.br1992

import com.github.br1992.geometry.Ray3
import com.github.br1992.geometry.vec3
import org.apache.commons.math3.fraction.Fraction
import org.jetbrains.kotlinx.multik.ndarray.operations.div
import org.jetbrains.kotlinx.multik.ndarray.operations.minus
import org.jetbrains.kotlinx.multik.ndarray.operations.plus
import org.jetbrains.kotlinx.multik.ndarray.operations.times

class Camera(private val aspectRatio: Fraction, private val viewportHeight: Double, private val focalLength: Double) {

    private val viewportWidth = viewportHeight * aspectRatio.toDouble()
    private val origin = vec3(0.0, 0.0, 0.0)
    private val horizontal = vec3(viewportWidth, 0.0, 0.0)
    private val vertical = vec3(0.0, viewportHeight, 0.0)
    private val upperLeftCorner = origin - horizontal.div(2.0) + vertical.div(2.0) - vec3(0.0, 0.0, focalLength)

    fun rayThroughPixel(u: Double, v: Double): Ray3 {
        return Ray3(origin, upperLeftCorner + horizontal * u - vertical * v - origin)
    }
}
