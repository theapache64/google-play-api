![](cover.jpeg)

# google-play-api

![buildStatus](https://img.shields.io/github/workflow/status/theapache64/google-play-api/Java%20CI%20with%20Gradle?style=plastic)
![latestVersion](https://img.shields.io/github/v/release/theapache64/google-play-api)
<a href="https://twitter.com/theapache64" target="_blank">
<img alt="Twitter: theapache64" src="https://img.shields.io/twitter/follow/theapache64.svg?style=social" />
</a>

> A coroutines based Kotlin library to access play store

## 🛠 Installation

```groovy
repositories {
    maven { url = uri("https://jitpack.io") }
}

dependencies {
   implementation("com.google.protobuf:protobuf-java:3.14.0")
   implementation("com.github.theapache64:google-play-api:latest.version")
}
```


## ⌨️ Usage

```kotlin
val username = "example@gmail.com"
val password = "pass1234"

// Logging in
val account = Play.login(username, password)

// Creating API using logged in account
val api = Play.getApi(account)

// Accessing API
val appDetails = api.details(packageName) // to get all app details
val downloadData = api.download("com.whatsapp") // to download APK

// and much more...
```

## 🥼 Run tests

```shell script
./gradlew test
```

## ✍️ Author

👤 **theapache64**

* Twitter: <a href="https://twitter.com/theapache64" target="_blank">@theapache64</a>
* Email: theapache64@gmail.com

This library is a combination of APIs collected from `raccoon4` and `playcrawler`.
All credit goes to them.

## 🤝 Contributing

Contributions are what make the open source community such an amazing place to be learn, inspire, and create. Any
contributions you make are **greatly appreciated**.

1. Open an issue first to discuss what you would like to change.
1. Fork the Project
1. Create your feature branch (`git checkout -b feature/amazing-feature`)
1. Commit your changes (`git commit -m 'Add some amazing feature'`)
1. Push to the branch (`git push origin feature/amazing-feature`)
1. Open a pull request

Please make sure to update tests as appropriate.

## ❤ Show your support

Give a ⭐️ if this project helped you!

<a href="https://www.patreon.com/theapache64">
  <img alt="Patron Link" src="https://c5.patreon.com/external/logo/become_a_patron_button@2x.png" width="160"/>
</a>

<a href="https://www.buymeacoffee.com/theapache64" target="_blank">
    <img src="https://cdn.buymeacoffee.com/buttons/v2/default-yellow.png" alt="Buy Me A Coffee" width="160">
</a>

<a href="https://www.paypal.me/theapache64" target="_blank">
    <img src="https://www.paypalobjects.com/en_US/i/btn/btn_donateCC_LG.gif" alt="Donation" width="160">
</a>

## 📝 License

```
Copyright © 2021 - theapache64

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```

_This README was generated by [readgen](https://github.com/theapache64/readgen)_ ❤
