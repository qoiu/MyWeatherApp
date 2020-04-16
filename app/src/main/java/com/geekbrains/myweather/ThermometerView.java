package com.geekbrains.myweather;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;

public class ThermometerView extends View {

    private int hotColor= Color.RED;
    private int backColor=Color.GRAY;
    private RectF thermometerRectangle = new RectF();
    private Rect temperatureValueRectangle = new Rect();
    private Rect[] addRectangle =new Rect[6];
    private Paint thermometerPaint;
    private Paint temperaturePaint;
    private double temperature = 30;
    private int round = 5;
    private int circleRad=10;
    private int circleX=10;
    private int circleY=10;

    public ThermometerView(Context context) {
        super(context);
        init();
    }

    public ThermometerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initAttr(context, attrs);
        init();
    }

    public ThermometerView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttr(context, attrs);
        init();
    }

    public ThermometerView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initAttr(context, attrs);
        init();
    }

    private void initAttr(Context context, AttributeSet attrs){
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.ThermometerView,
                0,0);
        hotColor=typedArray.getColor(R.styleable.ThermometerView_hot_color,Color.RED);
        backColor=typedArray.getColor(R.styleable.ThermometerView_thermometer_color,Color.GRAY);
        temperature = typedArray.getInteger(R.styleable.ThermometerView_temperature_value, 30);
        typedArray.recycle();
    }

    private void init(){
        thermometerPaint = new Paint();
        thermometerPaint.setColor(backColor);
        thermometerPaint.setStyle(Paint.Style.FILL);
        temperaturePaint = new Paint();
        temperaturePaint.setColor(hotColor);
        temperaturePaint.setStyle(Paint.Style.FILL);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        int width = w - getPaddingLeft() - getPaddingRight();
        int height = h - getPaddingTop() - getPaddingBottom();
        int padding = (int) (0.3 * width);
        round=(width - padding *2)/2;
        int rateHeight=(int)(height -3.4* padding);
        int addHeight=rateHeight/12;
        circleRad=(int)(width *0.4);
        circleX= width /2;
        circleY= height - padding *2;
        thermometerRectangle.set(padding, padding,
                width - padding,
                height - padding *2);
        temperatureValueRectangle.set((int)(padding *1.2),
                (int)((padding *1.4)+(rateHeight-(rateHeight*((double) Math.abs(temperature) /(double)30)))),
                (int)(width - padding *1.2),
                height - 2 * padding);
        for(int i=0;i<addRectangle.length;i++){
            if(addRectangle[i]==null)addRectangle[i]=new Rect();
            double addWidth=(i%2==0)?(padding *0.1):(padding *0.45);
            addRectangle[i].set(
                    width -(int)(padding *0.8),
                    (int)(padding *1.4+2*addHeight*i),
                    width -(int)addWidth,
                    (int)(padding *1.4+2*addHeight*i)+addHeight/3
            );
        }
    }

    public void setTemperature(double temperature) {
        temperature=Math.max(temperature,-30);
        temperature=Math.min(temperature,30);
        this.temperature = temperature;
        invalidate();

    }

    private int getRed(){
        return (int)((temperature>10)?temperature*8.5:0);
    }

    private int getBlue(){
        int b=(int)((temperature<10)?Math.abs(temperature-20)*8.5:0);
        return Math.min(b, 255);
    }

    private int getGreen(){
        return (int)(255-Math.abs(temperature)*8.5);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        onSizeChanged(getWidth(),getHeight(),0,0);
        super.onDraw(canvas);
        temperaturePaint.setARGB(255,getRed(),getGreen(),getBlue());
        canvas.drawRoundRect(thermometerRectangle, round, round, thermometerPaint);
        for(Rect r:addRectangle){
            canvas.drawRect(r,thermometerPaint);
        }
        canvas.drawCircle(circleX,circleY,circleRad,thermometerPaint);
        canvas.drawCircle(circleX,circleY,(int)(circleRad*0.8), temperaturePaint);
        canvas.drawRect(temperatureValueRectangle, temperaturePaint);
    }
}
