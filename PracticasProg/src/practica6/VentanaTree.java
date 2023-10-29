package practica6;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseEvent.*;
import java.awt.event.MouseMotionAdapter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.swing.*;
import javax.swing.event.*;
import javax.swing.table.*;
import javax.swing.tree.*;



public class VentanaTree extends JFrame{
	private Integer habitantesMuniSel = null;
	private String municipioSeleccionado = null;
	protected String provinciaSeleccionada = null;
	protected String autonomiaSeleccionada = null;
	private static  DataSetMunicipios datasetEnt = null;
	boolean alfabeticamente = true;
	JLabel label = null;
	JTree tree = null;
	JScrollPane scrollTree = null;
	JTable table = null;
	JScrollPane scrollTable = null;
	JPanel panel = null;
	JPanel botonera = null;
	JButton insercion = null;
	JButton borrado = null;
	JButton orden = null;
	List<Municipio> datosTabla = new ArrayList<Municipio>();
	MiTableModel modeloTabla = null;
	public VentanaTree() {
		super();
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setSize(1200,800);
		setLocationRelativeTo(null); 
		this.label = new JLabel();
		this.tree = new JTree();
		this.scrollTree = new JScrollPane(tree);
		this.table = new JTable();
		scrollTable = new JScrollPane(table); 
		this.panel = new JPanel() {
			 @Override
	            protected void paintComponent(Graphics g) { //Modificar el método paintComponent del JPanel para que aparezca el gráfico
	                super.paintComponent(g);
	                panel.setPreferredSize(new Dimension(210,tree.getHeight()));
	                float valorProvincia = 0;
	                float valorEstado = 0;
	                float alturaEstado = getHeight()-getHeight()/20;
	                int anchoBarra = 60;
		                
	                System.out.println("Pintado");
		           	for(Municipio muni:datasetEnt.getListaMunicipios()) {
		           		if(!tree.isSelectionEmpty()) {
		           			if(((DefaultMutableTreeNode)tree.getLastSelectedPathComponent()).isLeaf()&&tree.getLastSelectedPathComponent().toString().equals(muni.getProvincia())) {
			               		valorProvincia = valorProvincia+muni.getHabitantes();
			               	}
		           		}
		               	valorEstado=valorEstado+muni.getHabitantes();
		            }
		           	if (modeloTabla!=null) {
		                  // Altura de la barra de la provincia
		            	float alturaProvincia = valorProvincia/valorEstado*alturaEstado;
		            	g.drawString(String.format( "%,d", (int)valorProvincia ), 32, getHeight()-(int)alturaProvincia-5);
		            	
		                g.setColor(Color.GREEN);
		                g.fillRect(30, getHeight()-(int)alturaProvincia, anchoBarra, (int)alturaProvincia);
	
		                // Dibujar las líneas horizontales en la barra de la provincia
		                g.setColor(Color.BLACK);
		                float poblacionActual = 0;
		                float alturaActual = 0;
		                for (Municipio muni:datosTabla) {
		                    poblacionActual = poblacionActual+muni.getHabitantes();
		                    alturaActual = poblacionActual/valorEstado*alturaEstado;
		                	g.drawLine(30, getHeight()- (int)alturaActual, 90, getHeight()- (int) alturaActual);
		                }
		            }

	                // Dibujar la barra del total del estado
		           	g.drawString(String.format( "%,d", (int)valorEstado ), 120, getHeight()-(int)alturaEstado-5);
		           	g.setColor(Color.BLUE);
	                g.fillRect(120, getHeight()/20, anchoBarra, (int)alturaEstado);
	                
	            }
		};
		this.botonera = new JPanel();
		this.insercion = new JButton("Insertar");
		botonera.add(insercion);
		this.borrado = new JButton("Borrar");
		botonera.add(borrado);
		this.orden = new JButton("Ordenar");
		botonera.add(orden);
		this.setLayout(new BorderLayout());
		add(label, BorderLayout.NORTH);
		add(scrollTree, BorderLayout.WEST);
		add(scrollTable, BorderLayout.CENTER);
		add(panel, BorderLayout.EAST);
		add(botonera, BorderLayout.SOUTH);
		
	}
	public void setTree(DataSetMunicipios dataset) { //Añadimos todos los nodos al tree
		DefaultMutableTreeNode raiz = new DefaultMutableTreeNode("Municipios");
		DefaultTreeModel modelo = new DefaultTreeModel(raiz);
		this.tree.setModel(modelo);
		
		datasetEnt = dataset;
		tree.setCellRenderer( new MiTreeRenderer() );
		setAutonomias(dataset, raiz, modelo);
		setProvincias(dataset, raiz, modelo);
		tree.setEditable(false);
		setVisible(true);
		
	}
		//La llamamos en setTree()
	public void setAutonomias(DataSetMunicipios dataset, DefaultMutableTreeNode raiz, DefaultTreeModel modelo) {
		for (Municipio municipio: dataset.getListaMunicipios()) {
			boolean existe = false;
			for (int i=0; i<raiz.getChildCount(); i++) {
				if(municipio.getAutonomia().equals(raiz.getChildAt(i).toString())) {
					existe = true;
					break;
				}
			}
			if(!existe) {
				DefaultMutableTreeNode nuevo = new DefaultMutableTreeNode(municipio.getAutonomia());
				raiz.add(nuevo);
				modelo.nodesWereInserted( raiz, new int[] { raiz.getChildCount()-1 } );
			}
		}
	}
		//La llamamos en setTree()
	public void setProvincias(DataSetMunicipios dataset, DefaultMutableTreeNode raiz, DefaultTreeModel modelo) {
		for (Municipio municipio: dataset.getListaMunicipios()) {
			
			DefaultMutableTreeNode nodoAutonomia = null;
			for (int i=0; i<raiz.getChildCount(); i++) {
				if(municipio.getAutonomia().equals(raiz.getChildAt(i).toString())) {
					nodoAutonomia =((DefaultMutableTreeNode)raiz.getChildAt(i));
				}
			}
			boolean existe = false;
			for(int i=0; i<nodoAutonomia.getChildCount(); i++) {
				if (municipio.getProvincia().equals(nodoAutonomia.getChildAt(i).toString())) {
					existe = true;
					break;
				}
			}
			if(!existe) {
				DefaultMutableTreeNode nuevo = new DefaultMutableTreeNode(municipio.getProvincia());
				nodoAutonomia.add(nuevo);
				modelo.nodesWereInserted( nodoAutonomia, new int[] { nodoAutonomia.getChildCount()-1 } );
			}
		}	
	}
	
		//Añadimos toda la información a la tabla y añadimos un listeners y renderers
	public void setTable(DataSetMunicipios dataset) {
		tree.addTreeSelectionListener(new TreeSelectionListener() {
			@Override
			public void valueChanged(TreeSelectionEvent e) {
				datasetEnt = dataset;
				if(((DefaultMutableTreeNode)tree.getLastSelectedPathComponent()).isLeaf()) {
					panel.repaint();
					datosTabla = new ArrayList<Municipio>();
					autonomiaSeleccionada = ((DefaultMutableTreeNode)tree.getLastSelectedPathComponent()).getParent().toString();
					provinciaSeleccionada = tree.getLastSelectedPathComponent().toString();
					for(Municipio municipio: dataset.getListaMunicipios()) {
						if(municipio.getProvincia().equals(provinciaSeleccionada)) {
						datosTabla.add(municipio);
						}
					}
					modeloTabla = new MiTableModel();
					table.setModel( modeloTabla );
					//Ordenaar los datos
					Collections.sort(datosTabla, Comparator.comparing(Municipio::getNombre));

					// Paso 7
					table.setDefaultRenderer( Integer.class, new DefaultTableCellRenderer() {
						// private JProgressBar pbHabs = new JProgressBar( 0, 5000000 );
						// Paso 10:
						private JProgressBar pbHabs = new JProgressBar( 0, 5000000 ) {
							protected void paintComponent(java.awt.Graphics g) {
								Graphics2D g2d = (Graphics2D) g.create();
				                int progress = getValue();
				                float ratio = (float) progress / (float) getMaximum();
				                int red = (int) (ratio * 255);
				                int green = 255 - red;
				                g2d.setColor(new Color(red, green, 0));
				                g2d.fillRect(0, 0, getWidth(), getHeight());
				                g2d.dispose();

				                super.paintComponent(g);
				            }
				        };
				        

							
						@Override
						public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
								boolean hasFocus, int row, int column) {
							// System.out.println( "getTCR " + row + "," + column );
							if (((String)table.getColumnName(column)).equals("Población")) {
								// Si el dato es un Object o String sería esto
								// int valorCelda = Integer.parseInt( value.toString() );
								// pbHabs.setValue( valorCelda );
								// return pbHabs;
								// Pero si el dato está asegurado ser un Integer se puede castear:
								pbHabs.setValue((Integer) datosTabla.get(row).getHabitantes());
								
								
								int progress = pbHabs.getValue();
				                float ratio = (float) progress / (float) pbHabs.getMaximum();
				                int red = (int) (ratio * 255);
				                int green = 255 - red;
				                pbHabs.setForeground(new Color(red, green, 0));
								
								return pbHabs;
							}
							JLabel rendPorDefecto = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
							return rendPorDefecto;
						}
						
					});
				}
			}
		});
		
		// Paso 11
		table.setDefaultRenderer( String.class, new DefaultTableCellRenderer() {
			@Override
			public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
				Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column );
				c.setBackground( Color.WHITE );
				if (isSelected) {  // Observa que si no se pone esto uno no se entera cuándo la fila está seleccionada
					c.setBackground( Color.LIGHT_GRAY );
				}
				if (table.getColumnName(column).equals("Nombre")) { // Paso 11 del ejercicio
					if(habitantesMuniSel!=null) {
						if (habitantesMuniSel<datosTabla.get(row).getHabitantes()){
							c.setBackground( Color.RED );
						}
						else if(habitantesMuniSel>datosTabla.get(row).getHabitantes()){
							c.setBackground( Color.GREEN );
						}else {
							c.setBackground(Color.WHITE);
						}
					}else {
						c.setBackground(Color.WHITE);
					}
				}
				return c;
			}
		} );
		
		// Paso 8
		table.addMouseMotionListener( new MouseMotionAdapter() {
			@Override
			public void mouseMoved(MouseEvent e) {
				int filaEnTabla = table.rowAtPoint( e.getPoint() );
				int colEnTabla = table.columnAtPoint( e.getPoint() );
				if (((String)table.getColumnName(colEnTabla)).equals("Habitantes")&&filaEnTabla>=0&&filaEnTabla<datosTabla.size()) {
				int numHabs = datosTabla.get(filaEnTabla).getHabitantes();
					table.setToolTipText( String.format( "Población: %,d", numHabs ) );
				} else {
					table.setToolTipText( null );  // Desactiva
				}
			}
		});
			// Paso 11
		table.addMouseListener( new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
					
				if (SwingUtilities.isRightMouseButton(e)) {
					int filaEnTabla = table.rowAtPoint( e.getPoint() );
					int colEnTabla = table.columnAtPoint( e.getPoint() );
					if (table.getColumnName(colEnTabla).equals("Nombre") && filaEnTabla>=0 && !datosTabla.get(filaEnTabla).getNombre().equals(municipioSeleccionado)) {
						habitantesMuniSel = datosTabla.get(filaEnTabla).getHabitantes();
						municipioSeleccionado = datosTabla.get(filaEnTabla).getNombre();
					} else {
						habitantesMuniSel = null;
						municipioSeleccionado = null;
					}
				}
				table.repaint();
			}
		});
		

		// Paso 12
		table.setDefaultEditor( Integer.class, new DefaultCellEditor( new JTextField() ) {
			SpinnerNumberModel mSpinner = new SpinnerNumberModel( 200000, 50000, 5000000, 1000 );
			JSpinner spinner = new JSpinner( mSpinner );
			boolean lanzadoSpinner;
			@Override
			public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) { // Componente que se pone en la tabla al editar una celda
				if (!table.getColumnName(column).equals("Habitantes")) {
					lanzadoSpinner = false;
					return super.getTableCellEditorComponent(table, value, isSelected, row, column);
				}
				mSpinner.setValue( (Integer) value );
				lanzadoSpinner = true;
				return spinner;
			}
			@Override
			public Object getCellEditorValue() { // Valor que se retorna al acabar la edición
				
				if (lanzadoSpinner) {
					return spinner.getValue();
				} else {
					return Integer.parseInt( super.getCellEditorValue().toString() );
				}
			}
		});
	}
	//Creamos un nuevo modelo para la tabla
	private class MiTableModel implements TableModel {

		// Paso 7
		private final Class<?>[] CLASES_COLS = { String.class, Integer.class, Integer.class ,String.class, String.class };
		@Override
		public Class<?> getColumnClass(int columnIndex) {
			// Si no se quiere especializar cada columna:
			// return Object.class;
			return CLASES_COLS[columnIndex]; // Paso 7
		}

		@Override
		public int getColumnCount() {
			// System.out.println( "getColumnCount" );
			return 5;
		}

		@Override
		public int getRowCount() {
			// System.out.println( "getRowCount" );
			return datosTabla.size();
		}

		private final String[] cabeceras = {"Nombre", "Habitantes", "Población", "Provincia", "Autonomía" };
		@Override
		public String getColumnName(int columnIndex) {
			// System.out.println( "getColumnName " + columnIndex );
			return cabeceras[columnIndex];
		}

		@Override
		public Object getValueAt(int rowIndex, int columnIndex) {
			// System.out.println( "getValueAt " + rowIndex + "," + columnIndex );
			switch (columnIndex) {
			case 0:
				return datosTabla.get(rowIndex).getNombre();
			case 1:
				return datosTabla.get(rowIndex).getHabitantes();
			case 3:
				return datosTabla.get(rowIndex).getProvincia();
			case 4:
				return datosTabla.get(rowIndex).getAutonomia();
			default:
				return null;
			}
		}

		@Override
		public boolean isCellEditable(int rowIndex, int columnIndex) {
			System.out.println( "isCellEditable" );
			
			if (columnIndex == 2||columnIndex == 3||columnIndex == 4) {
				return false;
			}
			
			repaint();
			return true;
		}

		@Override
		public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
			System.out.println( "setValue " + aValue + "[" + aValue.getClass().getName() + "] " + rowIndex + "," + columnIndex );
			habitantesMuniSel = null;
			municipioSeleccionado = null;
			repaint();
			switch (columnIndex) {
			case 0:
				datosTabla.get(rowIndex).setNombre( (String) aValue );
				break;
			case 1:
				try {
					// Cuando no estaba especializada la columna había que tratarla como Object
					// datosMunis.getListaMunicipios().get(rowIndex).setHabitantes( Integer.parseInt((String)aValue) );
					// Pero ahora puede tratarse como Integer:
					datosTabla.get(rowIndex).setHabitantes( (Integer) aValue );
				} catch (NumberFormatException e) {
					JOptionPane.showMessageDialog( VentanaTree.this, "Nº de habitantes erróneo" );
				}
				break;
			case 3:
				datosTabla.get(rowIndex).setProvincia( (String) aValue );
				break;
			case 4:
				datosTabla.get(rowIndex).setAutonomia( (String) aValue );
				break;
			}
		}

		// Paso 5: trabajar con los escuchadores
		ArrayList<TableModelListener> listaEsc = new ArrayList<>();
		@Override
		public void addTableModelListener(TableModelListener l) {
			System.out.println( "addTableModelListener" );
			listaEsc.add( l );
		}

		@Override
		public void removeTableModelListener(TableModelListener l) {
			listaEsc.remove( l );
		}
		
		// DefaultTableModel lo hace así
		public void fireTableChanged( TableModelEvent e ) {
			for (TableModelListener l : listaEsc) {
				l.tableChanged( e );
			}
		}
		
	    // Paso 5
		public void borraFila( int fila ) {
			if (fila>=0&&fila<datosTabla.size()) {
				int respuesta = JOptionPane.showConfirmDialog(null, "¿Estás seguro de que quieres borrar "+datosTabla.get(fila).getNombre() +"?", "Confirmación de borrado", JOptionPane.YES_NO_OPTION);
				if(respuesta==JOptionPane.YES_OPTION) {
					System.out.println("Borrado "+datosTabla.get(fila).getNombre());
					JOptionPane.showMessageDialog(null, datosTabla.get(fila).getNombre() + " ha sido borrado", "Borrado", JOptionPane.INFORMATION_MESSAGE);
					datasetEnt.quitar(datosTabla.get(fila).getNombre());
					datosTabla.remove(fila);
					fireTableChanged( new TableModelEvent( modeloTabla, fila, datosTabla.size() ));
				}
			}
		}
		
	    // Paso 6
	    public void anyadeFila( int fila ) {
	    	if(provinciaSeleccionada!= null) {
	    		
	    		Municipio muni = new Municipio("", 50000, provinciaSeleccionada, autonomiaSeleccionada);
	    		datosTabla.add(muni);
	    		datasetEnt.anyadir(muni);
	    		System.out.println("Municipio anyadido");
				fireTableChanged( new TableModelEvent( modeloTabla, fila, datosTabla.size() ) );  // Para que detecte el cambio en todas
				table.revalidate();
	    	}
	    }
	    
	    public void ordenar( int fila ) {
	    	if(provinciaSeleccionada!= null) {
	    		if(alfabeticamente) {
	    			Collections.sort(datosTabla, new Comparator<Municipio>() {
	    			    public int compare(Municipio muni1, Municipio muni2) {
	    			        return Integer.compare(muni2.getHabitantes(), muni1.getHabitantes());
	    			    }
	    			});
	    			alfabeticamente= false;
	    		}else{
	    			Collections.sort(datosTabla, Comparator.comparing(Municipio::getNombre));
	    			alfabeticamente=true;
	    		}
	    		fireTableChanged( new TableModelEvent( modeloTabla, 0, datosTabla.size() ) );  // Para que detecte el cambio en todas
		    }
	    }   
	}
	//Les damos la funcionalidad requerida a los botones
	public void setBotones() {
		insercion.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				if(modeloTabla!=null) {
					int fila = table.getSelectedRow();
					modeloTabla.anyadeFila(fila);
					repaint();//Para que se modifiquen las progress bars y el gráfico de las provincia
				}
			}
		});
		borrado.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				if(modeloTabla!=null) {
					int fila = table.getSelectedRow();
					modeloTabla.borraFila(fila);
					repaint();//Para que se modifiquen las progress bars y el gráfico de las provincia
					
				}
			}
		});
		orden.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				if(modeloTabla!=null) {
					int fila = table.getSelectedRow();
					modeloTabla.ordenar(fila);
					
				}
			}
		});
	}
	//Crear el renderer pra que en el tree aparezca el progress bar
	@SuppressWarnings("serial")
	private static class MiTreeRenderer extends DefaultTreeCellRenderer {
		private JPanel panelLabel;
		private JLabel lTexto;
		private JProgressBar pbTam;
		@Override
		public Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel, boolean expanded,
				boolean leaf, int row, boolean hasFocus) {
			int habitantesProvincia = 0;
			
			
			panelLabel = new JPanel( new FlowLayout( FlowLayout.LEFT ) );
			
			panelLabel.setOpaque( false );
			panelLabel.setPreferredSize( new Dimension(350, 22 ) );
			lTexto = new JLabel();
			pbTam = new JProgressBar( 0, 7000000 );
			pbTam.setOpaque( false );
			panelLabel.add( lTexto );
			
			
			JLabel normal = (JLabel) super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);
			lTexto.setFont( normal.getFont() );
			DefaultMutableTreeNode nodo = (DefaultMutableTreeNode) value;
			if(nodo.isLeaf()&&!nodo.equals(tree.getModel().getRoot())&&!nodo.getParent().equals(tree.getModel().getRoot())) {	
				for (Municipio muni: datasetEnt.getListaMunicipios()) {
					if(nodo.toString().equals(muni.getProvincia())) {
					habitantesProvincia = habitantesProvincia+muni.getHabitantes();
					}
				}
				panelLabel.add( pbTam );
				pbTam.setValue(habitantesProvincia);
				float ratio = (float)habitantesProvincia/ (float)7000000;
                int red = (int) (ratio * 255);
                if(ratio>1) {
                	red =255;
                }
                int green = 255 - red;
				pbTam.setForeground( new Color( red, green, 0 ));
			}
			if(nodo.isRoot()) {
				lTexto.setFont(new Font(lTexto.getFont().getName(), Font.BOLD, 13 ));
			}
			lTexto.setText( normal.getText() );
			return panelLabel;
		}
		
	}
	
}


