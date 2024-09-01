package etu.nic.git.trajectories_swing.tools;

import java.awt.Font;
import java.awt.Shape;
import java.awt.font.FontRenderContext;
import java.awt.font.GlyphVector;
import java.awt.font.TextLayout;
import java.awt.geom.AffineTransform;

public class MarkerShapes {
    private static final Font font = new Font(
            Font.DIALOG,
//            "SansSerif",
            Font.PLAIN,
            10);

    public static Shape createShapeFromText(String text, Font font) {
        // Создание объекта FontRenderContext для получения информации о рендеринге шрифта
        FontRenderContext frc = new FontRenderContext(null, true, true);

        // Создание объекта TextLayout для текста
        TextLayout textLayout = new TextLayout(text, font, frc);

        // Преобразование TextLayout в Shape
        Shape shape = textLayout.getOutline(null);

        // Получение габаритов объекта Shape
        java.awt.Rectangle bounds = shape.getBounds();

        // Смещение объекта Shape так, чтобы его центр был в точке (0, 0)
        AffineTransform transform = AffineTransform.getTranslateInstance(-bounds.getCenterX(), -bounds.getCenterY());
        Shape centeredShape = transform.createTransformedShape(shape);

        return centeredShape;
    }

    public static Shape getShapeX() {
        return createShapeFromText("X", font);
    }
    public static Shape getShapeY() {
        return createShapeFromText("Y", font);
    }
    public static Shape getShapeZ() {
        return createShapeFromText("Z", font);
    }
}