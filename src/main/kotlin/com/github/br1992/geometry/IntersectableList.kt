package com.github.br1992.geometry

data class IntersectableList(val intersectables: List<Intersectable>): Intersectable {
    override fun intersects(ray: Ray3, tMin: Double, tMax: Double): Intersection {
        var closestTDistance = tMax
        var intersection: Intersection = NoIntersection

        intersectables.forEach { intersectable ->
            val maybeIntersection = intersectable.intersects(ray, tMin, closestTDistance)
            if (maybeIntersection is SurfaceIntersection) {
                intersection = maybeIntersection
                closestTDistance = maybeIntersection.tDistance
            }
        }

        return intersection
    }

}
