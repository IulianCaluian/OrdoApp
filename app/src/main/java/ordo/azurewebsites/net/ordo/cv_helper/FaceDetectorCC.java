package ordo.azurewebsites.net.ordo.cv_helper;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.util.Log;
import android.util.SparseArray;
import android.widget.Toast;

import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.face.Face;
import com.google.android.gms.vision.face.FaceDetector;

import ordo.azurewebsites.net.ordo.R;





/*
 * Copyright (C) 2017 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  	http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


public class FaceDetectorCC {


    private static final String LOG_TAG = FaceDetectorCC.class.getSimpleName();

    private static final float EMOJI_SCALE_FACTOR = .9f;
    private static final double SMILING_PROB_THRESHOLD = .15;
    private static final double EYE_OPEN_PROB_THRESHOLD = .5;

    /**
     * Method for detecting faces in a bitmap, and drawing emoji depending on the facial
     * expression.
     *
     * @param context The application context.
     * @param picture The picture in which to detect the faces.
     */
    public static Bitmap detectFacesandOverlayEmoji(Context context, Bitmap picture) {

        // Create the face detector, disable tracking and enable classifications
        FaceDetector detector = new FaceDetector.Builder(context)
                .setTrackingEnabled(false)
                .setClassificationType(FaceDetector.ALL_CLASSIFICATIONS)
                .build();

        // Build the frame
        Frame frame = new Frame.Builder().setBitmap(picture).build();

        // Detect the faces
        SparseArray<Face> faces = detector.detect(frame);


        // Initialize result bitmap to original picture
        Bitmap resultBitmap = picture;

        // If there are no faces detected, show a Toast message
        if (faces.size() == 0) {
            Toast.makeText(context, R.string.no_faces_message, Toast.LENGTH_SHORT).show();
        } else {

            // Iterate through the faces

            Face face = faces.valueAt(0);
            Emoji emoji = whichEmoji(face);

        }


        // Release the detector
        detector.release();

        return resultBitmap;
    }

    public static int detectFacesandOverlayEmojiState(Context context, Bitmap picture) {

        // Create the face detector, disable tracking and enable classifications
        FaceDetector detector = new FaceDetector.Builder(context)
                .setTrackingEnabled(false)
                .setClassificationType(FaceDetector.ALL_CLASSIFICATIONS)
                .build();

        // Build the frame
        Frame frame = new Frame.Builder().setBitmap(picture).build();

        // Detect the faces
        SparseArray<Face> faces = detector.detect(frame);


        // Initialize result bitmap to original picture
        Bitmap resultBitmap = picture;

        // If there are no faces detected, show a Toast message
        if (faces.size() == 0) {
            Toast.makeText(context, R.string.no_faces_message, Toast.LENGTH_SHORT).show();
            detector.release();
            return 0;
        } else {

            // Iterate through the faces

            Face face = faces.valueAt(0);
            Emoji emoji = whichEmoji(face);
            detector.release();
            if(emoji == Emoji.SMILE)
                return  1;
            else return 2;

        }

    }


    /**
     * Determines the closest emoji to the expression on the face, based on the
     * odds that the person is smiling and has each eye open.
     *
     * @param face The face for which you pick an emoji.
     */

    private static Emoji whichEmoji(Face face) {


        boolean smiling = face.getIsSmilingProbability() > SMILING_PROB_THRESHOLD;

        // Determine and log the appropriate emoji
        Emoji emoji;
        if(smiling) {
                emoji = Emoji.SMILE;

        } else {
                emoji = Emoji.FROWN;
        }

        // Log the chosen Emoji
        Log.d(LOG_TAG, "whichEmoji: " + emoji.name());
        return emoji;
    }

//    /**
//     * Combines the original picture with the emoji bitmaps
//     *
//     * @param backgroundBitmap The original picture
//     * @param emojiBitmap      The chosen emoji
//     * @param face             The detected face
//     * @return The final bitmap, including the emojis over the faces
//     */
//    private static Bitmap addBitmapToFace(Bitmap backgroundBitmap, Bitmap emojiBitmap, Face face) {
//
//        // Initialize the results bitmap to be a mutable copy of the original image
//        Bitmap resultBitmap = Bitmap.createBitmap(backgroundBitmap.getWidth(),
//                backgroundBitmap.getHeight(), backgroundBitmap.getConfig());
//
//        // Scale the emoji so it looks better on the face
//        float scaleFactor = EMOJI_SCALE_FACTOR;
//
//        // Determine the size of the emoji to match the width of the face and preserve aspect ratio
//        int newEmojiWidth = (int) (face.getWidth() * scaleFactor);
//        int newEmojiHeight = (int) (emojiBitmap.getHeight() *
//                newEmojiWidth / emojiBitmap.getWidth() * scaleFactor);
//
//
//        // Scale the emoji
//        emojiBitmap = Bitmap.createScaledBitmap(emojiBitmap, newEmojiWidth, newEmojiHeight, false);
//
//        // Determine the emoji position so it best lines up with the face
//        float emojiPositionX =
//                (face.getPosition().x + face.getWidth() / 2) - emojiBitmap.getWidth() / 2;
//        float emojiPositionY =
//                (face.getPosition().y + face.getHeight() / 2) - emojiBitmap.getHeight() / 3;
//
//        // Create the canvas and draw the bitmaps to it
//        Canvas canvas = new Canvas(resultBitmap);
//        canvas.drawBitmap(backgroundBitmap, 0, 0, null);
//        canvas.drawBitmap(emojiBitmap, emojiPositionX, emojiPositionY, null);
//
//        return resultBitmap;
//    }


    // Enum for all possible Emojis
    private enum Emoji {
        SMILE,
        FROWN
    }

}
