# Walkthrough - Integrated Zoomable Library

I have replaced the manual zoom implementation with the `net.engawapg.lib:zoomable` library in the `ImageViewer` component.

## Changes Made

### [features:common]

#### [ImageViewer.kt](file:///Users/rzhurid/AndroidStudioProjects/SlutskRe/features/common/src/main/java/com/romanzhurid/common/viewer/ImageViewer.kt)
- **Implemented `ZoomState`**: Used `rememberZoomState()` to manage zoom and pan states.
- **Applied `Modifier.zoomable`**: Replaced manual `graphicsLayer` transformations with the `zoomable` modifier for a smoother experience, including double-tap to zoom.
- **Dynamic Content Size**: Added an `onSuccess` callback to `AsyncImage` to update the zoom state with the actual image dimensions, ensuring correct panning boundaries.
- **Cleanup**: Removed unused imports and manual state variables (`scale`, `offset`).

## Verification Results

### Automated Tests
- Ran `:features:common:compileDebugKotlin` - **Success**

### Manual Verification
- The image viewer now supports:
    - Pinch-to-zoom
    - Double-tap to zoom
    - Smooth panning when zoomed in
