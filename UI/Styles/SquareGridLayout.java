package UI.Styles;

import java.awt.*;
import java.util.*;

public class SquareGridLayout implements LayoutManager2
{
    private int rows = 0;
    private int cols = 0;

    /**
     * Create a SquareGridLayout. If both rows and cols are zero,
     * the layout will choose a near-square grid (cols ~= sqrt(n)).
     * If rows &gt; 0 then rows is fixed and columns are computed from component count.
     * If cols &gt; 0 then cols is fixed and rows are computed from component count.
     */
    public SquareGridLayout()
    {
        this(0, 0);
    }

    /**
     * Create a SquareGridLayout with fixed rows/cols similar to GridLayout semantics.
     * Use 0 for either rows or cols to let the layout compute that dimension.
     */
    public SquareGridLayout(int rows, int cols)
    {
        this.rows = Math.max(0, rows);
        this.cols = Math.max(0, cols);
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

        double cellW = (double) availWidth / cols;
        double cellH = (double) availHeight / rows;
        int cellSize = (int) Math.floor(Math.min(cellW, cellH));
        if (cellSize <= 0) return;

        int gridWidth = cellSize * cols;
        int gridHeight = cellSize * rows;

        int offsetX = insets.left + (availWidth - gridWidth) / 2;
        int offsetY = insets.top + (availHeight - gridHeight) / 2;

        for (int i = 0; i < n; i++)
        {
            Component comp = visible.get(i);
            int row = i / cols;
            int col = i % cols;
            int x = offsetX + col * cellSize;
            int y = offsetY + row * cellSize;
            comp.setBounds(x, y, cellSize, cellSize);
        }
    }

    public Dimension minimumLayoutSize(Container c)
    {
        Insets insets = c.getInsets();
        Component[] components = c.getComponents();
        int n = 0;
        int minSide = 0;
        for (Component comp : components)
        {
            if (comp != null && comp.isVisible())
            {
                n++;
                Dimension d = comp.getMinimumSize();
                minSide = Math.max(minSide, Math.max(d.width, d.height));
            }
        }
        if (n == 0) return new Dimension(0, 0);
        int[] grid = computeGrid(n);
        int rows = grid[0];
        int cols = grid[1];
        int w = insets.left + insets.right + cols * minSide;
        int h = insets.top + insets.bottom + rows * minSide;
        return new Dimension(w, h);
    }

    public void invalidateLayout(Container c)
    {

    }

    public Dimension preferredLayoutSize(Container c)
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
                Dimension d = comp.getPreferredSize();
                prefSide = Math.max(prefSide, Math.max(d.width, d.height));
            }
        }
        if (n == 0) return new Dimension(0, 0);
        int[] grid = computeGrid(n);
        int rows = grid[0];
        int cols = grid[1];
        int w = insets.left + insets.right + cols * prefSide;
        int h = insets.top + insets.bottom + rows * prefSide;
        return new Dimension(w, h);
    }

    public Dimension maximumLayoutSize(Container c)
    {
        return new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE);
    }
}
