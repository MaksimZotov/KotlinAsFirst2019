@file:Suppress("UNUSED_PARAMETER", "ConvertCallChainIntoSequence")

package lesson6.task1

import lesson2.task2.daysInMonth
import java.lang.Exception
import java.lang.IllegalArgumentException
import kotlin.math.pow

/**
 * Пример
 *
 * Время представлено строкой вида "11:34:45", содержащей часы, минуты и секунды, разделённые двоеточием.
 * Разобрать эту строку и рассчитать количество секунд, прошедшее с начала дня.
 */
fun timeStrToSeconds(str: String): Int {
    val parts = str.split(":")
    var result = 0
    for (part in parts) {
        val number = part.toInt()
        result = result * 60 + number
    }
    return result
}

/**
 * Пример
 *
 * Дано число n от 0 до 99.
 * Вернуть его же в виде двухсимвольной строки, от "00" до "99"
 */
fun twoDigitStr(n: Int) = if (n in 0..9) "0$n" else "$n"

/**
 * Пример
 *
 * Дано seconds -- время в секундах, прошедшее с начала дня.
 * Вернуть текущее время в виде строки в формате "ЧЧ:ММ:СС".
 */
fun timeSecondsToStr(seconds: Int): String {
    val hour = seconds / 3600
    val minute = (seconds % 3600) / 60
    val second = seconds % 60
    return String.format("%02d:%02d:%02d", hour, minute, second)
}

/**
 * Пример: консольный ввод
 */
fun main() {
    println("Введите время в формате ЧЧ:ММ:СС")
    val line = readLine()
    if (line != null) {
        val seconds = timeStrToSeconds(line)
        if (seconds == -1) {
            println("Введённая строка $line не соответствует формату ЧЧ:ММ:СС")
        } else {
            println("Прошло секунд с начала суток: $seconds")
        }
    } else {
        println("Достигнут <конец файла> в процессе чтения строки. Программа прервана")
    }
}


/**
 * Средняя
 *
 * Дата представлена строкой вида "15 июля 2016".
 * Перевести её в цифровой формат "15.07.2016".
 * День и месяц всегда представлять двумя цифрами, например: 03.04.2011.
 * При неверном формате входной строки вернуть пустую строку.
 *
 * Обратите внимание: некорректная с точки зрения календаря дата (например, 30.02.2009) считается неверными
 * входными данными.
 */
var mounths = arrayOf("января", "февраля", "марта", "апреля", "мая", "июня",
        "июля", "августа", "сентября", "октября", "ноября", "декабря")

fun dateStrToDigit(str: String): String {
    val strList = str.split(" ")
    try {
        val day = strList[0].toInt()
        val mounth = mounths.indexOf(strList[1]) + 1
        val year = strList[2].toInt()
        if (day < 1 || daysInMonth(mounth, year) < day || strList.size != 3 || mounth !in 1..12)
            return ""
        return String.format("%02d.%02d.%d", day, mounth, year)
    } catch (e: Exception) {
        return ""
    }
}

/**
 * Средняя
 *
 * Дата представлена строкой вида "15.07.2016".
 * Перевести её в строковый формат вида "15 июля 2016".
 * При неверном формате входной строки вернуть пустую строку
 *
 * Обратите внимание: некорректная с точки зрения календаря дата (например, 30 февраля 2009) считается неверными
 * входными данными.
 */
fun dateDigitToStr(digital: String): String {
    val strList = digital.split(".")
    try {
        val day = strList[0].filterIndexed { i, c -> i != 0 || c != '0' }.toInt()
        val mounth = strList[1].filterIndexed { i, c -> i != 0 || c != '0' }.toInt()
        val year = strList[2].toInt()
        if (day < 1 || daysInMonth(mounth, year) < day || strList.size != 3 || mounth !in 1..12)
            return ""
        return String.format("%d %s %d", day, mounths[mounth - 1], year)
    } catch (e: Exception) {
        return ""
    }
}

/**
 * Средняя
 *
 * Номер телефона задан строкой вида "+7 (921) 123-45-67".
 * Префикс (+7) может отсутствовать, код города (в скобках) также может отсутствовать.
 * Может присутствовать неограниченное количество пробелов и чёрточек,
 * например, номер 12 --  34- 5 -- 67 -89 тоже следует считать легальным.
 * Перевести номер в формат без скобок, пробелов и чёрточек (но с +), например,
 * "+79211234567" или "123456789" для приведённых примеров.
 * Все символы в номере, кроме цифр, пробелов и +-(), считать недопустимыми.
 * При неверном формате вернуть пустую строку.
 *
 * PS: Дополнительные примеры работы функции можно посмотреть в соответствующих тестах.
 */
fun flattenPhoneNumber(phone: String): String {
    return if (Regex("""\+?[\d -]*(\(.+\))?[\d -]*""").matches(phone) && Regex("""\d""").find(phone) != null)
        phone.filter { it != ' ' && it != '-' && it != '(' && it != ')' }
    else
        ""
}


/**
 * Средняя
 *
 * Результаты спортсмена на соревнованиях в прыжках в длину представлены строкой вида
 * "706 - % 717 % 703".
 * В строке могут присутствовать числа, черточки - и знаки процента %, разделённые пробелами;
 * число соответствует удачному прыжку, - пропущенной попытке, % заступу.
 * Прочитать строку и вернуть максимальное присутствующее в ней число (717 в примере).
 * При нарушении формата входной строки или при отсутствии в ней чисел, вернуть -1.
 */
fun bestLongJump(jumps: String): Int {
    val list = jumps.split(" ", "-", "%")
    var max = -1
    try {
        for (item in list)
            if (item != "" && item.toInt() > max)
                max = item.toInt()
    } catch (e: Exception) {
        return -1
    }
    return max
}

/**
 * Сложная
 *
 * Результаты спортсмена на соревнованиях в прыжках в высоту представлены строкой вида
 * "220 + 224 %+ 228 %- 230 + 232 %%- 234 %".
 * Здесь + соответствует удачной попытке, % неудачной, - пропущенной.
 * Высота и соответствующие ей попытки разделяются пробелом.
 * Прочитать строку и вернуть максимальную взятую высоту (230 в примере).
 * При нарушении формата входной строки, а также в случае отсутствия удачных попыток,
 * вернуть -1.
 */
fun bestHighJump(jumps: String): Int {
    if (Regex("""\d+ \+""").find(jumps) != null) {
        val list = jumps.split(' ')
        var max = 0
        for (i in 0 until list.lastIndex)
            if (list[i + 1] == "+")
                max = maxOf(max, list[i].toInt())
        return max
    } else
        return -1
}

/**
 * Сложная
 *
 * В строке представлено выражение вида "2 + 31 - 40 + 13",
 * использующее целые положительные числа, плюсы и минусы, разделённые пробелами.
 * Наличие двух знаков подряд "13 + + 10" или двух чисел подряд "1 2" не допускается.
 * Вернуть значение выражения (6 для примера).
 * Про нарушении формата входной строки бросить исключение IllegalArgumentException
 */
fun plusMinus(expression: String): Int {
    val e = IllegalArgumentException()
    if (expression == "")
        throw e
    val listStr = expression.split(" ")
    for (i in listStr.indices) {
        if (i % 2 == 0 && (listStr[i].contains("+") || (listStr[i].contains("-"))) ||
                i % 2 != 0 && listStr[i] != "+" && listStr[i] != "-")
            throw e
        try {
            if (i % 2 == 0) listStr[i].toInt()
        } catch (ex: Exception) {
            throw e
        }
    }
    var sign = 1
    var result = 0
    for (item in listStr) {
        if (item != "+" && item != "-")
            result += sign * item.toInt()
        else if (item == "+")
            sign = 1
        else
            sign = -1
    }
    return result
}

/**
 * Сложная
 *
 * Строка состоит из набора слов, отделённых друг от друга одним пробелом.
 * Определить, имеются ли в строке повторяющиеся слова, идущие друг за другом.
 * Слова, отличающиеся только регистром, считать совпадающими.
 * Вернуть индекс начала первого повторяющегося слова, или -1, если повторов нет.
 * Пример: "Он пошёл в в школу" => результат 9 (индекс первого 'в')
 */
fun firstDuplicateIndex(str: String): Int {
    val list = str.toLowerCase().split(" ")
    var count = 0
    for (i in 0 until list.lastIndex) {
        if (list[i] == list[i + 1])
            return count
        count += list[i].length + 1
    }
    return -1
}

/**
 * Сложная
 *
 * Строка содержит названия товаров и цены на них в формате вида
 * "Хлеб 39.9; Молоко 62; Курица 184.0; Конфеты 89.9".
 * То есть, название товара отделено от цены пробелом,
 * а цена отделена от названия следующего товара точкой с запятой и пробелом.
 * Вернуть название самого дорогого товара в списке (в примере это Курица),
 * или пустую строку при нарушении формата строки.
 * Все цены должны быть больше либо равны нуля.
 */
fun mostExpensive(description: String): String {
    val list = description.split("; ")
    var max = 0.0
    var result = ""
    try {
        for (item in list) {
            val itemToList = item.split(" ")
            val name = itemToList[0]
            val digit = itemToList[1].split(".")
            var cur = -1.0
            if (digit.size == 2)
                cur = digit[0].toDouble() + digit[1].toDouble() / 10.0.pow(digit[1].length)
            else if (digit.size == 1)
                cur = digit[0].toDouble()
            if (cur >= max) {
                max = cur
                result = name
            }
        }
    } catch (e: Exception) {
        return ""
    }
    return result
}

/**
 * Сложная
 *
 * Перевести число roman, заданное в римской системе счисления,
 * в десятичную систему и вернуть как результат.
 * Римские цифры: 1 = I, 4 = IV, 5 = V, 9 = IX, 10 = X, 40 = XL, 50 = L,
 * 90 = XC, 100 = C, 400 = CD, 500 = D, 900 = CM, 1000 = M.
 * Например: XXIII = 23, XLIV = 44, C = 100
 *
 * Вернуть -1, если roman не является корректным римским числом
 */
fun fromRoman(roman: String): Int {
    val romanDigits = arrayOf("I", "IV", "V", "IX", "X", "XL", "L", "XC", "C", "CD", "D", "CM", "M")
    val arabDigits = arrayOf(1, 4, 5, 9, 10, 40, 50, 90, 100, 400, 500, 900, 1000)
    var result = 0
    if (roman == "")
        return -1
    var str = roman
    while (str != "") {
        for (i in romanDigits.indices) {
            if (str.endsWith(romanDigits[i])) {
                result += arabDigits[i]
                str = str.substring(0, str.lastIndex - romanDigits[i].length + 1)
                break
            } else if (i == romanDigits.lastIndex)
                return -1
        }
    }
    return result
}

/**
 * Очень сложная
 *
 * Имеется специальное устройство, представляющее собой
 * конвейер из cells ячеек (нумеруются от 0 до cells - 1 слева направо) и датчик, двигающийся над этим конвейером.
 * Строка commands содержит последовательность команд, выполняемых данным устройством, например +>+>+>+>+
 * Каждая команда кодируется одним специальным символом:
 *	> - сдвиг датчика вправо на 1 ячейку;
 *  < - сдвиг датчика влево на 1 ячейку;
 *	+ - увеличение значения в ячейке под датчиком на 1 ед.;
 *	- - уменьшение значения в ячейке под датчиком на 1 ед.;
 *	[ - если значение под датчиком равно 0, в качестве следующей команды следует воспринимать
 *  	не следующую по порядку, а идущую за соответствующей следующей командой ']' (с учётом вложенности);
 *	] - если значение под датчиком не равно 0, в качестве следующей команды следует воспринимать
 *  	не следующую по порядку, а идущую за соответствующей предыдущей командой '[' (с учётом вложенности);
 *      (комбинация [] имитирует цикл)
 *  пробел - пустая команда
 *
 * Изначально все ячейки заполнены значением 0 и датчик стоит на ячейке с номером N/2 (округлять вниз)
 *
 * После выполнения limit команд или всех команд из commands следует прекратить выполнение последовательности команд.
 * Учитываются все команды, в том числе несостоявшиеся переходы ("[" при значении под датчиком не равном 0 и "]" при
 * значении под датчиком равном 0) и пробелы.
 *
 * Вернуть список размера cells, содержащий элементы ячеек устройства после завершения выполнения последовательности.
 * Например, для 10 ячеек и командной строки +>+>+>+>+ результат должен быть 0,0,0,0,0,1,1,1,1,1
 *
 * Все прочие символы следует считать ошибочными и формировать исключение IllegalArgumentException.
 * То же исключение формируется, если у символов [ ] не оказывается пары.
 * Выход за границу конвейера также следует считать ошибкой и формировать исключение IllegalStateException.
 * Считать, что ошибочные символы и непарные скобки являются более приоритетной ошибкой чем выход за границу ленты,
 * то есть если в программе присутствует некорректный символ или непарная скобка, то должно быть выброшено
 * IllegalArgumentException.
 * IllegalArgumentException должен бросаться даже если ошибочная команда не была достигнута в ходе выполнения.
 *
 */

val primitiveCommands = arrayOf('+', '-', '>', '<', ' ')
var maxCommands = 0
var counterCommands = 0
var listCells = mutableListOf<Int>()
var i = 0

fun computeDeviceCells(cells: Int, commands: String, limit: Int): List<Int> {
    if (!checkCommands(commands)) throw IllegalArgumentException()
    maxCommands = limit
    counterCommands = 0
    listCells = MutableList(cells) { 0 }
    i = cells / 2
    doForString(commands)
    return listCells
}

fun checkCommands(string: String): Boolean {
    var count = 0
    for (item in string) {
        if (item == '[') count++
        if (item == ']') count--
        if (count < 0) return false
    }
    if (count != 0) return false
    return string.none { it !in listOf(' ', '+', '-', '>', '<', '[', ']') }
}

fun doForString(string: String) {
    var commands = string
    var localStrCommands = ""
    while (commands != "" && counterCommands < maxCommands) {
        if (commands == "") break
        if (commands[0] in primitiveCommands || commands[0] == '[' || commands[0] == ']') counterCommands++ else continue
        when (commands[0]) {
            in primitiveCommands -> {
                when (commands[0]) {
                    '+' -> listCells[i]++
                    '-' -> listCells[i]--
                    '>' -> i++
                    '<' -> i--
                }
                if (i !in 0..listCells.lastIndex) throw IllegalStateException()
                commands = removeFirstElement(commands)
            }
            '[' -> {
                localStrCommands = findLocalString(commands)
                when {
                    listCells[i] == 0 -> commands = removeLocalString(commands, localStrCommands, false)
                    localStrCommands == "" -> counterCommands = maxCommands
                    else -> {
                        doForString(localStrCommands)
                        commands = removeLocalString(commands, localStrCommands, true)
                    }
                }
            }
            ']' -> {
                when {
                    listCells[i] == 0 || localStrCommands == "" -> commands = removeFirstElement(commands)
                    localStrCommands != "" -> doForString(localStrCommands)
                }
            }
        }
    }
}

fun findLocalString(string: String): String {
    var count = 0
    for (i in string.indices) {
        if (string[i] == '[') count++
        else if (string[i] == ']') {
            if (count != 0) count--
            if (count == 0) return string.substring(1, i)
        }
    }
    return ""
}

fun removeFirstElement(string: String): String = string.substring(1)

fun removeLocalString(base: String, local: String, saveRightBracket: Boolean): String = when (saveRightBracket) {
    true -> base.substring(local.length + 1)
    else -> base.substring(local.length + 2)
}