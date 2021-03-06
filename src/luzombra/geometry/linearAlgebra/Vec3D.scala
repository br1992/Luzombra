package luzombra.geometry.linearAlgebra

/** 3-dimensional vector with common operations
  *
  * @constructor creates a new vector with coordinates x, y, z
  * @param x x-coordinate
  * @param y y-coordinate
  * @param z z-coordinate
  * @tparam A type of subclass of Vec3D
  */
abstract class Vec3D[A <: Vec3D[A]](val x: Float, val y: Float, val z: Float) extends Immutable {
  type Coor3 = (Float, Float, Float)

  /** Returns a new vector whose coordinates are the result of element-wise addition
    * between this vector and vec.
    * */
  def +(vec: A): A = this.map2(vec)(_ + _)

  /** Returns a new vector that is a scaled version of this vector. */
  def *[N](scalar: N)(implicit ev1: N => Float): A = this.map(_ * scalar)

  /** Returns a new vector whose coordinates are computed by applying a function
    * to each coordinate of this vector.
    */
  def map(f: Float => Float): A = fromCoor3(Vec3D.map(this.get)(f))

  /** Returns a new vector whose values are computed by applying a function to the
    * respective values of this vector and vec
    */
  def map2(vec: A)(f: (Float, Float) => Float): A = fromCoor3{
    Vec3D.map2(this.get, vec.get)(f)
  }

  /** Returns a new vector from a provided coor3 */
  def fromCoor3(vec: Coor3): A

  /** Returns true if both vectors are equivalent, false otherwise */
  def ==(vec: A): Boolean = {
    val a = this.get; val b = vec.get
    a._1 == b._1 && a._2 == b._2 && a._3 == b._3
  }

  /** Returns a Float whose value is computed by folding the values of this vector
    * with a function
    */
  def reduce(f: (Float, Float) => Float): Float = f(this.x, f(this.y, this.z))

  /** Returns a new coor3 with the coordinate values of this vector */
  val get: Coor3 = (this.x, this.y, this.z)

}

object Vec3D {
  type Coor3 = (Float, Float, Float)

  /** Returns a new coor3 whose values are computed by applying a function to the
    * respective values of a and b
    */
  def map2(a: Coor3, b: Coor3)(f: (Float, Float) => Float): Coor3 = {
    (f(a._1, b._1), f(a._2, b._2), f(a._3, b._3))
  }

  /** Returns a new coor3 whose values are computed by applying a function to the
    * respective values of vec */
  def map(vec: Coor3)(f: Float => Float): Coor3 = (f(vec._1), f(vec._2), f(vec._3))

}

