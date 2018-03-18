package edu.luc.etl.cs313.android.shapes.model;

/**
 * A shape visitor for calculating the bounding box, that is, the smallest
 * rectangle containing the shape. The resulting bounding box is returned as a
 * rectangle at a specific location.
 */
public class BoundingBox implements Visitor<Location> {

	// TODO entirely your job (except onCircle)

	@Override
	public Location onCircle(final Circle c) {
		final int radius = c.getRadius();
		return new Location(-radius, -radius, new Rectangle(2 * radius, 2 * radius));
	}

	@Override
	public Location onFill(final Fill f) {
		return f.getShape().accept(this);
	}

	@Override
	public Location onGroup(final Group g) {
        int min_x = 0;
        int min_y = 0;
        int max_x = 0;
        int max_y = 0;
        Location location;
        if(g.getShapes()!= null && g.getShapes().size() > 0){
            for (Shape shape:g.getShapes()) {
                location = shape.accept(this);
                if(min_x == 0 || location.getX() < min_x){
                    min_x = location.getX();
                }
                if(min_y == 0 || location.getY() < min_y){
                    min_y = location.getY();
                }
                int current_max_x = location.getX() + ((Rectangle)location.getShape()).getWidth();
                if(max_x == 0 || current_max_x > max_x){
                    max_x = current_max_x;
                }
                int current_max_y = location.getY() + ((Rectangle)location.getShape()).getHeight();
                if(max_y == 0 || current_max_y > max_y){
                    max_y = current_max_y;
                }
            }
        }
        return new Location(min_x, min_y, new Rectangle(max_x - min_x, max_y - min_y));
	}

	@Override
	public Location onLocation(final Location l) {
        Location aux = l.getShape().accept(this);
		return new Location(l.getX() + aux.getX(),l.getY() + aux.getY(),aux.getShape());
	}

	@Override
	public Location onRectangle(final Rectangle r) {
		return new Location(0, 0, r);
	}

	@Override
	public Location onStroke(final Stroke c) {
		return c.getShape().accept(this);
	}

	@Override
	public Location onOutline(final Outline o) {
		return o.getShape().accept(this);
	}

	@Override
	public Location onPolygon(final Polygon s) {
		int min_x = 0;
		int min_y = 0;
		int max_x = 0;
		int max_y = 0;
		if(s.getPoints() != null && s.getPoints().size() > 0){
			for (Point point:s.getPoints()) {
				if(min_x == 0 || point.getX() < min_x){
					min_x = point.getX();
				}
				if(min_y == 0 || point.getY() < min_y){
					min_y = point.getY();
				}
				if(max_x == 0 || point.getX() > max_x){
					max_x = point.getX();
				}
				if(max_y == 0 || point.getY() > max_y){
					max_y = point.getY();
				}
			}
		}
		return new Location(min_x, min_y, new Rectangle(max_x - min_x, max_y - min_y));
	}
}
