package UI.Styles;

import java.awt.LayoutManager2;
import java.awt.Component;
import java.awt.Container;
import java.awt.Insets;
import java.awt.Dimension;
import java.util.ArrayList;

public class SquareGridLayout implements LayoutManager2
{
    private int rows = 0;
    private int cols = 0;
    private int hgap = 0;
    private int vgap = 0;

    public SquareGridLayout()
    {
        this(0, 0, 0, 0);
    }

    public SquareGridLayout(int rows, int cols)
    {
        this(rows, cols, 0, 0);
    }

    public SquareGridLayout(int rows, int cols, int hgap, int vgap)
    {
        this.rows = Math.max(0, rows);
        this.cols = Math.max(0, cols);
        this.hgap = Math.max(0, hgap);
        this.vgap = Math.max(0, vgap);
    }

    public void setRows(int rows)
    {
        this.rows = Math.max(0, rows);
    }

    public void setColumns(int cols)
    {
        this.cols = Math.max(0, cols);
    }

    public int getRows()
    {
        return rows;
    }

    public int getColumns()
    {
        return cols;
    }

    public void setHgap(int hgap)
    {
        this.hgap = Math.max(0, hgap);
    }

    public void setVgap(int vgap)
    {
        this.vgap = Math.max(0, vgap);
    }

    public int getHgap()
    {
        return hgap;
    }

    public int getVgap()
    {
        return vgap;
    }

    private int[] computeGrid(int n)
    {
        int r, c;
        if (n <= 0)
        {
            return new int[]{0, 0};
        }
        if (this.rows > 0)
        {
            r = this.rows;
            c = (int) Math.ceil((double) n / r);
        }
        else if (this.cols > 0)
        {
            c = this.cols;
            r = (int) Math.ceil((double) n / c);
        }
        else
        {
            c = (int) Math.ceil(Math.sqrt(n));
            r = (int) Math.ceil((double) n / c);
        }
        return new int[]{r, c};
    }
    public void addLayoutComponent(String s, Component c)
    {

    }

    public void addLayoutComponent(Component c, Object o)
    {

    }

    public void removeLayoutComponent(Component c)
    {

    }

    public float getLayoutAlignmentY(Container c)
    {
        return 0.5f;
    }

    public float getLayoutAlignmentX(Container c)
    {
        return 0.5f;
    }

    public void layoutContainer(Container c)
    {
        Insets insets = c.getInsets();
        int availWidth = c.getWidth() - insets.left - insets.right;
        int availHeight = c.getHeight() - insets.top - insets.bottom;

        Component[] components = c.getComponents();

        ArrayList<Component> visible = new ArrayList<>();
        for (Component comp : components)
        {
            if (comp != null && comp.isVisible()) visible.add(comp);
        }

        int n = visible.size();
        if (n == 0 || availWidth <= 0 || availHeight <= 0) return;

        int[] grid = computeGrid(n);
        int rows = grid[0];
        int cols = grid[1];

        int gapWidth = (cols - 1) * hgap;
        int gapHeight = (rows - 1) * vgap;
        int usableWidth = availWidth - gapWidth;
        int usableHeight = availHeight - gapHeight;

        double cellW = (double) usableWidth / cols;
        double cellH = (double) usableHeight / rows;
        int cellSize = (int) Math.floor(Math.min(cellW, cellH));
        if (cellSize <= 0) return;

        int gridWidth = cellSize * cols + gapWidth;
        int gridHeight = cellSize * rows + gapHeight;

        int offsetX = insets.left + (availWidth - gridWidth) / 2;
        int offsetY = insets.top + (availHeight - gridHeight) / 2;

        for (int i = 0; i < n; i++)
        {
            Component comp = visible.get(i);
            int row = i / cols;
            int col = i % cols;
            int x = offsetX + col * (cellSize + hgap);
            int y = offsetY + row * (cellSize + vgap);
            comp.setBounds(x, y, cellSize, cellSize);
        }
    }

    public Dimension minimumLayoutSize(Container c)
    {
        return getLayoutSize(c, (Component comp) -> {
            return comp.getMinimumSize();
        });
    }

    public void invalidateLayout(Container c)
    {

    }

    public Dimension preferredLayoutSize(Container c)
    {
        return getLayoutSize(c, (Component comp) -> {
            return comp.getPreferredSize();
        });
    }

    private Dimension getLayoutSize(Container c, SizeGetter f)
    {
        Insets insets = c.getInsets();
        Component[] components = c.getComponents();
        int n = 0;
        int prefSide = 0;
        for (Component comp : components)
        {
            if (comp != null && comp.isVisible())
            {
                n++;
                Dimension d = f.getSize(comp);
                prefSide = Math.max(prefSide, Math.max(d.width, d.height));
            }
        }
        if (n == 0) return new Dimension(0, 0);
        int[] grid = computeGrid(n);
        int rows = grid[0];
        int cols = grid[1];
        int w = insets.left + insets.right + cols * prefSide + (cols - 1) * hgap;
        int h = insets.top + insets.bottom + rows * prefSide + (rows - 1) * vgap;
        return new Dimension(w, h);
    }

    private interface SizeGetter
    {
        public Dimension getSize(Component c);
    }

    public Dimension maximumLayoutSize(Container c)
    {
        return new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE);
    }
}
