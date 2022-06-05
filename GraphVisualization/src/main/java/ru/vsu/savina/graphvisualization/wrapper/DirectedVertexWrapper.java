package ru.vsu.savina.graphvisualization.wrapper;


import ru.vsu.savina.graphalgorithms.model.DirectedVertex;
import ru.vsu.savina.graphalgorithms.model.IVertex;

public class DirectedVertexWrapper implements IVertexWrapper {
   private DirectedVertex vertex;
   private double x;
   private double y;

   public DirectedVertexWrapper(DirectedVertex vertex, double x, double y) {
      this.vertex = vertex;
      this.x = x;
      this.y = y;
   }

   public DirectedVertexWrapper(DirectedVertex vertex) {
      this.vertex = vertex;
   }

   public DirectedVertexWrapper() {
      vertex = null;
   }

   @Override
   public IVertex getVertex() {
      return vertex;
   }

   @Override
   public void setVertex(IVertex vertex) {
      this.vertex = (DirectedVertex) vertex;
   }

   @Override
   public double getX() {
      return x;
   }

   @Override
   public void setX(double x) {
      this.x = x;
   }

   @Override
   public double getY() {
      return y;
   }

   @Override
   public void setY(double y) {
      this.y = y;
   }
}
