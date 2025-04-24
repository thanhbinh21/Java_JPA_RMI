package other;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;

public class Plot extends JPanel {
    private ArrayList<Integer> x = new ArrayList<>();
    private ArrayList<Integer> y = new ArrayList<>();
    private int spacex = 50;
    private int spacey = 30;
    private int dxy = 30;
    private int MaxX = 0;
    private int MaxY = 0;
    private int MinY = 0;
    private int fontSize = 15;

    private void setX(ArrayList<Integer> x) {
        if(x.stream().anyMatch(i -> i < 0))
            throw new IllegalArgumentException("x must be positive");
        this.x = x;
        this.MaxX = x.stream().max(Integer::compareTo).get();
    }

    private void setY(ArrayList<Integer> y) throws IllegalArgumentException {
        if(y.size() != this.x.size())
            throw new IllegalArgumentException("x and y must be same size");
        if(y.stream().anyMatch(i -> i < 0))
            throw new IllegalArgumentException("y must be positive");
        this.y = y;
        this.MaxY = y.stream().max(Integer::compareTo).get();
        this.MinY = y.stream().min(Integer::compareTo).get();
    }

    public void setX_Y(ArrayList<Integer> x, ArrayList<Integer> y) {
        setX(x);
        setY(y);
    }

    public Plot(ArrayList<Integer> x, ArrayList<Integer> y) throws IllegalArgumentException {
        setX(x);
        setY(y);
    }

    public Plot() {}

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if(x.isEmpty() || y.isEmpty())
            return;
        paintAxis(g);
        paintPoint(g);
        paintTicks(g);
        paintTickValues(g);
        paintLines(g);
    }

    protected void paintAxis(Graphics g) {
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setColor(Color.BLACK);
        g2d.setStroke(new BasicStroke(2));
        g2d.drawLine(spacex,getHeight()-spacey,spacex,spacey);
        g2d.drawLine(spacex,getHeight()-spacey,getWidth()-spacex,getHeight()-spacey);
    }

    protected void paintPoint(Graphics g) {
        int width = getWidth() - (spacex + dxy)*2;
        int height = getHeight() - (spacey + dxy)*2;
        double xStep = (double) width / MaxX;
        double upperMaxY = calculateUpper(MaxY);
        double yTicks = calculateTick(upperMaxY, MinY);
        double yStep = (double) height / upperMaxY;

        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setColor(Color.BLUE);
        for(int i = 0; i < x.size(); i++) {
            double y = this.y.get(i);
            if(MinY > upperMaxY/yTicks) {
                y = ((this.y.get(i).doubleValue() - MinY) / (upperMaxY - MinY));
                yStep = (double) height -  (double) height / yTicks;
                double heightMinY = (double) height / yTicks;
                g2d.fillOval((int) ((i+1)*xStep+spacex-5), (int) (getHeight()-(heightMinY+y*yStep+spacey+5)), 10, 10);
            }
            else
                g2d.fillOval((int) ((i+1)*xStep+spacex-5), (int) (getHeight()-(y*yStep+spacey+5)), 10, 10);
        }
    }

    protected void paintTicks(Graphics g) {
        int width = getWidth() - (spacex + dxy)*2;
        int height = getHeight() - (spacey + dxy)*2;
        double xStep = (double) width / x.size();
        double upperMaxY = calculateUpper(MaxY);
        double yTicks = calculateTick(upperMaxY, MinY);
        double yStep = (double) height / yTicks;

        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setColor(Color.BLACK);
        for(int i = 0; i <= MaxX; i++) {
            g2d.drawLine((int) (i*xStep+spacex), getHeight()-spacey-5, (int) (i*xStep+spacex), getHeight()-spacey+5);
        }
        for(double i = 0; i <= yTicks; i++) {
            g2d.drawLine(spacex-5, (int) (getHeight()-i * yStep-spacey), spacex+5, (int) (getHeight()-i * yStep-spacey));
        }
    }

    private double calculateUpper(double value) {
        String v = String.valueOf((int) value);
        double va = value < 100? value :
                                Double.parseDouble(v.substring(0, Math.min(v.length(), 3)))/10;
        double upper = Math.ceil(va);
        double e = v.length() - Math.min(v.length(), 3) + (value == va? 0 : Math.floor(Math.log10(upper)));
        return upper * Math.pow(10,e);
    }

    private int calculateTick(double maxY, double minY) {
        int tick = 0;
        double minMax = maxY - minY;
        double upperMinMaxY = calculateUpper(minMax);
        for(int i = 5; i <= 10; i++) {
            if(upperMinMaxY%i==0) {
                tick = i;
                break;
            }
        }
        return tick == 0? Math.min(5, (int) minMax + 1) : tick;
    }

    protected void paintTickValues(Graphics g) {
        int width = getWidth() - (spacex + dxy)*2;
        int height = getHeight() - (spacey + dxy)*2;
        double xStep = (double) width / x.size();
        double upperMaxY = calculateUpper(MaxY);
        double yTicks = calculateTick(upperMaxY, MinY);
        double yStep = (double) height / yTicks;
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setColor(Color.BLACK);
        Font font = new Font("Arial", Font.BOLD, fontSize);
        FontMetrics metrics = g2d.getFontMetrics(font);
        g2d.setFont(font);

        for(int i = 0; i < x.size(); i++) {
            g2d.drawString(String.valueOf(x.get(i)), (int) ((i+1)*xStep+spacex-fontSize/2), getHeight()-spacey+(fontSize+5));
        }
        for(int i = 1; i <= yTicks; i++) {
            double value = i / yTicks * upperMaxY;
            if(MinY > upperMaxY/yTicks)
                value = (upperMaxY-MinY)/(yTicks-1)*(i-1)+MinY;
            double log10 = Math.floor(Math.log10(value));
            value = log10 > 0? value / Math.pow(10, log10): value;
            String v = String.valueOf(value).substring(0, 3);
            v += log10 > 0? "e"+(int) log10 : "";
            g2d.drawString(v, spacex-(Math.min(metrics.stringWidth(v),spacex-5)+5), (int) (getHeight()-i*yStep-spacey+fontSize/2));
        }
    }

    protected void paintLines(Graphics g) {
        int width = getWidth() - (spacex + dxy)*2;
        int height = getHeight() - (spacey + dxy)*2;
        double xStep = (double) width / MaxX;
        double upperMaxY = calculateUpper(MaxY);
        double yTicks = calculateTick(upperMaxY, MinY);
        double yStep = (double) height / upperMaxY;

        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setColor(Color.BLUE);
        g2d.setStroke(new BasicStroke(2));
        for(int i = 1; i < x.size(); i++) {
            double y = this.y.get(i);
            double y_1 = this.y.get(i - 1);
            if(MinY > upperMaxY/yTicks) {
                y = ((this.y.get(i).doubleValue() - MinY) / (upperMaxY - MinY));
                y_1 = ((this.y.get(i - 1).doubleValue() - MinY) / (upperMaxY - MinY));
                yStep = (double) height -  (double) height / yTicks;
                double heightMinY = (double) height / yTicks;
                g2d.drawLine((int) (i*xStep+spacex), (int) (getHeight()-(heightMinY+y_1*yStep+spacey)), (int) ((i+1)*xStep+spacex), (int) (getHeight()-(heightMinY+y*yStep+spacey)));
            }
            else
                g2d.drawLine((int) (i*xStep+spacex), (int) (getHeight()-(y_1*yStep+spacey)), (int) ((i+1)*xStep+spacex), (int) (getHeight()-(y*yStep+spacey)));
        }
    }

    public static void main(String[] args) throws Exception {
        ArrayList<Integer> x = new ArrayList<>(Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15));
//        ArrayList<Integer> y = new ArrayList<>(Arrays.asList(1, 2, 4, 8, 16, 32, 64, 128, 256, 512, 1024, 2048, 4096, 8192, 16384));
        ArrayList<Integer> y = new ArrayList<>(Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15));
//        ArrayList<Integer> y = new ArrayList<>(Arrays.asList(13, 14, 15, 13, 14, 15, 13, 14, 15, 13, 14, 15, 13, 14, 15));
        JFrame frame = new JFrame("Quản Lý Tài Khoản");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1400, 800);
        frame.setLayout(null);
        Plot plot = new Plot(x, y);
        plot.setBounds(100, 100, 1000, 600);
        plot.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
        frame.add(plot);
        frame.setVisible(true);
    }
}
