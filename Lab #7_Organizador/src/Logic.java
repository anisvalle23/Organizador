
import java.awt.Color;
import javax.swing.*;
import java.io.*;
import java.util.Arrays;

public class Logic {

    private File seleccionado;
    private File copia;
    private boolean puedePegar = false;

    public Logic() {
        seleccionado = new File("Root");
        if (!seleccionado.exists()) {
            seleccionado.mkdir();
        }
    }

    public File getSeleccionado() {
        return seleccionado;
    }

    public void setSeleccionado(File seleccionado) {
        this.seleccionado = seleccionado;
    }

    public boolean esCarpetaSeleccionada() {
        return seleccionado != null && seleccionado.isDirectory();
    }

    public boolean esArchivoSeleccionado() {
        return seleccionado != null && seleccionado.isFile();
    }

    public void crearCarpeta() {
        String nombreCarpeta = mostrarDialogoInput("Ingrese el nombre de la carpeta:");
        if (nombreCarpeta != null && !nombreCarpeta.isBlank()) {
            try {
                File nuevaCarpeta = new File(seleccionado, nombreCarpeta);
                if (nuevaCarpeta.mkdir()) {
                    mostrarMensaje("Carpeta creada exitosamente", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    mostrarMensaje("No se pudo crear la carpeta", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (Exception e) {
                mostrarMensaje("Error al crear la carpeta: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    public void renombrarArchivo() {
        String nuevoNombre = mostrarDialogoInput("Ingrese el nuevo nombre:");
        if (nuevoNombre != null && !nuevoNombre.isBlank()) {
            try {
                if (seleccionado.getName().endsWith(".txt") && !nuevoNombre.endsWith(".txt")) {
                    nuevoNombre += ".txt";
                }
                File nuevoArchivo = new File(seleccionado.getParent(), nuevoNombre);
                if (seleccionado.renameTo(nuevoArchivo)) {
                    mostrarMensaje("Archivo renombrado correctamente", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    mostrarMensaje("No se pudo renombrar el archivo", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (Exception e) {
                mostrarMensaje("Error al renombrar el archivo: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    public void crearArchivoTexto() {
        String nombreArchivo = mostrarDialogoInput("Ingrese el nombre del archivo de texto:");
        if (nombreArchivo != null && !nombreArchivo.isBlank()) {
            try {
                new File(seleccionado, nombreArchivo + ".txt").createNewFile();
                mostrarMensaje("Archivo de texto creado exitosamente", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            } catch (IOException e) {
                mostrarMensaje("Error al crear el archivo: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    public void crearArchivoComercial() {
        String nombreArchivo = mostrarDialogoInput("Ingrese el nombre del archivo comercial:");
        if (nombreArchivo != null && !nombreArchivo.isBlank()) {
            try {
                new File(seleccionado, nombreArchivo + ".bin").createNewFile();
                mostrarMensaje("Archivo comercial creado exitosamente", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            } catch (IOException e) {
                mostrarMensaje("Error al crear el archivo: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    public void escribirEnArchivo() {
        if (esArchivoSeleccionado()) {
            String texto = mostrarDialogoInput("Ingrese el texto a escribir en el archivo:");
            if (texto != null && !texto.isBlank()) {
                try (BufferedWriter writer = new BufferedWriter(new FileWriter(seleccionado, true))) {
                    writer.write(texto);
                    writer.newLine();
                    mostrarMensaje("Texto escrito correctamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                } catch (IOException e) {
                    mostrarMensaje("Error al escribir en el archivo: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                mostrarMensaje("No se ingresó ningún texto.", "Advertencia", JOptionPane.WARNING_MESSAGE);
            }
        } else {
            mostrarMensaje("Debe seleccionar un archivo válido para escribir.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public File[] ordenarArchivosPorFecha() {
        if (esCarpetaSeleccionada()) {
            File[] carpetas = seleccionado.listFiles(File::isDirectory);
            File[] archivos = seleccionado.listFiles(File::isFile);
            if (archivos != null && archivos.length > 0) {
                Arrays.sort(archivos, (a, b) -> Long.compare(b.lastModified(), a.lastModified()));
            }
            return combinarCarpetasYArchivos(carpetas, archivos);
        }
        return null;
    }

    public File[] ordenarArchivosPorNombre() {
        if (esCarpetaSeleccionada()) {
            File[] carpetas = seleccionado.listFiles(File::isDirectory);
            File[] archivos = seleccionado.listFiles(File::isFile);
            if (archivos != null && archivos.length > 0) {
                Arrays.sort(archivos, (a, b) -> a.getName().compareToIgnoreCase(b.getName()));
            }
            return combinarCarpetasYArchivos(carpetas, archivos);
        }
        return null;
    }

    public File[] ordenarArchivosPorTamano() {
        if (esCarpetaSeleccionada()) {
            File[] carpetas = seleccionado.listFiles(File::isDirectory);
            File[] archivos = seleccionado.listFiles(File::isFile);
            if (archivos != null && archivos.length > 0) {
                Arrays.sort(archivos, (a, b) -> Long.compare(b.length(), a.length()));
            }
            return combinarCarpetasYArchivos(carpetas, archivos);
        }
        return null;
    }

    public File[] organizarArchivosPorTipo() {
        if (esCarpetaSeleccionada()) {
            File[] archivos = seleccionado.listFiles(File::isFile);
            if (archivos != null && archivos.length > 0) {
                for (File archivo : archivos) {
                    String extension = archivo.getName().substring(archivo.getName().lastIndexOf(".") + 1);
                    File carpetaTipo = new File(seleccionado, extension);
                    if (!carpetaTipo.exists()) {
                        carpetaTipo.mkdir();
                    }
                    File nuevoArchivo = new File(carpetaTipo, archivo.getName());
                    archivo.renameTo(nuevoArchivo);
                }
                mostrarMensaje("Archivos organizados por tipo", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                return seleccionado.listFiles();
            } else {
                mostrarMensaje("No hay archivos para organizar por tipo.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            mostrarMensaje("Debe seleccionar una carpeta válida.", "Error", JOptionPane.ERROR_MESSAGE);
        }
        return null;
    }

    public void mostrarArchivosOrdenados(File[] archivos, String mensaje) {
        if (archivos != null) {
            StringBuilder sb = new StringBuilder(mensaje + ":\n");
            for (File archivo : archivos) {
                sb.append(archivo.getName()).append("\n");
            }
            mostrarMensaje(sb.toString(), "Archivos ordenados", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    public void copiarArchivo() {
        try {
            copia = new File(seleccionado.getAbsolutePath());
            puedePegar = true;
            mostrarMensaje("Archivo copiado", "Información", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception e) {
            mostrarMensaje("Error al copiar archivo: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void pegarArchivo() {
        if (!puedePegar) {
            mostrarMensaje("No hay archivo para pegar", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        try {
            if (copia != null) {
                File nuevoArchivo = new File(seleccionado, generarNombreConSecuencia(copia.getName()));
                if (copia.isDirectory()) {
                    copiarDirectorio(copia, nuevoArchivo);
                } else {
                    copiarArchivoSimple(copia, nuevoArchivo);
                }
                mostrarMensaje("Archivo pegado correctamente", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                puedePegar = false;
            }
        } catch (Exception e) {
            mostrarMensaje("Error al pegar archivo: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private String generarNombreConSecuencia(String nombreOriginal) {
        String nuevoNombre = nombreOriginal;
        int contador = 1;
        while (new File(seleccionado, nuevoNombre).exists()) {
            int indexExtension = nombreOriginal.lastIndexOf('.');
            if (indexExtension > 0) {
                nuevoNombre = nombreOriginal.substring(0, indexExtension) + "_" + contador + nombreOriginal.substring(indexExtension);
            } else {
                nuevoNombre = nombreOriginal + "_" + contador;
            }
            contador++;
        }
        return nuevoNombre;
    }

    private void copiarArchivoSimple(File origen, File destino) throws IOException {
        try (InputStream in = new FileInputStream(origen); OutputStream out = new FileOutputStream(destino)) {
            byte[] buffer = new byte[1024];
            int length;
            while ((length = in.read(buffer)) > 0) {
                out.write(buffer, 0, length);
            }
        }
    }

    private void copiarDirectorio(File origen, File destino) throws IOException {
        if (!destino.exists()) {
            destino.mkdir();
        }
        String[] archivos = origen.list();
        if (archivos != null) {
            for (String archivo : archivos) {
                File archivoOrigen = new File(origen, archivo);
                File archivoDestino = new File(destino, archivo);
                if (archivoOrigen.isDirectory()) {
                    copiarDirectorio(archivoOrigen, archivoDestino);
                } else {
                    copiarArchivoSimple(archivoOrigen, archivoDestino);
                }
            }
        }
    }

    private void mostrarMensaje(String mensaje, String titulo, int tipoMensaje) {
        UIManager.put("OptionPane.background", new Color(43, 43, 43));
        UIManager.put("Panel.background", new Color(43, 43, 43));
        UIManager.put("OptionPane.messageForeground", Color.WHITE);
        UIManager.put("Button.background", new Color(52, 152, 219));
        UIManager.put("Button.foreground", Color.WHITE);
        JOptionPane.showMessageDialog(null, mensaje, titulo, tipoMensaje);
    }

    private String mostrarDialogoInput(String mensaje) {
        UIManager.put("OptionPane.background", new Color(43, 43, 43));
        UIManager.put("Panel.background", new Color(43, 43, 43));
        UIManager.put("OptionPane.messageForeground", Color.WHITE);
        UIManager.put("Button.background", new Color(52, 152, 219));
        UIManager.put("Button.foreground", Color.WHITE);
        return JOptionPane.showInputDialog(null, mensaje);
    }

    private File[] combinarCarpetasYArchivos(File[] carpetas, File[] archivos) {
        File[] resultado = new File[(carpetas != null ? carpetas.length : 0) + (archivos != null ? archivos.length : 0)];
        int index = 0;
        if (carpetas != null) {
            System.arraycopy(carpetas, 0, resultado, index, carpetas.length);
            index += carpetas.length;
        }
        if (archivos != null) {
            System.arraycopy(archivos, 0, resultado, index, archivos.length);
        }
        return resultado;
    }
}
