/*
 * Copyright (C) 2014 The Android Open Source Project
 * modified
 * SPDX-License-Identifier: Apache-2.0 AND GPL-3.0-only
 */

package helium314.keyboard.keyboard.internal;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import helium314.keyboard.keyboard.Key;
import helium314.keyboard.latin.common.CoordinateUtils;
import helium314.keyboard.latin.R;
import helium314.keyboard.latin.utils.ViewLayoutUtils;

import java.util.ArrayDeque;
import java.util.HashMap;

/**
 * This class controls pop up key previews. This class decides:
 * - what kind of key previews should be shown.
 * - where key previews should be placed.
 * - how key previews should be shown and dismissed.
 */
public final class KeyPreviewChoreographer {
    // Pixel geometry of res_hint_3.png. The outer canvas contains the baked gold halo while the
    // inner rectangle is the actual key face. Keep the bitmap's natural aspect ratio and size the
    // view from the face bounds so the halo doesn't make the preview key look undersized.
    private static final float HU_TAO_PREVIEW_BITMAP_WIDTH = 222.0f;
    private static final float HU_TAO_PREVIEW_BITMAP_HEIGHT = 275.0f;
    private static final float HU_TAO_PREVIEW_FACE_LEFT = 42.0f;
    private static final float HU_TAO_PREVIEW_FACE_TOP = 31.0f;
    private static final float HU_TAO_PREVIEW_FACE_RIGHT = 183.0f;
    private static final float HU_TAO_PREVIEW_FACE_BOTTOM = 228.0f;
    private static final float HU_TAO_VERTICAL_OVERSCAN = 0.03f;
    private static final float HU_TAO_PREVIEW_GAP_DP = 2.0f;
    private static final int HU_TAO_PREVIEW_TEXT_COLOR = 0xFFFDECD2;

    // Free {@link KeyPreviewView} pool that can be used for key preview.
    private final ArrayDeque<KeyPreviewView> mFreeKeyPreviewViews = new ArrayDeque<>();
    // Map from {@link Key} to {@link KeyPreviewView} that is currently being displayed as key
    // preview.
    private final HashMap<Key,KeyPreviewView> mShowingKeyPreviewViews = new HashMap<>();

    private final KeyPreviewDrawParams mParams;

    public KeyPreviewChoreographer(final KeyPreviewDrawParams params) {
        mParams = params;
    }

    public KeyPreviewView getKeyPreviewView(final Key key, final ViewGroup placerView) {
        KeyPreviewView keyPreviewView = mShowingKeyPreviewViews.remove(key);
        if (keyPreviewView != null) {
            return keyPreviewView;
        }
        keyPreviewView = mFreeKeyPreviewViews.poll();
        if (keyPreviewView != null) {
            return keyPreviewView;
        }
        final Context context = placerView.getContext();
        keyPreviewView = new KeyPreviewView(context, null /* attrs */);
        keyPreviewView.setBackgroundResource(R.drawable.hu_tao_key_preview);
        placerView.addView(keyPreviewView, ViewLayoutUtils.newLayoutParam(placerView, 0, 0));
        return keyPreviewView;
    }

    public boolean isShowingKeyPreview(final Key key) {
        return mShowingKeyPreviewViews.containsKey(key);
    }

    public void dismissKeyPreview(final Key key) {
        if (key == null) {
            return;
        }
        final KeyPreviewView keyPreviewView = mShowingKeyPreviewViews.get(key);
        if (keyPreviewView == null) {
            return;
        }
        // Dismiss preview
        mShowingKeyPreviewViews.remove(key);
        keyPreviewView.setTag(null);
        keyPreviewView.setVisibility(View.INVISIBLE);
        mFreeKeyPreviewViews.add(keyPreviewView);
    }

    public void placeAndShowKeyPreview(final Key key, final KeyboardIconsSet iconsSet,
            final KeyDrawParams drawParams, final int fullKeyboardViewWidth, final int[] keyboardOrigin,
            final ViewGroup placerView) {
        final KeyPreviewView keyPreviewView = getKeyPreviewView(key, placerView);
        placeKeyPreview(key, keyPreviewView, iconsSet, drawParams, fullKeyboardViewWidth, keyboardOrigin);
        showKeyPreview(key, keyPreviewView);
    }

    public void placeAndShowSinglePopupKeyPreview(final Key key, final PopupKeySpec popupKey,
            final KeyboardIconsSet iconsSet, final KeyDrawParams drawParams,
            final int fullKeyboardViewWidth, final int[] keyboardOrigin,
            final ViewGroup placerView) {
        final KeyPreviewView keyPreviewView = getKeyPreviewView(key, placerView);
        placeKeyPreview(key, keyPreviewView, iconsSet, drawParams, fullKeyboardViewWidth, keyboardOrigin);
        keyPreviewView.setPreviewVisual(popupKey, key, iconsSet, drawParams);
        showKeyPreview(key, keyPreviewView);
    }

    private void placeKeyPreview(Key key, KeyPreviewView keyPreviewView, KeyboardIconsSet iconsSet,
            KeyDrawParams drawParams, int fullKeyboardViewWidth, int[] originCoords) {
        keyPreviewView.setPreviewVisual(key, iconsSet, drawParams);
        keyPreviewView.setTextColor(HU_TAO_PREVIEW_TEXT_COLOR);
        int keyDrawWidth = key.getDrawWidth();
        float previewScale = key.getHeight() * (1.0f + HU_TAO_VERTICAL_OVERSCAN * 2.0f)
                / (HU_TAO_PREVIEW_FACE_BOTTOM - HU_TAO_PREVIEW_FACE_TOP);
        int previewWidth = Math.round(HU_TAO_PREVIEW_BITMAP_WIDTH * previewScale);
        int previewHeight = Math.round(HU_TAO_PREVIEW_BITMAP_HEIGHT * previewScale);
        int faceLeft = Math.round(HU_TAO_PREVIEW_FACE_LEFT * previewScale);
        int faceTop = Math.round(HU_TAO_PREVIEW_FACE_TOP * previewScale);
        int faceRight = Math.round(HU_TAO_PREVIEW_FACE_RIGHT * previewScale);
        int faceBottom = Math.round(HU_TAO_PREVIEW_FACE_BOTTOM * previewScale);
        // Treat the transparent halo margins like 9-patch content padding. This centers labels and
        // icons inside the framed key face and keeps popup-key geometry based on the face itself.
        keyPreviewView.setPadding(
                faceLeft, faceTop, previewWidth - faceRight, previewHeight - faceBottom);
        keyPreviewView.measure(
                View.MeasureSpec.makeMeasureSpec(previewWidth, View.MeasureSpec.EXACTLY),
                View.MeasureSpec.makeMeasureSpec(previewHeight, View.MeasureSpec.EXACTLY));
        mParams.setGeometry(keyPreviewView);
        int originX = CoordinateUtils.x(originCoords);
        // The key preview is horizontally aligned with the center of the visible part of the
        // parent key. If it doesn't fit in this {@link KeyboardView}, it is moved inward to fit and
        // the left/right background is used if such background is specified.
        int keyPreviewPosition;
        int previewX = key.getDrawX() + (keyDrawWidth - previewWidth) / 2 + originX;
        if (previewX + faceLeft < originX) {
            previewX = originX - faceLeft;
            keyPreviewPosition = KeyPreviewView.POSITION_LEFT;
        } else if (previewX + faceRight > fullKeyboardViewWidth + originX) {
            previewX = fullKeyboardViewWidth + originX - faceRight;
            keyPreviewPosition = KeyPreviewView.POSITION_RIGHT;
        } else {
            keyPreviewPosition = KeyPreviewView.POSITION_MIDDLE;
        }
        boolean hasPopupKeys = (key.getPopupKeys() != null);
        keyPreviewView.setPreviewBackground(hasPopupKeys, keyPreviewPosition);

        // Keep the pressed key visible, matching the original Hu Tao keyboard: the preview is a
        // separate key placed fully above it instead of a bubble that overlaps the pressed key.
        int previewGap = Math.round(HU_TAO_PREVIEW_GAP_DP
                * keyPreviewView.getResources().getDisplayMetrics().density);
        int previewY = key.getY() - faceBottom - previewGap
                + CoordinateUtils.y(originCoords);

        ViewLayoutUtils.placeViewAt(keyPreviewView, previewX, previewY, previewWidth, previewHeight);
        keyPreviewView.setPivotX(previewWidth / 2.0f);
        keyPreviewView.setPivotY(previewHeight);
    }

    void showKeyPreview(final Key key, final KeyPreviewView keyPreviewView) {
        keyPreviewView.setVisibility(View.VISIBLE);
        mShowingKeyPreviewViews.put(key, keyPreviewView);
    }

}
