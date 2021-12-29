package com.github.br1992.imageio

import com.github.br1992.geometry.RGB
import com.github.br1992.geometry.toAWT
import com.sksamuel.scrimage.ImmutableImage
import com.sksamuel.scrimage.nio.PngWriter
import com.sksamuel.scrimage.pixels.Pixel
import java.io.File

fun image(width: Int, height: Int, block: (Pixel) -> RGB): ImmutableImage {
    val image = ImmutableImage.create(width, height)

    return image.map { pixel ->
        block(pixel).toAWT()
    }
}

fun writeToFile(image: ImmutableImage, file: File) {
    image.output(PngWriter.NoCompression, file)
}
