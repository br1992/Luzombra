package com.github.br1992.material

import com.github.br1992.geometry.Ray3
import com.github.br1992.geometry.SurfaceIntersection
import com.github.br1992.geometry.reflect
import com.github.br1992.geometry.refract
import com.github.br1992.geometry.rgb
import com.github.br1992.geometry.unitVector
import org.jetbrains.kotlinx.multik.api.linalg.dot
import org.jetbrains.kotlinx.multik.ndarray.operations.unaryMinus
import java.lang.StrictMath.pow
import kotlin.math.min
import kotlin.math.sqrt
import kotlin.random.Random

class Dielectric(private val refractionIndex: Double) : Material {

    override fun scatter(inwardRay: Ray3, intersection: SurfaceIntersection): ScatterResponse? {
        val attenuation = rgb(1.0, 1.0, 1.0)
        val refractionRatio = if (intersection.isFrontFace) 1.0 / refractionIndex else refractionIndex

        val unitDirection = inwardRay.direction.unitVector()
        val angleCos = min(-unitDirection dot intersection.normal, 1.0)
        val angleSin = sqrt(1.0 - angleCos * angleCos)

        val canRefract = refractionRatio * angleSin <= 1.0

        return ScatterResponse(
            Ray3(
                intersection.point,
                if (canRefract && reflectance(angleCos, refractionIndex) <= Random.nextDouble()) {
                    refract(unitDirection, intersection.normal, refractionRatio)
                } else {
                    reflect(unitDirection, intersection.normal)
                }
            ),
            attenuation
        )
    }
}

internal fun reflectance(angleCos: Double, refractionIndex: Double): Double {
    var r0 = (1 - refractionIndex) / (1 + refractionIndex)
    r0 *= r0
    return r0 + (1 - r0) * pow(1 - angleCos, 5.0)
}
