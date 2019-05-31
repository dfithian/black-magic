.PHONY: all

docker_tag=android-rust

docker-image:
	curl https://sh.rustup.rs -sSf > docker/install-rustup.sh
	chmod +x docker/install-rustup.sh
	docker build docker --tag $(docker_tag)

build-aarch64:
	docker run --rm -v "$$PWD/rust":/usr/src/rust -w /usr/src/rust $(docker_tag) cargo build --target aarch64-linux-android --release

build-armv7:
	docker run --rm -v "$$PWD/rust":/usr/src/rust -w /usr/src/rust $(docker_tag) cargo build --target armv7-linux-androideabi --release

build-i686:
	docker run --rm -v "$$PWD/rust":/usr/src/rust -w /usr/src/rust $(docker_tag) cargo build --target i686-linux-android --release

build: build-aarch64 build-armv7 build-i686

bundle: build
	cp rust/target/aarch64-linux-android/release/libblack_magic.so android/app/src/main/jniLibs/arm64-v8a/libblack_magic.so
	cp rust/target/armv7-linux-androideabi/release/libblack_magic.so android/app/src/main/jniLibs/armeabi-v7a/libblack_magic.so
	cp rust/target/i686-linux-android/release/libblack_magic.so android/app/src/main/jniLibs/x86/libblack_magic.so
