/*
 * SPDX-License-Identifier: GPL-3.0-only
 */

package helium314.keyboard.keyboard.internal;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.SystemClock;
import android.text.TextUtils;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.Iterator;

import helium314.keyboard.keyboard.Key;
import helium314.keyboard.keyboard.internal.keyboard_parser.floris.KeyCode;
import helium314.keyboard.latin.R;
import helium314.keyboard.latin.common.Constants;

/** Draws the original OnePlus Ace Pro Hu Tao butterfly frames over pressed keys. */
public final class HuTaoButterflyAnimator {
    private static final long FRAME_DURATION_MILLIS = 24L;
    private static final int MAX_SIMULTANEOUS_EFFECTS = 8;
    private static final float EFFECT_HEIGHT_TO_KEY_HEIGHT = 1.20f;

    private static final int[] FRAME_RESOURCES = {
            R.drawable.hu_tao_butterfly_00,
            R.drawable.hu_tao_butterfly_01,
            R.drawable.hu_tao_butterfly_02,
            R.drawable.hu_tao_butterfly_03,
            R.drawable.hu_tao_butterfly_04,
            R.drawable.hu_tao_butterfly_05,
            R.drawable.hu_tao_butterfly_06,
            R.drawable.hu_tao_butterfly_07,
            R.drawable.hu_tao_butterfly_08,
            R.drawable.hu_tao_butterfly_09,
            R.drawable.hu_tao_butterfly_10,
            R.drawable.hu_tao_butterfly_11,
            R.drawable.hu_tao_butterfly_12,
            R.drawable.hu_tao_butterfly_13,
            R.drawable.hu_tao_butterfly_14,
            R.drawable.hu_tao_butterfly_15,
            R.drawable.hu_tao_butterfly_16,
            R.drawable.hu_tao_butterfly_17,
            R.drawable.hu_tao_butterfly_18,
            R.drawable.hu_tao_butterfly_19,
            R.drawable.hu_tao_butterfly_20,
            R.drawable.hu_tao_butterfly_21,
            R.drawable.hu_tao_butterfly_22,
            R.drawable.hu_tao_butterfly_23,
            R.drawable.hu_tao_butterfly_24,
            R.drawable.hu_tao_butterfly_25,
            R.drawable.hu_tao_butterfly_26,
            R.drawable.hu_tao_butterfly_27,
            R.drawable.hu_tao_butterfly_28,
            R.drawable.hu_tao_butterfly_29,
    };

    @NonNull
    private final Bitmap[] mFrames;
    @NonNull
    private final ArrayList<Effect> mEffects = new ArrayList<>();
    @NonNull
    private final Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG);
    @NonNull
    private final RectF mDestination = new RectF();

    public HuTaoButterflyAnimator(@NonNull final Context context) {
        mFrames = new Bitmap[FRAME_RESOURCES.length];
        for (int index = 0; index < FRAME_RESOURCES.length; index++) {
            mFrames[index] = BitmapFactory.decodeResource(context.getResources(), FRAME_RESOURCES[index]);
        }
    }

    public void start(@NonNull final Key key, final int paddingLeft, final int paddingTop) {
        if (!isVisibleCharacterKey(key)) {
            return;
        }
        if (mEffects.size() == MAX_SIMULTANEOUS_EFFECTS) {
            mEffects.remove(0);
        }
        final float centerX = paddingLeft + key.getDrawX() + key.getDrawWidth() * 0.5f;
        final float centerY = paddingTop + key.getY() + key.getHeight() * 0.5f;
        final float height = key.getHeight() * EFFECT_HEIGHT_TO_KEY_HEIGHT;
        final Bitmap firstFrame = mFrames[0];
        final float width = height * firstFrame.getWidth() / firstFrame.getHeight();
        mEffects.add(new Effect(centerX, centerY, width, height, SystemClock.uptimeMillis()));
    }

    private static boolean isVisibleCharacterKey(@NonNull final Key key) {
        if (key.getIconName() != null) {
            return false;
        }
        final int code = key.getCode();
        if (code == KeyCode.MULTIPLE_CODE_POINTS) {
            return !TextUtils.isEmpty(key.getOutputText());
        }
        if (code <= Constants.CODE_SPACE || !Character.isValidCodePoint(code)
                || Character.isWhitespace(code)) {
            return false;
        }
        final int type = Character.getType(code);
        return type != Character.CONTROL && type != Character.FORMAT;
    }

    /**
     * @return {@code true} while another animation frame needs to be drawn.
     */
    public boolean draw(@NonNull final Canvas canvas, final long now) {
        final Iterator<Effect> iterator = mEffects.iterator();
        while (iterator.hasNext()) {
            final Effect effect = iterator.next();
            final int frameIndex = (int) ((now - effect.startTime) / FRAME_DURATION_MILLIS);
            if (frameIndex >= mFrames.length) {
                iterator.remove();
                continue;
            }
            if (frameIndex < 0) {
                continue;
            }

            final float halfWidth = effect.width * 0.5f;
            final float halfHeight = effect.height * 0.5f;
            mDestination.set(
                    effect.centerX - halfWidth,
                    effect.centerY - halfHeight,
                    effect.centerX + halfWidth,
                    effect.centerY + halfHeight
            );
            canvas.drawBitmap(mFrames[frameIndex], null, mDestination, mPaint);
        }
        return !mEffects.isEmpty();
    }

    public void clear() {
        mEffects.clear();
    }

    private static final class Effect {
        private final float centerX;
        private final float centerY;
        private final float width;
        private final float height;
        private final long startTime;

        private Effect(final float centerX, final float centerY, final float width,
                final float height, final long startTime) {
            this.centerX = centerX;
            this.centerY = centerY;
            this.width = width;
            this.height = height;
            this.startTime = startTime;
        }
    }
}
