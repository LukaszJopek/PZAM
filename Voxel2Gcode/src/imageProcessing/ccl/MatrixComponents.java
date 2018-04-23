package imageProcessing.ccl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class MatrixComponents {

    public int[][] labeledMatrix;

    public MatrixComponents(int[][] matrix) {
        ArrayList<ArrayList<Integer>> linked = new ArrayList<ArrayList<Integer>>();
        int[][] labels = new int[matrix.length][matrix[0].length];

        int NextLabel = 0;

        //First pass
        for(int i=0; i<matrix.length; i++) {
            for( int j=0; j<matrix.length; j++) {
                labels[i][j] = 0;
            }
        }

        for(int i=0; i<matrix.length; i++) {
            for(int j=0; j<matrix[0].length; j++) {
                if(matrix[i][j] != 0) {

                    //Labels of neighbors
                    ArrayList<Integer> neighbors = new ArrayList<Integer>();
                    for(int ni=-1; ni<=1; ni++) {
                        for(int nj=-1; nj<=1; nj++) {
                            if(i+ni<0 || j+nj<0 || i+ni>labels.length-1 || j+nj>labels[0].length-1) {
                                continue;
                            }
                            else {
                                if(i+ni == 0 && i+nj == 0) continue;
                                if(labels[i+ni][j+nj] != 0) neighbors.add(labels[i+ni][j+nj]);
                            }
                        }
                    }

                    if(neighbors.size() == 0) {
                        ArrayList<Integer> tempArrayList = new ArrayList<Integer>();
                        tempArrayList.add(NextLabel);
                        linked.add(NextLabel, tempArrayList);
                        labels[i][j] = NextLabel;
                        NextLabel++;
                    }
                    else {

                        labels[i][j]=1000*1000;
                        for(int neighbor : neighbors) {
                            if(neighbor < labels[i][j]) labels[i][j] = neighbor;
                        }

                        for(int neighbor : neighbors) {
                            linked.set(neighbor,union(linked.get(neighbor),neighbors));
                        }
                    }
                }

            }
        }

        //Second pass
        for(int i=0; i<matrix.length; i++) {
            for(int j=0; j<matrix[0].length; j++) {
                ArrayList<Integer> EquivalentLabels = linked.get(labels[i][j]);
                labels[i][j]=1000*1000;
                for(int label : EquivalentLabels) {
                    if(label < labels[i][j]) labels[i][j]=label;
                }
            }
        }

        labeledMatrix = labels;
    }

    //union: http://stackoverflow.com/questions/5283047/intersection-and-union-of-arraylists-in-java
    public <T> ArrayList<T> union(ArrayList<T> list1, ArrayList<T> list2) {
        Set<T> set = new HashSet<T>();

        set.addAll(list1);
        set.addAll(list2);

        return new ArrayList<T>(set);
    }

    public int[][] getLabeledMatrix() {
        return labeledMatrix;
    }
    public int getNumberOfComponents() {
    	int mnumberOfComponents = 0;
        for(int i=0; i<labeledMatrix.length; i++) {
            for(int j=0; j<labeledMatrix[0].length; j++) {
            	mnumberOfComponents = Math.max(mnumberOfComponents, labeledMatrix[i][j]);
            	}
            }
        return mnumberOfComponents;
    }
    
    public void showCCL() {
        System.out.println("{");
        for(int i=0; i<labeledMatrix.length; i++) {
            System.out.print("{");
            for(int j=0; j<labeledMatrix[0].length; j++) {
                if(j != labeledMatrix[0].length-1)
                    System.out.print(labeledMatrix[i][j]+", ");
                else
                    System.out.print(labeledMatrix[i][j]);
            }
            if(i != labeledMatrix.length-1)
                System.out.println("},");
            else
                System.out.println("}");
        }
        System.out.print("}");
    }

    public static void main(String[] args) {
        int[][] matrix = {{0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 1,
                0, 0, 0, 0, 0, 0, 0}, {1, 1, 0, 1, 1, 0, 1, 1, 0, 1, 1, 1, 0, 1, 0,
                1, 1, 1, 1, 1, 1, 1, 1, 0, 1, 1, 1, 1, 1, 1}, {0, 1, 0, 0, 0, 0, 0,
                1, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                0}, {0, 1, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0,
                0, 1, 0, 0, 1, 0, 0, 1, 0}, {0, 1, 1, 0, 1, 1, 1, 1, 0, 1, 1, 1, 0,
                1, 1, 1, 1, 0, 1, 1, 0, 0, 1, 0, 0, 1, 1, 0, 1, 1}, {0, 0, 0, 0, 1,
                0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 1, 0, 0, 1, 0, 0, 0, 0, 0,
                0, 0}, {0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 1, 0, 0, 1, 0, 0, 1, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0, 0, 0}, {1, 1, 0, 0, 1, 0, 1, 1, 0, 1, 1, 0,
                0, 1, 0, 0, 1, 0, 1, 1, 0, 0, 1, 1, 1, 1, 0, 0, 1, 1}, {0, 1, 0, 0,
                1, 0, 0, 1, 0, 0, 0, 0, 0, 1, 0, 0, 1, 0, 0, 1, 0, 0, 1, 0, 0, 1,
                0, 0, 1, 0}, {0, 0, 0, 0, 1, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0,
                0, 1, 0, 0, 1, 0, 0, 1, 0, 0, 1, 0}, {1, 1, 1, 0, 1, 1, 0, 1, 1, 1,
                1, 1, 1, 1, 1, 1, 1, 0, 0, 1, 1, 0, 1, 0, 0, 1, 0, 0, 1, 1}, {0, 0,
                0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0,
                1, 0, 0, 0, 0}, {0, 1, 0, 0, 0, 0, 0, 1, 0, 0, 1, 0, 0, 0, 0, 0, 0,
                0, 0, 1, 0, 0, 1, 0, 0, 1, 0, 0, 0, 0}, {1, 1, 0, 0, 1, 1, 0, 1, 0,
                0, 1, 0, 1, 1, 0, 1, 1, 1, 0, 1, 0, 0, 1, 0, 0, 1, 1, 0, 1, 1}, {0,
                0, 0, 0, 1, 0, 0, 1, 0, 0, 1, 0, 0, 1, 0, 0, 0, 0, 0, 1, 0, 0, 1,
                0, 0, 0, 0, 0, 1, 0}, {0, 0, 0, 0, 1, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0,
                0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0}, {0, 1, 1, 1, 1, 0, 0,
                1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 0, 1, 1, 1, 1, 0, 0, 1, 1, 1, 1,
                0}, {0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0,
                0, 1, 0, 0, 1, 0, 0, 0, 0}, {0, 1, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0,
                1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 1, 0}, {0, 1, 1, 0, 1,
                1, 1, 1, 0, 1, 1, 0, 1, 1, 0, 0, 1, 1, 1, 1, 1, 0, 1, 1, 0, 1, 0, 1,
                1, 0}, {0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 1, 0, 0, 0,
                0, 0, 1, 0, 0, 1, 0, 0, 0, 0}, {0, 1, 0, 0, 0, 0, 0, 1, 0, 0, 1, 0,
                0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0}, {0, 1, 0, 1,
                1, 1, 0, 1, 1, 1, 1, 0, 1, 1, 0, 1, 1, 0, 1, 1, 1, 1, 1, 0, 0, 1,
                1, 1, 1, 1}, {0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0}, {0, 0, 0, 0, 1, 0, 0, 1, 0, 0,
                1, 0, 0, 1, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}, {0, 1,
                1, 0, 1, 1, 0, 1, 1, 0, 1, 1, 0, 1, 0, 1, 1, 0, 1, 1, 0, 0, 1, 1, 0,
                1, 1, 1, 1, 1}, {0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 1,
                0, 0, 0, 0, 0, 1, 0, 0, 1, 0, 0, 0, 0}, {0, 1, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 1, 0, 0, 1, 0, 0, 1, 0, 0, 1, 0, 0, 1, 0, 0, 1, 0}, {1,
                1, 0, 0, 1, 1, 1, 1, 0, 0, 1, 1, 0, 1, 1, 1, 1, 0, 1, 1, 0, 1, 1,
                0, 1, 1, 0, 1, 1, 0}, {0, 0, 0, 0, 1, 0, 0, 1, 0, 0, 1, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}};

        MatrixComponents matrixComponents = new MatrixComponents(matrix);
        int[][] newMatrix = matrixComponents.getLabeledMatrix();

        System.out.println("{");
        for(int i=0; i<newMatrix.length; i++) {
            System.out.print("{");
            for(int j=0; j<newMatrix[0].length; j++) {
                if(j != newMatrix[0].length-1)
                    System.out.print(newMatrix[i][j]+", ");
                else
                    System.out.print(newMatrix[i][j]);
            }
            if(i != newMatrix.length-1)
                System.out.println("},");
            else
                System.out.println("}");
        }
        System.out.print("}");
    }
}