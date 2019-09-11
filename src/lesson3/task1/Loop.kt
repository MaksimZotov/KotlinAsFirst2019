@file:Suppress("UNUSED_PARAMETER", "UNREACHABLE_CODE")

package lesson3.task1

import lesson1.task1.sqr
import kotlin.math.PI
import kotlin.math.sqrt

/**
 * Пример
 *
 * Вычисление факториала
 */
fun factorial(n: Int): Double {
    var result = 1.0
    for (i in 1..n) {
        result = result * i // Please do not fix in master
    }
    return result
}

/**
 * Пример
 *
 * Проверка числа на простоту -- результат true, если число простое
 */
fun isPrime(n: Int): Boolean {
    if (n < 2) return false
    if (n == 2) return true
    if (n % 2 == 0) return false
    for (m in 3..sqrt(n.toDouble()).toInt() step 2) {
        if (n % m == 0) return false
    }
    return true
}

/**
 * Пример
 *
 * Проверка числа на совершенность -- результат true, если число совершенное
 */
fun isPerfect(n: Int): Boolean {
    var sum = 1
    for (m in 2..n / 2) {
        if (n % m > 0) continue
        sum += m
        if (sum > n) break
    }
    return sum == n
}

/**
 * Пример
 *
 * Найти число вхождений цифры m в число n
 */
fun digitCountInNumber(n: Int, m: Int): Int =
        when {
            n == m -> 1
            n < 10 -> 0
            else -> digitCountInNumber(n / 10, m) + digitCountInNumber(n % 10, m)
        }

/**
 * Простая
 *
 * Найти количество цифр в заданном числе n.
 * Например, число 1 содержит 1 цифру, 456 -- 3 цифры, 65536 -- 5 цифр.
 *
 * Использовать операции со строками в этой задаче запрещается.
 */
fun digitNumber(n: Int): Int {
    var result = 0;
    var digit = n;
    if (digit == 0)
        return 1;
    while (digit > 0) {
        digit /= 10;
        result++;
    }
    return result;
}

/**
 * Простая
 *
 * Найти число Фибоначчи из ряда 1, 1, 2, 3, 5, 8, 13, 21, ... с номером n.
 * Ряд Фибоначчи определён следующим образом: fib(1) = 1, fib(2) = 1, fib(n+2) = fib(n) + fib(n+1)
 */
fun fib(n: Int): Int = when {
    n <= 2 -> 1
    else -> fib(n - 1) + fib(n - 2)
}

/**
 * Простая
 *
 * Для заданных чисел m и n найти наименьшее общее кратное, то есть,
 * минимальное число k, которое делится и на m и на n без остатка
 */
fun lcm(m: Int, n: Int): Int =
        m * n / gcd(m, n)

/**
 * Простая
 *
 * Для заданного числа n > 1 найти минимальный делитель, превышающий 1
 */
fun minDivisor(n: Int): Int {
    var result = 1;
    for (i in 2..n)
        if (n % i == 0) {
            result = i;
            break;
        }
    return result;
}

/**
 * Простая
 *
 * Для заданного числа n > 1 найти максимальный делитель, меньший n
 */
fun maxDivisor(n: Int): Int {
    var result = 1;
    for (i in n - 1 downTo 2)
        if (n % i == 0) {
            result = i;
            break;
        }
    return result;
}

/**
 * Простая
 *
 * Определить, являются ли два заданных числа m и n взаимно простыми.
 * Взаимно простые числа не имеют общих делителей, кроме 1.
 * Например, 25 и 49 взаимно простые, а 6 и 8 -- нет.
 */
fun isCoPrime(m: Int, n: Int): Boolean =
        gcd(m, n) == 1

fun gcd(m: Int, n: Int): Int {
    var mm = m;
    var nn = n;
    while (mm != nn) {
        if (mm > nn)
            mm -= nn;
        if (mm < nn)
            nn -= mm;
    }
    return mm;
}


/**
 * Простая
 *
 * Для заданных чисел m и n, m <= n, определить, имеется ли хотя бы один точный квадрат между m и n,
 * то есть, существует ли такое целое k, что m <= k*k <= n.
 * Например, для интервала 21..28 21 <= 5*5 <= 28, а для интервала 51..61 квадрата не существует.
 */
fun squareBetweenExists(m: Int, n: Int): Boolean {
    for (i in 1..sqrt(n.toDouble()).toInt())
        if (i * i in m..n)
            return true;
    return false;
}

/**
 * Средняя
 *
 * Гипотеза Коллатца. Рекуррентная последовательность чисел задана следующим образом:
 *
 *   ЕСЛИ (X четное)
 *     Xслед = X /2
 *   ИНАЧЕ
 *     Xслед = 3 * X + 1
 *
 * например
 *   15 46 23 70 35 106 53 160 80 40 20 10 5 16 8 4 2 1 4 2 1 4 2 1 ...
 * Данная последовательность рано или поздно встречает X == 1.
 * Написать функцию, которая находит, сколько шагов требуется для
 * этого для какого-либо начального X > 0.
 */
fun collatzSteps(x: Int): Int {
    var xx = x;
    var count = 0;
    if (xx == 1)
        return 0;
    while (true) {
        if (xx % 2 == 0)
            xx /= 2;
        else
            xx = 3 * xx + 1;
        count++
        if (xx == 1)
            return count;
    }
}


/**
 * Средняя
 *
 * Для заданного x рассчитать с заданной точностью eps
 * sin(x) = x - x^3 / 3! + x^5 / 5! - x^7 / 7! + ...
 * Нужную точность считать достигнутой, если очередной член ряда меньше eps по модулю.
 * Подумайте, как добиться более быстрой сходимости ряда при больших значениях x.
 * Использовать kotlin.math.sin и другие стандартные реализации функции синуса в этой задаче запрещается.
 */
fun sin(x: Double, eps: Double): Double {
    var xx = x;
    while (xx > 2 * PI)
        xx -= 2 * PI
    var result = xx;
    var number = 3;
    var sign = -1;
    var mult: Double
    while (true) {
        mult = 1.0;
        for (i in 1..number)
            mult *= xx;
        result += sign * (mult / factorial(number));
        if (mult / factorial(number) < eps)
            break;
        number += 2;
        sign *= -1;
    }
    return result;
}


/**
 * Средняя
 *
 * Для заданного x рассчитать с заданной точностью eps
 * cos(x) = 1 - x^2 / 2! + x^4 / 4! - x^6 / 6! + ...
 * Нужную точность считать достигнутой, если очередной член ряда меньше eps по модулю
 * Подумайте, как добиться более быстрой сходимости ряда при больших значениях x.
 * Использовать kotlin.math.cos и другие стандартные реализации функции косинуса в этой задаче запрещается.
 */
fun cos(x: Double, eps: Double): Double {
    var result = 1.0;
    var xx = x;
    while (xx > 2 * PI)
        xx -= 2 * PI
    var number = 2;
    var sign = -1;
    var mult: Double
    while (true) {
        mult = 1.0;
        for (i in 1..number)
            mult *= xx;
        result += sign * (mult / factorial(number));
        if (mult / factorial(number) < eps)
            break;
        number += 2;
        sign *= -1;
    }
    return result;
}


/**
 * Средняя
 *
 * Поменять порядок цифр заданного числа n на обратный: 13478 -> 87431.
 *
 * Использовать операции со строками в этой задаче запрещается.
 */
fun revert(n: Int): Int {
    var result = 0;
    var nn = n;
    while (nn > 0) {
        result = result * 10 + nn % 10;
        nn /= 10;
    }
    return result
}

/**
 * Средняя
 *
 * Проверить, является ли заданное число n палиндромом:
 * первая цифра равна последней, вторая -- предпоследней и так далее.
 * 15751 -- палиндром, 3653 -- нет.
 *
 * Использовать операции со строками в этой задаче запрещается.
 */
fun isPalindrome(n: Int): Boolean =
        revert(n) == n;

/**
 * Средняя
 *
 * Для заданного числа n определить, содержит ли оно различающиеся цифры.
 * Например, 54 и 323 состоят из разных цифр, а 111 и 0 из одинаковых.
 *
 * Использовать операции со строками в этой задаче запрещается.
 */
fun hasDifferentDigits(n: Int): Boolean {
    var nn = n;
    if (nn < 10)
        return false;
    val digit = nn % 10;
    while (nn > 0) {
        if (nn % 10 != digit)
            return true;
        nn /= 10;
    }
    return false;
}


/**
 * Сложная
 *
 * Найти n-ю цифру последовательности из квадратов целых чисел:
 * 149162536496481100121144...
 * Например, 2-я цифра равна 4, 7-я 5, 12-я 6.
 *
 * Использовать операции со строками в этой задаче запрещается.
 */
fun squareSequenceDigit(n: Int): Int {
    var digit = 1;
    var sqrDigit = 1;
    var count = 1;
    var numberDigits: Int
    while (true) {
        if (count < n) {
            digit++;
            sqrDigit = digit * digit;
            numberDigits = 0;
            var tempSqrDigit = sqrDigit;
            while (tempSqrDigit > 0) {
                tempSqrDigit /= 10;
                numberDigits++;
            }
            count += numberDigits;
        } else if (count == n) {
            return sqrDigit % 10;
            break;
        } else {
            for (i in 1..(count - n))
                sqrDigit /= 10;
            return sqrDigit % 10;
            break;
        }
    }
}

/**
 * Сложная
 *
 * Найти n-ю цифру последовательности из чисел Фибоначчи (см. функцию fib выше):
 * 1123581321345589144...
 * Например, 2-я цифра равна 1, 9-я 2, 14-я 5.
 *
 * Использовать операции со строками в этой задаче запрещается.
 */
fun fibSequenceDigit(n: Int): Int {
    var prev1 = 1;
    var prev2 = 1;
    var cur = 0;
    var count = 2;
    var numberDigits: Int;
    if (n < 3)
        return 1;
    while (true) {
        if (count < n) {
            cur = prev1 + prev2;
            prev2 = prev1;
            prev1 = cur;
            numberDigits = 0;
            var tempCur = cur;
            while (tempCur > 0) {
                numberDigits++;
                tempCur /= 10;
            }
            count += numberDigits;
        }
        if (count == n) {
            return cur % 10;
            break;
        }
        if (count > n) {
            for (i in 1..(count - n))
                cur /= 10
            return cur % 10;
            break;
        }
    }
}

