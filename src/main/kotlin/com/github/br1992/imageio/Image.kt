package com.github.br1992.imageio

import com.github.br1992.geometry.RGB
import com.github.br1992.geometry.toAWT
import com.sksamuel.scrimage.ImmutableImage
import com.sksamuel.scrimage.nio.PngWriter
import com.sksamuel.scrimage.pixels.Pixel
import kotlinx.coroutines.flow.map
import org.jetbrains.kotlinx.multik.ndarray.operations.div
import org.jetbrains.kotlinx.multik.ndarray.operations.map
import org.jetbrains.kotlinx.multik.ndarray.operations.times
import java.io.File
import java.lang.Integer.min
import java.util.concurrent.ExecutorService
import kotlin.math.sqrt

fun image(width: Int, height: Int, executor: ExecutorService, block: (Int, Int) -> RGB): ImmutableImage {
//    val pixelIndices = 0.until(height).flatMap { v ->
//        0.until(width).map { u ->
//            u to v
//        }
//    }
//
//    val pixelFutures = pixelIndices.map { (u, v) ->
//        executor.submit<Pixel> {
//            if (u == 1) {
//                println("Processing Pixel row $v")
//            }
//            val color = block(u, v).toAWT()
//            Pixel(u, v, color.red, color.green, color.blue, color.alpha)
//        }
//    }
//
//    val pixels = pixelFutures.map { it.get() }.sortedWith(PixelComparator()).toTypedArray()

    println("$width, $height")

    val fragments = buildList<RenderFragment> {
        var uStartIndex = 0

        while (uStartIndex < width) {
            val uNext = uStartIndex + fragmentWidth
            var vStartIndex = 0

            while (vStartIndex < height) {
                println("($uStartIndex, $vStartIndex)")
                val vNext = vStartIndex + fragmentHeight

                add(
                    RenderFragment(
                        uStartIndex.rangeTo(min(uNext - 1, width - 1)),
                        vStartIndex.rangeTo(min(vNext - 1, height - 1))
                    )
                )

                vStartIndex = vNext
            }

            uStartIndex = uNext
        }
    }

    val pixels = fragments.map { fragment ->
        executor.submit<List<Pixel>> {
            fragment.render(block)
        }
    }.flatMap { it.get() }.sortedWith(PixelComparator()).toTypedArray()

    return ImmutableImage.create(width, height, pixels)
}

const val fragmentHeight = 32
const val fragmentWidth = 32

data class RenderFragment(val uRange: IntRange, val vRange: IntRange) {

    fun render(block: (Int, Int) -> RGB): List<Pixel> {
        println("Starting Fragment (${uRange.first},${vRange.first})->(${uRange.last},${vRange.last})")
        return uRange.flatMap { u ->
            vRange.map { v ->
                val color = block(u, v).toAWT()
                Pixel(u, v, color.red, color.green, color.blue, color.alpha)
            }
        }
    }
}

class PixelComparator : Comparator<Pixel> {
    override fun compare(o1: Pixel, o2: Pixel): Int {
        return if (o1.y == o2.y) {
            o1.x - o2.x
        } else if (o1.y > o2.y) {
            1
        } else {
            -1
        }
    }
}

fun writeToFile(image: ImmutableImage, file: File) {
    image.output(PngWriter.NoCompression, file)
}

fun RGB.normalizeColor(sampleCount: Int): RGB {
    val scaleFactor = 1.0 / sampleCount

    return this.map { sqrt(it * scaleFactor) }
}
