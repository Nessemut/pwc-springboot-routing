package cz.pwc.borders.service;

import cz.pwc.borders.exception.NoAvailablePathException;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class Graph<T> {

    private final Map<T, List<T>> map = new HashMap<>();

    public void addNode(T c) {
        map.put(c, new LinkedList<>());
    }

    public void addBorder(T origin, T destination) {
        map.get(origin).add(destination);
    }

    public List<T> getOptimalPath(T origin, T destination) throws NoAvailablePathException {
        Queue<T> queue = new LinkedList<>();
        Map<T, T> prev = new HashMap<>();
        Set<T> visited = new HashSet<>();

        queue.offer(origin);
        visited.add(origin);

        while (!queue.isEmpty()) {
            T current = queue.poll();

            if (current.equals(destination)) {
                break;
            }

            for (T neighbor: map.get(current)) {
                if (!visited.contains(neighbor)) {
                    queue.offer(neighbor);
                    visited.add(neighbor);
                    prev.put(neighbor, current);
                }
            }
        }

        List<T> path = new ArrayList<>();
        T node = destination;

        while (node != null) {
            path.add(0, node);
            node = prev.get(node);
        }
        
        if (path.size() < 2) {
            throw new NoAvailablePathException();
        }

        return path;
    }

}
