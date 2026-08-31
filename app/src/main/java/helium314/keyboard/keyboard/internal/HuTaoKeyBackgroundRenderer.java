/*
 * SPDX-License-Identifier: GPL-3.0-only
 */

package helium314.keyboard.keyboard.internal;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.Drawable;

import androidx.annotation.NonNull;

import java.util.Objects;

import helium314.keyboard.keyboard.Key;
import helium314.keyboard.keyboard.internal.keyboard_parser.floris.KeyCode;
import helium314.keyboard.latin.R;
import helium314.keyboard.latin.common.Constants;

/** Draws the original Hu Tao key shapes without tinting or distorting their ornamentation. */
public final class HuTaoKeyBackgroundRenderer {
    private static final int NORMAL_HORIZONTAL_CAP = 20;
    private static final int SPACE_LEFT_CAP = 45;
    private static final int SPACE_RIGHT_CAP = 75;
    private static final float KEY_HORIZONTAL_OVERSCAN = 0.07f;
    private static final float KEY_VERTICAL_OVERSCAN = 0.03f;

    @NonNull
    private final Bitmap mNormalKey;
    @NonNull
    private final Bitmap mPressedKey;
    @NonNull
    private final Bitmap mNormalSpace;
    @NonNull
    private final Bitmap mPressedSpace;
    @NonNull
    private final Bitmap mNormalShift;
    @NonNull
    private final Bitmap mPressedShift;
    @NonNull
    private final Bitmap mNormalDelete;
    @NonNull
    private final Bitmap mPressedDelete;
    @NonNull
    private final Bitmap mNormalEnter;
    @NonNull
    private final Bitmap mPressedEnter;
    @NonNull
    private final Bitmap mNormalRoundFunction;
    @NonNull
    private final Bitmap mPressedRoundFunction;
    @NonNull
    private final Bitmap mNormalDiamondFunction;
    @NonNull
    private final Bitmap mPressedDiamondFunction;
    @NonNull
    private final Drawable mBackspaceIcon;
    @NonNull
    private final Drawable mReturnArrowIcon;
    @NonNull
    private final Drawable mSpaceGlyph;
    @NonNull
    private final Drawable mSpaceGlobeIcon;
    @NonNull
    private final Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG);
    @NonNull
    private final Paint mIconPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    @NonNull
    private final Rect mSource = new Rect();
    @NonNull
    private final RectF mDestination = new RectF();

    public HuTaoKeyBackgroundRenderer(@NonNull final Context context) {
        this(context, false);
    }

    public HuTaoKeyBackgroundRenderer(@NonNull final Context context,
            final boolean opaqueRegularKeys) {
        final Bitmap normalKey = BitmapFactory.decodeResource(
                context.getResources(), R.drawable.hu_tao_key_normal);
        final Bitmap pressedKey = BitmapFactory.decodeResource(
                context.getResources(), R.drawable.hu_tao_key_pressed);
        mNormalKey = opaqueRegularKeys ? makeSolidPixelsOpaque(normalKey) : normalKey;
        mPressedKey = opaqueRegularKeys ? makeSolidPixelsOpaque(pressedKey) : pressedKey;
        mNormalSpace = BitmapFactory.decodeResource(context.getResources(), R.drawable.hu_tao_space_normal);
        mPressedSpace = BitmapFactory.decodeResource(context.getResources(), R.drawable.hu_tao_space_pressed);
        mNormalShift = BitmapFactory.decodeResource(context.getResources(), R.drawable.hu_tao_shift_normal);
        mPressedShift = BitmapFactory.decodeResource(context.getResources(), R.drawable.hu_tao_shift_pressed);
        mNormalDelete = BitmapFactory.decodeResource(context.getResources(), R.drawable.hu_tao_delete_normal);
        mPressedDelete = BitmapFactory.decodeResource(context.getResources(), R.drawable.hu_tao_delete_pressed);
        mNormalEnter = BitmapFactory.decodeResource(context.getResources(), R.drawable.hu_tao_enter_normal);
        mPressedEnter = BitmapFactory.decodeResource(context.getResources(), R.drawable.hu_tao_enter_pressed);
        mNormalRoundFunction = BitmapFactory.decodeResource(context.getResources(),
                R.drawable.hu_tao_round_function_normal);
        mPressedRoundFunction = BitmapFactory.decodeResource(context.getResources(),
                R.drawable.hu_tao_round_function_pressed);
        mNormalDiamondFunction = BitmapFactory.decodeResource(context.getResources(),
                R.drawable.hu_tao_diamond_function_normal);
        mPressedDiamondFunction = BitmapFactory.decodeResource(context.getResources(),
                R.drawable.hu_tao_diamond_function_pressed);
        mBackspaceIcon = Objects.requireNonNull(
                context.getDrawable(R.drawable.hu_tao_backspace_filled));
        mReturnArrowIcon = Objects.requireNonNull(
                context.getDrawable(R.drawable.hu_tao_return_arrow));
        mSpaceGlyph = Objects.requireNonNull(
                context.getDrawable(R.drawable.hu_tao_space_glyph));
        mSpaceGlobeIcon = Objects.requireNonNull(
                context.getDrawable(R.drawable.sym_keyboard_language_switch_lxx));
    }

    /** Makes the key face fully opaque without flattening its translucent outer glow. */
    @NonNull
    private static Bitmap makeSolidPixelsOpaque(@NonNull final Bitmap source) {
        final Bitmap result = source.copy(Bitmap.Config.ARGB_8888, true);
        final int width = result.getWidth();
        final int height = result.getHeight();
        final int[] pixels = new int[width * height];
        result.getPixels(pixels, 0, width, 0, 0, width, height);
        for (int index = 0; index < pixels.length; index++) {
            if ((pixels[index] >>> 24) >= 0x80) {
                pixels[index] |= 0xFF000000;
            }
        }
        result.setPixels(pixels, 0, width, 0, 0, width, height);
        return result;
    }

    public void draw(@NonNull final Key key, @NonNull final Canvas canvas) {
        final int code = key.getCode();
        final boolean pressed = key.isPressedOrLocked();
        final boolean isSpace = code == Constants.CODE_SPACE;
        final float horizontalOverscan = isSpace
                ? 0f : key.getDrawWidth() * KEY_HORIZONTAL_OVERSCAN;
        final float verticalOverscan = key.getHeight() * KEY_VERTICAL_OVERSCAN;
        final int width = Math.round(key.getDrawWidth() + horizontalOverscan * 2f);
        final int height = Math.round(key.getHeight() + verticalOverscan * 2f);

        canvas.save();
        canvas.translate(-horizontalOverscan, -verticalOverscan);

        if (isSpace) {
            drawHorizontallyStretchable(pressed ? mPressedSpace : mNormalSpace,
                    canvas, width, height, SPACE_LEFT_CAP, SPACE_RIGHT_CAP);
        } else if (key.isShift()) {
            drawAspectFit(pressed ? mPressedShift : mNormalShift, canvas, width, height);
        } else if (code == KeyCode.DELETE) {
            drawAspectFit(pressed ? mPressedDelete : mNormalDelete, canvas, width, height);
        } else if (key.hasActionKeyBackground()) {
            drawAspectFit(pressed ? mPressedEnter : mNormalEnter, canvas, width, height);
        } else if (isRoundFunction(code)) {
            drawAspectFit(pressed ? mPressedRoundFunction : mNormalRoundFunction,
                    canvas, width, height);
        } else if (isDiamondFunction(code)) {
            drawAspectFit(pressed ? mPressedDiamondFunction : mNormalDiamondFunction,
                    canvas, width, height);
        } else {
            drawHorizontallyStretchable(pressed ? mPressedKey : mNormalKey,
                    canvas, width, height, NORMAL_HORIZONTAL_CAP);
        }
        canvas.restore();
    }

    /** Draws our custom visual, or reports that the source art already contains one. */
    public boolean drawTopVisual(@NonNull final Key key, @NonNull final Canvas canvas) {
        if (key.getCode() == Constants.CODE_SPACE) {
            drawSpaceTopVisual(key, canvas);
            return true;
        }
        if (key.getCode() == KeyCode.DELETE) {
            drawSquareGradientIcon(key, canvas, mBackspaceIcon, 0.52f);
            return true;
        }
        if (key.hasActionKeyBackground()) {
            final int width = Math.round(Math.min(key.getDrawWidth(), key.getHeight()) * 0.62f);
            final int height = Math.round(width * 66f / 80f);
            // The original 80x66 PNG has transparent padding on its right and bottom. Center its
            // visible 59x60 pixels rather than the full bitmap canvas.
            final int left = Math.round((key.getDrawWidth() - width) * 0.5f
                    + width * 10.5f / 80f);
            final int top = Math.round((key.getHeight() - height) * 0.5f
                    + height * 3f / 66f);
            drawOriginalIcon(canvas, mReturnArrowIcon, left, top, width, height);
            return true;
        }
        return key.isShift();
    }

    private void drawSpaceTopVisual(@NonNull final Key key, @NonNull final Canvas canvas) {
        final int keyWidth = key.getDrawWidth();
        final int keyHeight = key.getHeight();

        final int globeSize = Math.round(keyHeight * 0.25f);
        final int globeLeft = (keyWidth - globeSize) / 2;
        final int globeTop = Math.round(keyHeight * 0.20f);
        drawGradientIcon(canvas, mSpaceGlobeIcon,
                globeLeft, globeTop, globeSize, globeSize);

        final int glyphWidth = Math.round(keyWidth * 0.416f);
        final int glyphHeight = Math.round(
                glyphWidth * mSpaceGlyph.getIntrinsicHeight()
                        / (float) mSpaceGlyph.getIntrinsicWidth());
        final int glyphLeft = (keyWidth - glyphWidth) / 2;
        final int glyphTop = Math.round(keyHeight * 0.58f);
        drawOriginalIcon(canvas, mSpaceGlyph,
                glyphLeft, glyphTop, glyphWidth, glyphHeight);
    }

    private void drawSquareGradientIcon(@NonNull final Key key, @NonNull final Canvas canvas,
            @NonNull final Drawable icon, final float sizeRatio) {
        final int size = Math.round(Math.min(key.getDrawWidth(), key.getHeight()) * sizeRatio);
        final int left = (key.getDrawWidth() - size) / 2;
        final int top = (key.getHeight() - size) / 2;
        drawGradientIcon(canvas, icon, left, top, size, size);
    }

    /** Applies the official ivory-to-coral foreground treatment to any keyboard icon. */
    public void drawGradientIcon(@NonNull final Canvas canvas, @NonNull final Drawable icon,
            final int left, final int top, final int width, final int height) {
        final int right = left + width;
        final int bottom = top + height;
        final int layer = canvas.saveLayer(left, top, right, bottom, null);

        icon.setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_IN);
        icon.setBounds(left, top, right, bottom);
        icon.draw(canvas);

        mIconPaint.setShader(new LinearGradient(left, top, right, top,
                0xFFFFF4F3, 0xFFFFADA5, Shader.TileMode.CLAMP));
        mIconPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawRect(left, top, right, bottom, mIconPaint);
        mIconPaint.setXfermode(null);
        mIconPaint.setShader(null);
        canvas.restoreToCount(layer);
    }

    private static void drawOriginalIcon(@NonNull final Canvas canvas,
            @NonNull final Drawable icon, final int left, final int top,
            final int width, final int height) {
        icon.clearColorFilter();
        icon.setBounds(left, top, left + width, top + height);
        icon.draw(canvas);
    }

    private static boolean isRoundFunction(final int code) {
        return code == KeyCode.SYMBOL_ALPHA || code == KeyCode.SYMBOL
                || code == KeyCode.ALPHA || code == KeyCode.NUMPAD;
    }

    private static boolean isDiamondFunction(final int code) {
        return code == KeyCode.LANGUAGE_SWITCH || code == KeyCode.EMOJI
                || code < Constants.CODE_SPACE;
    }

    private void drawAspectFit(@NonNull final Bitmap bitmap, @NonNull final Canvas canvas,
            final int width, final int height) {
        final float scale = Math.min(
                width / (float) bitmap.getWidth(),
                height / (float) bitmap.getHeight());
        final float drawWidth = bitmap.getWidth() * scale;
        final float drawHeight = bitmap.getHeight() * scale;
        final float left = (width - drawWidth) * 0.5f;
        final float top = (height - drawHeight) * 0.5f;
        mDestination.set(left, top, left + drawWidth, top + drawHeight);
        canvas.drawBitmap(bitmap, null, mDestination, mPaint);
    }

    /**
     * Scales the frame vertically but stretches only its undecorated horizontal center. This is
     * effectively a three-patch and keeps the spacebar corners and gold trim in proportion.
     */
    private void drawHorizontallyStretchable(@NonNull final Bitmap bitmap,
            @NonNull final Canvas canvas, final int width, final int height, final int sourceCap) {
        drawHorizontallyStretchable(bitmap, canvas, width, height, sourceCap, sourceCap);
    }

    private void drawHorizontallyStretchable(@NonNull final Bitmap bitmap,
            @NonNull final Canvas canvas, final int width, final int height,
            final int sourceLeftCap, final int sourceRightCap) {
        final int sourceWidth = bitmap.getWidth();
        final int sourceHeight = bitmap.getHeight();
        final float verticalScale = height / (float) sourceHeight;
        float destinationLeftCap = sourceLeftCap * verticalScale;
        float destinationRightCap = sourceRightCap * verticalScale;
        final float capsWidth = destinationLeftCap + destinationRightCap;
        if (capsWidth > width) {
            final float capScale = width / capsWidth;
            destinationLeftCap *= capScale;
            destinationRightCap *= capScale;
        }

        drawSlice(bitmap, canvas, 0, sourceLeftCap,
                0f, destinationLeftCap, sourceHeight, height);
        drawSlice(bitmap, canvas, sourceLeftCap, sourceWidth - sourceRightCap,
                destinationLeftCap, width - destinationRightCap, sourceHeight, height);
        drawSlice(bitmap, canvas, sourceWidth - sourceRightCap, sourceWidth,
                width - destinationRightCap, width, sourceHeight, height);
    }

    private void drawSlice(@NonNull final Bitmap bitmap, @NonNull final Canvas canvas,
            final int sourceLeft, final int sourceRight,
            final float destinationLeft, final float destinationRight,
            final int sourceHeight, final int destinationHeight) {
        mSource.set(sourceLeft, 0, sourceRight, sourceHeight);
        mDestination.set(destinationLeft, 0, destinationRight, destinationHeight);
        canvas.drawBitmap(bitmap, mSource, mDestination, mPaint);
    }
}
