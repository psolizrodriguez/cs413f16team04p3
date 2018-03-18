package edu.luc.etl.cs313.android.shapes.android;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Rect;

import java.util.ArrayList;
import java.util.List;

import edu.luc.etl.cs313.android.shapes.model.*;

/**
 * A Visitor for drawing a shape to an Android canvas.
 */
public class Draw implements Visitor<Void> {

	// TODO entirely your job (except onCircle)

	private final Canvas canvas;

	private final Paint paint;

	public Draw(final Canvas canvas, final Paint paint) {
		this.canvas = canvas; // FIXME
		this.paint = paint; // FIXME
		paint.setStyle(Style.STROKE);
	}

	@Override
	public Void onCircle(final Circle c) {
		canvas.drawCircle(0, 0, c.getRadius(), paint);
		return null;
	}

	@Override
	public Void onStroke(final Stroke c) {
		int aux = c.getColor();
		paint.setColor(c.getColor());
        c.getShape().accept(this);
		paint.setColor(aux);
		return null;
	}

	@Override
	public Void onFill(final Fill f) {
		Style aux = paint.getStyle();
		paint.setStyle(Style.FILL_AND_STROKE);
        f.getShape().accept(this);
		paint.setStyle(aux);
		return null;
	}

	@Override
	public Void onGroup(final Group g) {
		if(g.getShapes()!= null && g.getShapes().size() > 0){
			for (Shape shape:g.getShapes()) {
				shape.accept(this);
			}
		}
		return null;
	}

	@Override
	public Void onLocation(final Location l) {
		canvas.translate(l.getX(),l.getY());
		l.getShape().accept(this);
		canvas.translate(-l.getX(),-l.getY());
		return null;
	}

	@Override
	public Void onRectangle(final Rectangle r) {
		canvas.drawRect( 0, 0, r.getWidth(), r.getHeight(), paint);
		return null;
	}

	@Override
	public Void onOutline(Outline o) {
		Style aux = paint.getStyle();
		paint.setStyle(Style.STROKE);
        o.getShape().accept(this);
		paint.setStyle(aux);
		return null;
	}

	@Override
	public Void onPolygon(final Polygon s) {
		if(s.getPoints() != null && s.getPoints().size() >0){
			final float[] pts = new float[(4*s.getPoints().size())];
			//first approach
			/*List<Integer> posList = new ArrayList();
			for (Point point:s.getPoints()) {
				posList.add(point.getX());
				posList.add(point.getY());
				if(posList.size() % 4 == 0){
					posList.add(point.getX());
					posList.add(point.getY());
				}
			}
			posList.add(s.getPoints().get(0).getX());
			posList.add(s.getPoints().get(0).getY());
			int counter = 0;
			for (Integer aux:posList) {
				pts[counter]= aux;
                counter++;
			}*/
			//second approach
			int i = pts.length-2;
			for (Point p: s.getPoints()){
				pts[i]= p.getX();
				i++;
				pts[i]=p.getY();
				i++;
				if(i == pts.length){
					i=0;
				}
				pts[i]= p.getX();
				i++;
				pts[i]=p.getY();
				i++;
			}
			canvas.drawLines(pts, paint);
		}

		return null;
	}
}
