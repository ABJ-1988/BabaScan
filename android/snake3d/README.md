# Snake 3D — Android-Projekt (`com.retro.snake3d`)

Buildbares Gradle-Projekt für das Spiel **Snake 3D**, aus dem das Play-Store-Bundle
(`.aab`) erzeugt wird.

## Warum es dieses Projekt gibt

Das vorhandene Artefakt war `Snake3D-v1.2.apk` — ein **Debug-Build**
(`android:debuggable="true"`, `versionCode=3`). Google Play weist debuggable
Artefakte beim Upload grundsätzlich zurück, und ein APK lässt sich nicht in ein
AAB konvertieren. Die Quellen lagen nicht vor, deshalb wurde das Projekt aus dem
APK rekonstruiert:

* **Code** — via `jadx` aus den DEX-Dateien (Kotlin → Java). Die Spiel-Logik ist
  unverändert übernommen; angepasst wurden nur Stellen, die als Java nicht
  kompilierbar waren (Kotlin-Default-Argumente, `EnumEntries`-Plumbing,
  Checked Exceptions, von jadx gelöschte Generics).
* **Ressourcen** — via `apktool` aus `resources.arsc`. Layout, Drawables, Farben,
  Strings und Styles entsprechen 1:1 dem Original.
* **Manifest** — neu geschrieben ohne `debuggable`; Package, Activity,
  Permissions, `screenOrientation`, `launchMode` und `configChanges` sind
  identisch zum Original.

## Unterschiede zum alten APK

| | alt (1.2 APK) | neu (AAB) |
|---|---|---|
| Build-Typ | debug | release |
| `android:debuggable` | `true` | nicht gesetzt |
| `versionCode` | 3 | 4 |
| `versionName` | 1.2 | 1.2 |
| Signatur | Android-Debug-Key | eigener Upload-Key (v2 + v3) |
| Format | APK | AAB |

`minSdk 24`, `targetSdk 35`, GLES 3.0 — wie zuvor.

## Bauen

Voraussetzungen: JDK 17+, Android SDK mit `platforms;android-35` und
`build-tools;35.0.0`.

```bash
export ANDROID_HOME=/pfad/zum/android-sdk

# Signing-Zugangsdaten anlegen (wird nicht eingecheckt)
cat > keystore.properties <<'EOF'
storeFile=snake3d-upload.jks
storePassword=DEIN_PASSWORT
keyAlias=snake3d-upload
keyPassword=DEIN_PASSWORT
EOF

./gradlew bundleRelease     # -> app/build/outputs/bundle/release/app-release.aab
./gradlew assembleRelease   # -> app/build/outputs/apk/release/  (zum Testen)
```

Ohne `keystore.properties` läuft der Build durch, erzeugt aber ein **unsigniertes**
Artefakt — Play lehnt das ab.

### Neuen Upload-Key erzeugen

```bash
keytool -genkeypair -v -keystore snake3d-upload.jks -alias snake3d-upload \
  -keyalg RSA -keysize 4096 -validity 10000
```

> Der Keystore und `keystore.properties` sind per `.gitignore` ausgeschlossen.
> Geht der Keystore verloren, sind Updates dieser App nur noch nach einem
> Upload-Key-Reset durch Google möglich — also sichern.

## Vor dem Upload prüfen

```bash
java -jar bundletool.jar validate --bundle=app/build/outputs/bundle/release/app-release.aab
java -jar bundletool.jar build-apks --mode=universal \
  --bundle=app/build/outputs/bundle/release/app-release.aab --output=snake.apks \
  --ks=snake3d-upload.jks --ks-key-alias=snake3d-upload
# universal.apk aus snake.apks entpacken und auf einem echten Gerät installieren
```
