public class Movement {

    private Player player;
    private TileHandler tileHandler;
    
    private int tickCounter = 0;
    private boolean canMove = false;

    public Movement(Player player, TileHandler tileHandler) {
        this.player = player;
        this.tileHandler = tileHandler;
    }

    public void tick() {
        // handles player speed
        if (!canMove) {
            tickCounter++;
        }
        if (tickCounter == 4) {
            canMove = true;
            tickCounter = 0;
        }
        if (canMove == true) {
            move();
        }
    }

    private void move() {
        Tile tile = player.getTile();
        int newRow = tile.getRow() + player.getVelY();
        int newCol = tile.getCol() + player.getVelX();

        if (inGrid(newRow, newCol)) {
            Tile newTile = Grid.getTile(newRow, newCol);

            // if the player is moving to a new path tile
            if (isPath(newTile)) {
                player.setTile(newTile);
            // if the player is pressing space or currently pushing
            } else if (pressingPush() || pushing()) {
                // makes sure the player cant walk backwards or go beside an existing push
                if (!isPush(newTile) && !adjacentPush(tile, newTile)) {
                    player.setTile(newTile);
                }
            }
            
            if (!isPath(tile) && isPath(newTile)) {
                tileHandler.scan();
            }
        }

        canMove = false;
    }

    // checks if a tile is in the grid given row and col
    private boolean inGrid(int row, int col) {
        return row >= 0 && row < Game.GRIDSIZE && col >= 0 && col < Game.GRIDSIZE;
    }

    // checks if the given tile is a path tile
    private boolean isPath(Tile tile) {
        return tile.getTileID() == TileID.PATH;
    }

    // checks if the player is pressing push (spacebar)
    private boolean pressingPush() {
        return player.push;
    }

    // checks if the player currently in a push
    private boolean pushing() {
        return player.getTile().getTileID() == TileID.PUSH;
    }

    // checks if the given tile is a path tiles
    private boolean isPush(Tile tile) {
        return tile.getTileID() == TileID.PUSH;
    }

    // checks if the new tile is beside an existing push
    private boolean adjacentPush(Tile curTile, Tile nextTile) {
        int curR = curTile.getRow();
        int curC = curTile.getCol();
        int nextR = nextTile.getRow();
        int nextC = nextTile.getCol();

        int r = nextR - curR;
        int c = nextC - curC;

        Tile t1, t2, t3;

        if (r == -1 || r == 1) {
            t1 = Grid.getTile(nextR + r, nextC - 1);
            t2 = Grid.getTile(nextR + r, nextC);
            t3 = Grid.getTile(nextR + r, nextC + 1);

            if (isPush(t1) || isPush(t2) || isPush(t3)) {
                return true;
            }
        }

        if (c == -1 || c == 1) {
            t1 = Grid.getTile(nextR - 1, nextC + c);
            t2 = Grid.getTile(nextR, nextC + c);
            t3 = Grid.getTile(nextR + 1, nextC + c);

            if (isPush(t1) || isPush(t2) || isPush(t3)) {
                return true;
            }
        }

        return false;
    }

}
