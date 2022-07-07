package com.github.br1992.material

import com.github.br1992.geometry.RGB
import com.github.br1992.geometry.Ray3
import com.github.br1992.geometry.SurfaceIntersection

interface Material {

    fun scatter(inwardRay: Ray3, intersection: SurfaceIntersection): ScatterResponse?
}

data class ScatterResponse(
    val ray: Ray3,
    val attenuation: RGB
)
