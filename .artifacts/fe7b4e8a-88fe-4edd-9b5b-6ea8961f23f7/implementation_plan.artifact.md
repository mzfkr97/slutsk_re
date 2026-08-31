# Implementation Plan - Fix Dagger MissingBinding for Context in CinemaComponent

The user is experiencing a Dagger compilation error in the `:features:cinema` module. The error indicates that `android.content.Context` cannot be provided. This happens because `CinemaViewModelFactory` depends on `NetworkStateProvider`, which requires `Context` in its constructor. Since `CinemaComponent` doesn't have `Context` in its graph and doesn't fetch `NetworkStateProvider` from its dependencies, it tries to instantiate `NetworkStateProvider` itself and fails.

## User Review Required

> [!IMPORTANT]
> This fix assumes that `NetworkStateProvider` should be provided by the parent component (`AppComponent`) which already has access to `Context`.

## Proposed Changes

### Cinema Feature

#### [MODIFY] [CinemaComponentDependencies.kt](file:///Users/rzhurid/AndroidStudioProjects/SlutskRe/features/cinema/src/main/java/com/romanzhurid/cinema/di/CinemaComponentDependencies.kt)

- Add `networkStateProvider: NetworkStateProvider` to the interface. This will allow `CinemaComponent` to resolve the `NetworkStateProvider` dependency by fetching it from the provided dependencies instead of trying to instantiate it locally.

## Verification Plan

### Automated Tests
- Run the Gradle task that was failing: `./gradlew :features:cinema:kspDebugKotlin`
- Perform a full build to ensure no other regressions: `./gradlew assembleDebug`

### Manual Verification
- None required as this is a build-time dependency injection issue.
