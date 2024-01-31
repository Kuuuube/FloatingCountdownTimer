# Floating Timer

A countdown timer and stopwatch that will float over other apps.

## Installation

1. Download the latest APK file from [Releases](https://github.com/Kuuuube/FloatingCountdownTimer/releases/latest)

2. Open the APK file

3. You may get a warning saying `Unsafe app blocked` `Play Protect doesn't recognize this app's developer.`

    This is because I am not a verified developer and have not uploaded any apps to the play store.

    To get past this warning, tap `More details` and `Install anyway`.

    If you would prefer to build it yourself, see [Building](#building).

## Usage

1. Set up your timer settings. Optionally, change additional settings from the `...` menu in the top right.

2. Click `Create` on the timer or stopwatch.

3. Control the timer in the following ways:

    - Dragging or moving it around the screen

    - Tapping on it to start and stop

    - Double tapping on it to reset the time

    - Dragging it to trash to remove it

## Building

Built on Android Studio Hedgehog | 2023.1.1 Patch2, installing through ADB is recommended for testing.

To install through ADB:

1. Enable ADB through developer options on your phone

2. Plug your phone into your computer or connect wirelessly

3. Load the project into Android Studio

4. Go to `Run` > `Run 'App'`

To build an APK:

1. Load the project into Android Studio

2. Go to `Build` > `Generate Signed Bundle / APK`

3. Select `APK`

4. If you already have a signing key, input it. Otherwise, select `Create New`.

5. Click `Next`

6. Select `Release`

7. Click `Create`

8. The build should appear under `./app/release/`
