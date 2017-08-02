package sample;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

/**
 * Created by smrut on 5/20/2017.
 */
public class NurikabeConfig implements Configuration{

    private String [][]  grid ;
    private int dimX;
    private int dimY;
    private String filename;
    private Coordinate lastCoor;
    private HashMap<Coordinate,List> islandCells;
    private List<Coordinate> seaCells;

    private int cursor;

    /**
     * Construct the initial configuration from an input file whose contents
     * are, for example:<br>
     * <tt><br>
     * 3 3          # rows columns<br>
     * 1 . #        # row 1, .=empty, 1-9=numbered island, #=island, &#64;=sea<br>
     * &#64; . 3    # row 2<br>
     * 1 . .        # row 3<br>
     * </tt><br>
     * @param filename the name of the file to read from
     * @throws FileNotFoundException if the file is not found
     *
     */
    public NurikabeConfig(String filename) throws FileNotFoundException {
        try (Scanner in = new Scanner(new File(filename))) {
            this.lastCoor=new Coordinate(0,0);
            this.seaCells=new ArrayList<Coordinate>();
            this.islandCells=new HashMap<>();
            List<Coordinate> islList=new ArrayList<>();
//            this.islandCells.put(new Coordinate(-1,-1),islList);

            String line=in.nextLine();
            String [] chars=line.split(" ");
            this.dimX=Integer.parseInt(chars[0]);
            this.dimY=Integer.parseInt(chars[1]);
            this.grid=new String[dimX][dimY];
            for(int c=0;c<dimX;c++){
                String line2=in.nextLine();
                chars=line2.split(" ");
                if(chars.length>0){
                    for(int j=0; j<dimY;j++){
                        grid[c][j]=chars[j];
                        if(chars[j].matches("[0-9]")) {
//                        if(chars[j].matches("1")){
//                            ArrayList<Coordinate> nebs=getNeighbors(new Coordinate(c, j));
//                            for (Coordinate coNeb:nebs
//                                 ) {
//                                seaCells.add(coNeb);
//                            }
//
//                        }


                            this.islandCells.put(new Coordinate(c, j), islList);
                        }
//                    System.out.println(grid[c][j]);
                    }

                }


            }




            in.close();
        }


    }
public int getDimX(){
        return dimX;
}
public int getDimY(){
    return dimY;
}
public String getCell(int i,int j){
    return grid[i][j];
}
public void setCell(int i,int j, String s){
    grid[i][j]=s;
}
public String [][] getGrid(){
    return grid;
}
    /**
     * The copy constructor takes a config, other, and makes a full "deep" copy
     * of its instance data.
     *
     * @param other the config to copy
     */
    protected NurikabeConfig(NurikabeConfig other) {



        this.dimX=other.dimX;
        this.dimY=other.dimY;
        this.grid=new String[other.dimX][other.dimY];
        for(int r=0 ; r<other.dimX;r++){
            for (int i = 0; i <other.dimY ; i++) {
//                System.arraycopy(other.grid[r][i],0,this.grid[r][i],0,other.grid.length);
                this.grid[r][i]=other.grid[r][i];
            }

        }
        this.lastCoor=new Coordinate(other.lastCoor.getRow(),other.lastCoor.getCol());
        this.islandCells=other.islandCells;
        this.seaCells=new ArrayList<>(other.seaCells);
//        System.out.println(this);

    }

    @Override
    public Collection<Configuration> getSuccessors() {

        LinkedList<Configuration> successor=new LinkedList<Configuration>();

        NurikabeConfig c1=new NurikabeConfig(this);
        NurikabeConfig c2=new NurikabeConfig(this);
        int count=0;


        for (int i = 0; i < c1.dimX; i++) {
            for (int k = 0; k <c1.dimY ; k++) {

                if (c1.grid[i][k].equals(".")) {
                    c1.grid[i][k] = "#";
                    c2.grid[i][k]="@";
                    c1.lastCoor=new Coordinate(i,k);
                    c2.lastCoor=new Coordinate(i,k);
                    c2.seaCells.add(c2.lastCoor);

                    successor.add(c1);
                    successor.add(c2);

                    return successor;

                }
            }
        }
        return successor;
//        System.out.println(" successor" +c1.dimX);


    }

    @Override
    public boolean isValid() {

        int row=lastCoor.getRow();
        int col=lastCoor.getCol();
        int islandSum=0;
        int toatlIsland=0;
        int totalHas=0;
        HashSet<Coordinate> visited=new HashSet<>();

        ArrayList<Coordinate> neighbors = new ArrayList<>();
        neighbors=getNeighbors(lastCoor);
        if(this.grid[row][col].equals("#") ) {
            if(chaeckOverlap(lastCoor)){
//                System.out.println("Overlap at "+ lastCoor.getRow() + lastCoor.getCol());
                return false;
            }
            int neigh=0;
            int sea=0;
//            System.out.println(neighbors);
////////////////////////////////////////////////////////////////////
            Set<Coordinate> isles1=this.islandCells.keySet();
            for (Coordinate cc:isles1
                    ) {
                ArrayList<Coordinate> c3Neighbors1=getNeighbors(cc);
                int visitedSize1=recurDFS(cc).size();
                int flag=0;
//                for (Coordinate c5:c3Neighbors1
//                        ) {
//                    if(this.grid[c5.getRow()][c5.getCol()].equals(".")){
//                        flag=1;
//                        break;
//                    }
//                }
                if(visitedSize1>Integer.parseInt(this.grid[cc.getRow()][cc.getCol()]) )return false;
            }
            ////////////////////
            for (Coordinate n:neighbors
                    ) {
                //if you find # then go to te parent number to check if the sum has exceeded

                if(this.grid[n.getRow()][n.getCol()].equals("#") || this.grid[n.getRow()][n.getCol()].matches("[0-9]")
                        ){//removed the check for .  || this.grid[n.getRow()][n.getCol()].equals(".")

                    neigh++;
                }
                else if(this.grid[n.getRow()][n.getCol()].equals(".")){
                    int remainingIsl=0;
                    for (int i = 0; i <dimX ; i++) {
                        for (int j = 0; j <dimY ; j++) {
                            if(this.grid[i][j].equals(".")){
                                remainingIsl++;
                            }
                        }

                    }


//
                    ArrayList<Coordinate> surrounding=getNeighbors(n);
                    HashSet<Coordinate> result;
                    for (Coordinate c:surrounding
                            ) { if(islandCells.keySet().contains(c)){
                        result= recurDFS(new Coordinate(c.getRow(), c.getCol()));
//                        System.out.println(result + " result.size" + c + Integer.parseInt(this.grid[c.getRow()][c.getCol()]) +" " +remainingIsl);
                        if(remainingIsl==result.size() &&  result.size()<=Integer.parseInt(this.grid[c.getRow()][c.getCol()]) ){
                            return true;
                        }
                        ////TODO
//////////////////////////////////////////////////////////////////////////////////////////////////////////
                        else if(result.size()<Integer.parseInt(this.grid[c.getRow()][c.getCol()])){
//                            System.out.println("checking result.size() " + result.size());
                            return true;
                        }
                        else{
                            return false;
                        }
////////////////////////////////////////////////////////////////////////////////////////////////////////
                    }
//
                    }
////                    recurDFS(new Coordinate(i, j));
                }
                else{
                    sea++;
                }


            }
            if(neigh==0 || sea==neighbors.size()) {return false;}
            for (int i = 0; i < dimX; i++) {
                for (int j = 0; j < dimY; j++) {

                    if (this.grid[i][j].matches("[1-9]")) {
                        //do dfs or bfs
//                        System.out.println("island cells" + i + j + this.islandCells.get(new Coordinate(i, j)));
                        visited = recurDFS(new Coordinate(i, j));
                        islandSum=visited.size();
                        totalHas++;
                        for (Coordinate v:visited
                                ) {
                            if(this.grid[v.getRow()][v.getCol()].matches("[0-9]") && (i!=v.getRow() && j!=v.getCol()) ){
                                return false;
                            }

                        }

                        if(Integer.parseInt(this.grid[i][j])!=1 && islandSum>Integer.parseInt(this.grid[i][j]) ){

                            return false;
                        }
                        if(Integer.parseInt(this.grid[i][j])==1 && islandSum>1){

                            return false;
                        }
                    }
                    if(this.grid[i][j].matches("#")){
                        toatlIsland++;
                    }
//                    if(this.grid[i][j].matches("@")){
//                        int validneigh=0;
//                        visited = recurDFS(new Coordinate(i, j));
//                        islandSum=visited.size();
//                        for (Coordinate v:visited
//                                ) {
//                            if(!this.grid[v.getRow()][v.getCol()].matches("@")  ){
//                                validneigh++;
//                            }
//
//                        }
//                        if(validneigh==0){
//                            return false;
//                        }
//                    }
                }

            }
//            System.out.println("Total islands: " + toatlIsland + " total hashes :" +totalHas);
//            if(toatlIsland<totalHas)return false;
            Set<Coordinate> cellsNumbered=this.islandCells.keySet();
///////////////////check if there are correct number of islands
            Set<Coordinate> isles=this.islandCells.keySet();
            for (Coordinate c3:isles
                    ) {
                ArrayList<Coordinate> c3Neighbors=getNeighbors(c3);
                int visitedSize=recurDFS(c3).size();
                int flag=0;
                for (Coordinate c4:c3Neighbors
                        ) {
                    if(this.grid[c4.getRow()][c4.getCol()].equals(".")){
                        flag=1;
                        break;
                    }
                }
                if(visitedSize!=Integer.parseInt(this.grid[c3.getRow()][c3.getCol()]) && flag==0)return false;
            }

            return true;
        }
        //check if island is valid

        //check if sea i valid
        else if(this.grid[row][col].equals("@")){
//            if(seaCells.contains(lastCoor))return true;
            int neigh=0;
            int poolCount=0;
            int seaCount=0;
            for (Coordinate n:neighbors
                    ) {

                if(this.grid[n.getRow()][n.getCol()].equals("@")
                        || this.grid[n.getRow()][n.getCol()].equals(".")){
                    neigh++;
                }


            }
            if(neigh==0) {return false;}

            if(row==dimX-1 & col==dimY-1){
                ArrayList<Coordinate> seaNeighbor=new ArrayList<>();

                for (int i = 0; i <= row; i++) {
                    for (int j = 0; j <=col ; j++) {
                        System.out.println(this.grid[i][j]);
                        seaNeighbor=getNeighbors(new Coordinate(i,j));

                        if(this.grid[i][j].equals("@")){
                            seaCount++;

                        }

                    }

                }
                int finSea=recurDFS(new Coordinate(row,col)).size();
//                System.out.println("final sea :" + finSea + " seacount :" + seaCount);
                if(seaCount!=finSea){
//                System.out.println(recurDFS(new Coordinate(row,col))+ " pool size");
//                    System.out.println("seacount:" + seaCount + " fianl sea count: "+ finSea);
                    return false;//has no neighboring sea cell
                }
            }



            if(col>0 && this.grid[row][col-1].equals("@")) {
                poolCount++;
            }
            if(row>0 && col>0 && this.grid[row-1][col-1].equals("@")){
                poolCount++;
            }
            if(row>0 && this.grid[row-1][col].equals("@")){
                poolCount++;
            }
            if(poolCount>=3){return false;}



            return true;

        }





        return false;
    }

    @Override
    public boolean isGoal() {

        for (int i = 0; i < dimX; i++) {
            for (int j = 0; j <dimY ; j++) {
                if(this.grid[i][j].equals(".")){
                    return false;
                }
            }

        }
//        System.out.println("checking goal");
        return true;
    }

    /**
     * Returns a string representation of the puzzle, e.g.: <br>
     * <tt><br>
     * 1 . #<br>
     * &#64; . 3<br>
     * 1 . .<br>
     * </tt><br>
     */
    @Override
    public String toString() {

        String line="";
        for(int i=0;i<dimX;i++){
            for(int j=0;j<dimY;j++){
                line+=grid[i][j] + " ";

            }
            line+="\n";
        }
        return line;
    }
    private ArrayList getNeighbors (Coordinate co){
        ArrayList islandList=new ArrayList();
        int c=co.getRow();
        int j=co.getCol();
//     System.out.println("c :" +c + " j: " + j);
        if (j > 0) {
//                 System.out.println("left");
            islandList.add(new Coordinate(c, j - 1));
        }
        if (c > 0 ) {
//                 System.out.println("top");
            islandList.add(new Coordinate(c - 1, j));

        }
        if (c >= 0 && j < dimY - 1) {
//                 System.out.println("right");
            islandList.add(new Coordinate(c, j + 1));
        }
        if (c < dimX - 1) {
//                 System.out.println("bottom");
            islandList.add(new Coordinate(c + 1, j));
//                 System.out.println(islandList);



        }


        return islandList;
    }

    private void visitDFS(Coordinate c, Set<Coordinate> visited) {
        ArrayList<Coordinate> neighbor=getNeighbors(c);
//        System.out.println(" last coor "+ this.grid[lastCoor.getRow()][lastCoor.getCol()]);

        for (Coordinate co1:neighbor) {
//            System.out.println("inside visitdfs  " + this.grid[co1.getRow()][co1.getCol()]);
            if(!visited.contains(co1) && this.grid[co1.getRow()][co1.getCol()].equals(this.grid[lastCoor.getRow()][lastCoor.getCol()])) {
                visited.add(co1);
                visitDFS(co1, visited);
            }
        }
    }

    private HashSet<Coordinate> recurDFS(Coordinate c){

        HashSet<Coordinate> visted=new HashSet<>();
        visted.add(c);

        visitDFS(c,visted);
//        System.out.println(visted.size()+ " visited size " +" row:col " +  c.getRow() + c.getCol());
        return (visted);
    }


    private void addToList(Coordinate c){
        List<Coordinate> neigh=this.islandCells.get(c);
        HashSet<Coordinate> visited=new HashSet<>();
        visited.add(c);
        ArrayList<Coordinate> getNeighborsList=getNeighbors(c);
        if(Integer.parseInt(this.grid[c.getRow()][c.getCol()])>1){
            for (Coordinate coor:getNeighborsList
                    ) {
                neigh.add(coor);

            }

        }
    }

    private boolean chaeckOverlap(Coordinate c){
        ArrayList<Coordinate> origin=getNeighbors(c);
        Set<Coordinate> islandKeys=this.islandCells.keySet();
        int neighborCount=0;
        for (Coordinate c2:origin
                ) {
            if(this.grid[c2.getRow()][c2.getCol()].matches("[0-9]")){
                neighborCount++;
            }

//           for (Coordinate key:islandKeys
//                ) {
//               if()
//           }
//               if(islandKeys.contains(coordinate)){
//                   neighborCount++;
//               }
//           }
            if(neighborCount>1){
                return true;
            }



        }
        return false;
    }
}
