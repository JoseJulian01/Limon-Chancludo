package mx.udg.limonchancludo.game.model

const val START_LIVES = 3

/**
 * Estado completo del juego (mutable, vive dentro del motor).
 */
class GameState(val maze: Maze, val level: Int) {

    val player = Actor(maze.spawnCol, maze.spawnRow, Direction.LEFT)

    var status: GameStatus = GameStatus.RUNNING
        private set

    var score: Int = 0
        private set

    var lives: Int = START_LIVES
        private set

    var lemonsRemaining: Int = maze.lemonCount()
        private set

    var invulnerable: Float = 0f
        private set

    /** true = limón ya comido en esa celda (índice row*width + col). */
    val eaten: BooleanArray = BooleanArray(maze.width * maze.height)

    private var nextExtraLife = ScoreSystem.EXTRA_LIFE_SCORE

    fun isEaten(col: Int, row: Int): Boolean {
        if (row < 0 || row >= maze.height || col < 0 || col >= maze.width) return true
        return eaten[row * maze.width + col]
    }

    fun eatLemon(col: Int, row: Int): Int {
        if (row < 0 || row >= maze.height || col < 0 || col >= maze.width) return 0
        val index = row * maze.width + col
        if (eaten[index]) return 0
        val tile = maze.tileAt(col, row)
        if (!tile.isLemon) return 0
        eaten[index] = true
        lemonsRemaining--
        val points = ScoreSystem.pointsFor(tile)
        addScore(points)
        if (lemonsRemaining == 0) {
            addScore(ScoreSystem.levelBonus(level))
            status = GameStatus.LEVEL_CLEARED
        }
        return points
    }

    fun lemonTile(col: Int, row: Int): Tile = maze.tileAt(col, row)

    internal fun addScore(points: Int) {
        score += points
        if (score >= nextExtraLife && lives < 9) {
            lives++
            nextExtraLife += ScoreSystem.EXTRA_LIFE_SCORE
        }
    }

    internal fun setInvulnerable(seconds: Float) {
        invulnerable = seconds
    }

    internal fun tickInvulnerable(dt: Float) {
        if (invulnerable > 0f) invulnerable = (invulnerable - dt).coerceAtLeast(0f)
    }

    fun loseLife() {
        lives--
        if (lives <= 0) {
            lives = 0
            status = GameStatus.GAME_OVER
        } else {
            player.snapTo(maze.spawnCol, maze.spawnRow)
            player.dir = Direction.LEFT
        }
    }
}
