package sem3tp.Storage;

import sem3tp.Player;

import java.util.ArrayList;

public class PlayerStorage extends Storage<Player> {
    public PlayerStorage(){
        this.list=new ArrayList<Player>();
    }

    @Override
    public boolean contains(Player o) {
        return super.contains(o);
    }

    @Override
    public Player get(Player player) {
        return super.get(player);
    }

    @Override
    public Player getByIndex(int index) {
        return super.getByIndex(index);
    }

    @Override
    public void update(Player player) {
        super.update(player);
    }

    @Override
    public void delete(Player player) {
        super.delete(player);
    }

    @Override
    public void insert(Player player) {
        super.insert(player);
    }

    @Override
    public int getSize() {
        return super.getSize();
    }
}
