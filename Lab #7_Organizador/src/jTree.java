import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.tree.*;
import java.awt.*;
import java.io.File;
import javax.swing.border.LineBorder;

public class jTree extends JFrame {
    private JTree arbolDirectorio;
    private Logic logicaExplorador;
    private DefaultMutableTreeNode raiz;
    private DefaultTreeModel modeloArbol;

    public jTree(Logic logicaExplorador) {
        this.logicaExplorador = logicaExplorador;
        initComponents();
    }

    private void initComponents() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("Explorador de Archivos");
        setSize(1000, 700);
        setLocationRelativeTo(null);

        JPanel panelPrincipal = new JPanel(new BorderLayout());
        panelPrincipal.setBorder(new EmptyBorder(10, 10, 10, 10));

        raiz = new DefaultMutableTreeNode("Root");
        arbolDirectorio = new JTree(raiz);
        arbolDirectorio.setFont(new Font("Arial", Font.PLAIN, 16));
        modeloArbol = (DefaultTreeModel) arbolDirectorio.getModel();
        arbolDirectorio.addTreeSelectionListener(e -> seleccionarDirectorio());

        JScrollPane scrollPaneArbol = new JScrollPane(arbolDirectorio);
        scrollPaneArbol.setBorder(BorderFactory.createLineBorder(new Color(52, 152, 219), 2));
        scrollPaneArbol.setPreferredSize(new Dimension(700, 600));

        JPanel panelBotones = new JPanel();
        panelBotones.setLayout(new BoxLayout(panelBotones, BoxLayout.Y_AXIS));
        panelBotones.setBorder(new EmptyBorder(10, 10, 10, 10));

        String[] opciones = {
            "Crear Carpeta", "Cambiar Nombre", "Crear archivo de texto", "Crear archivo comercial", 
            "Escribir en archivo", "Ordenar por fecha", "Ordenar por nombre", "Ordenar por tamaño", 
            "Ordenar por tipo", "Copiar", "Pegar", "Salir"
        };

        Dimension botonDimension = new Dimension(200, 50);

        for (String opcion : opciones) {
            JButton button = crearBotonColorido(opcion, botonDimension);
            button.addActionListener(e -> ejecutarAccion(opcion));
            panelBotones.add(button);
            panelBotones.add(Box.createRigidArea(new Dimension(0, 10)));
        }

        panelPrincipal.add(panelBotones, BorderLayout.WEST);
        panelPrincipal.add(scrollPaneArbol, BorderLayout.CENTER);

        add(panelPrincipal);
        setVisible(true);
        actualizarArbol();
    }

    private JButton crearBotonColorido(String texto, Dimension dim) {
        JButton boton = new JButton(texto);
        boton.setPreferredSize(dim);
        boton.setMaximumSize(dim);
        boton.setFont(new Font("Arial", Font.BOLD, 14));
        boton.setBackground(new Color(41, 128, 185));
        boton.setForeground(Color.WHITE);
        boton.setFocusPainted(false);
        boton.setBorder(new LineBorder(new Color(52, 152, 219), 2));
        boton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        boton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                boton.setBackground(new Color(52, 152, 219));
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                boton.setBackground(new Color(41, 128, 185));
            }
        });

        return boton;
    }

    private void ejecutarAccion(String opcion) {
        File[] archivosOrdenados = null;
        switch (opcion) {
            case "Crear Carpeta":
                logicaExplorador.crearCarpeta();
                break;
            case "Cambiar Nombre":
                logicaExplorador.renombrarArchivo();
                break;
            case "Crear archivo de texto":
                logicaExplorador.crearArchivoTexto();
                break;
            case "Crear archivo comercial":
                logicaExplorador.crearArchivoComercial();
                break;
            case "Escribir en archivo":
                logicaExplorador.escribirEnArchivo();
                break;
            case "Ordenar por fecha":
                archivosOrdenados = logicaExplorador.ordenarArchivosPorFecha();
                break;
            case "Ordenar por nombre":
                archivosOrdenados = logicaExplorador.ordenarArchivosPorNombre();
                break;
            case "Ordenar por tamaño":
                archivosOrdenados = logicaExplorador.ordenarArchivosPorTamano();
                break;
            case "Ordenar por tipo":
                archivosOrdenados = logicaExplorador.organizarArchivosPorTipo();
                break;
            case "Copiar":
                logicaExplorador.copiarArchivo();
                break;
            case "Pegar":
                logicaExplorador.pegarArchivo();
                break;
            case "Salir":
                System.exit(0);
                break;
        }

        if (archivosOrdenados != null) {
            logicaExplorador.mostrarArchivosOrdenados(archivosOrdenados, "Archivos ordenados");
            actualizarArbolOrdenado(archivosOrdenados);
        } else {
            actualizarArbol();
        }
    }

    private void seleccionarDirectorio() {
        TreePath rutaSeleccionada = arbolDirectorio.getSelectionPath();
        if (rutaSeleccionada != null) {
            String ruta = "";
            for (Object nodo : rutaSeleccionada.getPath()) {
                ruta += nodo.toString() + File.separator;
            }
            logicaExplorador.setSeleccionado(new File(ruta));
        }
    }

    private void actualizarArbol() {
        raiz.removeAllChildren();
        cargarDirectorios(new File("Root"), raiz);
        modeloArbol.reload();
    }

    private void actualizarArbolOrdenado(File[] archivosOrdenados) {
        raiz.removeAllChildren();
        DefaultMutableTreeNode nodoPadre = new DefaultMutableTreeNode(logicaExplorador.getSeleccionado().getName());

        for (File archivo : archivosOrdenados) {
            DefaultMutableTreeNode nuevoNodo = new DefaultMutableTreeNode(archivo.getName());
            nodoPadre.add(nuevoNodo);
        }

        raiz.add(nodoPadre);
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
