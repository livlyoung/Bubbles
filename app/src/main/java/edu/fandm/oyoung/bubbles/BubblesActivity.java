package edu.fandm.oyoung.bubbles;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import java.util.Random;

public class BubblesActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Make the activity full screen
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(new BubblesView(this));
    }

    private class BubblesView extends View {
        private Paint paint;
        private int circleX, circleY, circleRadius;
        private int circleColor;
        private Random random;

        public BubblesView(Context context) {
            super(context);
            paint = new Paint();
            paint.setAntiAlias(true);
            random = new Random();
        }

        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);
            // Draw the circle
            paint.setColor(circleColor);
            canvas.drawCircle(circleX, circleY, circleRadius, paint);
        }

        @Override
        public boolean onTouchEvent(MotionEvent event) {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                // Remove the old circle
                circleRadius = 0;
                // Generate a new random position and color
                circleX = random.nextInt(canvas.getWidth());
                circleY = random.nextInt(canvas.getHeight());
                circleColor = Color.argb(255, random.nextInt(256), random.nextInt(256), random.nextInt(256));
                // Increase the circle radius gradually until it fills the screen
                final Point startPoint = new Point(circleX, circleY);
                final Point endPoint = new Point(canvas.getWidth()/2, canvas.getHeight()/2);
                final int maxRadius = Math.max(canvas.getWidth(), canvas.getHeight()) / 2;
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
