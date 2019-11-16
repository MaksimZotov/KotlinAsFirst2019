@file:Suppress("UNUSED_PARAMETER")

package lesson9.task2

import com.sun.org.apache.xpath.internal.operations.Bool
import lesson8.task1.hexagonByThreePoints
import lesson9.task1.Matrix
import lesson9.task1.MatrixImpl
import lesson9.task1.createMatrix
import java.lang.IllegalArgumentException
import java.lang.IllegalStateException
import kotlin.math.abs
import kotlin.math.max

// Все задачи в этом файле требуют наличия реализации интерфейса "Матрица" в Matrix.kt

/**
 * Пример
 *
 * Транспонировать заданную матрицу matrix.
 * При транспонировании строки матрицы становятся столбцами и наоборот:
 *
 * 1 2 3      1 4 6 3
 * 4 5 6  ==> 2 5 5 2
 * 6 5 4      3 6 4 1
 * 3 2 1
 */
fun <E> transpose(matrix: Matrix<E>): Matrix<E> {
    if (matrix.width < 1 || matrix.height < 1) return matrix
    val result = createMatrix(height = matrix.width, width = matrix.height, e = matrix[0, 0])
    for (i in 0 until matrix.width) {
        for (j in 0 until matrix.height) {
            result[i, j] = matrix[j, i]
        }
    }
    return result
}

/**
 * Пример
 *
 * Сложить две заданные матрицы друг с другом.
 * Складывать можно только матрицы совпадающего размера -- в противном случае бросить IllegalArgumentException.
 * При сложении попарно складываются соответствующие элементы матриц
 */
operator fun Matrix<Int>.plus(other: Matrix<Int>): Matrix<Int> {
    require(!(width != other.width || height != other.height))
    if (width < 1 || height < 1) return this
    val result = createMatrix(height, width, this[0, 0])
    for (i in 0 until height) {
        for (j in 0 until width) {
            result[i, j] = this[i, j] + other[i, j]
        }
    }
    return result
}

/**
 * Сложная
 *
 * Заполнить матрицу заданной высоты height и ширины width
 * натуральными числами от 1 до m*n по спирали,
 * начинающейся в левом верхнем углу и закрученной по часовой стрелке.
 *
 * Пример для height = 3, width = 4:
 *  1  2  3  4
 * 10 11 12  5
 *  9  8  7  6
 */

enum class Direction { Right, Down, Left, Up }

fun generateSpiral(height: Int, width: Int): Matrix<Int> {
    val matrix = MatrixImpl(height, width, 0)
    var count = 0
    var row = 0
    var column = 0
    var rightGap = 0
    var leftGap = 0
    var upGap = 0
    var downGap = 0
    var direction = Direction.Right
    while (true) {
        count++
        matrix[row, column] = count
        if (count == height * width) return matrix
        when (direction) {
            Direction.Right -> {
                if (width != 1) column++ else row++
                if (column >= width - 1 - rightGap) {
                    direction = Direction.Down
                    rightGap++
                }
            }
            Direction.Down -> {
                row++
                if (row >= height - 1 - downGap) {
                    direction = Direction.Left
                    downGap++
                }
            }
            Direction.Left -> {
                column--
                if (column <= leftGap) {
                    direction = Direction.Up
                    leftGap++
                }
            }
            Direction.Up -> {
                row--
                if (row <= upGap + 1) {
                    direction = Direction.Right
                    upGap++
                }
            }
        }
    }
}

/**
 * Сложная
 *
 * Заполнить матрицу заданной высоты height и ширины width следующим образом.
 * Элементам, находящимся на периферии (по периметру матрицы), присвоить значение 1;
 * периметру оставшейся подматрицы – значение 2 и так далее до заполнения всей матрицы.
 *
 * Пример для height = 5, width = 6:
 *  1  1  1  1  1  1
 *  1  2  2  2  2  1
 *  1  2  3  3  2  1
 *  1  2  2  2  2  1
 *  1  1  1  1  1  1
 */
fun generateRectangles(height: Int, width: Int): Matrix<Int> {
    val matrix = MatrixImpl(height, width, 0)
    var number = 1
    var count = 0
    var row = 0
    var column = 0
    var rightGap = 0
    var leftGap = 0
    var upGap = 0
    var downGap = 0
    var direction = Direction.Right
    var nextNumber = false
    while (true) {
        count++
        matrix[row, column] = number
        if (count == height * width) return matrix
        when (direction) {
            Direction.Right -> {
                if (nextNumber) {
                    number++
                    nextNumber = false
                }
                if (width != 1) column++ else row++
                if (column >= width - 1 - rightGap) {
                    direction = Direction.Down
                    rightGap++
                }
            }
            Direction.Down -> {
                row++
                if (row >= height - 1 - downGap) {
                    direction = Direction.Left
                    downGap++
                }
            }
            Direction.Left -> {
                column--
                if (column <= leftGap) {
                    direction = Direction.Up
                    leftGap++
                }
            }
            Direction.Up -> {
                row--
                if (row <= upGap + 1) {
                    direction = Direction.Right
                    upGap++
                    nextNumber = true
                }
            }
        }
    }
}


/**
 * Сложная
 *
 * Заполнить матрицу заданной высоты height и ширины width диагональной змейкой:
 * в левый верхний угол 1, во вторую от угла диагональ 2 и 3 сверху вниз, в третью 4-6 сверху вниз и так далее.
 *
 * Пример для height = 5, width = 4:
 *  1  2  4  7
 *  3  5  8 11
 *  6  9 12 15
 * 10 13 16 18
 * 14 17 19 20
 */
fun generateSnake(height: Int, width: Int): Matrix<Int> {
    val matrix = MatrixImpl(height, width, 0)
    var count = 1
    var row = -1
    var column = -1
    fun goOnDiagonal() {
        while (column >= 0 && row < height) {
            matrix[row, column] = count
            row++
            column--
            count++
        }
    }
    for (i in 1..width) {
        row = 0
        column = i - 1
        goOnDiagonal()
    }
    for (i in 1 until height) {
        row = i
        column = width - 1
        goOnDiagonal()
    }
    return matrix
}

/**
 * Средняя
 *
 * Содержимое квадратной матрицы matrix (с произвольным содержимым) повернуть на 90 градусов по часовой стрелке.
 * Если height != width, бросить IllegalArgumentException.
 *
 * Пример:    Станет:
 * 1 2 3      7 4 1
 * 4 5 6      8 5 2
 * 7 8 9      9 6 3
 */
fun <E> rotate(matrix: Matrix<E>): Matrix<E> {
    if (matrix.height != matrix.width) throw IllegalArgumentException()
    val matrixResult = MatrixImpl(matrix.height, matrix.height, matrix[0, 0])
    for (i in 0 until matrix.height)
        for (j in 0 until matrix.height)
            matrixResult[i, j] = matrix[matrix.height - 1 - j, i]
    return matrixResult
}

/**
 * Сложная
 *
 * Проверить, является ли квадратная целочисленная матрица matrix латинским квадратом.
 * Латинским квадратом называется матрица размером n x n,
 * каждая строка и каждый столбец которой содержат все числа от 1 до n.
 * Если height != width, вернуть false.
 *
 * Пример латинского квадрата 3х3:
 * 2 3 1
 * 1 2 3
 * 3 1 2
 */
fun isLatinSquare(matrix: Matrix<Int>): Boolean {
    if (matrix.height != matrix.width) return false
    val n = matrix.height
    for (i in 0 until n) {
        val digitsHeight = Array(n) { false }
        val digitsWidth = Array(n) { false }
        for (j in 0 until n) {
            if (matrix[i, j] in 1..n) digitsHeight[matrix[i, j] - 1] = true else return false
            if (matrix[j, i] in 1..n) digitsWidth[matrix[j, i] - 1] = true else return false
        }
        if (digitsHeight.any { !it } || digitsWidth.any { !it }) return false
    }
    return true
}

/**
 * Средняя
 *
 * В матрице matrix каждый элемент заменить суммой непосредственно примыкающих к нему
 * элементов по вертикали, горизонтали и диагоналям.
 *
 * Пример для матрицы 4 x 3: (11=2+4+5, 19=1+3+4+5+6, ...)
 * 1 2 3       11 19 13
 * 4 5 6  ===> 19 31 19
 * 6 5 4       19 31 19
 * 3 2 1       13 19 11
 *
 * Поскольку в матрице 1 х 1 примыкающие элементы отсутствуют,
 * для неё следует вернуть как результат нулевую матрицу:
 *
 * 42 ===> 0
 */
fun sumNeighbours(matrix: Matrix<Int>): Matrix<Int> {
    val matrixResult = MatrixImpl(matrix.height, matrix.width, 0)
    for (i in 0 until matrix.height)
        for (j in 0 until matrix.width)
            for (k in i - 1..i + 1)
                for (l in j - 1..j + 1)
                    if (k in 0 until matrix.height && l in 0 until matrix.width && (k != i || l != j))
                        matrixResult[i, j] += matrix[k, l]
    return matrixResult
}

/**
 * Средняя
 *
 * Целочисленная матрица matrix состоит из "дырок" (на их месте стоит 0) и "кирпичей" (на их месте стоит 1).
 * Найти в этой матрице все ряды и колонки, целиком состоящие из "дырок".
 * Результат вернуть в виде Holes(rows = список дырчатых рядов, columns = список дырчатых колонок).
 * Ряды и колонки нумеруются с нуля. Любой из спискоов rows / columns может оказаться пустым.
 *
 * Пример для матрицы 5 х 4:
 * 1 0 1 0
 * 0 0 1 0
 * 1 0 0 0 ==> результат: Holes(rows = listOf(4), columns = listOf(1, 3)): 4-й ряд, 1-я и 3-я колонки
 * 0 0 1 0
 * 0 0 0 0
 */
fun findHoles(matrix: Matrix<Int>): Holes {
    val rows = mutableListOf<Int>()
    val columns = mutableListOf<Int>()
    for (i in 0 until matrix.height) {
        val digitsWidth = Array(matrix.width) { false }
        for (j in 0 until matrix.width) if (matrix[i, j] == 0) digitsWidth[j] = true
        if (digitsWidth.all { it }) rows.add(i)
    }
    for (i in 0 until matrix.width) {
        val digitsHeight = Array(matrix.height) { false }
        for (j in 0 until matrix.height) if (matrix[j, i] == 0) digitsHeight[j] = true
        if (digitsHeight.all { it }) columns.add(i)
    }
    return Holes(rows, columns)
}

/**
 * Класс для описания местонахождения "дырок" в матрице
 */
data class Holes(val rows: List<Int>, val columns: List<Int>)

/**
 * Средняя
 *
 * В целочисленной матрице matrix каждый элемент заменить суммой элементов подматрицы,
 * расположенной в левом верхнем углу матрицы matrix и ограниченной справа-снизу данным элементом.
 *
 * Пример для матрицы 3 х 3:
 *
 * 1  2  3      1  3  6
 * 4  5  6  =>  5 12 21
 * 7  8  9     12 27 45
 *
 * К примеру, центральный элемент 12 = 1 + 2 + 4 + 5, элемент в левом нижнем углу 12 = 1 + 4 + 7 и так далее.
 */
fun sumSubMatrix(matrix: Matrix<Int>): Matrix<Int> {
    val matrixResult = MatrixImpl(matrix.height, matrix.width, 0)
    for (i in 0 until matrix.height)
        for (j in 0 until matrix.width)
            for (k in 0..i)
                for (l in 0..j)
                    matrixResult[i, j] += matrix[k, l]
    return matrixResult
}

/**
 * Сложная
 *
 * Даны мозаичные изображения замочной скважины и ключа. Пройдет ли ключ в скважину?
 * То есть даны две матрицы key и lock, key.height <= lock.height, key.width <= lock.width, состоящие из нулей и единиц.
 *
 * Проверить, можно ли наложить матрицу key на матрицу lock (без поворота, разрешается только сдвиг) так,
 * чтобы каждой единице в матрице lock (штырь) соответствовал ноль в матрице key (прорезь),
 * а каждому нулю в матрице lock (дырка) соответствовала, наоборот, единица в матрице key (штырь).
 * Ключ при сдвиге не может выходить за пределы замка.
 *
 * Пример: ключ подойдёт, если его сдвинуть на 1 по ширине
 * lock    key
 * 1 0 1   1 0
 * 0 1 0   0 1
 * 1 1 1
 *
 * Вернуть тройку (Triple) -- (да/нет, требуемый сдвиг по высоте, требуемый сдвиг по ширине).
 * Если наложение невозможно, то первый элемент тройки "нет" и сдвиги могут быть любыми.
 */
fun canOpenLock(key: Matrix<Int>, lock: Matrix<Int>): Triple<Boolean, Int, Int> {
    var count0 = 0
    var count1 = 0
    for (i in 0 until key.height)
        for (j in 0 until key.width)
            if (key[i, j] == 0) count0++ else count1++
    for (i in 0 until lock.height)
        for (j in 0 until lock.width) {
            var localCount0 = 0
            var localCount1 = 0
            if (key.height + i <= lock.height)
                for (l in 0 until key.height)
                    if (key.width + j <= lock.width)
                        for (k in 0 until key.width) {
                            if (lock[i + l, j + k] == 1 && key[l, k] == 0) localCount0++
                            if (lock[i + l, j + k] == 0 && key[l, k] == 1) localCount1++
                        }
            if (localCount0 == count0 && localCount1 == count1) return Triple(true, i, j)
        }
    return Triple(false, 0, 0)
}

/**
 * Простая
 *
 * Инвертировать заданную матрицу.
 * При инвертировании знак каждого элемента матрицы следует заменить на обратный
 */
operator fun Matrix<Int>.unaryMinus(): Matrix<Int> {
    for (i in 0 until height)
        for (j in 0 until width)
            this[i, j] *= -1
    return this
}

/**
 * Средняя
 *
 * Перемножить две заданные матрицы друг с другом.
 * Матрицы можно умножать, только если ширина первой матрицы совпадает с высотой второй матрицы.
 * В противном случае бросить IllegalArgumentException.
 * Подробно про порядок умножения см. статью Википедии "Умножение матриц".
 */
operator fun Matrix<Int>.times(other: Matrix<Int>): Matrix<Int> {
    if (height != other.width || width != other.height) throw IllegalArgumentException()
    val matrixResult = MatrixImpl(height, other.width, 0)
    for (i in 0 until height)
        for (j in 0 until other.width) {
            var curCell = 0
            for (r in 0 until width) curCell += this[i, r] * other[r, j]
            matrixResult[i, j] = curCell
        }
    return matrixResult
}

/**
 * Сложная
 *
 * В матрице matrix размером 4х4 дана исходная позиция для игры в 15, например
 *  5  7  9  1
 *  2 12 14 15
 *  3  4  6  8
 * 10 11 13  0
 *
 * Здесь 0 обозначает пустую клетку, а 1-15 – фишки с соответствующими номерами.
 * Напомним, что "игра в 15" имеет квадратное поле 4х4, по которому двигается 15 фишек,
 * одна клетка всегда остаётся пустой. Цель игры -- упорядочить фишки на игровом поле.
 *
 * В списке moves задана последовательность ходов, например [8, 6, 13, 11, 10, 3].
 * Ход задаётся номером фишки, которая передвигается на пустое место (то есть, меняется местами с нулём).
 * Фишка должна примыкать к пустому месту по горизонтали или вертикали, иначе ход не будет возможным.
 * Все номера должны быть в пределах от 1 до 15.
 * Определить финальную позицию после выполнения всех ходов и вернуть её.
 * Если какой-либо ход является невозможным или список содержит неверные номера,
 * бросить IllegalStateException.
 *
 * В данном случае должно получиться
 * 5  7  9  1
 * 2 12 14 15
 * 0  4 13  6
 * 3 10 11  8
 */
fun fifteenGameMoves(matrix: Matrix<Int>, moves: List<Int>): Matrix<Int> {
    if (moves.any { it !in 1..15 }) throw IllegalStateException()
    if (moves.isEmpty()) return matrix
    var curI = -1
    var curJ = -1
    var moveFound = false
    for (i in 0 until matrix.height) {
        if (moveFound) break
        for (j in 0 until matrix.width)
            if (matrix[i, j] == moves[0]) {
                for ((k, l) in listOf(i - 1 to j, i + 1 to j, i to j - 1, i to j + 1))
                    if (k in 0 until matrix.height && l in 0 until matrix.width && matrix[k, l] == 0) {
                        curI = i
                        curJ = j
                        matrix[k, l] = moves[0]
                        matrix[curI, curJ] = 0
                        moveFound = true
                        break
                    }
                if (!moveFound) throw IllegalStateException()
                else break
            }
    }
    for (i in 1..moves.lastIndex) {
        moveFound = false
        for ((k, l) in listOf(curI - 1 to curJ, curI + 1 to curJ, curI to curJ - 1, curI to curJ + 1))
            if (k in 0 until matrix.height && l in 0 until matrix.width && matrix[k, l] == moves[i]) {
                matrix[k, l] = 0
                matrix[curI, curJ] = moves[i]
                curI = k
                curJ = l
                moveFound = true
                break
            }
        if (!moveFound) throw IllegalStateException()
    }
    return matrix
}

/**
 * Очень сложная
 *
 * В матрице matrix размером 4х4 дана исходная позиция для игры в 15, например
 *  5  7  9  2
 *  1 12 14 15
 *  3  4  6  8
 * 10 11 13  0
 *
 * Здесь 0 обозначает пустую клетку, а 1-15 – фишки с соответствующими номерами.
 * Напомним, что "игра в 15" имеет квадратное поле 4х4, по которому двигается 15 фишек,
 * одна клетка всегда остаётся пустой.
 *
 * Цель игры -- упорядочить фишки на игровом поле, приведя позицию к одному из следующих двух состояний:
 *
 *  1  2  3  4          1  2  3  4
 *  5  6  7  8   ИЛИ    5  6  7  8
 *  9 10 11 12          9 10 11 12
 * 13 14 15  0         13 15 14  0
 *
 * Можно математически доказать, что РОВНО ОДНО из этих двух состояний достижимо из любой исходной позиции.
 *
 * Вернуть решение -- список ходов, приводящих исходную позицию к одной из двух упорядоченных.
 * Каждый ход -- это перемена мест фишки с заданным номером с пустой клеткой (0),
 * при этом заданная фишка должна по горизонтали или по вертикали примыкать к пустой клетке (но НЕ по диагонали).
 * К примеру, ход 13 в исходной позиции меняет местами 13 и 0, а ход 11 в той же позиции невозможен.
 *
 * Одно из решений исходной позиции:
 *
 * [8, 6, 14, 12, 4, 11, 13, 14, 12, 4,
 * 7, 5, 1, 3, 11, 7, 3, 11, 7, 12, 6,
 * 15, 4, 9, 2, 4, 9, 3, 5, 2, 3, 9,
 * 15, 8, 14, 13, 12, 7, 11, 5, 7, 6,
 * 9, 15, 8, 14, 13, 9, 15, 7, 6, 12,
 * 9, 13, 14, 15, 12, 11, 10, 9, 13, 14,
 * 15, 12, 11, 10, 9, 13, 14, 15]
 *
 * Перед решением этой задачи НЕОБХОДИМО решить предыдущую
 */

fun fifteenGameSolution(matrix: Matrix<Int>): List<Int> {
    val list = mutableListOf<Int>()
    val engaged = createEngaged(matrix)
    var posZero = getPos(matrix, 0)
    fun moveCurNumToTarget(curNum: Int, posMainTarget: Pair<Int, Int>, fillHorizontal: Boolean) {
        var posCurNum = getPos(matrix, curNum)
        while (posMainTarget != posCurNum) {
            showMatrix(matrix)
            // соседняя с curNum ячейка (next hop), через которую лежит путь к основному таргету
            val posLocalTarget = getPosLocalTarget(posCurNum, posMainTarget, fillHorizontal)
            engaged[posCurNum] = true
            val trajectory = getTrajectoryZeroToLocalTarget(matrix, engaged, posZero, posLocalTarget)
            moveZeroToLocalTarget(matrix, trajectory)
            // на этом моменте 0 находиться на месте локального таргета
            // теперь нужно лишь поменять позиции 0 и curNum, т.е. в локальный таргет
            // поместить curNum, а в ячеку, где ранее был curNum, поместить 0
            matrix[posLocalTarget.first, posLocalTarget.second] = curNum
            matrix[posCurNum.first, posCurNum.second] = 0
            engaged[posCurNum] = false
            val temp = posCurNum
            posCurNum = posLocalTarget
            posZero = temp
        }
        engaged[posMainTarget] = true
    }
    // здесь идея в том, что предпоследние снизу/сверху вначале перемещаются в самый низ/верх, затем те,
    // что должны быть на последнем месте, встают перед предпоследними, и уже лишь после этого все
    // встают на свои места
    // получается так, что вначале ячейки заполняются по горизонтали, потом по вертикали - как резултат,
    // размер из n*n становится (n-1)*(n-1)
    // и так до конца
    val forMoving = movingAndCheckAndHorizontal(matrix)
    val listOfMoving = forMoving.first
    val listForCheck = forMoving.second
    val listForHorizontal = forMoving.third
    val n = matrix.height
    for (i in listOfMoving.indices) {
        // обработка случая, когда предпоследний встал на место последнего,
        // а сам последний находиться на месте предпоследнего (примеры: 1, 2, 4, 3   1, 2, 0, 3
        // ну или 2 вариант в примерах                                  5, 7, 0, 9   5, 7, 4, 9)
        if (listOfMoving[i] in listForCheck && (getPos(matrix, listOfMoving[i].first) in listOf(listOfMoving[i + 1].second,
                        listOfMoving[i + 1].second.first + 1 to listOfMoving[i + 1].second.second,
                        listOfMoving[i + 1].second.first to listOfMoving[i + 1].second.second + 1))) {
            engaged[listOfMoving[i - 1].second] = false
            moveCurNumToTarget(
                    listOfMoving[i].first, n - 1 to n - 1, listOfMoving[i].first in listForHorizontal
            )
            moveCurNumToTarget(
                    listOfMoving[i - 1].first, n - 2 to n - 2, listOfMoving[i].first in listForHorizontal
            )
            engaged[n - 1 to n - 1] = false
            moveCurNumToTarget(
                    listOfMoving[i - 1].first, listOfMoving[i - 1].second, listOfMoving[i].first in listForHorizontal
            )
            // тут смысл в том, что мы последний и предпоследний перемещаем в правый нижний угол таким образом,
            // чтобы при повторной попытке их расставить последний не встал снова на место предпоследнего
            // i - это последний, i - 1 - предпоследний
        }
        moveCurNumToTarget(
                listOfMoving[i].first, listOfMoving[i].second, listOfMoving[i].first in listForHorizontal
        )
    }
    return list
}

fun createEngaged(matrix: Matrix<Int>): MutableMap<Pair<Int, Int>, Boolean> {
    val map = mutableMapOf<Pair<Int, Int>, Boolean>()
    val n = matrix.height
    for (i in 0 until n)
        for (j in 0 until n)
            map[i to j] = false
    return map
}

fun movingAndCheckAndHorizontal(matrix: Matrix<Int>):
        Triple<List<Pair<Int, Pair<Int, Int>>>, List<Pair<Int, Pair<Int, Int>>>, List<Int>> {
    val listOfMoving = mutableListOf<Pair<Int, Pair<Int, Int>>>()
    val listForCheck = mutableListOf<Pair<Int, Pair<Int, Int>>>()
    val listForHorizontal = mutableListOf<Int>()
    val n = matrix.height
    for (i in 0..n - 3) {
        for (j in i + 1 until n) {
            if (j != n - 1) {
                listOfMoving.add(i * n + j to (i to j - 1))
                listForHorizontal.add(i * n + j)
                continue
            }
            listOfMoving.add(i * n + j to (i to j))
            listOfMoving.add(i * n + j + 1 to (i + 1 to j))
            listOfMoving.add(i * n + j to (i to j - 1))
            listOfMoving.add(i * n + j + 1 to (i to j))
            listForCheck.add(i * n + j + 1 to (i + 1 to j))
            listForHorizontal.add(i * n + j)
            listForHorizontal.add(i * n + j + 1)
        }
        for (j in i + 2 until n) {
            if (j != n - 1) {
                listOfMoving.add((j - 1) * n + i + 1 to (j - 1 to i))
                continue
            }
            listOfMoving.add((j - 1) * n + i + 1 to (j to i))
            listOfMoving.add((j) * n + i + 1 to (j to i + 1))
            listForCheck.add((j) * n + i + 1 to (j to i + 1))
            listOfMoving.add((j - 1) * n + i + 1 to (j - 1 to i))
            listOfMoving.add((j) * n + i + 1 to (j to i))
        }
    }
    return Triple(listOfMoving, listForCheck, listForHorizontal)
}

fun getTrajectoryZeroToLocalTarget(matrix: Matrix<Int>, engaged: Map<Pair<Int, Int>, Boolean>,
                                   posZero: Pair<Int, Int>, target: Pair<Int, Int>): List<Pair<Int, Int>> {
    var trajectory = mutableListOf<Pair<Int, Int>>()
    var found = false
    fun goToNeighbors(pos: Pair<Int, Int>, way: MutableList<Pair<Int, Int>>, count: Int) {
        val n = matrix.height
        if (!way.contains(pos) && count < 2 * n && !found && pos.first in 0 until n
                && pos.second in 0 until n && engaged[pos] == false) {
            val count = count + 1
            way.add(pos)
            if (pos == target) {
                found = true
                trajectory = way
            } else {
                goToNeighbors(pos.first + 1 to pos.second, way.toMutableList(), count)
                goToNeighbors(pos.first - 1 to pos.second, way.toMutableList(), count)
                goToNeighbors(pos.first to pos.second + 1, way.toMutableList(), count)
                goToNeighbors(pos.first to pos.second - 1, way.toMutableList(), count)
            }
        }
    }
    goToNeighbors(posZero, mutableListOf(), 0)
    return trajectory
}

fun moveZeroToLocalTarget(matrix: Matrix<Int>, way: List<Pair<Int, Int>>) {
    for (i in 0 until way.lastIndex) {
        val temp = matrix[way[i + 1].first, way[i + 1].second]
        matrix[way[i + 1].first, way[i + 1].second] = 0
        matrix[way[i].first, way[i].second] = temp
        showMatrix(matrix)
    }
}

fun getPos(matrix: Matrix<Int>, element: Int): Pair<Int, Int> {
    for (i in 0 until matrix.height) for (j in 0 until matrix.width) if (matrix[i, j] == element) return i to j
    throw IllegalArgumentException()
}

fun getPosLocalTarget(posNum: Pair<Int, Int>, posMainTarget: Pair<Int, Int>, fillHorizontal: Boolean): Pair<Int, Int> {
    return if (fillHorizontal) {
        if (posNum.second != posMainTarget.second)
            posNum.first to posNum.second + (posMainTarget.second - posNum.second) / abs(posMainTarget.second - posNum.second)
        else
            posNum.first + (posMainTarget.first - posNum.first) / abs(posMainTarget.first - posNum.first) to posNum.second
    } else {
        if (posNum.first != posMainTarget.first)
            posNum.first + (posMainTarget.first - posNum.first) / abs(posMainTarget.first - posNum.first) to posNum.second
        else
            posNum.first to posNum.second + (posMainTarget.second - posNum.second) / abs(posMainTarget.second - posNum.second)
    }
}

fun showMatrix(matrix: Matrix<Int>) {
    for (i in 0 until matrix.height) {
        for (j in 0 until matrix.width) print(matrix[i, j].toString() + ", ")
        println()
    }

    println()
}