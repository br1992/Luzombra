package com.github.br1992.material

import com.github.br1992.geometry.RGB
import com.github.br1992.geometry.Ray3
import com.github.br1992.geometry.SurfaceIntersection
import com.github.br1992.geometry.isNearZero
import com.github.br1992.geometry.randomUnitVec3InSphere
import org.jetbrains.kotlinx.multik.ndarray.operations.plus

class Lambertian(private val color: RGB) : Material {

    override fun scatter(inwardRay: Ray3, intersection: SurfaceIntersection): ScatterResponse {
        val scatterDirection = (intersection.normal + randomUnitVec3InSphere())
            .takeIf { !it.isNearZero() }
            ?: intersection.normal

        return ScatterResponse(
            Ray3(intersection.point, scatterDirection),
            color
        )
    }
}
