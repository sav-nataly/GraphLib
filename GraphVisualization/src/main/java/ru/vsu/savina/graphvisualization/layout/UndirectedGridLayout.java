package ru.vsu.savina.graphvisualization.layout;

import ru.vsu.savina.graphalgorithms.model.UndirectedGraph;
import ru.vsu.savina.graphalgorithms.model.UndirectedVertex;
import ru.vsu.savina.graphvisualization.graphics.Rectangle;
import ru.vsu.savina.graphvisualization.wrapper.IGraphWrapper;
import ru.vsu.savina.graphvisualization.wrapper.UndirectedGraphWrapper;

import java.util.List;

public class UndirectedGridLayout implements ILayout<UndirectedGraph> {

    @Override
    public IGraphWrapper layout(UndirectedGraph graph) {
        UndirectedGraphWrapper wrapper = new UndirectedGraphWrapper(graph);
        List<List<UndirectedVertex>> components = GraphLayoutHelper.getConnectedComponents(graph);

        List<Rectangle> rectangles = GraphLayoutHelper.getRectangles(components.size());
        for (int i = 0; i < components.size(); i++) {
            List<List<Integer>> subgraphAdjList = GraphLayoutHelper.getComponentAdjList(graph, components.get(i));
            double[][] coordinates = GridLayout.gridLayout(subgraphAdjList, rectangles.get(i));

            GraphLayoutHelper.createGraphWrapper(wrapper, components.get(i), coordinates, subgraphAdjList);
        }
        return wrapper;
    }
}
