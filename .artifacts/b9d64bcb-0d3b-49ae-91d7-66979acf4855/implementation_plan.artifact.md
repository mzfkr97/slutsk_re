# Add Coil AsyncImage to Cinema Screen

The goal is to integrate Coil to display cinema movie posters using `AsyncImage` in the `CinemaCard`.

## User Review Required

> [!IMPORTANT]
> I will be adding Coil 3 as a dependency. Coil 3 requires an explicit network fetcher (like OkHttp) to be included in the dependencies.

## Proposed Changes

### Build Configuration

#### [MODIFY] [libs.versions.toml](file:///Users/rzhurid/AndroidStudioProjects/SlutskRe/gradle/libs.versions.toml)
- Add `coil` version.
- Add `coil-compose` and `coil-network-okhttp` libraries.

#### [MODIFY] [build.gradle.kts (cinema)](file:///Users/rzhurid/AndroidStudioProjects/SlutskRe/features/cinema/build.gradle.kts)
- Add Coil dependencies.

### Cinema Feature

#### [MODIFY] [CinemaScreen.kt](file:///Users/rzhurid/AndroidStudioProjects/SlutskRe/features/cinema/src/main/java/com/romanzhurid/cinema/ui/CinemaScreen.kt)
- Restore the `CinemaCard` composable (which was accidentally removed in a previous edit).
- Integrate `AsyncImage` into `CinemaCard` to show the `imageUrl`.

## Verification Plan

### Automated Tests
- Run `:features:cinema:assembleDebug` to ensure successful compilation.

### Manual Verification
- Deploy the app and navigate to the Cinema screen.
- Verify that movie images are loading and displayed correctly in the cards.
