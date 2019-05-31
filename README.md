# Black Magic

An Android app I made to automate weekly emails to my soccer teammates (black
magicians). Written in Kotlin with a JNI wrapper over a Rust web scraping
library.

## Setup

### Prerequisites

You need Docker and Android Studio. Installation instructions available online
for your OS.

### Rust

`make docker-image` builds the base docker image. I had to do this because of
OpenSSL cross-compilation problems using rustup on macOS.

`make build` builds the Rust library in a few major supported Android
architectures (currently aarch64, armv7, i686). There are individual make rules
for compiling each of these.

`make bundle` copies the Rust .so library to the Android directory for JNI
libraries.

### Android

Once the Rust libraries are copied over, run the app in Android studio and
connect your favorite device.

## Contributing

Contributions welcome! Maybe at some point given my frustrations with OpenSSL
I'll put the Docker image on DockerHub.
