package xExperimental.whitebaord.tools;

import xExperimental.whitebaord.properties.Properties;

import xExperimental.whitebaord.components.Canvas;

/**
 * An Eraser to erase your mistakes on a Canvas2.
 */
public class Eraser extends Pen {

    /**
     * Create an eraser object.
     */
    public Eraser(Canvas canvas) {
	super(canvas);
    }

    @Override
    protected void startLine(double x, double y) {
	// Let the Pen class deal with most of this.
	super.startLine(x, y);
	line.setStroke(Properties.getBackColor());
    }

    @Override
    protected void endLine(double x, double y) {
	line.setEndX(x);
	line.setEndY(y);
	getCanvas().addErase(line);
    }

}
