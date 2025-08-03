package visual;

import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.util.ArrayList;
import javax.swing.JPanel;

import logico.Equipo;
import logico.SerieNacional;

public class BarraWinrate extends JPanel {
    private ArrayList<Equipo> misEquipos;

    public BarraWinrate() {
        SerieNacional serieNacional = SerieNacional.getInstance();
        misEquipos = serieNacional.getMisEquipos();    
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        ArrayList<Equipo> equipos = SerieNacional.getInstance().getMisEquipos();
        int width = getWidth();
        int height = getHeight();

        if (equipos == null || equipos.isEmpty()) return;

        double totalWinrate = 0.0;
        for (Equipo eq : equipos) {
            totalWinrate += eq.getEstadistica().winrate();
        }
        
        if (totalWinrate == 0) return;

        Color[] colores = {
            Color.RED, Color.GREEN, Color.BLUE, Color.ORANGE, Color.MAGENTA,
            Color.PINK, Color.YELLOW, new Color(255, 87, 51),
            new Color(24, 212, 221), new Color(100, 100, 255)
        };

        int x = 0;
        int barHeight = height - 20;
        int y = 10;

        for (int i = 0; i < equipos.size(); i++) {
            Equipo equipo = equipos.get(i);
            double winrate = equipo.getEstadistica().winrate();
            int segmentWidth = (int) ((winrate / totalWinrate) * width);

            g.setColor(colores[i % colores.length]);
            g.fillRect(x, y, segmentWidth, barHeight);

            String text = String.format("%.0f%%", (winrate / totalWinrate) * 100);
            g.setColor(Color.WHITE);
            FontMetrics fm = g.getFontMetrics();
            int textWidth = fm.stringWidth(text);
            int textX = x + (segmentWidth - textWidth) / 2;
            int textY = y + (barHeight + fm.getAscent()) / 2 - 2;

            if (segmentWidth > textWidth + 6) {
                g.drawString(text, textX, textY);
            }

            x += segmentWidth;
        }
    }
}
