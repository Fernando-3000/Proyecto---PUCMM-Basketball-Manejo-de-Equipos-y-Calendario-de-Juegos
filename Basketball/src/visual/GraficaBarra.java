package visual;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

import logico.Jugador;
import logico.SerieNacional;

public class GraficaBarra extends JPanel {

    private List<Jugador> topJugadores;
    private static final int TOP_SIZE = 5;
    private static final int BAR_GAP = 40;
    private static final int LABEL_HEIGHT = 30;
    private static final int IMAGE_SIZE = 50;
    private static final Color[] COLORS = {
        new Color(33, 150, 243),   // Azul
        new Color(255, 193, 7),    // Amarillo
        new Color(244, 67, 54),    // Rojo
        new Color(156, 39, 176),   // Morado
        new Color(0, 150, 136)     // Verde turquesa
    };

    /**
     * Constructor
     */
    public GraficaBarra() {
        setBackground(Color.WHITE);
        setTopJugadores(TOP_SIZE);
    }

    /**
     * Establece los mejores jugadores (ordenados por puntos)
     */
    public void setTopJugadores(int topSize) {
        List<Jugador> jugadores = SerieNacional.getInstance().getMisJugadores();
        if (jugadores == null || jugadores.isEmpty()) {
            this.topJugadores = new ArrayList<>();
            return;
        }

        // Copiar y ordenar por puntos totales (descendente)
        List<Jugador> sorted = new ArrayList<>(jugadores);
        sorted.sort((j1, j2) -> Integer.compare(
            j2.getEstadisticas().getPuntosTot(),
            j1.getEstadisticas().getPuntosTot()
        ));

        // Tomar los top N
        this.topJugadores = sorted.subList(0, Math.min(topSize, sorted.size()));
        repaint(); // Redibujar
    }

    /**
     * Método principal de dibujo
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        int width = getWidth();
        int height = getHeight();

        if (topJugadores == null || topJugadores.isEmpty()) {
            g2d.setColor(Color.GRAY);
            g2d.setFont(new Font("Arial", Font.ITALIC, 16));
            g2d.drawString("No hay jugadores para mostrar", width / 2 - 120, height / 2);
            return;
        }

        // Espacio disponible para barras
        int availableHeight = height - LABEL_HEIGHT - IMAGE_SIZE - 20;
        int barWidth = Math.max(30, (width - 2 * BAR_GAP) / Math.max(1, topJugadores.size()) - 20);
        int maxPoints = topJugadores.get(0).getEstadisticas().getPuntosTot(); // El mayor

        // Dibujar cada barra
        for (int i = 0; i < topJugadores.size(); i++) {
            Jugador jugador = topJugadores.get(i);
            int puntos = jugador.getEstadisticas().getPuntosTot();
            int barHeight = (int) ((double) puntos / maxPoints * availableHeight);

            int x = BAR_GAP + i * (barWidth + 20);
            int y = height - barHeight - LABEL_HEIGHT - IMAGE_SIZE - 10;

            // Color de la barra
            g2d.setColor(COLORS[i % COLORS.length]);
            g2d.fillRoundRect(x, y, barWidth, barHeight, 10, 10);

            // Borde oscuro
            g2d.setColor(Color.DARK_GRAY);
            g2d.drawRoundRect(x, y, barWidth, barHeight, 10, 10);

            // Valor encima de la barra
            g2d.setColor(Color.BLACK);
            g2d.setFont(new Font("Arial", Font.BOLD, 12));
            g2d.drawString(String.valueOf(puntos), x + barWidth / 2 - 10, y - 5);

            // Nombre del jugador
            g2d.setFont(new Font("Arial", Font.PLAIN, 12));
            String nombre = jugador.getNombre().charAt(0) + ". " + jugador.getApellido();
            int nombreX = x + barWidth / 2 - g2d.getFontMetrics().stringWidth(nombre) / 2;
            g2d.drawString(nombre, nombreX, height - LABEL_HEIGHT + 5);

            // Foto del jugador
            BufferedImage img = null;
            File foto = jugador.getFotoFile();
            if (foto != null && foto.exists()) {
                try {
                    img = ImageIO.read(foto);
                } catch (IOException e) {
                    System.err.println("No se pudo cargar la imagen de: " + jugador.getNombre());
                }
            }

            if (img != null) {
                g2d.drawImage(img, x + (barWidth - IMAGE_SIZE) / 2, y - IMAGE_SIZE - 5, IMAGE_SIZE, IMAGE_SIZE, null);
            } else {
                // Si no hay foto, dibuja un recuadro gris
                g2d.setColor(Color.LIGHT_GRAY);
                g2d.fillRect(x + (barWidth - IMAGE_SIZE) / 2, y - IMAGE_SIZE - 5, IMAGE_SIZE, IMAGE_SIZE);
                g2d.setColor(Color.GRAY);
                g2d.drawRect(x + (barWidth - IMAGE_SIZE) / 2, y - IMAGE_SIZE - 5, IMAGE_SIZE, IMAGE_SIZE);
                g2d.drawString("No foto", x + (barWidth - 30) / 2, y - 30);
            }
        }

        // Título
        g2d.setColor(Color.BLACK);
        g2d.setFont(new Font("Arial", Font.BOLD, 16));
        g2d.drawString("Top " + topJugadores.size() + " Jugadores por Puntos", BAR_GAP, 30);
    }

    /**
     * Actualiza la gráfica (útil cuando cambian las estadísticas)
     */
    public void actualizar() {
        setTopJugadores(TOP_SIZE);
    }
}