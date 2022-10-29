package zeroxgame;

import java.awt.Point;

public enum VictorySolutions {
	//solutions when are tree elements aligned
	UpDiagonalRight(new Point[] {new Point(0,0), new Point(1,1), new Point(2,2)}),
	UpDiagonalLeft(new Point[] {new Point(0,2), new Point(1,1), new Point(2,0)}),
	HorizontalUp(new Point[] {new Point(0,0), new Point(1,0), new Point(2,0)}),
	HorizontalMid(new Point[] {new Point(0,1), new Point(1,1), new Point(2,1)}),
	HorizontalDown(new Point[] {new Point(0,2), new Point(1,2), new Point(2,2)}),
	VerticalRight(new Point[] {new Point(0,0), new Point(0,1), new Point(0,2)}),
	VerticalMid(new Point[] {new Point(1,0), new Point(1,1), new Point(1,2)}),
	VerticalLeft(new Point[] {new Point(2,0), new Point(2,1), new Point(2,2)});
	
	
	Point solutionCoords[];
	
	VictorySolutions(Point points[]) {
		solutionCoords = points;
	}
	
	public Point[] getSolution() {
		return solutionCoords;
	}
}
