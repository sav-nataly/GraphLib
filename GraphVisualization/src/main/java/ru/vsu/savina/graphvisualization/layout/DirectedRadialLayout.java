package ru.vsu.savina.graphvisualization.layout;

import ru.vsu.savina.graphalgorithms.model.DirectedGraph;
import ru.vsu.savina.graphalgorithms.model.DirectedVertex;
import ru.vsu.savina.graphvisualization.graphics.Rectangle;
import ru.vsu.savina.graphvisualization.wrapper.DirectedGraphWrapper;
import ru.vsu.savina.graphvisualization.wrapper.IGraphWrapper;

import java.util.List;

public class DirectedRadialLayout implements ILayout<DirectedGraph> {
    @Override
    public IGraphWrapper layout(DirectedGraph graph) {
        DirectedGraphWrapper wrapper = new DirectedGraphWrapper(graph);
        List<List<DirectedVertex>> components = GraphLayoutHelper.getConnectedComponents(graph);

        List<Rectangle> rectangles = GraphLayoutHelper.getRectangles(components.size());
        for (int i = 0; i < components.size(); i++) {
            List<List<Integer>> subgraphAdjList = GraphLayoutHelper.getComponentAdjList(graph, components.get(i));
            if (!GraphLayoutHelper.checkCyclic(subgraphAdjList)) {
                double[][] coordinates = RadialLayout.radialLayout(subgraphAdjList, rectangles.get(i));
                GraphLayoutHelper.createGraphWrapper(wrapper, components.get(i), coordinates, subgraphAdjList);
            }
        }
        return wrapper;
    }
}
