package com.github.br1992.material

import com.github.br1992.geometry.RGB
import com.github.br1992.geometry.Ray3
import com.github.br1992.geometry.SurfaceIntersection
import com.github.br1992.geometry.randomVec3InUnitSphere
import com.github.br1992.geometry.reflect
import com.github.br1992.geometry.unitVector
import org.jetbrains.kotlinx.multik.api.linalg.dot
import org.jetbrains.kotlinx.multik.ndarray.operations.plus
import org.jetbrains.kotlinx.multik.ndarray.operations.times

class Metal(private val color: RGB, fuzziness: Double = 0.0) : Material {
    private val fuzziness = fuzziness.coerceIn(0.0, 1.0)

    override fun scatter(inwardRay: Ray3, intersection: SurfaceIntersection): ScatterResponse? {
        val reflected = reflect(inwardRay.direction.unitVector(), intersection.normal)

        return ScatterResponse(
            Ray3(intersection.point, reflected + randomVec3InUnitSphere() * fuzziness),
            color
        ).takeIf { reflected dot intersection.normal > 0 }
    }

}