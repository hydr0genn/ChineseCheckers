package sem3tp.Board;

import javafx.scene.paint.Color;
import sem3tp.Mover.Directions;

public enum Colors {
    Black,
    White,
    Yellow,
    Green,
    Red,
    Blue,
    Grey;

    static {
        Black.setOppositeColor(White);
        White.setOppositeColor(Black);
        Yellow.setOppositeColor(Green);
        Green.setOppositeColor(Yellow);
        Red.setOppositeColor(Blue);
        Blue.setOppositeColor(Red);

        Red.setDirectionCreate(Directions.NorthWest);
        Black.setDirectionCreate(Directions.NorthEast);
        Yellow.setDirectionCreate(Directions.East);
        Blue.setDirectionCreate(Directions.SouthEast);
        White.setDirectionCreate(Directions.SouthWest);
        Green.setDirectionCreate(Directions.West);

        Black.setVisualColor(Color.web("#000000", 1));
        White.setVisualColor(Color.web("#ffffff", 1));
        Yellow.setVisualColor(Color.web("#FFFF00", 1));
        Green.setVisualColor(Color.web("#008000", 1));
        Red.setVisualColor(Color.web("#ff0000", 1));
        Blue.setVisualColor(Color.web("#00FFFF", 1));
        Grey.setVisualColor(Color.web("#808080", 1));

        Black.setBorderColor(Color.web("#FFFF00", 1));
        White.setBorderColor(Color.web("#ffffff", 1));
        Yellow.setBorderColor(Color.web("#FFFF00", 1));
        Green.setBorderColor(Color.web("#008000", 1));
        Red.setBorderColor(Color.web("#ff0000", 1));
        Blue.setBorderColor(Color.web("#00FFFF", 1));
        Grey.setBorderColor(Color.web("#808080", 1));
    }


    private Colors oppositeColor;
    private Directions directionCreate;
    private Color visualColor;
    private Color BorderColor;


    public void setVisualColor(Color visualColor){
        this.visualColor = visualColor;
    }

    public Color getVisualColor() {
        return visualColor;
    }

    public void setBorderColor(Color BorderColor){
        this.BorderColor = BorderColor;
    }

    public Color getBorderColor(){
        return BorderColor;
    }

    public void setDirectionCreate(Directions directionCreate) {
        this.directionCreate = directionCreate;
    }

    public Directions getDirectionCreate() {
        return directionCreate;
    }

    public void setOppositeColor(Colors oppositeColor) {
        this.oppositeColor = oppositeColor;
    }

    public Colors getOppositeColor() {
        return oppositeColor;
    }
}
