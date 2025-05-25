package SeatAllocator;

import javax.swing.table.AbstractTableModel;
import java.util.List;

public class HallTableModel extends AbstractTableModel {

 static final long serialVersionUID = 1L;
	private List<hall> halls;
    private final String[] columnNames = {"Hall Name", "Bench Count"};

    public HallTableModel(List<hall> halls) {
        this.halls = halls;
    }

    public void setHalls(List<hall> halls) {
        this.halls = halls;
        fireTableDataChanged();
    }

    public hall getHallAt(int row) {
        return halls.get(row);
    }

    @Override
    public int getRowCount() {
        return halls.size();
    }

    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    @Override
    public String getColumnName(int column) {
        return columnNames[column];
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        hall h = halls.get(rowIndex);
        return switch (columnIndex) {
            case 0 -> h.getHname();
            case 1 -> h.getBenchCount();
            default -> null;
        };
    }
}