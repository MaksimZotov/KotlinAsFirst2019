@file:Suppress("UNUSED_PARAMETER")

package lesson8.task1

import lesson1.task1.sqr
import java.lang.IllegalArgumentException
import kotlin.math.*

/**
 * Точка на плоскости
 */
data class Point(val x: Double, val y: Double) {
    /**
     * Пример
     *
     * Рассчитать (по известной формуле) расстояние между двумя точками
     */
    fun distance(other: Point): Double = sqrt(sqr(x - other.x) + sqr(y - other.y))
}

/**
 * Треугольник, заданный тремя точками (a, b, c, см. constructor ниже).
 * Эти три точки хранятся в множестве points, их порядок не имеет значения.
 */
@Suppress("MemberVisibilityCanBePrivate")
class Triangle private constructor(private val points: Set<Point>) {

    private val pointList = points.toList()

    val a: Point get() = pointList[0]

    val b: Point get() = pointList[1]

    val c: Point get() = pointList[2]

    constructor(a: Point, b: Point, c: Point) : this(linkedSetOf(a, b, c))

    /**
     * Пример: полупериметр
     */
    fun halfPerimeter() = (a.distance(b) + b.distance(c) + c.distance(a)) / 2.0

    /**
     * Пример: площадь
     */
    fun area(): Double {
        val p = halfPerimeter()
        return sqrt(p * (p - a.distance(b)) * (p - b.distance(c)) * (p - c.distance(a)))
    }

    /**
     * Пример: треугольник содержит точку
     */
    fun contains(p: Point): Boolean {
        val abp = Triangle(a, b, p)
        val bcp = Triangle(b, c, p)
        val cap = Triangle(c, a, p)
        return abp.area() + bcp.area() + cap.area() <= area()
    }

    override fun equals(other: Any?) = other is Triangle && points == other.points

    override fun hashCode() = points.hashCode()

    override fun toString() = "Triangle(a = $a, b = $b, c = $c)"
}

/**
 * Окружность с заданным центром и радиусом
 */
data class Circle(val center: Point, val radius: Double) {
    /**
     * Простая
     *
     * Рассчитать расстояние между двумя окружностями.
     * Расстояние между непересекающимися окружностями рассчитывается как
     * расстояние между их центрами минус сумма их радиусов.
     * Расстояние между пересекающимися окружностями считать равным 0.0.
     */
    fun distance(other: Circle): Double {
        val dist = center.distance(other.center) - (radius + other.radius)
        return if (dist < 0) 0.0 else dist
    }

    /**
     * Тривиальная
     *
     * Вернуть true, если и только если окружность содержит данную точку НА себе или ВНУТРИ себя
     */
    fun contains(p: Point): Boolean =
            center.distance(p) <= radius
}

/**
 * Отрезок между двумя точками
 */
data class Segment(val begin: Point, val end: Point) {
    override fun equals(other: Any?) =
            other is Segment && (begin == other.begin && end == other.end || end == other.begin && begin == other.end)

    override fun hashCode() =
            begin.hashCode() + end.hashCode()
}

/**
 * Средняя
 *
 * Дано множество точек. Вернуть отрезок, соединяющий две наиболее удалённые из них.
 * Если в множестве менее двух точек, бросить IllegalArgumentException
 */
fun diameter(vararg points: Point): Segment {
    if (points.size < 2)
        throw  IllegalArgumentException()
    var p1 = points[0]
    var p2 = points[1]
    var max = p1.distance(p2)
    for (i in 0 until points.lastIndex)
        for (j in (i + 1)..points.lastIndex) {
            val cur = points[i].distance(points[j])
            if (cur > max) {
                max = cur
                p1 = points[i]
                p2 = points[j]
            }
        }
    return Segment(p1, p2)
}

/**
 * Простая
 *
 * Построить окружность по её диаметру, заданному двумя точками
 * Центр её должен находиться посередине между точками, а радиус составлять половину расстояния между ними
 */
fun circleByDiameter(diameter: Segment): Circle {
    val centerX = (diameter.begin.x + diameter.end.x) / 2
    val centerY = (diameter.begin.y + diameter.end.y) / 2
    val radius = diameter.begin.distance(diameter.end) / 2
    return Circle(Point(centerX, centerY), radius)
}

/**
 * Прямая, заданная точкой point и углом наклона angle (в радианах) по отношению к оси X.
 * Уравнение прямой: (y - point.y) * cos(angle) = (x - point.x) * sin(angle)
 * или: y * cos(angle) = x * sin(angle) + b, где b = point.y * cos(angle) - point.x * sin(angle).
 * Угол наклона обязан находиться в диапазоне от 0 (включительно) до PI (исключительно).
 */
class Line private constructor(val b: Double, val angle: Double) {
    init {
        require(angle >= 0 && angle < PI) { "Incorrect line angle: $angle" }
    }

    constructor(point: Point, angle: Double) : this(point.y * cos(angle) - point.x * sin(angle), angle)

    /**
     * Средняя
     *
     * Найти точку пересечения с другой линией.
     * Для этого необходимо составить и решить систему из двух уравнений (каждое для своей прямой)
     */
    fun crossPoint(other: Line): Point {
        if (angle == other.angle) throw Exception("Нет общей точки или прямые совпадают")
        val x = (other.b * cos(angle) - b * cos(other.angle)) / sin(angle - other.angle)
        val y = if (other.angle == PI / 2) x * tan(angle) + b / cos(angle) else x * tan(other.angle) + other.b / cos(other.angle)
        return Point(x, y)
    }

    override fun equals(other: Any?) = other is Line && angle == other.angle && b == other.b

    override fun hashCode(): Int {
        var result = b.hashCode()
        result = 31 * result + angle.hashCode()
        return result
    }

    override fun toString() = "Line(${cos(angle)} * y = ${sin(angle)} * x + $b)"
}

/**
 * Средняя
 *
 * Построить прямую по отрезку
 */
fun lineBySegment(s: Segment): Line =
        Line(s.begin, (PI + atan((s.end.y - s.begin.y) / (s.end.x - s.begin.x))) % PI)

/**
 * Средняя
 *
 * Построить прямую по двум точкам
 */
fun lineByPoints(a: Point, b: Point): Line =
        Line(a, (PI + atan((b.y - a.y) / (b.x - a.x))) % PI)

/**
 * Сложная
 *
 * Построить серединный перпендикуляр по отрезку или по двум точкам
 */
fun bisectorByPoints(a: Point, b: Point): Line =
        Line(Point((a.x + b.x) / 2, (a.y + b.y) / 2), (3 * PI / 2 + atan((b.y - a.y) / (b.x - a.x))) % PI)

/**
 * Средняя
 *
 * Задан список из n окружностей на плоскости. Найти пару наименее удалённых из них.
 * Если в списке менее двух окружностей, бросить IllegalArgumentException
 */
fun findNearestCirclePair(vararg circles: Circle): Pair<Circle, Circle> {
    if (circles.size < 2)
        throw IllegalArgumentException()
    var c1 = circles[0]
    var c2 = circles[1]
    var min = c1.distance(c2)
    for (i in 0 until circles.lastIndex)
        for (j in (i + 1)..circles.lastIndex) {
            val cur = circles[i].distance(circles[j])
            if (cur < min) {
                min = cur
                c1 = circles[i]
                c2 = circles[j]
            }
        }
    return Pair(c1, c2)

}

/**
 * Сложная
 *
 * Дано три различные точки. Построить окружность, проходящую через них
 * (все три точки должны лежать НА, а не ВНУТРИ, окружности).
 * Описание алгоритмов см. в Интернете
 * (построить окружность по трём точкам, или
 * построить окружность, описанную вокруг треугольника - эквивалентная задача).
 */
fun circleByThreePoints(a: Point, b: Point, c: Point): Circle {
    val center = bisectorByPoints(a, b).crossPoint(bisectorByPoints(b, c))
    val radius = center.distance(a)
    return Circle(center, radius)
}

/**
 * Очень сложная
 *
 * Дано множество точек на плоскости. Найти круг минимального радиуса,
 * содержащий все эти точки. Если множество пустое, бросить IllegalArgumentException.
 * Если множество содержит одну точку, вернуть круг нулевого радиуса с центром в данной точке.
 *
 * Примечание: в зависимости от ситуации, такая окружность может либо проходить через какие-либо
 * три точки данного множества, либо иметь своим диаметром отрезок,
 * соединяющий две самые удалённые точки в данном множестве.
 */

fun minContainingCircle(vararg points: Point): Circle = when {
    points.isEmpty() -> throw IllegalArgumentException()
    points.size == 1 -> Circle(points[0], 0.0)
    else -> {
        val byDiameter = minCircleByDiameter(*points)
        val byThreePoints = minCircleByThreePoints(*points)
        if (byDiameter.radius < byThreePoints.radius) byDiameter
        else byThreePoints
    }
}

fun minCircleByDiameter(vararg points: Point): Circle {
    if (points.size < 2) throw IllegalArgumentException()
    var min = Circle(Point(0.0, 0.0), Double.MAX_VALUE)
    for (i in 0..points.lastIndex - 1)
        for (j in (i + 1)..points.lastIndex) {
            val cur = circleByDiameter(Segment(points[i], points[j]))
            if (cur.radius < min.radius && circleContainsPoints(cur, *points))
                min = cur
        }
    return min
}

fun minCircleByThreePoints(vararg points: Point): Circle {
    if (points.size < 2) throw IllegalArgumentException()
    var min = Circle(Point(0.0, 0.0), Double.MAX_VALUE)
    for (i in 0..points.lastIndex - 2)
        for (j in (i + 1)..points.lastIndex - 1)
            for (k in (j + 1)..points.lastIndex) {
                val cur = circleByThreePoints(points[i], points[j], points[k])
                if (cur.radius < min.radius && circleContainsPoints(cur, *points))
                    min = cur
            }
    return min
}

fun circleContainsPoints(circle: Circle, vararg points: Point): Boolean {
    for (i in points.indices) if (!circle.contains(points[i])) return false
    return true
}

