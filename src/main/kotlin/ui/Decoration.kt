import androidx.compose.ui.graphics.Color
import java.awt.image.BufferedImage
import javax.imageio.ImageIO

private var icon: BufferedImage? = null
fun icAppRounded(): BufferedImage {
    if (icon != null) {
        return icon!!
    }
    try {
        val imageRes = "funds_icon.png"
        val img = Thread.currentThread().contextClassLoader.getResource(imageRes)
        val bitmap: BufferedImage? = ImageIO.read(img)
        if (bitmap != null) {
            icon = bitmap
            return bitmap
        }
    } catch (e: Exception) {
        e.printStackTrace()
    }
    return BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB)
}

fun getFlagColor(isFall: Boolean): Color {
    return if (isFall) green else red
}

val red = Color(235, 89, 131)
val green = Color(98, 206, 134)
val blue = Color(85, 191, 220)