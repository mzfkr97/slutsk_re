# Implementation Plan - Replace Manual Zoom with Zoomable Library

Replace the manual pinch-to-zoom logic in `ImageViewer.kt` with the `net.engawapg.lib:zoomable` library for better performance and standard behavior (like double-tap to zoom).

## Proposed Changes

### [features:common]

#### [MODIFY] [ImageViewer.kt](file:///Users/rzhurid/AndroidStudioProjects/SlutskRe/features/common/src/main/java/com/romanzhurid/common/viewer/ImageViewer.kt)
- Remove manual `scale` and `offset` states.
- Initialize `ZoomState` using `rememberZoomState()`.
- Apply `Modifier.zoomable(zoomState)` to the `AsyncImage`.
- Update `zoomState.setContentSize` in the `onSuccess` callback of `AsyncImage` to ensure correct panning boundaries.
- Remove the `graphicsLayer` modifier that was used for manual transformations.

## Verification Plan

### Manual Verification
- Deploy the app and open the image viewer.
- Verify pinch-to-zoom works smoothly.
- Verify double-tap toggles zoom.
- Verify panning works correctly when zoomed in.
- Verify the "Close" button still works.
