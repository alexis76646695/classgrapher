package ui.canvas;

import core.LogicBoard;
import core.Point;
import entities.classes.BaseClass;
import entities.relations.Relation;
import java.awt.*;
import java.util.Optional;
import javax.swing.*;
import ui.shapes.Shape;

public class Canvas extends JPanel {
  public LogicBoard logicBoard;


  public Canvas(LogicBoard logicBoard) {
    this.logicBoard = logicBoard;
    this.addListeners();
  }

  private void addListeners() {
    CanvasListener canvasListener = new CanvasListener(this);
    addMouseListener(canvasListener);
    addMouseMotionListener(canvasListener);
  }

  public void paint(Graphics graphics) {
    Dimension dimension = this.getSize();
    graphics.setColor(Color.white);
    graphics.fillRect(0, 0, dimension.width, dimension.height);
    graphics.setColor(Color.black);

    paintShapes(graphics);
    paintConectors(graphics);
  }

  public void clean() {
    this.logicBoard.clean();
    repaint();
  }

  public Optional<Shape> getShape(Point point) {
    return logicBoard.getShape(point);
  }

  public void undo() {
    if (logicBoard.shapes.size() > 0){
      logicBoard.undo();
      repaint();
    }
  }

  public void redo() {
    logicBoard.redo();
    repaint();
  }

  private void paintShapes(Graphics graphics){
    logicBoard.shapes.stream().filter(v -> Optional.ofNullable(v).isPresent()
        && !v.getClass().isInstance(Relation.class))
        .forEach(shape -> shape.draw(graphics));
  }

  private void paintConectors(Graphics graphics) {
    logicBoard.connectors.forEach(connector -> {
      BaseClass baseClassA = logicBoard.shapes.stream().filter(s -> s.getId().equals(((Shape) connector
          .getClassA())
          .getId())).map(s -> (BaseClass) s).findFirst().get();

      BaseClass baseClassB = logicBoard.shapes.stream().filter(s -> s.getId().equals(((Shape) connector
          .getClassB())
          .getId())).map(s -> (BaseClass) s).findFirst().get();

      int x1 = baseClassA.getPointOne().x
          + ((Math.abs(baseClassA.getPointTwo().x - baseClassA.getPointOne().x)) / 2);
      int y1 = baseClassA.getPointOne().y;

      int x2 = baseClassB.getPointOne().x
          + ((Math.abs(baseClassB.getPointTwo().x - baseClassB.getPointOne().x)) / 2);
      int y2 = baseClassB.getPointOne().y;

      ((Shape) connector.getRelation()).addPoints(new Point(x1, y1), new Point(x2, y2)).draw(graphics);
    });
  }
}
