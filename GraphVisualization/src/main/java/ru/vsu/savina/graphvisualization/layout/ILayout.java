package ru.vsu.savina.graphvisualization.layout;

import ru.vsu.savina.graphalgorithms.model.IGraph;
import ru.vsu.savina.graphvisualization.wrapper.IGraphWrapper;

public interface ILayout<T extends IGraph> {
    IGraphWrapper layout(T graph);
}
