package mx.udg.limonchancludo.game.model

/**
 * Actor en movimiento por el grid.
 * [col]/[row] es la celda de origen; [progress] avanza hacia [targetCol]/[targetRow].
 */
open class Actor(
    col: Int,
    row: Int,
    var dir: Direction = Direction.RIGHT
) {
    var col: Int = col
        private set
    var row: Int = row
        private set
    var targetCol: Int = col
        private set
    var targetRow: Int = row
        private set
    var progress: Float = 0f
        private set
    var queuedDir: Direction? = null
    var moving: Boolean = false
        private set

    /** Coordenadas continuas en celdas (para render interpolado). */
    val x: Float get() = col + (targetCol - col) * progress
    val y: Float get() = row + (targetRow - row) * progress

    /** Verdadero si el salto al objetivo cruza el túnel (no interpolar en X). */
    val wrapsHorizontally: Boolean get() = kotlin.math.abs(targetCol - col) > 1

    fun snapTo(c: Int, r: Int) {
        col = c
        row = r
        targetCol = c
        targetRow = r
        progress = 0f
        moving = false
    }

    fun beginMove(tc: Int, tr: Int) {
        targetCol = tc
        targetRow = tr
        progress = 0f
        moving = true
    }

    fun advance(p: Float) {
        progress = p
    }

    fun arrive() {
        col = targetCol
        row = targetRow
        progress = 0f
        moving = false
    }

    /** Invierte el sentido a mitad de celda (giro de 180° del jugador). */
    fun reverseInPlace() {
        val oldCol = col
        val oldRow = row
        col = targetCol
        row = targetRow
        targetCol = oldCol
        targetRow = oldRow
        progress = 1f - progress
        moving = true
    }

    fun setDirection(newDir: Direction) {
        dir = newDir
        queuedDir = null
    }
}
