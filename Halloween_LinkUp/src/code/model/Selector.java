package code.model;

import java.awt.Point;

public class Selector {

	private Point _selectedFirst;
	private Point _selectedSecond;
	private Board _board;

	public Selector(Board b) {
		_board = b;
		clearSelections();
	}

	public void select(Point p) {
		if (_selectedFirst == null) {
			_selectedFirst = p;
		} else {
			_selectedSecond = p;
			if (adjacent(_selectedFirst, _selectedSecond)) {
				_board.exchange(_selectedFirst, _selectedSecond);
			}
			clearSelections();
		}
	}

	public Point selectedFirst() {
		return _selectedFirst;
	}

	private boolean adjacent(Point p, Point q) {
		return Math.abs(p.x - q.x) + Math.abs(p.y - q.y) == 1;
	}

	private void clearSelections() {
		_selectedFirst = null;
		_selectedSecond = null;
	}

}
