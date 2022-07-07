package com.github.br1992.geometry

import com.github.br1992.material.Material
import org.jetbrains.kotlinx.multik.api.linalg.dot
import org.jetbrains.kotlinx.multik.api.mk
import org.jetbrains.kotlinx.multik.ndarray.operations.div
import org.jetbrains.kotlinx.multik.ndarray.operations.minus
import kotlin.math.sqrt

data class Sphere(val center: Pos3, val radius: Double, val material: Material) : Intersectable {
    override fun intersects(ray: Ray3, tMin: Double, tMax: Double): Intersection {
        val rayToSphereCenter = ray.origin - center
        val a = ray.direction.lengthSquared()
        val halfB = mk.linalg.dot(rayToSphereCenter, ray.direction)
        val c = rayToSphereCenter.lengthSquared() - radius * radius
        val discriminant = halfB * halfB - a * c

        return if (discriminant < 0) {
            NoIntersection
        } else {
            val discriminantSqrt = sqrt(discriminant)
            var tDistance = (-halfB - discriminantSqrt) / a
            if (tDistance < tMin || tDistance > tMax) {
                tDistance = (-halfB + discriminantSqrt) / a
                if (tDistance < tMin || tDistance > tMax)
                    return NoIntersection
            }
            val pointOnSphere = ray.at(tDistance)
            val outwardNormal = (pointOnSphere - center) / radius
            val (isFrontFace, normal) = determineNormalOrientation(ray, outwardNormal)
            SurfaceIntersection(
                point = pointOnSphere,
                normal = normal,
                tDistance = tDistance,
                material = material,
                isFrontFace = isFrontFace
            )
        }
    }
}
