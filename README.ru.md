# Сборщик Android-приложения
[🇬🇧 English](/README.md) [🇷🇺 Russian](/README.ru.md)

## О репозитории

* Это компонент, который объединяет мобильное приложение и веб-сайт.
* Для работы используется Gradle, который в свою очередь запускает еще один Gradle внутри себя (предполагается выполнение сборки внутри Docker)
* Во время компиляции сборщик заменяет стандартные брендинг и конечные точки на те, которые указаны в ENV
* Консольный поток дублируется в LOG-файле с помощью Log4j


## Структура файлов
В папке /builder/ приложение имеет только одну открытую папку /builder/workdir для Docker, со следующей структурой:

```
/builder/workdir/
├── latest.log         -   Log-файл (общий для всех запусков)
├── state.json         -   Файл состояния приложения (с информацией о последней сборке)
├── temp
│   │── manifest.json  -   Временный файл манифеста релиза перед копированием
│   └── application    -   Папка с проектом мобильного приложения Android
│       └── ...
└── output
    ├── release.json   -   Информация о выходном артефакте
    └── ShTP.apk       -   Собранный файл APK
```

Файлы <ins>latest.log</ins>, <ins>state.json</ins>, <ins>папка temp</ins> не важны для других компонентов, но их нужно сохранить в файловой системе хранения.
Однако директория output нужна для передачи файла APK на backend.

#### Манифест релиза `release.json`
```
{
  "change_log": "Отличное обновление",   # Формат MD
  "release_date": 1694864027785,    # Время в миллисекундах в формате Epoch
  "version_name": "0.3.7",          # Людо-читаемая версия
  "version_tag": 45                 # Скрытый код версии
}
```

#### Состояние приложения `state.json`
Этот файл содержит только информацию о предыдущей сборке (сейчас это идентификатор последнего коммита)
```
{
  "checked_version": "e7ff3af6a430b49160cc0ba6f2a5631d7f192600"
}
```

## Переменные среды

`VOLUME_FOLDER` (По умолчанию: workdir/state.json)

`OUT_RELEASE_JSON` (По умолчанию: workdir/output/release.json)

`OUT_RELEASE_APK` (По умолчанию: workdir/output/ShTP.apk)

`TEMP_RELEASE_JSON` (По умолчанию: workdir/temp/manifest.json)

`TEMP_RELEASE_APP` (По умолчанию: workdir/temp/application)

`WAIT_MINUTES` (По умолчанию: 5)

`GIT_REPO` (По умолчанию: https://github.com/ITClassDev/Mobile)