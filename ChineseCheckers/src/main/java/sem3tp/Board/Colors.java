package sem3tp.Board;

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
    }

    private Colors oppositeColor;
    private Directions directionCreate;

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
