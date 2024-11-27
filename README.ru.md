[![en](https://img.shields.io/badge/lang-en-red.svg)](https://github.com/qwatro2/kpv-logs/blob/master/README.md)
[![pt-br](https://img.shields.io/badge/lang-ru-green.svg)](https://github.com/qwatro2/kpv-logs/blob/master/README.ru.md)

# Анализатор логов

# Как пользоваться?

В IDEA открываем настройки конфигурации

![image.png](images/image.png)

![image.png](images/image%201.png)

![image.png](images/image%202.png)

Вот тут можно написать аргументы командной строки, после этого запускаем проект.

Если аргумент `--path` отсутствует, увидим ошибку

![image.png](images/image%203.png)

Если аргументом `--path` является не путь к локальным файлам или к файлу в интернете, увидим ошибку

![image.png](images/image%204.png)

Если аргументы `--from` и `--to` не в формате `YYYY-MM-DD`, увидим ошибку

![image.png](images/image%205.png)

Если аргументом `--format` является не `markdown` или `adoc`, увидим ошибку



![image.png](images/image%206.png)

Если указан только один из аргументов `--filter-field` и `--filter-value`, увидим ошибку

![image.png](images/image%207.png)

Если аргументом `--filter-field` указано поле, которого нет в логах, увидим ошибку

![image.png](images/image%208.png)

![image.png](images/image%209.png)

![image.png](images/image%2010.png)

Если аргумент `--output` содержит `*` , увидим ошибку

![image.png](images/image%2011.png)

Если аргумент `--output` не является локальным путем, увидим ошибку

![image.png](images/image%2012.png)

Если аргумент `--output` ведет к файлу, который нельзя создать или открыть, увидим ошибку

![image.png](images/image%2013.png)

Далее, если ошибок нет, программа проанализирует указанные логи и выведет их в формате

- `markdown` или `adoc`, если он указан соответствующим аргументом командной строки
- `plain text`, если аргумент не указан

  ![image.png](images/image%2014.png)


и

- в консоль, если не указан аргумент `--output`
- в файл, указанный аргументом `--output`

Метрики, которые считают количество запросов по какому-то признаку, выводят не более 5 с самым большим количеством записей.

# Что происходит внутри?

1. Парсинг аргументов командной строки
2. Валидация этих аргументов
3. Получение `Stream` записей с логами
4. Собираем статистики по этим логам
5. Печатаем собранные статистики
