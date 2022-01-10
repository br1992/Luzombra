package com.github.br1992.imageio

import com.github.br1992.geometry.RGB
import com.github.br1992.geometry.toAWT
import com.sksamuel.scrimage.ImmutableImage
import com.sksamuel.scrimage.nio.PngWriter
import com.sksamuel.scrimage.pixels.Pixel
import java.io.File
import java.util.concurrent.CompletableFuture
import java.util.concurrent.Executor
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import java.util.concurrent.ForkJoinPool
import java.util.concurrent.Future
import java.util.concurrent.TimeUnit
import kotlinx.coroutines.Job
import kotlinx.coroutines.asCoroutineDispatcher
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.flatMapMerge
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.runBlocking
import org.jetbrains.kotlinx.multik.ndarray.operations.div
import org.jetbrains.kotlinx.multik.ndarray.operations.map
import org.jetbrains.kotlinx.multik.ndarray.operations.times
import kotlin.math.nextDown
import kotlin.math.sqrt

fun image(width: Int, height: Int, block: (Int, Int) -> RGB): ImmutableImage {
//    val image = ImmutableImage.create(width, height)
//
//    return image.map { pixel ->
//        block(pixel.x, pixel.y).toAWT()
//    }

    val executor = ForkJoinPool(8)

    val pixelIndices = 0.until(height).flatMap { v ->
        0.until(width).map { u ->
            u to v
        }
    }

    val pixels = runBlocking(executor.asCoroutineDispatcher()) {
        pixelIndices.asFlow().flatMapMerge(8) { (u, v) ->
            if (u == 1) {
                println("Processing Pixel row $v")
            }
            val color = block(u, v).toAWT()
            flowOf(Pixel(u, v, color.red, color.green, color.blue, color.alpha))
        }.toList().sortedWith(PixelComparator()).toTypedArray()
    }

    executor.awaitTermination(15L, TimeUnit.SECONDS)
//
    return ImmutableImage.create(width, height, pixels)
}

class PixelComparator: Comparator<Pixel> {
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