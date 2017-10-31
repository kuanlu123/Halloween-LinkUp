package code.model;

import java.awt.Point;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;

public class Board {
	private ArrayList<ArrayList<String>> _board;
	private ArrayList<String> _colorFileNames;
	private ArrayList<String> _row;
	private Random _rand;
	private int _score = 0;

	private static int MAX_COLORS = 6; // max possible is 6

	public Board(int rows, int cols) {
		_board = new ArrayList<ArrayList<String>>();
		_rand = new Random();
		_colorFileNames = new ArrayList<String>();
		for (int i = 0; i < MAX_COLORS; i = i + 1) {
			_colorFileNames.add("Images/Tile-" + i + ".png");
		}
		for (int r = 0; r < rows; r = r + 1) {
			_row = new ArrayList<String>();
			for (int c = 0; c < cols; c = c + 1) {
				_row.add(_colorFileNames.get(_rand.nextInt(_colorFileNames.size())));
			}
			_board.add(_row);
		}

		while (match().size() > 0 || legalMove(rows, cols) == false) {
			_row.removeAll(_row);
			_board.clear();
			for (int r = 0; r < rows; r = r + 1) {
				_row = new ArrayList<String>();
				for (int c = 0; c < cols; c = c + 1) {
					_row.add(_colorFileNames.get(_rand.nextInt(_colorFileNames.size())));
				}
				_board.add(_row);
			}
		}
	}

	public boolean legalMove(int rows, int cols) {
		int flag = 0;
		for (int r = 0; r < rows - 1; r = r + 1) {
			for (int c = 0; c < cols; c = c + 1) {
				Point a = new Point(r, c);
				String temp = get(a);
				Point b = new Point(r + 1, c);
				set(a, get(b));
				set(b, temp);
				if (match().size() > 0) {
					flag = 1;
				}
				String temp1 = get(a);
				set(a, get(b));
				set(b, temp1);
			}
		}

		for (int r = 0; r < rows; r = r + 1) {
			for (int c = 0; c < cols - 1; c = c + 1) {
				Point a = new Point(r, c);
				String temp = get(a);
				Point b = new Point(r, c + 1);
				set(a, get(b));
				set(b, temp);
				if (match().size() > 0) {
					flag = 1;
				}
				String temp1 = get(a);
				set(a, get(b));
				set(b, temp1);
			}
		}
		if (flag == 1) {
			return true;
		}
		return false;
	}

	public HashSet<Point> hint(int rows, int cols) {
		HashSet<Point> hint1 = new HashSet<Point>();
		for (int r = 0; r < rows - 1; r = r + 1) {
			for (int c = 0; c < cols; c = c + 1) {
				Point a = new Point(r, c);
				String temp = get(a);
				Point b = new Point(r + 1, c);
				set(a, get(b));
				set(b, temp);
				if (match().size() > 0) {
					hint1.addAll(match());
				}
				String temp1 = get(a);
				set(a, get(b));
				set(b, temp1);
			}
		}

		HashSet<Point> hint2 = new HashSet<Point>();
		for (int r = 0; r < rows; r = r + 1) {
			for (int c = 0; c < cols - 1; c = c + 1) {
				Point a = new Point(r, c);
				String temp = get(a);
				Point b = new Point(r, c + 1);
				set(a, get(b));
				set(b, temp);
				if (match().size() > 0) {
					hint2.addAll(match());
				}
				String temp1 = get(a);
				set(a, get(b));
				set(b, temp1);
			}
		}
		Random r = new Random();
		if (r.nextInt(1) == 0) {
			return hint1;
		}
		return hint2;
	}

	public int rows() {
		return _board.size();
	}

	public int cols() {
		return _board.get(0).size();
	}

	public String get(Point p) {
		return _board.get(p.x).get(p.y);
	}

	private String set(Point p, String s) {
		return _board.get(p.x).set(p.y, s);
	}

	public void exchange(Point p, Point q) {
		String temp = get(p);
		set(p, get(q));
		set(q, temp);
		if (match().size() > 0) {
			eliminate();
			if (legalMove(5, 5) == false) {
				System.out.println("You Win !");
				System.out.println("Your Final Score is " + _score);
				System.exit(0);
			}
		} else {
			String temp1 = get(p);
			set(p, get(q));
			set(q, temp1);
		}
	}

	private HashSet<Point> match() {
		return match(3);
	}

	private HashSet<Point> match(int runLength) {
		HashSet<Point> matches = verticalMatch(runLength);
		matches.addAll(horizontalMatch(runLength));
		return matches;
	}

	private HashSet<Point> horizontalMatch(int runLength) {
		HashSet<Point> matches = new HashSet<Point>();
		int minCol = 0;
		int maxCol = cols() - runLength;
		for (int r = 0; r < rows(); r = r + 1) {
			for (int c = minCol; c <= maxCol; c = c + 1) { // The cols we can
															// START checking in
				HashSet<String> values = new HashSet<String>();
				HashSet<Point> points = new HashSet<Point>();
				for (int offset = 0; offset < runLength; offset = offset + 1) {
					Point p = new Point(r, c + offset);
					points.add(p);
					String s = get(p);
					values.add(s);
				}
				if (values.size() == 1) {
					matches.addAll(points);
				}
			}
		}
		return matches;
	}

	private HashSet<Point> verticalMatch(int runLength) {
		HashSet<Point> matches = new HashSet<Point>();
		int minRow = 0;
		int maxRow = rows() - runLength;
		for (int c = 0; c < cols(); c = c + 1) {
			for (int r = minRow; r <= maxRow; r = r + 1) { // The rows we can
															// START checking in
				HashSet<String> values = new HashSet<String>();
				HashSet<Point> points = new HashSet<Point>();
				for (int offset = 0; offset < runLength; offset = offset + 1) {
					Point p = new Point(r + offset, c);
					points.add(p);
					String s = get(p);
					values.add(s);
				}
				if (values.size() == 1) {
					matches.addAll(points);
				}
			}
		}
		return matches;
	}

	public void eliminate() {
		int matchPoints = 0;
		for (Point s : match()) {
			matchPoints = matchPoints + 1;
			for (int i = s.x; i > 0; --i) {
				Point p = new Point(i - 1, s.y);
				String temp = get(s);
				set(s, get(p));
				set(p, temp);
				s.setLocation(p);
			}
			_board.get(s.x).set(s.y, _colorFileNames.get(_rand.nextInt(_colorFileNames.size())));
		}
		_score = _score + 3 + (matchPoints - 3) * (matchPoints - 3);
	}

}
