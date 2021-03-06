package luzombra.geometry.linearAlgebra

/** Mat4x4 that translates vectors
  *
  * @param x translation along x-axis
  * @param y translation along y-axis
  * @param z translation along z-axis
  */
class TransMat(val x: Float, val y: Float, val z: Float) extends Mat4x4(
  (1, 0, 0, 0), (0, 1, 0, 0), (0, 0, 1, 0), (x, y, z, 1)) {

  /** Returns a transMat that reverses the translation by this transMat */
  override val inv: TransMat = TransMat(-x, -y, -z)

}

object TransMat {

  /** Returns a new transMat */
  def apply(x: Float, y: Float, z: Float): TransMat = new TransMat(x, y, z)

}
