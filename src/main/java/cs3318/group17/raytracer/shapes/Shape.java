package cs3318.group17.raytracer.shapes;

import cs3318.group17.raytracer.Ray;
import cs3318.group17.raytracer.RayHit;
import cs3318.group17.raytracer.math.Point;
import cs3318.group17.raytracer.pigments.Finish;
import cs3318.group17.raytracer.pigments.Pigment;

import java.awt.Color;


public abstract class Shape {
	public Pigment pigment;
	public Finish finish;

	public Shape(Pigment pigment, Finish finish) {
		setPigment(pigment);
		setFinish(finish);
	}


	public final void setPigment(Pigment pigment) {
		this.pigment = pigment;
	}

	public final void setFinish(Finish finish) {
		this.finish = finish;
	}

	public abstract RayHit intersect(Ray ray);

	public boolean contains(Point p) { return false; }

	public final Color getColor(Point p) {
		return pigment.getColor(p);
	}
}
