package ui.canvas;

import core.Connector;
import core.Point;
import core.Tool;
import core.ToolUtils;
import core.exception.CanNotBeCreatedException;
import core.exception.ConnectorException;
import entities.classes.BaseClass;
import entities.relations.Relation;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import ui.forms.FormInput;
import core.Shape;

public class CanvasListener implements MouseListener, MouseMotionListener {
  private static final int double_click = 2;
  private Canvas canvas;
  private Optional<Shape> currentShape = Optional.empty();
  private Tool currentRelation = Tool.RELATION;
  private int selection = 0;

  public CanvasListener(Canvas canvas) {
    this.canvas = canvas;
  }

  @Override
  public void mousePressed(MouseEvent e) {
    java.awt.Point point = e.getPoint();

    if (ToolUtils.isToolRelation(canvas.logicBoard.currentTool)) {
      execRelation(point);
      return;
    }

    Point pointPressed = new Point(point.x, point.y);
    currentShape = canvas.getShape(pointPressed);

    if (!currentShape.isPresent() && ToolUtils.isToolClass(canvas.logicBoard.currentTool)) {
      try {
        Shape newShape = createNewShape(pointPressed);
        canvas.logicBoard.shapes.add(newShape);
        canvas.addMemento();
        canvas.logicBoard.currentTool = Tool.ANY;
        canvas.repaint();
      } catch (CanNotBeCreatedException e1) {
        e1.printStackTrace();
      }
    }
  }

  private Shape createNewShape(Point pointPressed) throws CanNotBeCreatedException {
      String shapeName = FormInput.getNameFromInput();
      return (Shape) BaseClass.getNewBaseClass(canvas.logicBoard.currentTool, shapeName, pointPressed);
  }

    @Override
  public void mouseReleased(MouseEvent e) {
      boolean isToolRelation = canvas.logicBoard.currentTool == Tool.RELATION;
      boolean isToolInheritRelation = canvas.logicBoard.currentTool == Tool.INHERIT_RELATION;
      boolean isToolInterfaceRelation = canvas.logicBoard.currentTool == Tool.INTERFACE_RELATION;
      boolean isToolAggregationRelation = canvas.logicBoard.currentTool == Tool.AGGREGATION_RELATION;
      boolean isToolCompositionRelation = canvas.logicBoard.currentTool == Tool.COMPOSITION_RELATION;
      if (!isToolRelation && !isToolInheritRelation && !isToolInterfaceRelation
            && !isToolAggregationRelation && !isToolCompositionRelation) {
        if (currentShape.isPresent() && !isTargetClassSelected()) {
          canvas.addMemento();
        }
      currentShape = Optional.empty();
    }
  }

  @Override
  public void mouseDragged(MouseEvent e) {
    java.awt.Point point = e.getPoint();
    List<Shape> shapes = canvas.logicBoard.shapes;
    currentShape.ifPresent(currentShape ->
        canvas.logicBoard.shapes = shapes.stream().map(shape -> {
          if (shape.getId().equals(currentShape.getId())) {
            try {
              shape.addPoint(new Point(point.x, point.y));

            } catch (Exception ea){
              System.out.print(ea.getMessage());
            }
            return shape;
          }
          return shape;
        }).collect(Collectors.toList()));
    canvas.repaint();
  }

  @Override
  public void mouseClicked(MouseEvent e) {
    if (e.getClickCount() == double_click) {
      java.awt.Point point = e.getPoint();
      currentShape = canvas.getShape(new Point(point.x, point.y));
      currentShape.ifPresent(cs -> {
        canvas.logicBoard.shapes = getCollectShapes(cs);
        canvas.repaint();
      });
    }
  }

  public List<Shape> getCollectShapes(Shape cs) {
    return canvas.logicBoard.shapes.stream().map(s -> {
      if (s.getId().equals(cs.getId())) {
        return (Shape) ((BaseClass) s).setTitle(FormInput.getNameFromInput());
      }
      return s;
    }).collect(Collectors.toList());
  }

  private void execRelation(java.awt.Point point) {
    if (isOriginClassSelected()) {
        markOriginClassAsCurrentShape(point);
      return;
    }
    if (isTargetClassSelected()) {
        createConnectorToTargetClass(point);
    }
  }

    private void createConnectorToTargetClass(java.awt.Point point) {
        canvas.getShape(new Point(point.x, point.y)).ifPresent(shape -> {
          try {
            Connector connector;
            connector = new Connector(
                (BaseClass) currentShape.get(),
                (BaseClass) shape,
                Relation.getNewRelation(currentRelation));

            canvas.logicBoard.addConnector(connector);
            canvas.logicBoard.shapes.add((Shape) connector.getRelation());
          } catch (ConnectorException | CanNotBeCreatedException e1) {
            e1.printStackTrace();
          }

          selection = 0;
          canvas.logicBoard.currentTool = Tool.ANY;
          canvas.repaint();
        });
    }

    private void markOriginClassAsCurrentShape(java.awt.Point point) {
        currentRelation = canvas.logicBoard.currentTool;
        canvas.getShape(new Point(point.x, point.y)).ifPresent(shape -> {
          currentShape = Optional.of(shape);
          selection++;
        });
    }

    private boolean isTargetClassSelected() {
        return selection == 1;
    }

    private boolean isOriginClassSelected() {
        return selection == 0;
    }

    @Override
  public void mouseEntered(MouseEvent e) {}

  @Override
  public void mouseExited(MouseEvent e) {}

  @Override
  public void mouseMoved(MouseEvent e) {}
}
