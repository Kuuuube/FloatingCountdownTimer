# Floating Timer

<a href='https://play.google.com/store/apps/details?id=xyz.tberghuis.floatingtimer'><img alt='Get it on Google Play' src='https://play.google.com/intl/en_us/badges/static/images/badges/en_badge_web_generic.png' width="200"/></a>

Features both a countdown timer and stopwatch that will float over other apps.

* Can be dragged / moved around.
* Tap to start / pause
* Double tap to reset
* Drag to trash to exit

SYSTEM_ALERT_WINDOW uses ComposeView that I got working using hacks discussed
https://gist.github.com/handstandsam/6ecff2f39da72c0b38c07aa80bbb5a2f
https://stackoverflow.com/questions/64585547/jetpack-compose-crash-when-adding-view-to-window-manager

## Screenshot
<img alt='Screenshot' src='https://github.com/tberghuis/FloatingCountdownTimer/raw/master/docs/images/Screenshot_home.png' width="350"/> <img alt='Screenshot' src='https://github.com/tberghuis/FloatingCountdownTimer/raw/master/docs/images/Screenshot_halo_color.png' width="350"/>

## Building

Built on Android Studio Hedgehog | 2023.1.1 Patch2, installing through ADB is recommended for testing.

To install through ADB:

1. Enable ADB through developer options on your phone

2. Plug your phone into your computer or connect wirelessly

3. Run the app from Android Studio
