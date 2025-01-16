package sem3tp.Mover;

public enum Variants {
    OneJump,
    TwoJumps;

    static {
        OneJump.possibleJumps=1;
        TwoJumps.possibleJumps=2;
    }

    public int possibleJumps;

    public int getPossibleJumps(){
        return possibleJumps;
    }

}
