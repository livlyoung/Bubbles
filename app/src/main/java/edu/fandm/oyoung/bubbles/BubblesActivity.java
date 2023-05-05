package edu.fandm.oyoung.bubbles;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import java.util.Random;


/**
 * BubblesActivity class is responsible for creating a full-screen activity
 * with a custom view that displays an expanding circle when touched.
 */
public class BubblesActivity extends Activity {
    /**
     * onCreate is called when the activity is created. It makes the activity
     * full screen and sets the content view to a new instance of BubblesView.
     *
     * @param savedInstanceState the saved state of the activity.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Make the activity full screen
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(new BubblesView(this, null));
    }
    /**
     * BubblesView class is a custom view that displays
     * an expanding circle when touched.
     * Source: ChatGPT
     */
    public class BubblesView extends View {
        private Paint paint;
        private int circleX, circleY, circleRadius;
        private int circleColor;
        private Random random;

        /**
         * BubblesView constructor initializes the paint, random, and circle properties.
         *
         * @param context the context of the view.
         * @param attrs the attribute set of the view.
         */
        public BubblesView(Context context, AttributeSet attrs) {
            super(context, attrs);
            paint = new Paint();
            paint.setAntiAlias(true);
            random = new Random();
        }

        /**
         * onDraw is called when the view is drawn.
         * It draws the circle with the current color and radius.
         *
         * @param canvas the canvas to draw on.
         */
        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);
            // Draw the circle
            paint.setColor(circleColor);
            canvas.drawCircle(circleX, circleY, circleRadius, paint);
        }

        /**
         * onTouchEvent is called when the view is touched.
         * It generates a new circle with a random position and color,
         * and gradually increases its radius until it fills the screen.
         *
         * @param event the motion event that triggered the touch.
         * @return true if the touch event was handled, false otherwise.
         */
        @Override
        public boolean onTouchEvent(MotionEvent event) {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                // Remove the old circle
                circleRadius = 0;
                // Generate a new random position and color
                circleX = random.nextInt(getWidth());
                circleY = random.nextInt(getHeight());
                circleColor = Color.argb(255, random.nextInt(256), random.nextInt(256), random.nextInt(256));
                // Increase the circle radius gradually until it fills the screen
                final Point startPoint = new Point(circleX, circleY);
                final Point endPoint = new Point(getWidth()/2, getHeight()/2);
                final int maxRadius = Math.max(getWidth(), getHeight()) / 2;
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        while (circleRadius < maxRadius) {
                            circleRadius += 10;
                            final int currentRadius = circleRadius;
                            post(new Runnable() {
                                @Override
                                public void run() {
                                    invalidate(startPoint.x - currentRadius,
                                            startPoint.y - currentRadius,
                                            startPoint.x + currentRadius,
                                            startPoint.y + currentRadius);
                                }
                            });
                            try {
                                Thread.sleep(20);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }).start();
                return true;
            }
            return false;
        }
    }
}

