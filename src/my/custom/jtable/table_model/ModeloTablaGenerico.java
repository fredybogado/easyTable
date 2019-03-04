package my.custom.jtable.table_model;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import javax.swing.table.AbstractTableModel;

import my.custom.jtable.util.WraperUtil;

public class ModeloTablaGenerico extends AbstractTableModel{
	
	private static final long serialVersionUID = 5623346388561641297L;

	private String titulos[] = {};
	private String columnas[] = {};
	private List<?> lista = new ArrayList<>();
	
	public void setLista(List<?> lista) {
		this.lista = lista;
	}
	
	public void setColumnas(String[] columns) {
		this.columnas = columns;
	}
	
	public void setTitulos(String[] columnsNames) {
		this.titulos = columnsNames;
	}

	@Override
	public String getColumnName(int i) {
		return titulos[i];
	}
	
	@Override
	public int getRowCount() {
		return lista.size();
	}

	@Override
	public int getColumnCount() {
		return columnas.length;
	}
	
	@Override
	public Object getValueAt(int r, int c) {
		try {
			//si contiene + se divide en dos atributos 
			String atributos[] = columnas[c].replace("+", " ").split(" ");
			
			Object val = null;
			for (int i = 0; i < atributos.length; i++) {
				//se recupera el atributo de manera dinamica utilizando el nombre con reflection. ejempo nombre
				Field field = lista.get(r).getClass().getDeclaredField(atributos[i]);
				field.setAccessible(true);
				//se recupera el valor del atributo
				if(val == null) val = field.get(lista.get(r));
				else val += " " + field.get(lista.get(r));
			}
			return val;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	@Override
	public Class<?> getColumnClass(int c) {
		if(lista !=null && lista.size()>0){
			try {
				//si contiene + se divide en dos atributos 
				String atributos[] = columnas[c].replace("+", " ").split(" ");
				Class<?> columnType = String.class;
				if(atributos.length == 1) {
					//se recupera el atributo de manera dinamica utilizando el nombre con reflection. ejempo nombre
					Field field = lista.get(0).getClass().getDeclaredField(atributos[0]);
					field.setAccessible(true);
					//se recupera el tipo de dato del atributo
					columnType = field.getType();
				}
				columnType = columnType.isPrimitive() ? WraperUtil.wrap(columnType) : columnType;
				return columnType;
			} catch (Exception e) {
				return Object.class;
			}
		}else{
			return Object.class;
		}
	}
	
}
