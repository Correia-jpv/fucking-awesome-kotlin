
import link.kotlin.scripts.dsl.Article
import link.kotlin.scripts.dsl.LanguageCodes.RU
import link.kotlin.scripts.dsl.LinkType.article
import java.time.LocalDate

// language=Markdown
val body = """

## Часть 1. Введение
Андрей Бреслав расскажет про то, что такое Kotlin, как у него сейчас дела и т.д.
[Андрей Бреслав — Что такое Kotlin Введение](https://www.youtube.com/watch?v=HWyd1gYMkl0)

## Часть 2. К релизу и далее
Начиная с версии 1.0, Kotlin предоставляет гарантии обратной совместимости с существующим кодом. Дмитрий Жемеров расскажет о том, что в точности они обещают на будущее, о том, как это обещание повлияло на дизайн языка, и о том, под какие направления будущего развития уже есть заготовки в версии 1.0.
[Дмитрий Жемеров, JetBrains — Kotlin - к релизу и далее](https://www.youtube.com/watch?v=m5T0M7SnCC0)

## Часть 3. Самое сложное — совместимость
[Андрей Бреслав — Kotlin - самое сложное — совместимость](https://www.youtube.com/watch?v=LWFx4QWrTyo)
Андрей Бреслав расскажет о том, как они боролись за прозрачную совместимость: чтобы Kotlin и Java могли дружно обитать в одном проекте. Для этого пришлось придумать немало оригинальных решений и пойти на многие компромиссы на всех уровнях: от системы типов до плагинов к билд-системам.

## Часть 4. Сессия вопросов и ответов
[Андрей Бреслав и Дмитрий Жемеров — Kotlin - сессия вопросов и ответов](https://www.youtube.com/watch?v=YOmdOTlhZa8)
Андрей, Дмитрий и другие разработчики Kotlin с удовольствием ответят на ваши вопросы.

"""

Article(
  title = "Видео со встречи JUG.ru с разработчиками Kotlin",
  url = "https://github.com/Heapy/awesome-kotlin/blob/master/app/rss/articles/%5BRU%5D%20%D0%92%D0%B8%D0%B4%D0%B5%D0%BE%20%D1%81%D0%BE%20%D0%B2%D1%81%D1%82%D1%80%D0%B5%D1%87%D0%B8%20JUG.ru%20%D1%81%20%D1%80%D0%B0%D0%B7%D1%80%D0%B0%D0%B1%D0%BE%D1%82%D1%87%D0%B8%D0%BA%D0%B0%D0%BC%D0%B8%20Kotlin.md",
  categories = listOf(
    "Kotlin",
    "Video"
  ),
  type = article,
  lang = RU,
  author = "JetBrains",
  date = LocalDate.of(2016, 3, 4),
  body = body
)
