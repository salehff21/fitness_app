# Fitness App

Android fitness tracker for workouts, meals, and water. Kotlin + ViewBinding + SQLite.

[![Android CI](https://github.com/salehff21/fitness_app/actions/workflows/android-ci.yml/badge.svg)](https://github.com/salehff21/fitness_app/actions/workflows/android-ci.yml)
![Platform](https://img.shields.io/badge/platform-Android-3DDC84)
![Kotlin](https://img.shields.io/badge/kotlin-1.9%2B-7F52FF)
![Min SDK](https://img.shields.io/badge/minSdk-26-informational)
![Target SDK](https://img.shields.io/badge/targetSdk-35-informational)
![License](https://img.shields.io/badge/license-TBD-lightgrey)

---

## Table of Contents
- [Overview](#overview)
- [Tech Stack](#tech-stack)
- [Features](#features)
- [Getting Started](#getting-started)
- [Build and Run](#build-and-run)
- [Configuration](#configuration)
- [Project Structure](#project-structure)
- [Permissions](#permissions)
- [Dependencies](#dependencies)
- [Contributing](#contributing)
- [Roadmap](#roadmap)
- [License](#license)
- [Third-Party Licenses](#third-party-licenses)

## Overview
Simple, offline-first fitness tracker. Log workouts, meals, and water intake with a clean UI. Data is stored locally using `androidx.sqlite`. Health Connect client is included for future sync.

## Tech Stack
- **Language:** Kotlin
- **Architecture:** Activities + Fragments, RecyclerView, ViewBinding
- **Storage:** `androidx.sqlite`
- **Build:** Gradle, Android Gradle Plugin
- **Tests:** JUnit, AndroidX Test, Espresso

## Features
- Log workouts with a built-in timer dialog.
- Track meals and daily water intake.
- View basic reports and history.
- Ready for Health Connect integration.

## Getting Started
**Requirements**
- Android Studio (latest stable)
- JDK 11

**Clone**
```bash
git clone https://github.com/salehff21/fitness_app.git
cd fitness_app
