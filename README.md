# Android Builder

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

Files <ins>latest.log, state.json, temp folder</ins> not important for another components, but you need to store them on a storage fs.
The output directory is already needed to transfer the APK file to the backend

#### Release Manifest
```
{
  "change_log": "Perfect update",   # MD Format
  "release_date": 1694864027785,    # Epoch time in millis 
  "version_name": "0.3.7",          # User-friendly public version
  "version_tag": 45                 # Hidden version code
}
```

#### Application State
This files contains only information about previous build (now last build commit id)
```
{
  "checked_version": "e7ff3af6a430b49160cc0ba6f2a5631d7f192600"
}
```