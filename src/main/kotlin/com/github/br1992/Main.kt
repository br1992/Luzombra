@file:JvmName("Main")

package com.github.br1992

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.options.default
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.types.file
import com.github.br1992.imageio.image
import com.github.br1992.imageio.writeToFile
import com.github.br1992.geometry.Ray3
import com.github.br1992.geometry.rayColor
import com.github.br1992.geometry.vec3
import java.io.File
import java.nio.file.Files
import java.nio.file.Path
import org.jetbrains.kotlinx.multik.ndarray.operations.div
import org.jetbrains.kotlinx.multik.ndarray.operations.minus
import org.jetbrains.kotlinx.multik.ndarray.operations.plus
import org.jetbrains.kotlinx.multik.ndarray.operations.times
import org.springframework.context.annotation.ComponentScan

abstract class Renderer : CliktCommand()

class TestImage : CliktCommand(name = "test-image") {
//    val width by option().int().default(256)
//    val height by option().int().default(256)
    val outputFile: File by option().file().default(Files.createFile(Path.of("/Users/brivera/projects/luzombra/target/image.png")).toFile())

    override fun run() {
        val aspectRation = 16.0 / 9.0
        val width = 400
        val height = (width / aspectRation).toInt()


        val viewportHeight = 2.0
        val viewportWidth = aspectRation * viewportHeight
        val focalLength = 1.0

        val origin = vec3(0.0, 0.0, 0.0)
        val horizontal = vec3(viewportWidth, 0.0, 0.0)
        val vertical = vec3(0.0, viewportHeight, 0.0)
        val upperLeftCorner = origin - horizontal.div(2.0) + vertical.div(2.0) - vec3(0.0, 0.0, focalLength)

        val image = image(width, height) { pixel ->
            val u = pixel.x.toDouble() / (width-1)
            val v = (height - 1 - pixel.y).toDouble() / (height-1)
            val ray = Ray3(origin, upperLeftCorner + horizontal * u - vertical * v - origin)
            val pixelColor = rayColor(ray)
            pixelColor
        }
        writeToFile(image, outputFile)
    }

}

fun main(args: Array<String>) {
    TestImage().main(args)
}

@ComponentScan
class MainConfig