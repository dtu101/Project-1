package animateHandlers;

import java.awt.Image;

public abstract class Animation {
	protected AnimationHandler animationHandler;
	
	protected double x;
	protected double y;
	protected double w;
	protected double h;
	
	public abstract void finished();

	public abstract Image getImage();
}
