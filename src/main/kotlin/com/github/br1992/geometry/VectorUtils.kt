package com.github.br1992.geometry

import org.apache.commons.math3.distribution.UniformRealDistribution
import org.jetbrains.kotlinx.multik.api.d1array
import org.jetbrains.kotlinx.multik.api.linalg.dot
import org.jetbrains.kotlinx.multik.api.mk
import org.jetbrains.kotlinx.multik.ndarray.data.D1Array
import org.jetbrains.kotlinx.multik.ndarray.data.get
import org.jetbrains.kotlinx.multik.ndarray.operations.div
import org.jetbrains.kotlinx.multik.ndarray.operations.map
import org.jetbrains.kotlinx.multik.ndarray.operations.minus
import org.jetbrains.kotlinx.multik.ndarray.operations.plus
import org.jetbrains.kotlinx.multik.ndarray.operations.sum
import org.jetbrains.kotlinx.multik.ndarray.operations.times
import org.jetbrains.kotlinx.multik.ndarray.operations.unaryMinus
import java.awt.Color
import kotlin.math.abs
import kotlin.math.min
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

fun rayColor(ray: Ray3, world: Intersectable, maxDepth: Int): RGB {
    if (maxDepth <= 0) return black

    val intersection = world.intersects(ray, 0.001, Double.POSITIVE_INFINITY)
    if (intersection is SurfaceIntersection) {
        val scatterResponse = intersection.material.scatter(ray, intersection)

        return if (scatterResponse != null) {
            return scatterResponse.attenuation * rayColor(scatterResponse.ray, world, maxDepth - 1)
        } else {
            black
        }
    }

    val unitDirection = unit3(ray.direction)
    val t = 0.5 * (unitDirection.y() + 1.0)
    return rgb(1.0, 1.0, 1.0) * (1.0 - t) + rgb(0.5, 0.7, 1.0) * t
}

fun pos3(x: Double, y: Double, z: Double): Pos3 = vec3(x, y, z)
fun rgb(r: Double, g: Double, b: Double): RGB = vec3(r, g, b)

val black = rgb(0.0, 0.0, 0.0)

fun randomVec3(min: Double, max: Double): Vec3 {
    val rgn = UniformRealDistribution(min, max)

    return vec3(rgn.sample(), rgn.sample(), rgn.sample())
}

fun randomVec3InUnitSphere(): Vec3 {
    var randomVector: Vec3?
    do {
        randomVector = randomVec3(-1.0, 1.0)
    } while (randomVector!!.lengthSquared() >= 1.0)

    return randomVector
}

fun randomUnitVec3InSphere(): Vec3 {
    return randomVec3InUnitSphere().unitVector()
}

fun randomUnitVec3InHemisphere(normal: Vec3): Vec3 {
    val randomVec3InSphere = randomUnitVec3InSphere()
    return if (randomVec3InSphere dot normal > 0.0) {
        randomVec3InSphere
    } else {
        - randomVec3InSphere
    }
}

fun reflect(inward: Vec3, normal: Vec3): Vec3 {
    return inward - 2 * (inward dot normal) * normal
}

fun refract(inward: Vec3, normal: Vec3, refractionRatio: Double): Vec3 {
    val cosTheta = min(-inward dot normal, 1.0)
    val vecPerpendicularToNormal = refractionRatio * (inward + cosTheta * normal)
    val vecParallelToNormal = -sqrt(abs(1.0 - vecPerpendicularToNormal.lengthSquared())) * normal

    return vecPerpendicularToNormal + vecParallelToNormal
}

fun RGB.toAWT(): Color {
    return Color(this.x().toFloat().coerceIn(0F, 1F), this.y().toFloat().coerceIn(0F, 1F), this.z().toFloat().coerceIn(0F, 1F))
}

fun Vec3.unitVector(): Vec3 {
    return this / this.length()
}

fun Vec3.lengthSquared(): Double {
    return this.map { it * it }.sum()
}

fun Vec3.length(): Double {
    return sqrt(this.lengthSquared())
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
