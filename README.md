# Hu Tao Board

Hu Tao Board is an unofficial Hu Tao-themed Android keyboard based on
[HeliBoard](https://github.com/HeliBorg/HeliBoard). It recreates the look and feel of the
OnePlus Ace Pro Hu Tao Edition keyboard while keeping HeliBoard's practical features and
customizability.

[![Download the latest APK](https://img.shields.io/badge/Download-Latest%20APK-8f2938?style=for-the-badge&logo=android&logoColor=white)](https://github.com/Twindisme/HuTaoBoard/releases/latest/download/HuTaoBoard-latest.apk)

## Features

- Hu Tao keycaps, special key shapes, spacebar, toolbar, and background artwork
- Themed key previews, long-press panels, clipboard suggestions, and butterfly effects
- Suggestions, multilingual typing, clipboard history, emoji, and HeliBoard customization
- Built-in update checking and verified downloads from GitHub Releases
- No advertisements, analytics, or telemetry

## Installation

1. Download the latest APK using the button above.
2. Allow your browser to install unknown apps if Android asks.
3. Install Hu Tao Board, then enable and select it in Android's keyboard settings.

If an early test build is already installed and Android reports a signature conflict, back up its
settings, uninstall it, and install the release APK. Releases use a consistent signing key, so later
versions can update normally.

## Network access

Unlike upstream HeliBoard, Hu Tao Board requests internet access for its built-in updater. It only
contacts this repository's GitHub Releases to check for and download updates. Downloaded APKs are
verified against the release's SHA-256 checksum before Android's installer is opened.

## Credits and license

Hu Tao Board is based on [HeliBoard](https://github.com/HeliBorg/HeliBoard), which is based on
OpenBoard and the AOSP LatinIME keyboard. The source code remains available under the
[GNU General Public License v3.0](LICENSE).

This is an unofficial fan project and is not affiliated with or endorsed by HoYoverse, OnePlus, or
their respective rights holders. Hu Tao and related artwork belong to their respective owners.
