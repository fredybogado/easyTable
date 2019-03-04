package my.custom.jtable.component;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

import my.custom.jtable.table_model.ModeloTablaGenerico;
import my.custom.jtable.util.Comparacion;

public class MyTable extends JTable {

	private static final long serialVersionUID = -1835257686646299108L;
	private ModeloTablaGenerico modelo;
	private List<Map<String, Object>> condiciones = new ArrayList<Map<String,Object>>();
	private int anchoColumnaArray[];
	
	public MyTable() {
		this.modelo = new ModeloTablaGenerico();
		this.setModel(this.modelo);
	}
	
	//{"id","nombre+apellido=>Nombre y Apellido","documento","activo=>Estado","fechaRegistro"}
	public void config(String[] columns) {
		modelo.setColumnas(getAtributos(columns));
		modelo.setTitulos(getColumnas(columns));
	}
	
	public void setDatos(List<?> lista) {
		modelo.setLista(lista);
		modelo.fireTableStructureChanged();
	}
	
	public void agregarCondicion(int indiceColumna, Color color, Comparacion comparacion, Object v1, Object v2) {
		Map<String, Object> map = new HashMap<>();
		map.put("index", indiceColumna);
		map.put("color", color);
		map.put("com", comparacion);
		map.put("v1", v1);
		map.put("v2", v2);
		condiciones.add(map);
	}
	
	public void agregarCondicion(int indiceColumna, Color color, Comparacion comparacion, Object v1) {
		agregarCondicion(indiceColumna, color, comparacion, v1, null);
	}

	private String[] getColumnas(String[] columnas) {
		String nuevaColumnas[] = new String[columnas.length];
		for (int i = 0; i < nuevaColumnas.length; i++) {
			String[] parts = columnas[i].split("=>");
			if(parts.length > 1) nuevaColumnas[i] = parts[1].toUpperCase();
			else {
				parts = parts[0].split("(?=\\p{Upper})");
				for (int j = 0; j < parts.length; j++) {
					if(j==0) nuevaColumnas[i] = parts[j].toUpperCase();
					else nuevaColumnas[i] = nuevaColumnas[i]+" "+parts[j].toUpperCase();
				}
			}
		}
		return nuevaColumnas;
	}

	private String[] getAtributos(String[] columnas) {
		String atributos[] = new String[columnas.length];
		for (int i = 0; i < atributos.length; i++) {
			String[] parts = columnas[i].split("=>");
			atributos[i] = parts[0];
		}
		return atributos;
	}
	
	@Override
	public Component prepareRenderer(TableCellRenderer renderer, int row, int column)
	{
		Component c = super.prepareRenderer(renderer, row, column);
		
		//cambia el tamaño de las columnas segun el contenido
		if (row == 0) {
			if(column == 0) anchoColumnaArray = new int[this.getColumnCount()];
			anchoColumnaArray[column] = 15;
		}
		anchoColumnaArray[column] = Math.max(c.getPreferredSize().width +1 , anchoColumnaArray[column]);
		
		if (row == this.getRowCount()-1) {
			columnModel.getColumn(column).setPreferredWidth(anchoColumnaArray[column]);
		}
		
		//comprueba la condiciones para colorear
		comprobarCondiciones(c, row);
		return c;
	}	

	private void comprobarCondiciones(Component comp, int fila) {
		Color color = Color.white;
		for (int i = 0; i < condiciones.size(); i++) {
			Comparacion comparacion = (Comparacion) condiciones.get(i).get("com");
			Object valorColumna = this.getValueAt(fila, (int) condiciones.get(i).get("index"));
			Object v1 = null;
			Object v2 = null;
			if(condiciones.get(i).get("v1").getClass()==Comparacion.Self.class){
				int c = ((Comparacion.Self) condiciones.get(i).get("v1")).getColumn();
				v1 = this.getValueAt(fila, c);
			}else v1 = condiciones.get(i).get("v1");
			boolean resultado;
			if (comparacion == Comparacion.BETWEEN) {
				if(condiciones.get(i).get("v2").getClass()==Comparacion.Self.class){
					int c = ((Comparacion.Self) condiciones.get(i).get("v2")).getColumn();
					v2 = this.getValueAt(fila, c);
				}else v2 = condiciones.get(i).get("v2");
				resultado = comparacion.compare(valorColumna,v1,v2);
			}else{
				resultado = comparacion.compare(valorColumna,v1);
			}
			
			if(resultado){
				color = (Color) condiciones.get(i).get("color");
			}
		}
		comp.setBackground(color);
		if(Color.white != color) comp.setFont(new Font("Dialog", Font.BOLD, 12));
	}
	
	

}
