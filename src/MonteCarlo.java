
import java.awt.*;
import java.util.concurrent.ThreadLocalRandom;
import javax.swing.JApplet;

public class MonteCarlo extends JApplet implements Runnable {

    int width, height;

    long rect = 0;
    long area = 0;

    double radius = 100;
    double radius_squared = radius * radius;

    int bar_height = 25;

    Thread thread = null;
    boolean threadSuspended;

    Image image;
    Graphics screen;

    public void init() {


        setSize((int) (2 * radius), (int) (2 * radius + bar_height));
        setBackground(Color.black);


        width = getSize().width;
        height = getSize().height;


        image = createImage(width, height);
        screen = image.getGraphics();

        showStatus("Monte Carlo Simulation - Pi");
    }

    public void start() {

        if (thread == null) {
            thread = new Thread(this);
            thread.setPriority(Thread.MIN_PRIORITY);
            threadSuspended = false;
            thread.start();
        } else {
            if (threadSuspended) {
                threadSuspended = false;
                synchronized (this) {
                    notify();
                }
            }
        }
    }

    public void stop() {
        threadSuspended = true;
    }

    public void paint(Graphics g) {

        double x0 = getX() + width / 2;
        double y0 = getY() + (height + bar_height) / 2;

        double x1 = x0 + getRandomPosition(-radius, radius);
        double y1 = y0 + getRandomPosition(-radius, radius);

        double r_squared = Math.pow(x1 - x0, 2) + Math.pow(y1 - y0, 2);


        if (r_squared < radius_squared) {
            area++;
            screen.setColor(Color.yellow);
        } else {
            screen.setColor(Color.red);
        }

        rect++;

        screen.drawLine((int) x1, (int) y1, (int) x1, (int) y1);

        screen.setColor(Color.black);
        screen.fillRect(0, 0, width, bar_height);
        screen.setColor(Color.green);
        screen.drawString("π = " + (4d * area / rect), 8, 16);

        g.drawImage(image, 0, 0, this);

    }

    public void run() {
        while (true) repaint();
    }

    private double getRandomPosition(double min, double max) {
        return ThreadLocalRandom.current().nextDouble(min, max);
    }
}