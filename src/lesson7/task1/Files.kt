@file:Suppress("UNUSED_PARAMETER", "ConvertCallChainIntoSequence")

package lesson7.task1

import jdk.nashorn.internal.runtime.regexp.RegExpMatcher
import lesson3.task1.digitNumber
import java.io.File
import kotlin.math.pow

/**
 * Пример
 *
 * Во входном файле с именем inputName содержится некоторый текст.
 * Вывести его в выходной файл с именем outputName, выровняв по левому краю,
 * чтобы длина каждой строки не превосходила lineLength.
 * Слова в слишком длинных строках следует переносить на следующую строку.
 * Слишком короткие строки следует дополнять словами из следующей строки.
 * Пустые строки во входном файле обозначают конец абзаца,
 * их следует сохранить и в выходном файле
 */
fun alignFile(inputName: String, lineLength: Int, outputName: String) {
    val outputStream = File(outputName).bufferedWriter()
    var currentLineLength = 0
    for (line in File(inputName).readLines()) {
        if (line.isEmpty()) {
            outputStream.newLine()
            if (currentLineLength > 0) {
                outputStream.newLine()
                currentLineLength = 0
            }
            continue
        }
        for (word in line.split(" ")) {
            if (currentLineLength > 0) {
                if (word.length + currentLineLength >= lineLength) {
                    outputStream.newLine()
                    currentLineLength = 0
                } else {
                    outputStream.write(" ")
                    currentLineLength++
                }
            }
            outputStream.write(word)
            currentLineLength += word.length
        }
    }
    outputStream.close()
}

/**
 * Средняя
 *
 * Во входном файле с именем inputName содержится некоторый текст.
 * На вход подаётся список строк substrings.
 * Вернуть ассоциативный массив с числом вхождений каждой из строк в текст.
 * Регистр букв игнорировать, то есть буквы е и Е считать одинаковыми.
 *
 */

fun countSubstrings(inputName: String, substrings: List<String>): Map<String, Int> {
    val text = File(inputName).readText().toLowerCase()
    val list = substrings.map { it.toLowerCase() }
    val map = mutableMapOf<String, Int>()
    for (i in list.indices) {
        map[substrings[i]] = 0
        for (j in 0 until text.length - list[i].length + 1)
            for (k in list[i].indices) {
                if (list[i][k] == text[j + k]) {
                    if (k == list[i].lastIndex)
                        map[substrings[i]] = map[substrings[i]]!! + 1
                } else
                    break
            }
    }
    return map
}


/**
 * Средняя
 *
 * В русском языке, как правило, после букв Ж, Ч, Ш, Щ пишется И, А, У, а не Ы, Я, Ю.
 * Во входном файле с именем inputName содержится некоторый текст на русском языке.
 * Проверить текст во входном файле на соблюдение данного правила и вывести в выходной
 * файл outputName текст с исправленными ошибками.
 *
 * Регистр заменённых букв следует сохранять.
 *
 * Исключения (жюри, брошюра, парашют) в рамках данного задания обрабатывать не нужно
 *
 */

val lettersToReplace = mapOf('Ы' to 'И', 'Я' to 'А', 'Ю' to 'У', 'ы' to 'и', 'я' to 'а', 'ю' to 'у')
val lettersToCheck = setOf('Ж', 'Ч', 'Ш', 'Щ', 'ж', 'ч', 'ш', 'щ')

fun sibilants(inputName: String, outputName: String) {
    val text = File(inputName).readText()
    val writer = File(outputName).printWriter()
    if (text.isNotEmpty()) {
        var nextLetter = text[0]
        for (i in 1..text.lastIndex) {
            writer.print(nextLetter)
            nextLetter =
                    if (text[i - 1] !in lettersToCheck || lettersToReplace[text[i]] == null) text[i]
                    else lettersToReplace[text[i]]!!
        }
        writer.print(text.last())
    }
    writer.close()
}


/**
 * Средняя
 *
 * Во входном файле с именем inputName содержится некоторый текст (в том числе, и на русском языке).
 * Вывести его в выходной файл с именем outputName, выровняв по центру
 * относительно самой длинной строки.
 *
 * Выравнивание следует производить путём добавления пробелов в начало строки.
 *
 *
 * Следующие правила должны быть выполнены:
 * 1) Пробелы в начале и в конце всех строк не следует сохранять.
 * 2) В случае невозможности выравнивания строго по центру, строка должна быть сдвинута в ЛЕВУЮ сторону
 * 3) Пустые строки не являются особым случаем, их тоже следует выравнивать
 * 4) Число строк в выходном файле должно быть равно числу строк во входном (в т. ч. пустых)
 *
 */
fun centerFile(inputName: String, outputName: String) {
    val outputStream = File(outputName).bufferedWriter()
    var inputLines = File(inputName).readLines()
    if (inputLines.isNotEmpty()) {
        inputLines = inputLines.map { it.trim() }
        val maxLength = inputLines.maxBy { it.length }!!.length
        for (line in inputLines) {
            outputStream.write(" ".repeat((maxLength - line.length) / 2) + line)
            outputStream.newLine()
        }
    }
    outputStream.close()
}

/**
 * Сложная
 *
 * Во входном файле с именем inputName содержится некоторый текст (в том числе, и на русском языке).
 * Вывести его в выходной файл с именем outputName, выровняв по левому и правому краю относительно
 * самой длинной строки.
 * Выравнивание производить, вставляя дополнительные пробелы между словами: равномерно по всей строке
 *
 * Слова внутри строки отделяются друг от друга одним или более пробелом.
 *
 * Следующие правила должны быть выполнены:
 * 1) Каждая строка входного и выходного файла не должна начинаться или заканчиваться пробелом.
 * 2) Пустые строки или строки из пробелов трансформируются в пустые строки без пробелов.
 * 3) Строки из одного слова выводятся без пробелов.
 * 4) Число строк в выходном файле должно быть равно числу строк во входном (в т. ч. пустых).
 *
 * Равномерность определяется следующими формальными правилами:
 * 5) Число пробелов между каждыми двумя парами соседних слов не должно отличаться более, чем на 1.
 * 6) Число пробелов между более левой парой соседних слов должно быть больше или равно числу пробелов
 *    между более правой парой соседних слов.
 *
 * Следует учесть, что входной файл может содержать последовательности из нескольких пробелов  между слвоами. Такие
 * последовательности следует учитывать при выравнивании и при необходимости избавляться от лишних пробелов.
 * Из этого следуют следующие правила:
 * 7) В самой длинной строке каждая пара соседних слов должна быть отделена В ТОЧНОСТИ одним пробелом
 * 8) Если входной файл удовлетворяет требованиям 1-7, то он должен быть в точности идентичен выходному файлу
 */

fun alignFileByWidth(inputName: String, outputName: String) {
    val writer = File(outputName).bufferedWriter()
    var inputLines = File(inputName).readLines()
    if (inputLines.isNotEmpty()) {
        inputLines = inputLines.map { it.trim() }
        val maxLength = inputLines.maxBy { it.length }!!.length
        for (line in inputLines) {
            val words = line.split(" ").filter { it.isNotEmpty() }
            val countWords = words.count()
            val countSymbols = line.count { it != ' ' }
            val countSpaces = maxLength - countSymbols
            val currentSizeOfGap = if (countWords != 1) countSpaces / (countWords - 1) + 1 else 1
            val currentLengthOfLine = currentSizeOfGap * (countWords - 1) + countSymbols
            val dif = maxOf(0, currentLengthOfLine - maxLength)
            for (i in words.indices) {
                writer.write(words[i])
                if (i != words.lastIndex) {
                    if (words.lastIndex - i > dif)
                        writer.write(" ".repeat(currentSizeOfGap))
                    else
                        writer.write(" ".repeat(currentSizeOfGap - 1))
                }
            }
            writer.newLine()
        }
    }
    writer.close()
}

/**
 * Средняя
 *
 * Во входном файле с именем inputName содержится некоторый текст (в том числе, и на русском языке).
 *
 * Вернуть ассоциативный массив, содержащий 20 наиболее часто встречающихся слов с их количеством.
 * Если в тексте менее 20 различных слов, вернуть все слова.
 *
 * Словом считается непрерывная последовательность из букв (кириллических,
 * либо латинских, без знаков препинания и цифр).
 * Цифры, пробелы, знаки препинания считаются разделителями слов:
 * Привет, привет42, привет!!! -привет?!
 * ^ В этой строчке слово привет встречается 4 раза.
 *
 * Регистр букв игнорировать, то есть буквы е и Е считать одинаковыми.
 * Ключи в ассоциативном массиве должны быть в нижнем регистре.
 *
 */

fun top20Words(inputName: String): Map<String, Int> {
    val map = mutableMapOf<String, Int>()
    val text = File(inputName).readText()
    if (text.isEmpty()) return map
    val words = text.toLowerCase().split(Regex("""[^а-яА-Яa-zA-ZёЁ]+"""))
    for (word in words) map[word] = map.getOrDefault(word, 0) + 1
    return map.toList().sortedByDescending { it.second }.filterIndexed { i, it -> i < 20 && it.first != "" }.toMap()
}

/**
 * Средняя
 *
 * Реализовать транслитерацию текста из входного файла в выходной файл посредством динамически задаваемых правил.

 * Во входном файле с именем inputName содержится некоторый текст (в том числе, и на русском языке).
 *
 * В ассоциативном массиве dictionary содержится словарь, в котором некоторым символам
 * ставится в соответствие строчка из символов, например
 * mapOf('з' to "zz", 'р' to "r", 'д' to "d", 'й' to "y", 'М' to "m", 'и' to "yy", '!' to "!!!")
 *
 * Необходимо вывести в итоговый файл с именем outputName
 * содержимое текста с заменой всех символов из словаря на соответствующие им строки.
 *
 * При этом регистр символов в словаре должен игнорироваться,
 * но при выводе символ в верхнем регистре отображается в строку, начинающуюся с символа в верхнем регистре.
 *
 * Пример.
 * Входной текст: Здравствуй, мир!
 *
 * заменяется на
 *
 * Выходной текст: Zzdrавствуy, mир!!!
 *
 * Пример 2.
 *
 * Входной текст: Здравствуй, мир!
 * Словарь: mapOf('з' to "zZ", 'р' to "r", 'д' to "d", 'й' to "y", 'М' to "m", 'и' to "YY", '!' to "!!!")
 *
 * заменяется на
 *
 * Выходной текст: Zzdrавствуy, mир!!!
 *
 * Обратите внимание: данная функция не имеет возвращаемого значения
 */
fun transliterate(inputName: String, dictionary: Map<Char, String>, outputName: String) {
    val lowerDictionary = dictionary.mapKeys { it.key.toLowerCase() }.mapValues {
        it.value.toLowerCase()
    }
    val upperDictionary = dictionary.mapKeys { it.key.toUpperCase() }.mapValues {
        it.value.mapIndexed { i, it -> if (i != 0) it.toLowerCase() else it.toUpperCase() }.joinToString("")
    }
    val text = File(inputName).readText()
    val writer = File(outputName).printWriter()
    for (letter in text)
        when (letter) {
            in lowerDictionary.keys -> writer.print(lowerDictionary[letter])
            in upperDictionary.keys -> writer.print(upperDictionary[letter])
            else -> writer.print(letter)
        }
    writer.close()
}

/**
 * Средняя
 *
 * Во входном файле с именем inputName имеется словарь с одним словом в каждой строчке.
 * Выбрать из данного словаря наиболее длинное слово,
 * в котором все буквы разные, например: Неряшливость, Четырёхдюймовка.
 * Вывести его в выходной файл с именем outputName.
 * Если во входном файле имеется несколько слов с одинаковой длиной, в которых все буквы разные,
 * в выходной файл следует вывести их все через запятую.
 * Регистр букв игнорировать, то есть буквы е и Е считать одинаковыми.
 *
 * Пример входного файла:
 * Карминовый
 * Боязливый
 * Некрасивый
 * Остроумный
 * БелогЛазый
 * ФиолетОвый

 * Соответствующий выходной файл:
 * Карминовый, Некрасивый
 *
 * Обратите внимание: данная функция не имеет возвращаемого значения
 */
fun chooseLongestChaoticWord(inputName: String, outputName: String) {
    val lines = File(inputName).readLines()
    val list = mutableListOf<String>()
    var maxLength = -1
    for (item in lines) if (item.toLowerCase().length == item.toLowerCase().toSet().size) when {
        item.length == maxLength -> list.add(item)
        item.length > maxLength -> {
            list.clear()
            list.add(item)
            maxLength = item.length
        }
    }
    val writer = File(outputName).bufferedWriter()
    writer.write(list.joinToString(", "))
    writer.close()
}

/**
 * Сложная
 *
 * Реализовать транслитерацию текста в заданном формате разметки в формат разметки HTML.
 *
 * Во входном файле с именем inputName содержится текст, содержащий в себе элементы текстовой разметки следующих типов:
 * - *текст в курсивном начертании* -- курсив
 * - **текст в полужирном начертании** -- полужирный
 * - ~~зачёркнутый текст~~ -- зачёркивание
 *
 * Следует вывести в выходной файл этот же текст в формате HTML:
 * - <i>текст в курсивном начертании</i>
 * - <b>текст в полужирном начертании</b>
 * - <s>зачёркнутый текст</s>
 *
 * Кроме того, все абзацы исходного текста, отделённые друг от друга пустыми строками, следует обернуть в теги <p>...</p>,
 * а весь текст целиком в теги <html><body>...</body></html>.
 *
 * Все остальные части исходного текста должны остаться неизменными с точностью до наборов пробелов и переносов строк.
 * Отдельно следует заметить, что открывающая последовательность из трёх звёздочек (***) должна трактоваться как "<b><i>"
 * и никак иначе.
 *
 * При решении этой и двух следующих задач полезно прочитать статью Википедии "Стек".
 *
 * Пример входного файла:
Lorem ipsum *dolor sit amet*, consectetur **adipiscing** elit.
Vestibulum lobortis, ~~Est vehicula rutrum *suscipit*~~, ipsum ~~lib~~ero *placerat **tortor***,

Suspendisse ~~et elit in enim tempus iaculis~~.
 *
 * Соответствующий выходной файл:
<html>
<body>
<p>
Lorem ipsum <i>dolor sit amet</i>, consectetur <b>adipiscing</b> elit.
Vestibulum lobortis. <s>Est vehicula rutrum <i>suscipit</i></s>, ipsum <s>lib</s>ero <i>placerat <b>tortor</b></i>.
</p>
<p>
Suspendisse <s>et elit in enim tempus iaculis</s>.
</p>
</body>
</html>
 *
 * (Отступы и переносы строк в примере добавлены для наглядности, при решении задачи их реализовывать не обязательно)
 */
fun markdownToHtmlSimple(inputName: String, outputName: String) {
    TODO()
    /*
    val lines = File(inputName).readLines()
    val writer = File(outputName).printWriter()
    val font = mutableMapOf("*" to false, "**" to false, "~~" to false)
    writer.print("<html><body><p>")
    for (line in lines) {
        if (line.isEmpty()) {
            writer.print("</p><p>")
            continue
        }
        var i = -1
        while (i < line.lastIndex) {
            i++
            if (line[i] == '*') {
                if (i + 1 > line.lastIndex || line[i + 1] != '*') {
                    if (!font["*"]!!) {
                        font["*"] = true
                        writer.print("<i>")
                    } else {
                        font["*"] = false
                        writer.println("</i>")
                    }
                } else {
                    if (!font["**"]!!) {
                        font["**"] = true
                        writer.print("<b>")
                    } else {
                        font["**"] = false
                        writer.print("</b>")
                    }
                    i++
                }
                continue
            }
            if (line[i] == '~' && i + 1 <= line.lastIndex && line[i + 1] == '~') {
                if (!font["~~"]!!) {
                    font["~~"] = true
                    writer.print("<s>")
                } else {
                    font["~~"] = false
                    writer.print("</s>")
                }
                i++
                continue
            }
            writer.print(line[i])
        }
    }
    writer.print("</p></body></html>")
    writer.close()
    */
}

/**
 * Сложная
 *
 * Реализовать транслитерацию текста в заданном формате разметки в формат разметки HTML.
 *
 * Во входном файле с именем inputName содержится текст, содержащий в себе набор вложенных друг в друга списков.
 * Списки бывают двух типов: нумерованные и ненумерованные.
 *
 * Каждый элемент ненумерованного списка начинается с новой строки и символа '*', каждый элемент нумерованного списка --
 * с новой строки, числа и точки. Каждый элемент вложенного списка начинается с отступа из пробелов, на 4 пробела большего,
 * чем список-родитель. Максимально глубина вложенности списков может достигать 6. "Верхние" списки файла начинются
 * прямо с начала строки.
 *
 * Следует вывести этот же текст в выходной файл в формате HTML:
 * Нумерованный список:
 * <ol>
 *     <li>Раз</li>
 *     <li>Два</li>
 *     <li>Три</li>
 * </ol>
 *
 * Ненумерованный список:
 * <ul>
 *     <li>Раз</li>
 *     <li>Два</li>
 *     <li>Три</li>
 * </ul>
 *
 * Кроме того, весь текст целиком следует обернуть в теги <html><body>...</body></html>
 *
 * Все остальные части исходного текста должны остаться неизменными с точностью до наборов пробелов и переносов строк.
 *
 * Пример входного файла:
///////////////////////////////начало файла/////////////////////////////////////////////////////////////////////////////
 * Утка по-пекински
 * Утка
 * Соус
 * Салат Оливье
1. Мясо
 * Или колбаса
2. Майонез
3. Картофель
4. Что-то там ещё
 * Помидоры
 * Фрукты
1. Бананы
23. Яблоки
1. Красные
2. Зелёные
///////////////////////////////конец файла//////////////////////////////////////////////////////////////////////////////
 *
 *
 * Соответствующий выходной файл:
///////////////////////////////начало файла/////////////////////////////////////////////////////////////////////////////
<html>
<body>
<ul>
<li>
Утка по-пекински
<ul>
<li>Утка</li>
<li>Соус</li>
</ul>
</li>
<li>
Салат Оливье
<ol>
<li>Мясо
<ul>
<li>
Или колбаса
</li>
</ul>
</li>
<li>Майонез</li>
<li>Картофель</li>
<li>Что-то там ещё</li>
</ol>
</li>
<li>Помидоры</li>
<li>
Фрукты
<ol>
<li>Бананы</li>
<li>
Яблоки
<ol>
<li>Красные</li>
<li>Зелёные</li>
</ol>
</li>
</ol>
</li>
</ul>
</body>
</html>
///////////////////////////////конец файла//////////////////////////////////////////////////////////////////////////////
 * (Отступы и переносы строк в примере добавлены для наглядности, при решении задачи их реализовывать не обязательно)
 */
fun markdownToHtmlLists(inputName: String, outputName: String) {
    TODO()
    /*
    val lines = File(inputName).readLines()
    val writer = File(outputName).printWriter()
    val level = Array(6) { "" }
    writer.print("<html><body>")
    for (line in lines) {
        val curGap = line.count() - line.trim().count()
        val curLevel = curGap / 4
        if (level[curLevel] != "") {
            level[curLevel] = when (line[0]) {
                '*' -> "<ul>"
                in '0'..'9' -> "<ol>"

            }
        }
    }
    writer.print("</body></html>")
    writer.close()
     */
}

/**
 * Очень сложная
 *
 * Реализовать преобразования из двух предыдущих задач одновременно над одним и тем же файлом.
 * Следует помнить, что:
 * - Списки, отделённые друг от друга пустой строкой, являются разными и должны оказаться в разных параграфах выходного файла.
 *
 */
fun markdownToHtml(inputName: String, outputName: String) {
    TODO()
}

/**
 * Средняя
 *
 * Вывести в выходной файл процесс умножения столбиком числа lhv (> 0) на число rhv (> 0).
 *
 * Пример (для lhv == 19935, rhv == 111):
19935
 *    111
--------
19935
+ 19935
+19935
--------
2212785
 * Используемые пробелы, отступы и дефисы должны в точности соответствовать примеру.
 * Нули в множителе обрабатывать так же, как и остальные цифры:
235
 *  10
-----
0
+235
-----
2350
 *
 */
fun printMultiplicationProcess(lhv: Int, rhv: Int, outputName: String) {
    val res = lhv * rhv
    val lhvSize = digitNumber(lhv)
    val rhvSize = digitNumber(rhv)
    val resSize = digitNumber(res)
    val writer = File(outputName).printWriter()
    writer.println(" ".repeat(resSize - lhvSize + 1) + lhv)
    writer.println("*" + " ".repeat(resSize - rhvSize) + rhv)
    writer.println("-".repeat(resSize + 1))
    val firstMult = lhv * (rhv % 10)
    writer.println(" ".repeat(resSize - digitNumber(firstMult) + 1) + firstMult)
    for (i in 2..rhvSize) {
        val curMult = lhv * (rhv / 10.0.pow(i - 1) % 10).toInt()
        writer.println("+" + " ".repeat(resSize - digitNumber(curMult) - i + 1) + curMult)
    }
    writer.println("-".repeat(resSize + 1))
    writer.println(" $res")
    writer.close()
}


/**
 * Сложная
 *
 * Вывести в выходной файл процесс деления столбиком числа lhv (> 0) на число rhv (> 0).
 *
 * Пример (для lhv == 19935, rhv == 22):
19935 | 22
-198     906
----
13
-0
--
135
-132
----
3

 * Используемые пробелы, отступы и дефисы должны в точности соответствовать примеру.
 *
 */
fun printDivisionProcess(lhv: Int, rhv: Int, outputName: String) {
    val writer = File(outputName).printWriter()

    val res = lhv / rhv
    if (res == 0 && lhv > 9) {
        val lhvSize = digitNumber(lhv)
        writer.println("$lhv | $rhv\n" + " ".repeat(lhvSize - 2) + "-0   " + 0 + "\n" + "-".repeat(lhvSize) + "\n" + lhv)
        writer.close()
        return
    }
    val resSize = digitNumber(res)
    val resList = mutableListOf<Int>()
    for (i in 0 until resSize)
        resList.add(res / 10.0.pow(resSize - i - 1).toInt() % 10)

    val lhvSize = digitNumber(lhv)
    val lhvList = mutableListOf<Int>()
    for (i in 0 until lhvSize)
        lhvList.add(lhv / 10.0.pow(lhvSize - i - 1).toInt() % 10)
    var indToAdd = 0

    // a - то, из чего вычитаем, b - то, что вычитаем: 199 / 22 -> a = 199, b = 22 * 9 = 198
    var a = 0
    var b = resList[0] * rhv
    for (i in 0..lhvSize) {
        a = a * 10 + lhvList[i]
        if (a >= b) break
    }
    var aSize = digitNumber(a)
    var bSize = digitNumber(b)

    var sizeOfGap = aSize - bSize
    if (aSize - bSize > 0) {
        writer.println("$lhv | $rhv")
        sizeOfGap--
    } else
        writer.println(" $lhv | $rhv")
    writer.println(" ".repeat(sizeOfGap) + "-" + resList[0] * rhv + " ".repeat(lhvSize - aSize + 3) + res)
    writer.println("-".repeat(bSize + 1))
    indToAdd += aSize
    a -= b
    sizeOfGap += bSize - digitNumber(a) + 1
    writer.print(" ".repeat(sizeOfGap) + a)

    for (i in 1 until resSize) {
        if (a == 0) sizeOfGap++
        a = a * 10 + lhvList[indToAdd]
        b = rhv * resList[i]
        aSize = digitNumber(a)
        bSize = digitNumber(b)
        writer.println(lhvList[indToAdd])
        sizeOfGap += aSize - bSize - 1
        writer.println(" ".repeat(sizeOfGap) + "-" + b)
        if (b != 0) {
            writer.println(" ".repeat(sizeOfGap) + "-".repeat(bSize + 1))
            if (aSize == bSize) sizeOfGap++
            sizeOfGap += aSize - digitNumber(a - b)
            a -= b
            writer.print(" ".repeat(sizeOfGap) + a)
        } else {
            if (a < 10) {
                sizeOfGap -= aSize - bSize
                writer.println(" ".repeat(sizeOfGap) + "-".repeat(2))
                sizeOfGap++
            } else {
                sizeOfGap -= aSize - bSize - 1
                writer.println(" ".repeat(sizeOfGap) + "-".repeat(aSize))
            }
            writer.print(" ".repeat(sizeOfGap) + a)
        }
        indToAdd++
    }

    writer.close()
}

