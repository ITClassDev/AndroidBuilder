# Android Builder

## About

* It is an adjacent component between a mobile application and a website.
* Gradle is used for work, which will sing another Gradle inside itself (it is supposed to run the construction inside Docker)
* During compilation, the collector replaces the standard branding and endpoints with those specified in ENV
* Console information is duplicated in the LOG file at the expense of Log4j

## File structure
Application in /builder/ folder have only one open volume folder /builder/workdir for the docker, with this structure.
Files latest.log, state.json, temp folder not important for another components, but you need to store them on a storage fs.

The output directory is already needed to transfer the APK file to the backend
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

