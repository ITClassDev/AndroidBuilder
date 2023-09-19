# Android-app Builder
[🇬🇧 English](/README.md) [🇷🇺 Russian](/README.ru.md)

## About

* It is an adjacent component between a mobile application and a website.
* Gradle is used for work, which will sing another Gradle inside itself (it is supposed to run the construction inside Docker)
* During compilation, the collector replaces the standard branding and endpoints with those specified in ENV
* Console information is duplicated in the LOG file at the expense of Log4j

## File structure
Application in /builder/ folder have only one open volume folder /builder/workdir for the docker, with this structure.

```
/builder/workdir/
├── latest.log         -   Log file (single for all runs)
├── state.json         -   Application state file (for latest build info)
├── temp
│   │── manifest.json  -   Temp release manifest before copying
│   └── application    -   Android Mobile project folder
│       └── ...
└── output
    ├── release.json   -   Output artefact information
    └── ShTP.apk       -   Built APK file
```

Files <ins>latest.log</ins>, <ins>state.json</ins>, <ins>temp folder</ins> not important for another components, but you need to store them on a storage fs.
But output directory is needed to transfer the APK file to the backend


#### Release Manifest `release.json`
```json5
{
  "change_log": {                   // MD Format
    "en": "**Perfect** _update_",
    "ru": "~~Отличное~~ обновление"
  },
  "release_date": 1694864027785,    // Epoch time in millis 
  "version_name": "0.3.7",          // User-friendly public version
  "version_tag": 45                 // Hidden version code
}
```

#### Application State `state.json`
This files contains only information about previous build (now last build commit id)
```
{
  "checked_version": "e7ff3af6a430b49160cc0ba6f2a5631d7f192600"
}
```

## Environment variables

`VOLUME_FOLDER` (Default: workdir/state.json)

`OUT_RELEASE_JSON` (Default: workdir/output/release.json)

`OUT_RELEASE_APK` (Default: workdir/output/ShTP.apk)

`TEMP_RELEASE_JSON` (Default: workdir/temp/manifest.json)

`TEMP_RELEASE_APP` (Default: workdir/temp/application)

`WAIT_MINUTES` (Default: 5)

`GIT_REPO` (Default: https://github.com/ITClassDev/Mobile)