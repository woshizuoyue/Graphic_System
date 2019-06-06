
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Random;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;
import javafx.stage.Stage;


public class GG0937 extends Application{


    GraphicPicturePane myPicture;
    RadioButton addVertex;
    RadioButton addEdge;
    RadioButton moveVertex;
    RadioButton shortestPath;
    RadioButton changeWeight;
    TextField text;


    public static void main(String []args){

        launch(args);
    }

    // GUI skeleton;
    @Override
    public void start(Stage stage){


        // TODO Auto-generated method stub
        stage.setTitle("Graph GUI");
        HBox hb = new HBox(2);
        VBox vb = new VBox(20);

        addVertex = new RadioButton(" Add Vertex ");
        addEdge = new RadioButton (" Add Edge ");
        moveVertex = new RadioButton(" Move Vertex ");
        shortestPath = new RadioButton(" Shortest Path ");
        changeWeight = new RadioButton(" Change Weight ");
        text = new TextField();
        Button helpButton = new Button(" Help Button ");
        Button addAllEdges = new Button(" Add All Edges ");
        Button randomWeight = new Button(" Random Weights ");
        Button minSpanTree = new Button(" Minmium Spanning Tree ");

        ToggleGroup tg = new ToggleGroup();

        //help button ; an introduction;
        helpButton.setOnAction( new EventHandler<ActionEvent>(){

            @Override
            public void handle(ActionEvent arg0) {
                // TODO Auto-generated method stub
                Stage s = new Stage();
                s.setTitle("Instruction");

                Button closeButton = new Button(" Close ");
                Pane pane = new Pane();
                pane.getChildren().add(closeButton);
                Scene helpscene = new Scene(pane);
                TextArea t = new TextArea();

                t.setText("The Instruction:"
                        +"1. add vertex button could add vertex in the right pane\n"
                        + "2. add edge button could add a edge between two adges\n"
                        + "3. move vertex button could drag a vertex to move to anywhere\n"
                        + "4. change weight could add weights in a edge\n "
                        + "(sometimes the first click have to click twice to get weight)\n"
                        + "5. add all edges could add edges between every two vetices\n"
                        + "6. random weights could assign random weights(between 1-20) to every edges\n"
                        + "7. minimum spanning tree could calculate");

                pane.getChildren().add(t);

                closeButton.setOnAction(new EventHandler<ActionEvent>(){

                    @Override
                    public void handle(ActionEvent arg0) {
                        // TODO Auto-generated method stub
                        s.close();
                    }

                });
                s.setScene(helpscene);
                s.show();
            }
        });

        // add all edge button;
        addAllEdges.setOnAction( new EventHandler<ActionEvent>(){

            @Override
            public void handle(ActionEvent event) {
                // TODO Auto-generated method stub
                myPicture.allEdges();
            }

        });

        // randmWeights button to create some randomWeights for Edges;
        randomWeight.setOnAction( new EventHandler<ActionEvent>(){

            @Override
            public void handle(ActionEvent event) {
                // TODO Auto-generated method stub
                myPicture.getRandomWeights();
            }

        });

        // minimum spanning tree;
        minSpanTree.setOnAction(new EventHandler<ActionEvent>(){

            @Override
            public void handle(ActionEvent event) {
                // TODO Auto-generated method stub
                myPicture.getMinSpanTree();
            }

        });

        myPicture = new GraphicPicturePane();

        addVertex.setToggleGroup(tg);
        addEdge.setToggleGroup(tg);
        moveVertex.setToggleGroup(tg);
        shortestPath.setToggleGroup(tg);
        changeWeight.setToggleGroup(tg);


        vb.getChildren().addAll(addVertex, addEdge, moveVertex, shortestPath, changeWeight, text,
                addAllEdges, randomWeight, minSpanTree, helpButton);
        hb.getChildren().addAll(vb,myPicture);

        Scene myScene = new Scene(hb);


        stage.setScene(myScene);
        stage.show();
    }

    // Graphic Picture Pane class, create a Pane to draw graph in this pane;

    class GraphicPicturePane extends Pane{

        double scenex=0;
        double sceney=0;
        final double diffx=155.0;
        final double diffy=0.0;
        int count=0;
        int edgeCount=0;

        Vertex v;
        Vertex draggedVertex;
        Edge edge;
        ArrayList<Vertex> vertexList, holdList;
        ArrayList<Edge> edgeList;
        LinkedList<Vertex> tempList;
        Map<Double , Vertex> vertexMap;

        public GraphicPicturePane(){

            vertexList = new ArrayList<>();
            holdList = new ArrayList<>();
            edgeList = new ArrayList<>();
            vertexMap = new HashMap<>();

            setPrefSize(500,500);

            radioAction();


        }

        //methods for radio button functions;
        public void radioAction(){

            setOnMouseClicked(new EventHandler<MouseEvent>(){

                @Override
                public void handle(MouseEvent e) {
                    // TODO Auto-generated method stub

                    // addVertex button;
                    if(addVertex.isSelected()){

                        count = count + 1;
                        //create a new vertex object;
                        v = new Vertex(e.getX(),e.getY(),5,Color.RED,count);

                        // add vertex object to arraylist that type is vertex;
                        vertexList.add(v);

                        vertexMap.put(v.getdiffer(), v);


                        v.setOnMousePressed( new EventHandler<MouseEvent>(){

                            @Override
                            public void handle(MouseEvent event) {
                                // TODO Auto-generated method stub
                                scenex = event.getSceneX();
                                sceney = event.getSceneY();
                                draggedVertex = (Vertex) event.getSource();
                                draggedVertex.toFront();
                            }

                        });
                        v.setCenterX(e.getX());
                        v.setCenterY(e.getY());
                        getChildren().add(v);

                        //test of vertexList;
                        //System.out.println(vertexList);
                    }

                    // addEdge button;
                    if(addEdge.isSelected()){

                        double x,y;
                        x = e.getX();
                        y = e.getY();
                        vertexList.get(getPoint(x,y)).setFill(Color.GREEN);


                        holdList.add(vertexList.get(getPoint(x,y)));

                        if(holdList.size()==2){

                            // if vertex was chosen duplicated;
                            //do not create edge object;
                            //else create a new edge object;

                            if(holdList.get(0).equals(holdList.get(1))){
                                colorBack();
                                holdList.clear();

                            }
                            else{

                                colorBack();
                                edgeCount = edgeCount+1;
                                edge = new Edge(holdList.get(0).getx(), holdList.get(0).gety(),
                                        holdList.get(1).getx(),holdList.get(1).gety(),edgeCount);
                                edge.startXProperty().bind(holdList.get(0).centerXProperty());
                                edge.startYProperty().bind(holdList.get(0).centerYProperty());
                                edge.endXProperty().bind(holdList.get(1).centerXProperty());
                                edge.endYProperty().bind(holdList.get(1).centerYProperty());
							/*
							edge.setOnMouseClicked( new EventHandler<MouseEvent>(){

								@Override
								public void handle(MouseEvent event) {
									// TODO Auto-generated method stub
									edge.setStroke(Color.GREEN);
									System.out.print("haha");
								}

							});
							*/

                                edge.setStrokeWidth(5.0);
                                edge.setStroke(Color.BLUE);

                                edgeList.add(edge);
                                getChildren().add(edge);

                                holdList.clear();
                            }
                        }
                    }// addEdge is end;


                    if(moveVertex.isSelected()){
                        double mousex ,mousey;
                        mousex = e.getX();
                        mousey = e.getY();

                        //draggedVertex is a tempVertex for dragging;

                        draggedVertex = vertexList.get(getPoint(mousex,mousey));

                        draggedVertex.setFill(Color.GREEN);
                        draggedVertex.setCursor(Cursor.HAND);


                        draggedVertex.setOnMouseDragged( new EventHandler<MouseEvent>(){

                            @Override
                            public void handle(MouseEvent event) {
                                // TODO Auto-generated method stub

                                double offsetx , offsety;
                                double newgetx, newgety;

                                offsetx = event.getSceneX()-scenex;
                                offsety = event.getSceneY()-sceney;


                                draggedVertex = (Vertex) event.getSource();

                                draggedVertex.setCenterX(draggedVertex.getCenterX()+offsetx);
                                draggedVertex.setCenterY(draggedVertex.getCenterY()+offsety);

                                scenex = event.getSceneX();
                                sceney = event.getSceneY();

                                newgetx = scenex-diffx;
                                newgety = sceney-diffy;

                                draggedVertex.setx(newgetx);
                                draggedVertex.sety(newgety);
                            }

                        });

                    }//end of moveEdge;


                    //change weights method for select a edge to set a weight;

                    if(changeWeight.isSelected()){


                        for(int i=0;i<edgeList.size();i++){

                            Edge tempEdge;
                            tempEdge = edgeList.get(i);
                            tempEdge.setOnMouseClicked(new EventHandler<MouseEvent>(){

                                @Override
                                public void handle(MouseEvent event) {
                                    // TODO Auto-generated method stub
                                    tempEdge.setWeight(Integer.parseInt(text.getText()));
                                    tempEdge.myText.setX((tempEdge.getStartX()+tempEdge.getEndX())/2);
                                    tempEdge.myText.setY((tempEdge.getStartY()+tempEdge.getEndY())/2);
                                    tempEdge.myText.xProperty().bind((tempEdge.startXProperty().add(tempEdge.endXProperty())).divide(2));
                                    tempEdge.myText.yProperty().bind((tempEdge.startYProperty().add(tempEdge.endYProperty())).divide(2));
                                    tempEdge.myText.setText(text.getText());
                                    tempEdge.myText.setFill(Color.RED);
                                    tempEdge.myText.setStyle("-fx-font-size:20px");

                                    if(!getChildren().contains(tempEdge.myText)){
                                        getChildren().add(tempEdge.myText);
                                    }
                                }
                            });
                        }
                        //end of for loop;
                    }
                    //end of changeweight;


                    // shortest path algorithm;
                    if(shortestPath.isSelected())
                    {
                        // continued;
                    }

                }
            });


        }

        //getPoint method for click Pane to get vertex;

        public int getPoint(double x, double y){

            double differ = Math.abs(x-y);
            double []temp = new double[100];

            for(int i=0;i<vertexList.size();i++){

                temp[i]=Math.abs(vertexList.get(i).getdiffer()-differ);
            }

            double minval = temp[0];
            int minindex = 0;

            for(int i=0;i<vertexList.size();i++){
                if(minval>temp[i]){
                    minval=temp[i];
                    minindex=i;
                }
            }

            return minindex;
        }

        // colorBack method for make the color back;
        public void colorBack(){
            for(int j=0; j<holdList.size(); j++){
                for(int i=0; i<vertexList.size();i++){
                    if(holdList.get(j).equals(vertexList.get(i)))
                        vertexList.get(i).setFill(Color.RED);
                }
            }
        }

        // allEdges method for clicking button when make all vertice of edges;

        public void allEdges(){
            for(int i=0;i<vertexList.size();i++){
                for(int j=i+1;j<vertexList.size();j++){
                    holdList.add(vertexList.get(i));
                    holdList.add(vertexList.get(j));
                    edge = new Edge(holdList.get(0).getx(),holdList.get(0).gety(),
                            holdList.get(1).getx(),holdList.get(1).gety(),10);

                    edge.startXProperty().bind(holdList.get(0).centerXProperty());
                    edge.startYProperty().bind(holdList.get(0).centerYProperty());
                    edge.endXProperty().bind(holdList.get(1).centerXProperty());
                    edge.endYProperty().bind(holdList.get(1).centerYProperty());

                    edge.setStrokeWidth(5.0);
                    edge.setStroke(Color.BLUE);
                    getChildren().add(edge);
                    edgeList.add(edge);
                    holdList.clear();
                }
            }
        }

        // getRandomWeight method for getting weights randomly;
        public void getRandomWeights()
        {
            //continued;
            Random p = new Random();
            Edge tempEdge;
            for(int i=0;i<edgeList.size();i++){
                tempEdge = edgeList.get(i);
                int randWeights = p.nextInt(20)+1;
                tempEdge.setWeight(randWeights);
                tempEdge.myText.setX((tempEdge.getStartX()+tempEdge.getEndX())/2);
                tempEdge.myText.setY((tempEdge.getStartY()+tempEdge.getEndY())/2);
                tempEdge.myText.xProperty().bind((tempEdge.startXProperty().add(tempEdge.endXProperty())).divide(2));
                tempEdge.myText.yProperty().bind((tempEdge.startYProperty().add(tempEdge.endYProperty())).divide(2));
                tempEdge.myText.setText(String.valueOf(randWeights));
                tempEdge.myText.setFill(Color.RED);
                tempEdge.myText.setStyle("-fx-font-size:20px");
                if(!getChildren().contains(tempEdge.myText)){
                    getChildren().add(tempEdge.myText);
                }
            }

        }


        //minSpanTree method;
        //Using Prim's Algorithm;

        public void getMinSpanTree(){

            ArrayList<Edge> tempEdgeList = new ArrayList<>();
            tempList = new LinkedList<>();
            Vertex hold;
            Vertex tempNear;
            Edge tempEdge;

            for(int i=0;i<vertexList.size();i++){
                vertexList.get(i).setclose(10000);
                vertexList.get(i).setPVertex(null);
                tempList.addLast(vertexList.get(i));
            }

            tempList.getLast().setclose(0);

            while(!tempList.isEmpty())
            {
                hold = minStore(tempList);

                tempList.remove(hold);

                if(hold.getPVertex()!=null){

                    tempEdge=getTempEdge(hold,hold.getPVertex(),hold.getNearEdge());
                    tempEdge.setStroke(Color.GREEN);
                }

                hold.getNearEdge();

                for(int i=0;i<hold.getNearVertex(hold).size();i++){
                    for(int j=0;j<tempList.size();j++){
                        tempNear = tempList.get(j);

                        if(hold.getNearVertex(hold).get(i).equals(tempNear)
                                &&getTempEdge(hold,tempNear,hold.getNearEdge()).getWeight()<tempNear.getclose())
                        {
                            tempNear.setPVertex(hold);

                            tempNear.setclose(getTempEdge(hold,tempNear,hold.getNearEdge()).getWeight());

                        }
                    }
                }
            }//end while loop;
            System.out.println(vertexMap.keySet());
            System.out.println(vertexMap.values());
            System.out.println(vertexMap.entrySet());

        }// end of minspantree;


        // minStore for minspantree;
        // to get the minValue for a vertex in vertexList;

        public Vertex minStore(LinkedList<Vertex> p){

            int min=p.getFirst().getclose();
            int minIndex=0;

            for(int i=0;i<p.size();i++){
                if(min>p.get(i).getclose()){
                    min = p.get(i).getclose();
                    minIndex = i;
                }
            }
            return p.get(minIndex);
        }

        // get edge every one single time;
        public Edge getTempEdge(Vertex hold, Vertex tempNear,ArrayList<Edge> inputList)
        {
            Edge p = null;

            for(int i=0;i<inputList.size();i++){

                if(inputList.get(i).startx==hold.getx()&&inputList.get(i).starty==hold.gety())
                {
                    if(inputList.get(i).endx==tempNear.getx()&&inputList.get(i).endy==tempNear.gety())
                    {
                        p = inputList.get(i);
                    }
                }
                if(inputList.get(i).endx==hold.getx()&&inputList.get(i).endy==hold.gety())
                {
                    if(inputList.get(i).startx==tempNear.getx()&&inputList.get(i).starty==tempNear.gety())
                    {
                        p = inputList.get(i);
                    }
                }
            }
            return p;
        }
        //continue method


    }// end of Graphic class;


    //Vertex class;
    class Vertex extends Circle{

        double x,y;
        double differ;
        int shortestSave;
        Vertex PVertex;
        int name;
        ArrayList<Edge> nearEdgeList;
        ArrayList<Vertex> nearVertexList;

        // constructor 1;
        public Vertex(double x,double y,double r,Paint p,int name){
            super(r,p);
            this.x =x;
            this.y =y;
            this.name = name;
        }
        // constructor 2;
        public Vertex(double x,double y){this.x = x; this.y = y;}

        // getters and setters;
        public void setx(double x){this.x = x;}
        public void sety(double y){this.y = y;}
        public double getx(){return x;}
        public double gety(){return y;}
        public double getdiffer(){	return Math.abs(x-y);}
        public int getName(){return name;}


        // for minSpaningTree;
        public void setclose(int save){
            this.shortestSave = save;
        }
        public int getclose(){
            return this.shortestSave;
        }
        public void setPVertex(Vertex v){
            PVertex = v;
        }
        public Vertex getPVertex(){
            return PVertex;
        }


        // getNearEdge method;
        public ArrayList<Edge> getNearEdge(){
            nearEdgeList = new ArrayList<>();
            for(int i=0;i<myPicture.edgeList.size();i++){
                if((myPicture.edgeList.get(i).startx==x&&myPicture.edgeList.get(i).starty==y)
                        ||(myPicture.edgeList.get(i).endx==x&&myPicture.edgeList.get(i).endy==y)){
                    nearEdgeList.add(myPicture.edgeList.get(i));
                }
            }
            return nearEdgeList;
        }


        // getNearVertex method;
        public ArrayList<Vertex> getNearVertex(Vertex temp){

            nearVertexList = new ArrayList<>();

            for(int i=0;i<nearEdgeList.size();i++){

                //  1. condition;
                // if one side of edge startx equals to the vertex of x
                //then get the end point of vertex is adjacent point;

                if((temp.getx()==nearEdgeList.get(i).startx&&temp.gety()==nearEdgeList.get(i).starty)
                        &&(temp.getx()!=nearEdgeList.get(i).endx&&temp.gety()!=nearEdgeList.get(i).endy))
                {
                    double tempx,tempy;
                    tempx = nearEdgeList.get(i).endx;
                    tempy = nearEdgeList.get(i).endy;

                    //test every vertices in the vertexList is match the one side of edge in the nearEdgeList or not;
                    // if matched, then get the vertex data in the vertexList, to add in the nearVertexList;


                    for(int j=0;j<myPicture.tempList.size();j++){
                        if(myPicture.tempList.get(j).getx()==tempx&&myPicture.tempList.get(j).gety()==tempy)
                        {
                            nearVertexList.add(myPicture.tempList.get(j));
                        }
                    }
                }

                // 2. condition;
                // if one side of edge endx equals to the vertex of x,
                // it means the startx and starty of vertex is an adjcent vertex of goal vertex;

                else{
                    double tempx,tempy;
                    tempx = nearEdgeList.get(i).startx;
                    tempy = nearEdgeList.get(i).starty;
                    for(int j=0;j<myPicture.vertexList.size();j++){
                        if(myPicture.vertexList.get(j).getx()==tempx&&myPicture.vertexList.get(j).gety()==tempy)
                        {
                            nearVertexList.add(myPicture.vertexList.get(j));
                        }
                    }
                }
            }
            return nearVertexList;
        }

        // boolean method for test two vertices are adjacent or not;
        public boolean isAdjacent(Vertex point){
            return nearVertexList.contains(point);
        }


        @Override
        public String toString(){
            return String.valueOf(x)+"@"+"@"+String.valueOf(y)+"MY Name: "+name;
        }
    }

    class Edge extends Line{
        double startx,starty,endx,endy;
        int weight;
        int name;
        Text myText;

        public Edge(double sx, double sy, double ex, double ey, int name){
            super(sx,sy,ex,ey);
            startx = sx;
            starty = sy;
            endx = ex;
            endy = ey;
            this.name=name;
            myText = new Text();
        }
        // weights method;
        public void setWeight(int w){
            weight = w;
        }
        public int getWeight(){
            return weight;
        }

        public String toString(){

            return "Edge: "+name+" ("+" vertex1: "+startx+" @ "+starty+"///"+" vertex2: "+endx+" @ "+endy+"$$"+weight+")";
        }
    }

}

