import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class ClothSim extends JPanel implements MouseListener, MouseMotionListener, ActionListener
{

    final int WIDTH = 800, HEIGHT = 600;
    final int ROWS = 30, COLS = 40;
    final int SPACING = 15;
    final int CONSTRAINT_ITERATIONS = 5;
    final float GRAVITY = 0.5f;
    final float WIND = 0.2f;

    ArrayList<Particle> particles = new ArrayList<>();
    ArrayList<Constraint> constraints = new ArrayList<>();

    Timer timer;
    Particle dragged = null;

    public ClothSim()
    {
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        setBackground(Color.BLACK);
        addMouseListener(this);
        addMouseMotionListener(this);
        initCloth();
        timer = new Timer(16, this);
        timer.start();
    }

    void initCloth()
    {
        particles.clear();
        constraints.clear();

        Particle[][] grid = new Particle[ROWS][COLS];

        for (int y = 0; y < ROWS; y++)
        {
            for (int x = 0; x < COLS; x++)
            {
                int px = 100 + x * SPACING;
                int py = 50 + y * SPACING;
                boolean pinned = (y == 0 && x % 5 == 0);
                Particle p = new Particle(px, py, pinned);
                grid[y][x] = p;
                particles.add(p);

                if (x > 0)
                {
                    constraints.add(new Constraint(p, grid[y][x - 1]));
                }

                if (y > 0)
                {
                    constraints.add(new Constraint(p, grid[y - 1][x]));
                }
            }
        }
    }

    public void actionPerformed(ActionEvent e)
    {
        for (Particle p : particles)
        {
            p.update();
        }

        for (int i = 0; i < CONSTRAINT_ITERATIONS; i++)
        {
            for (Constraint c : constraints)
            {
                c.enforce();
            }
        }

        repaint();
    }

    public void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setColor(Color.WHITE);

        for (Constraint c : constraints)
        {
            c.draw(g2);
        }

        for (Particle p : particles)
        {
            p.draw(g2);
        }
    }

    class Particle
    {
        float x, y;
        float prevX, prevY;
        boolean pinned;

        public Particle(float x, float y, boolean pinned)
        {
            this.x = prevX = x;
            this.y = prevY = y;
            this.pinned = pinned;
        }

        void update()
        {
            if (pinned)
            {
                return;
            }

            float vx = x - prevX;
            float vy = y - prevY;
            prevX = x;
            prevY = y;
            x += vx + WIND * Math.random();
            y += vy + GRAVITY;
        }

        void draw(Graphics2D g)
        {
            g.fillOval((int) x - 2, (int) y - 2, 4, 4);
        }

        void applyConstraint(float nx, float ny)
        {
            if (pinned)
            {
                return;
            }

            x = nx;
            y = ny;
        }
    }

    class Constraint
    {
        Particle p1, p2;
        float restLength;

        public Constraint(Particle a, Particle b)
        {
            p1 = a;
            p2 = b;
            restLength = distance(p1, p2);
        }

        void enforce()
        {
            float dx = p2.x - p1.x;
            float dy = p2.y - p1.y;
            float dist = (float) Math.sqrt(dx * dx + dy * dy);
            float diff = restLength - dist;
            float percent = diff / dist / 2;
            float offsetX = dx * percent;
            float offsetY = dy * percent;

            if (!p1.pinned)
            {
                p1.applyConstraint(p1.x - offsetX, p1.y - offsetY);
            }

            if (!p2.pinned)
            {
                p2.applyConstraint(p2.x + offsetX, p2.y + offsetY);
            }
        }

        void draw(Graphics2D g)
        {
            g.drawLine((int) p1.x, (int) p1.y, (int) p2.x, (int) p2.y);
        }

        float distance(Particle a, Particle b)
        {
            return (float) Math.sqrt(Math.pow(a.x - b.x, 2) + Math.pow(a.y - b.y, 2));
        }
    }

    public void mousePressed(MouseEvent e)
    {
        for (Particle p : particles)
        {
            if (Math.abs(p.x - e.getX()) < 10 && Math.abs(p.y - e.getY()) < 10)
            {
                dragged = p;
                break;
            }
        }
    }

    public void mouseReleased(MouseEvent e)
    {
        dragged = null;
    }

    public void mouseDragged(MouseEvent e)
    {
        if (dragged != null)
        {
            dragged.prevX = dragged.x = e.getX();
            dragged.prevY = dragged.y = e.getY();
        }
    }

    public void mouseClicked(MouseEvent e) {}
    public void mouseEntered(MouseEvent e) {}
    public void mouseExited(MouseEvent e) {}
    public void mouseMoved(MouseEvent e) {}

    public static void main(String[] args)
    {
        JFrame frame = new JFrame("Cloth Simulation");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setContentPane(new ClothSim());
        frame.pack();
        frame.setVisible(true);
    }
}
