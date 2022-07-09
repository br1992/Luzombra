package com.github.br1992.geometry

import com.github.br1992.material.Material
import org.jetbrains.kotlinx.multik.api.linalg.dot
import org.jetbrains.kotlinx.multik.ndarray.operations.unaryMinus

interface Intersectable {
    fun intersects(ray: Ray3, tMin: Double, tMax: Double): Intersection
}

sealed interface Intersection

object NoIntersection : Intersection
data class SurfaceIntersection(
    val point: Pos3,
    val normal: Vec3,
    val tDistance: Double,
    val material: Material,
    val isFrontFace: Boolean
) : Intersection

fun determineNormalOrientation(ray: Ray3, outwardNormal: Vec3): Pair<Boolean, Vec3> {
    val frontFace = (ray.direction dot outwardNormal) < 0
    val normal = if (frontFace) outwardNormal else outwardNormal.unaryMinus()

    return frontFace to normal
}
