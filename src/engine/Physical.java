package engine;

import objects.*;

public class Physical {
	public static boolean isCollide(AnimateObject A, AnimateObject B) {
		int Ax = (int) A.getX();
		int Ay = (int) A.getY();
		int Aw = (int) A.getWidth();
		int Ah = (int) A.getHeight();
		int Bx = (int) B.getX();
		int By = (int) B.getY();
		int Bw = (int) B.getWidth();
		int Bh = (int) B.getHeight();
		boolean collideX = false;
		boolean collideY = false;
		if (Ax < Bx + Bw && Ax > Bx) {
			collideX = true;
		}
		if (Bx < Ax + Aw && Bx > Ax) {
			collideX = true;
		}
		if (Ay < By + Bh && Ay > By) {
			collideY = true;
		}
		if (By < Ay + Ah && By > Ay) {
			collideY = true;
		}
		return (collideX&&collideY);
	}
}
