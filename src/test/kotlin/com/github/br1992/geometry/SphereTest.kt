package com.github.br1992.geometry

import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeInstanceOf

class SphereTest : FreeSpec() {

    init {
        "Sphere" - {
            "should intersect with ray inside of it" {
                val sphere = Sphere(pos3(0.0, 0.0, 0.0), 1.0)
                val ray = Ray3(pos3(0.0, 0.0, 0.0), vec3(1.0, 1.0, 1.0))

                sphere.intersects(ray, 0.0, Double.POSITIVE_INFINITY).shouldBeInstanceOf<SurfaceIntersection>()
            }

            "should intersect with ray crossing through it" {
                val sphere = Sphere(pos3(0.0, 0.0, 0.0), 1.0)
                val ray = Ray3(pos3(0.5, 0.5, 2.0), vec3(0.0, 0.0, -1.0))

                sphere.intersects(ray, 0.0, Double.POSITIVE_INFINITY).shouldBeInstanceOf<SurfaceIntersection>()
            }

            "should intersect with ray tangent to it" {
                val sphere = Sphere(pos3(0.0, 0.0, 0.0), 1.0)
                val ray = Ray3(pos3(.99999, 0.0, 2.0), vec3(0.0, 0.0, -1.0))

                sphere.intersects(ray, 0.0, Double.POSITIVE_INFINITY).shouldBeInstanceOf<SurfaceIntersection>()
            }

            "should not intersect with ray facing away from it" {
                val sphere = Sphere(pos3(0.0, 0.0, 0.0), 1.0)
                val ray = Ray3(pos3(1.0, 0.0, 2.0), vec3(0.0, 0.0, 1.0))

                sphere.intersects(ray, 0.0, Double.POSITIVE_INFINITY).shouldBeInstanceOf<NoIntersection>()
            }

            "should not intersect with ray that misses it" {
                val sphere = Sphere(pos3(0.0, 0.0, 0.0), 1.0)
                val ray = Ray3(pos3(0.0, 0.0, 2.0), vec3(0.0, 1.0, 1.0))

                sphere.intersects(ray, 0.0, Double.POSITIVE_INFINITY).shouldBeInstanceOf<NoIntersection>()
            }
        }
    }

}
