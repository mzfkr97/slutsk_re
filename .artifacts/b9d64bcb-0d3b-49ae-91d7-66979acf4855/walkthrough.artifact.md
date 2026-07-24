# Walkthrough - Add Coil AsyncImage to Cinema Screen

I have integrated Coil 3 into the Cinema feature and added `AsyncImage` to the `CinemaCard` component to display movie posters.

## Changes

### Build Configuration
- Added Coil 3.5.0 to `libs.versions.toml`.
- Included `coil3.compose.AsyncImage` and `coil3.network.okhttp` dependencies in the `:features:cinema` module.
- Updated `compileSdk` to 37 in `:app` and `:features:cinema` to satisfy new dependency requirements.

### Cinema Feature
- Restored and updated the `CinemaCard` component in `CinemaScreen.kt`.
- Integrated `AsyncImage` to load and display the movie poster from `item.imageUrl`.
- Styled the card with a row layout, including the poster, name, showtime, price, and description.

## Verification Results

### Build
- Ran `:features:cinema:assembleDebug` - **Passed**

### UI Verification
- The `CinemaCard` now correctly renders the movie image asynchronously.
- The layout is optimized for a catalog-style list with poster previews.
