@file:JvmName("Main")

package com.github.br1992

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.options.default
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.types.file
import com.github.br1992.geometry.IntersectableList
import com.github.br1992.geometry.Pos3
import com.github.br1992.geometry.RGB
import com.github.br1992.imageio.image
import com.github.br1992.imageio.writeToFile
import com.github.br1992.geometry.Ray3
import com.github.br1992.geometry.Sphere
import com.github.br1992.geometry.pos3
import com.github.br1992.geometry.rayColor
import com.github.br1992.geometry.rgb
import com.github.br1992.geometry.vec3
import com.github.br1992.imageio.normalizeColor
import java.io.File
import java.nio.file.Files
import java.nio.file.Path
import java.util.concurrent.Executors
import java.util.concurrent.Executors.newFixedThreadPool
import java.util.concurrent.ForkJoinPool
import java.util.concurrent.ThreadPoolExecutor
import java.util.concurrent.TimeUnit
import org.apache.commons.math3.distribution.UniformRealDistribution
import org.apache.commons.math3.fraction.Fraction
import org.jetbrains.kotlinx.multik.api.JvmEngineType
import org.jetbrains.kotlinx.multik.api.NativeEngineType
import org.jetbrains.kotlinx.multik.api.mk
import org.jetbrains.kotlinx.multik.ndarray.operations.div
import org.jetbrains.kotlinx.multik.ndarray.operations.minus
import org.jetbrains.kotlinx.multik.ndarray.operations.plus
import org.jetbrains.kotlinx.multik.ndarray.operations.times
import org.springframework.context.annotation.ComponentScan
import kotlin.time.ExperimentalTime
import kotlin.time.measureTimedValue

abstract class Renderer : CliktCommand()

class TestImage : CliktCommand(name = "test-image") {
//    val width by option().int().default(256)
//    val height by option().int().default(256)
    val outputFile: File by option().file().default(Files.createFile(Path.of("/Users/brivera/projects/luzombra/target/image.png")).toFile())

    @OptIn(ExperimentalTime::class)
    override fun run() {
        mk.setEngine(JvmEngineType)

        val aspectRatio = Fraction(16, 9)
        val width = 400
        val height = (width / aspectRatio.toDouble()).toInt()
        val samplesPerPixel = 100
        val randomUnitGenerator = UniformRealDistribution(0.0, 1.0)

        val camera = Camera(Fraction(16, 9), 2.0, 1.0)

        val world = IntersectableList(listOf(
            Sphere(pos3(0.0 ,0.0,-1.0), 0.5),
            Sphere(pos3(0.0,-100.5,-1.0), 100.0)
        ))

        val executor = newFixedThreadPool(8)

        val image = measureTimedValue {
            image(width, height, executor) { x, y ->
                var pixelColor = rgb(0.0, 0.0, 0.0)
                repeat(samplesPerPixel) {
                    val u = (x.toDouble() + randomUnitGenerator.sample()) / (width-1)
                    val v = (y.toDouble() + randomUnitGenerator.sample()) / (height-1)
                    val ray = camera.rayThroughPixel(u, v)
                    pixelColor += rayColor(ray, world, 50)
                }
                pixelColor.normalizeColor(samplesPerPixel)
            }
        }
        println("${mk.engine} to ${image.duration} to render image")
        writeToFile(image.value, outputFile)
        executor.awaitTermination(1L, TimeUnit.SECONDS)
    }

}

fun main(args: Array<String>) {
    TestImage().main(args)
}

@ComponentScan
class MainConfig