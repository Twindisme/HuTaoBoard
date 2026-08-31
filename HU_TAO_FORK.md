# Hu Tao Board

This is a private, personal fork of HeliBoard v4.0 with assets extracted from
the official OnePlus Ace Pro Hu Tao Edition firmware.

## Current changes

- Separate Android application ID: `helium314.keyboard.hutao`
- Separate app and input-method name: `Hu Tao Board`
- Built-in Hu Tao color theme enabled by default
- Original starry red portrait keyboard background
- Original gold-framed normal, pressed, and space-key artwork
- Original round and diamond artwork for Enter, Shift, Delete, and function keys
- Protected frame corners so wide keys no longer stretch their ornamentation
- Tighter default key gaps and no extra internal bottom padding
- Taller keys, slightly oversized artwork, and an exact-aspect spacebar
- Language and emoji utility slots enabled to restore the compact original bottom-row proportions
- Exact-style ivory-to-coral foreground treatment for functional icons
- Solid gradient Backspace and the exact original return-arrow PNG for Enter
- Original candidate-bar artwork, backpack toggle states, and flower flourish
  adapted around HeliBoard's functional toolbar
- Backpack opens Clipboard; the chevron over the right flower flourish toggles
  between toolbar buttons and word suggestions
- Original 30-frame butterfly effect on every key press
- Butterfly playback relaxed to 24 ms per frame (720 ms total) so the artwork
  remains visible while typing

The typing engine, layouts, settings, offline behavior, and permissions remain
those of HeliBoard v4.0.

## Private updates with Obtainium

The fork can publish signed APKs to a private GitHub repository for Obtainium
to track. The keyboard itself remains offline and does not need internet or
package-install permissions.

The `Release Hu Tao Board` GitHub Actions workflow builds and publishes a
release whenever a `v*` tag is pushed. It requires these repository secrets:

- `HU_TAO_KEYSTORE_BASE64`: the permanent JKS keystore encoded as one-line base64
- `HU_TAO_KEYSTORE_PASSWORD`: the keystore password
- `HU_TAO_KEY_ALIAS`: the signing-key alias
- `HU_TAO_KEY_PASSWORD`: the signing-key password

Never replace or lose this signing key. Android only accepts an update when it
has the same application ID, the same signing certificate, and a higher
`versionCode`. Increment both the version code and name before tagging a new
release.

The existing `personal` APK uses Android's debug certificate. Moving to the
permanent release certificate therefore requires uninstalling that test build
once before installing the first release APK.

## Local build requirements

- JDK 17
- Android SDK platform 36
- Android NDK `28.0.13004108`

After those are installed, the private installable APK can be produced with
`./gradlew :app:assemblePersonal`. Its separate application ID allows it to
coexist with the regular HeliBoard app. The `personal` variant is not
debuggable or minified, and is signed with the machine's local Android debug
certificate for straightforward sideloading.

## Local visual testing

A standalone Android 15 emulator is configured as `HuTaoNord4` at the phone's
1240 x 2772 resolution and 450 dpi. Android Studio is not required.

- Start it with `./tools/start-hu-tao-emulator.sh`.
- Install and select the latest test APK with
  `./tools/install-hu-tao-test-apk.sh`.
- Pass a different APK path as the install script's first argument when needed.

## Asset provenance

- Device: OnePlus Ace Pro Hu Tao Edition (`PGP110Ovt`)
- Firmware build: `PGP110Ovt_11.H.23_3230_202512311446`
- Firmware partition: `my_company`
- Original theme path: `/etc/inputmethod/theme/baidu_theme.bda`
- Original theme SHA-256:
  `6cc2a065ecd5b1ff588b3b621d52d7baea398ebf26ece36499c31061ffcf0ade`
- Keyboard background in the theme: `res/res_26back1_1.png`
- Key artwork in the theme: `res/res_26btn_d_*.png`,
  `res/res_26space_*.png`, `res/res_26btn_shift_*.png`,
  `res/res_26btn_del_*.png`, `res/res_26enter_*.png`, and
  `res/res_26btn_F1_*.png`
- Animation frames in the theme: `res/hudie00.png` through
  `res/hudie29.png`

## Rights and distribution

HeliBoard source remains GPL-3.0. The bundled Hu Tao/Genshin Impact and
OnePlus theme artwork is not covered by HeliBoard's license and belongs to its
respective rights holders. Keep this asset-bearing fork and any APK built from
it private; do not publish it to an app store or public repository.
