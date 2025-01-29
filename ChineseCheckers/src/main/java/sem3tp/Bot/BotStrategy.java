package sem3tp.Bot;

import sem3tp.Board.Colors;
import sem3tp.GUI.FXPole;
import sem3tp.Game;
import sem3tp.Poles.StandardPole;
import sem3tp.Poles.TrianglePole;
import sem3tp.Storage.FXPoleStorage;

public class BotStrategy {

    private static BotStrategy instance;

    private BotStrategy(){}

    public static BotStrategy getInstance() {
        if(instance==null){
            synchronized (BotStrategy.class) {
                if(instance==null){
                    instance= new BotStrategy();
                }
            }
        }
        return instance;
    }

    private double CalculateCumulativeDistance(double CumSum,FXPole BotPole, FXPole potentialPole, Colors color){
        CumSum -= getDistance(color, BotPole);
        CumSum += getDistance(color,potentialPole);
        return CumSum;
    }



    public Result findBestMove(FXPole BotPole, Game game, Colors color, double CumulativeDistance){
        Validator validator = Validator.getInstance();
        Result minimum = new Result(CumulativeDistance, new FXPole(new StandardPole(0,0,0)));
        FXPoleStorage possibleMoves = validator.findPossibleMoves(BotPole, game.getBoard().getAllFXPoles(), game.getVariant());
        System.out.println("FIND MOVe" + possibleMoves.getSize());

        for(FXPole potentialMove: possibleMoves.getAll()){
            if(potentialMove.getPole() instanceof TrianglePole){
                System.out.println("-----");
            }
            FXPoleStorage parents = new FXPoleStorage();
            parents.insert(BotPole);

            double potentialCumSum = CalculateCumulativeDistance(CumulativeDistance, BotPole, potentialMove, color);
            Result potentialResult = new Result(potentialCumSum, potentialMove);
            if(potentialResult.compareTo(minimum)<0){
                minimum=potentialResult;
            }

            if(validator.hasJumped(BotPole, potentialMove)){
                Result bestJumpSequence = findBestJump(potentialResult, game, parents, color);
                if(bestJumpSequence.compareTo(minimum)<0){
                    minimum=bestJumpSequence;
                }
            }
        }
        return minimum;
    }

    public double getDistance(Colors color, FXPole potential){
        return Math.sqrt(Math.pow(potential.calculateCartesianValueX()-color.getAim().calculateCartesianValueX(),2)+Math.pow(potential.calculateCartesianValueY()-color.getAim().calculateCartesianValueY(),2));
    }



    private Result findBestJump(Result source, Game game, FXPoleStorage parents, Colors color){
        Result min = source; //czyli po wykonaniu skoku jestesmy - minimalna opcja to po prostu skipniecie po skoku
        Validator validator = Validator.getInstance();
        parents.insert(source.fxPole);
        FXPoleStorage possibleJumps = validator.findPossibleJumps(source.fxPole,game.getBoard().getAllFXPoles(),game.getVariant(), parents);
        //dostajemy mozliwe skoki

        System.out.println(possibleJumps.getSize());
        for(FXPole anotherJump: possibleJumps.getAll()){
            double anotherCumSum = CalculateCumulativeDistance(source.distance, source.fxPole,anotherJump,color);
            Result anotherResult = new Result(anotherCumSum, anotherJump);

            if(anotherResult.compareTo(min)<0){
                min = anotherResult;
            }

            if(validator.hasJumped(source.fxPole,anotherJump)){
                Result bestJumpSequence = findBestJump(anotherResult, game, parents, color);
                if(bestJumpSequence.compareTo(min)<0){
                    min=bestJumpSequence;
                }
            }

        }
        return min;
//        return getMin(possibleJumps, source, source, game, parents, color);
    }

//    private Result getMin(FXPoleStorage possibleMoves, Result source, Result min, Game game, FXPoleStorage parents, Colors color){
//        Validator validator = Validator.getInstance();
//        for(FXPole potential : possibleMoves.getAll()){
//            if(validator.hasJumped(source.fxPole,potential)){
//                Result potentialBest = new Result(getDistance(color, potential), potential);
//                Result bestJump = findBestJump(potentialBest, game, parents, color);
//                if(bestJump.compareTo(min)<0){
//                    min=potentialBest;
//                }
//            }
//            else{
//                double d = getDistance(color, potential);
//                if(d<min.distance){
//                    min = new Result(d, potential);
//                }
//            }
//        }
//        return min;
//    }

    private Result getMin(FXPole potential, Result source, Result min, Game game, FXPoleStorage parents, Colors color){
        Validator validator = Validator.getInstance();
        if(validator.hasJumped(source.fxPole,potential)){
            Result potentialBest = new Result(getDistance(color, potential), potential);
            Result bestJump = findBestJump(potentialBest, game, parents, color);
            if(bestJump.compareTo(min)<0){
                min=potentialBest;
            }
        }
        else{
            double d = getDistance(color, potential);
            if(d<min.distance){
                min = new Result(d, potential);
            }
        }
        return min;
    }

}


