/* ===========================================================
* GTNA : Graph-Theoretic Network Analyzer
* ===========================================================
*
* (C) Copyright 2009-2011, by Benjamin Schiller (P2P, TU Darmstadt)
* and Contributors
*
* Project Info: http://www.p2p.tu-darmstadt.de/research/gtna/
*
* GTNA is free software: you can redistribute it and/or modify
* it under the terms of the GNU General Public License as published by
* the Free Software Foundation, either version 3 of the License, or
* (at your option) any later version.
*
* GTNA is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
* GNU General Public License for more details.
*
* You should have received a copy of the GNU General Public License
* along with this program. If not, see <http://www.gnu.org/licenses/>.
*
* ---------------------------------------
* GraphSPallFloyd.java
* ---------------------------------------
* (C) Copyright 2009-2011, by Benjamin Schiller (P2P, TU Darmstadt)
* and Contributors
*
* Original Author: truong;
* Contributors: -;
*
* Changes since 2011-05-17
* ---------------------------------------
*
*/
package gtna.graph.sorting.algorithms;

import gtna.graph.Edge;
import gtna.graph.Graph;

/**
* @author truong
*
*/
public class GraphSPAllFloyd extends GraphSPAll {
private boolean[][] p;
private int[][] d;
public final int INF = Integer.MAX_VALUE / 2;

/**
* @param g
*/
public GraphSPAllFloyd(Graph g) {
super(g);
int V = g.getNodes().length;
p = new boolean[V][V];
d = new int[V][V];

for (int s = 0; s < V; s++) {
for (int t = 0; t < V; t++) {
d[s][t] = INF;
}
}

for (int s = 0; s < V; s++) {
for (int t = 0; t < V; t++) {
if (g.getEdges().contains(s, t)) {
p[s][t] = true;
d[s][t] = 1;
}
}
}

for (int s = 0; s < V; s++) {
d[s][s] = 0;
}

for (int i = 0; i < V; i++) {
for (int s = 0; s < V; s++) {
if (p[s][i]) {
for (int t = 0; t < V; t++) {
if (s != t) {
if (d[s][t] > d[s][i] + d[i][t]) {
p[s][t] = p[s][i];
d[s][t] = d[s][i] + d[i][t];
}
}
}
}
}
}

}

public GraphSPAllFloyd(Graph g, boolean[] removed) {
super(g);
int V = g.getNodes().length;
p = new boolean[V][V];
d = new int[V][V];

for (int s = 0; s < V; s++) {
for (int t = 0; t < V; t++) {
d[s][t] = INF;
}
}

for (int s = 0; s < V; s++) {
	if (removed[s]) continue;
for (int t = 0; t < V; t++) {
	if (removed[t]) continue;
if (g.getEdges().contains(s, t)) {
p[s][t] = true;
d[s][t] = 1;
}
}
}

for (int s = 0; s < V; s++) {
	if (removed[s]) continue;
d[s][s] = 0;
}

for (int i = 0; i < V; i++) {
	if (removed[i]) continue;
for (int s = 0; s < V; s++) {
	if (removed[s]) continue;
if (p[s][i]) {
for (int t = 0; t < V; t++) {
	if (removed[t]) continue;
if (s != t) {
if (d[s][t] > d[s][i] + d[i][t]) {
p[s][t] = p[s][i];
d[s][t] = d[s][i] + d[i][t];
}
}
}
}
}
}

}



/*
* (non-Javadoc)
*
* @see gtna.graph.sorting.algorithms.GraphSPall#dist(int, int)
*/
@Override
public int dist(int src, int dst) {
return d[src][dst];
}

}
