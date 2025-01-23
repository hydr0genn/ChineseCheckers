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

        Black.setVisualColor(Color.web("#252525", 1));
        White.setVisualColor(Color.web("#fdfaf9", 1));
        Yellow.setVisualColor(Color.web("#FFB932", 1));
        Green.setVisualColor(Color.web("#35DD2C", 1));
        Red.setVisualColor(Color.web("#FD3237", 1));
        Blue.setVisualColor(Color.web("#3860C4", 1));
        Grey.setVisualColor(Color.web("#808080", 1));

        Black.setBorderColor(Color.web("#000000", 1));
        White.setBorderColor(Color.web("#ffffff", 1));
        Yellow.setBorderColor(Color.web("#CE8800", 1));
        Green.setBorderColor(Color.web("#08A700", 1));
        Red.setBorderColor(Color.web("#CC0005", 1));
        Blue.setBorderColor(Color.web("#0D318A", 1));
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
