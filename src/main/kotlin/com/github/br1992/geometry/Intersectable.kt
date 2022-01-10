package com.github.br1992.geometry

import org.jetbrains.kotlinx.multik.api.linalg.dot
import org.jetbrains.kotlinx.multik.api.mk
import org.jetbrains.kotlinx.multik.ndarray.operations.unaryMinus

interface Intersectable {
    fun intersects(ray: Ray3, tMin: Double, tMax: Double): Intersection
}

sealed interface Intersection

object NoIntersection : Intersection
data class SurfaceIntersection(val point: Pos3, val normal: Vec3, val tDistance: Double, val isFrontFace: Boolean) : Intersection

fun determineNormalOrientation(ray: Ray3, outwardNormal: Vec3): Pair<Boolean, Vec3> {
    val frontFace = mk.linalg.dot(ray.direction, outwardNormal) < 0
    val normal =  if (frontFace)  outwardNormal  else outwardNormal.unaryMinus()

    return frontFace to normal
}