package etu.nic.git.trajectories_swing.tools;

import java.awt.Font;
import java.awt.Shape;
import java.awt.font.FontRenderContext;
import java.awt.font.GlyphVector;
import java.awt.font.TextLayout;
import java.awt.geom.AffineTransform;

/**
 * Класс, содержащий формы для маркеров в виде букв (X, Y и Z)
 */
public class MarkerShapes {
    private static final Font font = new Font(
            Font.DIALOG,
            Font.PLAIN,
            10);

    /**
     * Создание объекта {@link Shape} из текста
     * @param text текст, который станет формой
     * @param font шрифт для текста
     * @return форма в виде переданного текста
     */
    public static Shape createShapeFromText(String text, Font font) {
        FontRenderContext frc = new FontRenderContext(null, true, true);

        TextLayout textLayout = new TextLayout(text, font, frc);

        Shape shape = textLayout.getOutline(null);

        java.awt.Rectangle bounds = shape.getBounds();

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