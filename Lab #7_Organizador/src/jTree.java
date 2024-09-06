
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.tree.*;
import java.awt.*;
import java.io.File;
import javax.swing.border.LineBorder;

public class jTree extends JFrame {

    private JTree arbolDirectorios;
    private Logic logicaArchivos;
    private DefaultMutableTreeNode nodoRaiz;
    private DefaultTreeModel modeloArbol;

    public jTree(Logic logicaArchivos) {
        this.logicaArchivos = logicaArchivos;
        inicializarComponentes();
    }

    private void inicializarComponentes() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("Explorador de Archivos");
        setSize(1000, 700);
        setLocationRelativeTo(null);

        JPanel panelPrincipal = new JPanel(new BorderLayout());
        panelPrincipal.setBorder(new EmptyBorder(10, 10, 10, 10));

        JToolBar barraHerramientas = new JToolBar();
        barraHerramientas.setFloatable(false);

        String[] opcionesOrdenar = {"Ordenar por...", "Nombre", "Fecha", "Tipo", "Tamaño"};
        JComboBox<String> listaOrdenar = new JComboBox<>(opcionesOrdenar);
        listaOrdenar.addActionListener(e -> ejecutarAccionOrdenar((String) listaOrdenar.getSelectedItem()));

        String[] opcionesGestion = {"Gestión de archivos...", "Crear Carpeta", "Cambiar Nombre", "Copiar", "Pegar"};
        JComboBox<String> listaGestion = new JComboBox<>(opcionesGestion);
        listaGestion.addActionListener(e -> ejecutarAccionGestion((String) listaGestion.getSelectedItem()));

        String[] opcionesCrear = {"Crear...", "Crear archivo de texto", "Crear archivo comercial"};
        JComboBox<String> listaCrear = new JComboBox<>(opcionesCrear);
        listaCrear.addActionListener(e -> ejecutarAccionCrear((String) listaCrear.getSelectedItem()));

        String[] opcionesRegistrar = {"Registrar en archivo...", "Escribir en archivo"};
        JComboBox<String> listaRegistrar = new JComboBox<>(opcionesRegistrar);
        listaRegistrar.addActionListener(e -> ejecutarAccionRegistrar((String) listaRegistrar.getSelectedItem()));

        JButton botonSalir = new JButton("Salir");
        botonSalir.addActionListener(e -> System.exit(0));

        barraHerramientas.add(listaOrdenar);
        barraHerramientas.addSeparator();
        barraHerramientas.add(listaGestion);
        barraHerramientas.addSeparator();
        barraHerramientas.add(listaCrear);
        barraHerramientas.addSeparator();
        barraHerramientas.add(listaRegistrar);
        barraHerramientas.add(Box.createHorizontalGlue());
        barraHerramientas.add(botonSalir);

        nodoRaiz = new DefaultMutableTreeNode("Root");
        arbolDirectorios = new JTree(nodoRaiz);
        arbolDirectorios.setFont(new Font("Arial", Font.PLAIN, 16));
        modeloArbol = (DefaultTreeModel) arbolDirectorios.getModel();
        arbolDirectorios.addTreeSelectionListener(e -> seleccionarDirectorio());

        JScrollPane scrollPaneArbol = new JScrollPane(arbolDirectorios);
        scrollPaneArbol.setBorder(BorderFactory.createLineBorder(new Color(52, 152, 219), 2));
        scrollPaneArbol.setPreferredSize(new Dimension(700, 600));

        panelPrincipal.add(barraHerramientas, BorderLayout.NORTH);
        panelPrincipal.add(scrollPaneArbol, BorderLayout.CENTER);

        add(panelPrincipal);
        setVisible(true);
        actualizarArbol();
    }

    private void ejecutarAccionOrdenar(String opcion) {
        File[] archivosOrdenados = null;
        switch (opcion) {
            case "Nombre":
                archivosOrdenados = logicaArchivos.ordenarArchivosPorNombre();
                break;
            case "Fecha":
                archivosOrdenados = logicaArchivos.ordenarArchivosPorFecha();
                break;
            case "Tipo":
                archivosOrdenados = logicaArchivos.organizarArchivosPorTipo();
                break;
            case "Tamaño":
                archivosOrdenados = logicaArchivos.ordenarArchivosPorTamano();
                break;
        }

        if (archivosOrdenados != null) {
            logicaArchivos.mostrarArchivosOrdenados(archivosOrdenados, "Archivos ordenados");
            actualizarArbolOrdenado(archivosOrdenados);
        } else {
            actualizarArbol();
        }
    }

    private void ejecutarAccionGestion(String opcion) {
        switch (opcion) {
            case "Crear Carpeta":
                logicaArchivos.crearCarpeta();
                break;
            case "Cambiar Nombre":
                logicaArchivos.renombrarArchivo();
                break;
            case "Copiar":
                logicaArchivos.copiarArchivo();
                break;
            case "Pegar":
                logicaArchivos.pegarArchivo();
                break;
        }
        actualizarArbol();
    }

    private void ejecutarAccionCrear(String opcion) {
        switch (opcion) {
            case "Crear archivo de texto":
                logicaArchivos.crearArchivoTexto();
                break;
            case "Crear archivo comercial":
                logicaArchivos.crearArchivoComercial();
                break;
        }
        actualizarArbol();
    }

    private void ejecutarAccionRegistrar(String opcion) {
        if ("Escribir en archivo".equals(opcion)) {
            logicaArchivos.escribirEnArchivo();
        }
    }

    private void seleccionarDirectorio() {
        TreePath rutaSeleccionada = arbolDirectorios.getSelectionPath();
        if (rutaSeleccionada != null) {
            StringBuilder ruta = new StringBuilder();
            for (Object nodo : rutaSeleccionada.getPath()) {
                ruta.append(nodo.toString()).append(File.separator);
            }
            logicaArchivos.setSeleccionado(new File(ruta.toString()));
        }
    }

    private void actualizarArbol() {
        nodoRaiz.removeAllChildren();
        cargarDirectorios(new File("Root"), nodoRaiz);
        modeloArbol.reload();
    }

    private void actualizarArbolOrdenado(File[] archivosOrdenados) {
        nodoRaiz.removeAllChildren();
        DefaultMutableTreeNode nodoPadre = new DefaultMutableTreeNode(logicaArchivos.getSeleccionado().getName());

        for (File archivo : archivosOrdenados) {
            DefaultMutableTreeNode nuevoNodo = new DefaultMutableTreeNode(archivo.getName());
            nodoPadre.add(nuevoNodo);
        }

        nodoRaiz.add(nodoPadre);
        modeloArbol.reload();
    }

    private void cargarDirectorios(File directorio, DefaultMutableTreeNode nodoPadre) {
        File[] archivos = directorio.listFiles();
        if (archivos != null) {
            for (File archivo : archivos) {
                DefaultMutableTreeNode nuevoNodo = new DefaultMutableTreeNode(archivo.getName());
                nodoPadre.add(nuevoNodo);
                if (archivo.isDirectory()) {
                    cargarDirectorios(archivo, nuevoNodo);
                }
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new jTree(new Logic()));
    }
}
