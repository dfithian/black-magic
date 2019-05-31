FROM rust:1.35
MAINTAINER Dan Fithian <daniel.m.fithian@gmail.com>

# dependencies for build
RUN apt-get update && apt-get install -qq -y --no-install-recommends \
    openjdk-8-jdk \
    libssl-dev

# install rust
RUN mkdir /rust
ADD install-rustup.sh /rust
RUN ./rust/install-rustup.sh -y
RUN rustup target add aarch64-linux-android armv7-linux-androideabi i686-linux-android

# install android
ENV ANDROID_HOME /android
RUN mkdir /android
RUN cd /android \
    && wget https://dl.google.com/android/repository/sdk-tools-linux-4333796.zip \
    && unzip sdk-tools-linux-4333796.zip \
    && echo y | /android/tools/bin/sdkmanager ndk-bundle
ENV PATH "/android/ndk-bundle/toolchains/llvm/prebuilt/linux-x86_64/bin/:${PATH}"

# post install setup
ENV PKG_CONFIG_ALLOW_CROSS 1
RUN ln -s /android/ndk-bundle/toolchains/llvm/prebuilt/linux-x86_64/bin/aarch64-linux-android28-clang /android/ndk-bundle/toolchains/llvm/prebuilt/linux-x86_64/bin/aarch64-linux-android-clang
RUN ln -s /android/ndk-bundle/toolchains/llvm/prebuilt/linux-x86_64/bin/armv7a-linux-androideabi28-clang /android/ndk-bundle/toolchains/llvm/prebuilt/linux-x86_64/bin/arm-linux-androideabi-clang
RUN ln -s /android/ndk-bundle/toolchains/llvm/prebuilt/linux-x86_64/bin/i686-linux-android28-clang /android/ndk-bundle/toolchains/llvm/prebuilt/linux-x86_64/bin/i686-linux-android-clang
