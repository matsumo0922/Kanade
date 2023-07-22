<h1 align="center">Kanade</h1>

<p align="center">
Experience the thrill and excitement of music with Kanade!<br>
Kanade is an elegant music player app that can display synchronized lyrics.
</p>

<div align="center">
  <a href="./LICENSE">
    <img alt="LICENSE" src="https://img.shields.io/badge/license-GPL-blue.svg?maxAge=43200" />
  </a>
  <a href="">
    <img src="https://img.shields.io/badge/supports-Android%208+-AD2A5A">
  </a>
  <a href="https://github.com/matsumo0922/KanadeMark3/actions/workflows/pull-request-ci.yml/badge.svg">
    <img alt="CI" src="https://github.com/matsumo0922/KanadeMark3/actions/workflows/pull-request-ci.yml/badge.svg" />
  </a>
  <a href="https://open.vscode.dev/matsumo0922/KanadeMark3">
    <img alt="Open in VSCode" src="https://img.shields.io/static/v1?logo=visualstudiocode&label=&message=Open%20in%20Visual%20Studio%20Code&labelColor=2c2c32&color=007acc&logoColor=007acc" />
  </a>
</div>

<hr>

<p align="center">
    <img src="./media/screenshots.png" width="100%" alt="screenshots">
</p>

<p align="center">Do you speak Japanese？Japanese <a href="./README-ja.md">READEME</a> is Here!</p>

## Status
#### WIP :construction:

It's still under development! Please see the sections below for the features that are already implemented and those that should be implemented next. We don't yet have a planned completion date, but we aim to finish it within the year 2023! Contributions are always welcome. Please follow the sections below to build the app.

## Why?

I created this as a test of my own skills! It's like a portfolio. I wanted to deepen my knowledge of `Android`, `Kotlin`, and `Jetpack Compose`, and let people know about my capabilities. In addition, I was unsatisfied that existing music player apps could not display lyrics for music stored locally. Even subscription music services like Spotify can display lyrics.

## Tech Stack

- <a href="https://kotlinlang.org/">Kotlin</a>
- <a href="https://kotlinlang.org/docs/coroutines-overview.html">Kotlin Coroutines</a>
- <a href="https://kotlinlang.org/docs/flow.html">Kotlin Flow<a>
- <a href="https://developer.android.com/jetpack/compose?hl=ja">Jetpack Compose</a>
- <a href="https://m3.material.io/">Material3</a>

## Feature
#### Ready!!

- Music playback function
  - Playback using Media3
  - Reading / Writing from MediaStore
  - MediaStyle Notification
  - Equalizer
  - Editing / Display of synchronized lyrics
  - Playlist creation
- Music information retrieval
  - <a href="last.fm">Last.fm API</a>
  - <a href="https://www.musixmatch.com">Musixmatch API</a>
  - <a href="https://www.spotify.com">Spotify API</a>

#### Not Ready...

- Fast forward / Rewind music (in seconds)
- Equalizer preset function
- Creation of synchronized lyrics (like WALKMAN)
- Reading / Writing playlist from MediaStore
- Music recommendation function
- Artist recommendation function
- Overall billing system
- Overall advertising system

## Architecture
This shows the architecture diagram of the app. It has become quite complex, so some modules and dependencies are omitted to give an overview.
```mermaid
%%{
init: {
'theme': 'neutral'
}
}%%

graph LR
  subgraph gradle 
    build-logic  
  end  
  subgraph application
    app  
  end  
  subgraph core
    common
    datastore
    design
    model
    music
    repository
    ui
  end
  subgraph feature
    album
    artist
    home
    playlist
    song
  end
  app --> album
  app --> artist
  app --> playlist
  app --> song
  app --> home
  home --> ui
  home --> music
  playlist --> ui
  playlist --> music
  song --> ui
  song --> music
  album --> ui
  album --> music
  artist --> ui
  artist --> music
  album --> ui
  album --> music
  ui --> design
  ui --> repository
  music --> design
  music --> repository
  repository --> datastore
  datastore --> model
  design --> model
  model --> common
```

## Contribute

This app uses Gradle's Convention Plugins to standardize the build logic, and all logic is described in a module called `build-logic`. For more information about this approach, please visit [nowinandroid](https://github.com/matsumo0922/nowinandroid/tree/main/build-logic).

If you find any bugs or want to improve a feature, or want to develop a new feature, please write an issue first. Then assign yourself and get to work on development. Pull requests are always welcome :smile:

When using the <a href="last.fm">Last.fm API</a> or <a href="https://www.musixmatch.com">Musixmatch API</a>, please add the API key to `local.properties`. By default, it is an empty string. For details, please read `app/build.gradle.kts`.
