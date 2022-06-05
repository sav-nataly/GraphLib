package ru.vsu.savina.graphvisualization.application;

import org.json.JSONArray;
import org.json.JSONObject;
import ru.vsu.savina.graphalgorithms.model.*;
import ru.vsu.savina.graphvisualization.exception.FileReaderException;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class JSONGraphReader {

    public static UndirectedGraph readUndirectedGraph(File file) {
        String str = readFile(file);
        return createUndirectedGraph(str);
    }

    public static DirectedGraph readDirectedGraph(File file) {
        String str = readFile(file);
        return createDirectedGraph(str);
    }

    public static String readFile(File file) {
        if (!file.getName().substring(file.getName().lastIndexOf(".")).equals(".json")) {
            throw new FileReaderException("Wrong file extension");
        }
       try {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            StringBuilder graphString = new StringBuilder();
            String str;
            while ((str = reader.readLine()) != null) {
                graphString.append(str).append("\n");
            }
            reader.close();

            return graphString.toString();
        } catch (IOException ex) {
            throw new FileReaderException("Cannot read file " + file.getName());
        }
    }

    public static UndirectedGraph createUndirectedGraph(String jsonString) {
        UndirectedGraph graph = new UndirectedGraph();

        JSONObject obj = new JSONObject(jsonString).getJSONObject("graph");

        createVertices(obj.getJSONArray("vertices"), graph);
        createEdges(obj.getJSONArray("edges"), graph);

        return graph;
    }

    public static DirectedGraph createDirectedGraph(String jsonString) {
        DirectedGraph graph = new DirectedGraph();

        JSONObject obj = new JSONObject(jsonString).getJSONObject("graph");

        createVertices(obj.getJSONArray("vertices"), graph);
        createEdges(obj.getJSONArray("edges"), graph);

        return graph;
    }

    private static void createVertices(JSONArray array, UndirectedGraph graph) {
        for(int i = 0; i < array.length(); i++)
        {
            JSONObject obj = array.getJSONObject(i);
            if (!obj.keySet().contains("uid"))
                throw new FileReaderException("Wrong json structure");

            UndirectedVertex vertex = new UndirectedVertex(UUID.fromString(obj.getString("uid")));

            if (obj.keySet().contains("metadata")) {
                vertex.setMetadata(getMetadata(obj.getJSONObject("metadata")));
            }

            graph.addVertex(vertex);

        }
    }

    private static void createVertices(JSONArray array, DirectedGraph graph) {
        for(int i = 0; i < array.length(); i++)
        {
            JSONObject obj = array.getJSONObject(i);
            if (!obj.keySet().contains("uid"))
                throw new FileReaderException("Wrong json structure");

            DirectedVertex vertex = new DirectedVertex(UUID.fromString(obj.getString("uid")));

            if (obj.keySet().contains("metadata")) {
                vertex.setMetadata(getMetadata(obj.getJSONObject("metadata")));
            }

            graph.addVertex(vertex);

        }
    }

    private static Map<String, Object> getMetadata(JSONObject data) {
        Map<String, Object> metadata = new HashMap<>();

        for (String key : data.keySet())
            metadata.put(key, data.get(key));

        return metadata;
    }


    private static void createEdges(JSONArray array, UndirectedGraph graph) {
        for(int i = 0; i < array.length(); i++)
        {
            JSONObject obj = array.getJSONObject(i);
            UndirectedVertex source = (UndirectedVertex) graph.getVertex(UUID.fromString(obj.getString("source")));
            UndirectedVertex target = (UndirectedVertex) graph.getVertex(UUID.fromString(obj.getString("target")));


            if (source == null || target == null) {
                throw new FileReaderException("Wrong graph structure");
            }

            UndirectedEdge edge = new UndirectedEdge(new HashMap<>() {
                {
                    put(VertexType.SOURCE, source);
                    put(VertexType.TARGET, target);
                }
            });

            if (obj.keySet().contains("metadata")) {
                edge.setMetadata(getMetadata(obj.getJSONObject("metadata")));
            }
            graph.addEdge(edge);
        }
    }


    private static void createEdges(JSONArray array, DirectedGraph graph) {
        for(int i = 0; i < array.length(); i++)
        {
            JSONObject obj = array.getJSONObject(i);
            DirectedVertex source = (DirectedVertex) graph.getVertex(UUID.fromString(obj.getString("source")));
            DirectedVertex target = (DirectedVertex) graph.getVertex(UUID.fromString(obj.getString("target")));


            if (source == null || target == null) {
                throw new FileReaderException("Wrong graph structure");
            }

            DirectedEdge edge = new DirectedEdge(new HashMap<>() {
                {
                    put(VertexType.SOURCE, source);
                    put(VertexType.TARGET, target);
                }
            });

            if (obj.keySet().contains("metadata")) {
                edge.setMetadata(getMetadata(obj.getJSONObject("metadata")));
            }
            graph.addEdge(edge);
        }
    }
}
